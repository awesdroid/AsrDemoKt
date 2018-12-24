package io.awesdroid.asrdemokt.ui

import android.app.Fragment
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import io.awesdroid.asasr.SpeechRecognizedResult
import io.awesdroid.asrdemokt.R
import io.awesdroid.asrdemokt.beans.ChatItem
import io.awesdroid.asrdemokt.events.EventMainActivity
import io.awesdroid.asrdemokt.events.EventMainActivity.EventId.START_REC
import io.awesdroid.asrdemokt.events.EventMainActivity.EventId.STOP_REC
import io.awesdroid.asrdemokt.events.EventPageChat
import io.awesdroid.asrdemokt.events.EventPageChat.EventId.UPDATE_CHAT
import io.awesdroid.asrdemokt.events.EventPageHome
import io.awesdroid.asrdemokt.ui.ChatPage.Companion.TYPE_PEOPLE
import io.awesdroid.asrdemokt.ui.HomePage.UIType.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 * Created by awesdroid.
 */

class HomePage : Fragment() {

    @BindView(R.id.spectrum_voice)
    lateinit var voiceSpectrum: VoiceSpectrum
    @BindView(R.id.tv_asr)
    lateinit var asrTextView: TextView

    private var unbinder: Unbinder? = null

    private var recState = STATE_INIT
    private var speechRecResult: String? = null

    internal enum class UIType {
        UI_START_LISTENING,
        UI_STOP_LISTING,
        UI_UPDATE_ASR
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventPageHome(event: EventPageHome) {
        val id = event.id
        val data = event.data
        when (id) {
            EventPageHome.EventId.START_LISTENING -> startListening()

            EventPageHome.EventId.STOP_LISTENING -> stopListening()

            EventPageHome.EventId.UI_ON_VOICE_START -> {
                voiceSpectrum.setVisualizerColor(Color.YELLOW)
                updateASR("listening ...", Color.YELLOW)
            }

            EventPageHome.EventId.UI_ON_VOICE -> {
                val voiceData = data as ByteArray
                voiceSpectrum.updateVisualizer(voiceData)
            }

            EventPageHome.EventId.SPEECH_RECOGNIZED_RESULT -> handleSpeechRecognizedResult(data as SpeechRecognizedResult)

            else -> {
            }
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)

        EventBus.getDefault().register(this)
        speechRecResult = ""
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.page_home, container, false)

        unbinder = ButterKnife.bind(this, rootView)

        /* Init page */
        initPageHome()

        return rootView
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: recState $recState")
        if (recState == STATE_STOP) {
            EventBus.getDefault().post(EventPageHome(EventPageHome.EventId.START_LISTENING, null))
        }
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: recState = $recState")
        if (recState != STATE_STOP) {
            stopListening()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        if (unbinder != null) {
            unbinder!!.unbind()
            unbinder = null
        }

        EventBus.getDefault().unregister(this)

        recState = STATE_INIT
        stopVoiceRecorder()
    }

    /**
     * Private methods
     */
    private fun initPageHome() {
        updateUI(UIType.UI_STOP_LISTING)
    }


    private fun updateUI(type: UIType) {
        when (type) {
            UI_START_LISTENING -> {
                voiceSpectrum.visibility = View.VISIBLE
                asrTextView.visibility = View.VISIBLE
            }

            UI_STOP_LISTING -> {
                voiceSpectrum.visibility = View.GONE
                asrTextView.visibility = View.GONE
            }

            UI_UPDATE_ASR -> asrTextView.visibility = View.VISIBLE
        }
    }

    private fun updateASR(result: String?, color: Int) {
        asrTextView.text = result
        asrTextView.setTextColor(color)
        updateUI(UIType.UI_UPDATE_ASR)
        asrTextView.invalidate()
    }


    private fun startListening() {
        updateUI(UIType.UI_START_LISTENING)
        Log.d(TAG, "startListening: recState = $recState")
        if (recState != STATE_LISTENING) {
            startVoiceRecorder()
            recState = STATE_LISTENING
        }
    }

    private fun stopListening() {
        updateUI(UIType.UI_STOP_LISTING)
        stopVoiceRecorder()
        recState = STATE_STOP
        Log.d(TAG, "stopListening: recState = $recState")
    }

    private fun startVoiceRecorder() {
        EventBus.getDefault().post(EventMainActivity(START_REC))
    }

    private fun stopVoiceRecorder() {
        EventBus.getDefault().post(EventMainActivity(STOP_REC))
    }

    private fun handleSpeechRecognizedResult(result: SpeechRecognizedResult) {
        val isFinal = result.isFinal
        val text = result.text

        var color: Int
        if (text.isEmpty()) {
            color = Color.GRAY
            speechRecResult = "NOT RECOGNIZED!"
        } else {
            color = Color.GREEN
            speechRecResult = text
        }
        voiceSpectrum.setVisualizerColor(color)
        voiceSpectrum.invalidate()
        color = if (isFinal) {
            Color.CYAN
        } else {
            Color.GRAY
        }
        updateASR(speechRecResult, color)

        if (isFinal) {
            val item = ChatItem()
            item.setType(TYPE_PEOPLE)
            item.setContent(speechRecResult!!)
            EventBus.getDefault().post(EventPageChat(UPDATE_CHAT, item))
            // TODO: The result is able to be fed into a chatbot to get answer if it's available
        }
    }

    companion object {
        private val TAG = HomePage::class.java.simpleName
        /* State */
        const val STATE_INIT = 0
        const val STATE_LISTENING = 1
        const val STATE_STOP = 2
    }
}
