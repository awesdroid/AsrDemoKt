package io.awesdroid.asrdemokt.ui

/**
 * Created by awesdroid.
 */

import android.Manifest
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.FragmentActivity
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import io.awesdroid.asasr.Event
import io.awesdroid.asasr.SpeechService
import io.awesdroid.asrdemokt.R
import io.awesdroid.asrdemokt.events.EventMainActivity
import io.awesdroid.asrdemokt.events.EventPageHome
import io.awesdroid.asrdemokt.events.EventPageHome.EventId.*
import io.awesdroid.asrdemokt.ui.FragmentPageAdapter.Companion.HOME_PAGE
import io.awesdroid.asrdemokt.viewmodel.MyViewModel
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.util.*
import java.util.stream.IntStream


class MainActivity : FragmentActivity(), BottomNavigationView.OnNavigationItemSelectedListener,
    EasyPermissions.PermissionCallbacks {

    private var pagerAdapter: PagerAdapter? = null
    private var serviceConnection: ServiceConnection? = null
    lateinit var viewPager: ViewPager

    @BindView(R.id.bottom_nav_view)
    lateinit var bottomNavigationView: BottomNavigationView
    @BindView(R.id.my_toolbar)
    lateinit var toolbar: Toolbar
    @BindView(R.id.toolbar_text)
    lateinit var titleText: TextView

    internal var menuItem: MenuItem? = null
    private var unbinder: Unbinder? = null
    internal var dummy = ByteArray(5)
    internal var runnable: Runnable = object : Runnable {
        override fun run() {
            IntStream.range(0, 4).forEach { v -> dummy[v] = Random().nextInt(3).toByte() }
            EventBus.getDefault().post(EventPageHome(UI_ON_VOICE, dummy))
            handler!!.postDelayed(this, 100)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        handler = Handler()

        unbinder = ButterKnife.bind(this)
        bottomNavigationView.setOnNavigationItemSelectedListener(this)
        checkPermissions()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        viewPager.currentItem = item.order
        return true
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()

        handler!!.removeCallbacks(runnable)
        handler = null
        unbindSpeechService()
        EventBus.getDefault().unregister(this)
        pagerAdapter = null
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventMainActivity(event: EventMainActivity) {
        Log.d(TAG, "onEventMainActivity: event = $event")
        when (event.id) {
            EventMainActivity.EventId.START_REC -> speechService!!.startVoiceRecorder()
            EventMainActivity.EventId.STOP_REC -> speechService!!.stopVoiceRecorder()
            else -> {
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    private fun hasAudioPermission(): Boolean {
        return EasyPermissions.hasPermissions(this, Manifest.permission.RECORD_AUDIO)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        init()
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        AppSettingsDialog.Builder(this)
            .setRationale(R.string.rationale_audio)
            .setRequestCode(RC_AUDIO_PERM)
            .build()
            .show()
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.d(TAG, String.format("onActivityResult: requestCode = %d, resultCode = %d", requestCode, resultCode))
        if (requestCode == RC_AUDIO_PERM) {

            Toast.makeText(
                this,
                getString(R.string.returned_from_app_settings, if (hasAudioPermission()) "YES" else "NO"),
                Toast.LENGTH_LONG
            )
                .show()
            if (hasAudioPermission()) {
                init()
            } else {
                finish()
            }
        }
    }

    /**
     * Private methods
     */
    private fun checkPermissions() {
        if (hasAudioPermission()) {
            init()
        } else {
            EasyPermissions.requestPermissions(
                this,
                getString(R.string.rationale_audio),
                RC_AUDIO_PERM,
                Manifest.permission.RECORD_AUDIO
            )
        }
    }

    private fun init() {
        bindSpeechService()
        EventBus.getDefault().register(this)

        titleText.setText(R.string.item_home)
        pagerAdapter = FragmentPageAdapter(fragmentManager)
        viewPager = findViewById<ViewPager>(R.id.pager)
        viewPager.adapter = pagerAdapter
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageSelected(position: Int) {
                Log.d(TAG, "onPageSelected: position = $position")
                if (menuItem == null) {
                    bottomNavigationView.menu.getItem(0).isChecked = false
                } else {
                    menuItem!!.isChecked = false
                }

                bottomNavigationView.menu.getItem(position).isChecked = true
                menuItem = bottomNavigationView.menu.getItem(position)

                if (position == HOME_PAGE) {
                    titleText.setText(R.string.item_home)
                    EventBus.getDefault().post(EventPageHome(START_LISTENING, null))
                } else {
                    titleText.setText(R.string.item_chat)
                    EventBus.getDefault().post(EventPageHome(STOP_LISTENING, null))
                    handler!!.removeCallbacks(runnable)
                }
            }

            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
        })
    }

    private fun initViewModel() {
        val viewModel = ViewModelProviders.of(this).get(MyViewModel::class.java)
        viewModel.init(speechService!!)
        viewModel.event!!.observe(this, Observer<Event> { this.handleEvent(it!!) })
    }

    private fun bindSpeechService() {
        serviceConnection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName, service: IBinder) {
                Log.w(TAG, "onServiceConnected")
                speechService = SpeechService.from(service)
                initViewModel()
                EventBus.getDefault().post(EventPageHome(START_LISTENING, null))
            }

            override fun onServiceDisconnected(name: ComponentName) {
                speechService = null
            }
        }

        bindService(
            Intent(this, SpeechService::class.java),
            serviceConnection, Context.BIND_AUTO_CREATE
        )
    }

    private fun unbindSpeechService() {
        if (serviceConnection != null) {
            unbindService(serviceConnection)
            serviceConnection = null
            speechService = null
        }
    }


    private fun handleEvent(event: Event) {
        when (event) {
            Event.START_RECOGNIZING -> {
                EventBus.getDefault().post(EventPageHome(UI_ON_VOICE_START, null))
                EventBus.getDefault().post(EventPageHome(START_LISTENING, null))
                handler!!.post(runnable)
            }

            Event.STOP_RECOGNIZING -> {
            }

            Event.RECOGNIZING -> Observable.just(event)
                .map<ByteArray> { this.generateSpectrum(it) }
                .observeOn(Schedulers.trampoline())
                .subscribe { spctrum -> EventBus.getDefault().post(EventPageHome(UI_ON_VOICE, spctrum)) }

            Event.RESULT -> {
                Log.w(TAG, "result = $event")
                EventBus.getDefault().post(EventPageHome(SPEECH_RECOGNIZED_RESULT, event.result))
            }

            else -> {
            }
        }
    }

    private fun generateSpectrum(event: Event): ByteArray {
        val fft = event.voiceData.data
        val segment = 6
        val segLen = fft.size / segment
        var sum = 0
        val result = ByteArray(segment)
        for (i in 0 until segment) {
            for (j in 0 until segLen) {
                val idx = segLen * i + j
                sum += fft[idx].toInt()
            }
            result[i] = (sum / segLen).toByte()
        }
        return result
    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName
        private val RC_AUDIO_PERM = 100
        private var speechService: SpeechService? = null

        private var handler: Handler? = null
    }
}
