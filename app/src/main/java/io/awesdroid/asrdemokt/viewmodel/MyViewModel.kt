package io.awesdroid.asrdemokt.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import io.awesdroid.asasr.Event
import io.awesdroid.asasr.SpeechService
import io.awesdroid.asrdemokt.db.entity.CredentialEntity
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

import javax.inject.Inject
import java.io.ByteArrayInputStream
import java.util.Objects


/**
 * @author awesdroid
 */
class MyViewModel(application: Application) : AndroidViewModel(application) {
    private var disposable: Disposable? = null
    var event: LiveData<Event>? = MutableLiveData()
        private set
    private var observer: Observer<Event>? = object : Observer<Event> {
        override fun onNext(event: Event) {
            updateEvent(event)
        }

        override fun onSubscribe(d: Disposable) {
            disposable = d
        }

        override fun onError(e: Throwable) {}

        override fun onComplete() {

        }
    }

    @Inject
    @JvmField
    var repository: MyRepository? = null

    private val credential: LiveData<CredentialEntity>?

    private fun updateEvent(event: Event) {
        (this.event as MutableLiveData<Event>).postValue(event)
    }

    init {
        DaggerRepositoryComponent.create().inject(this)
        credential = repository!!.getCredential()
    }

    fun init(speechService: SpeechService) {
        credential!!.observeForever { credentialEntity ->
            Log.d(
                TAG, "observeForever(): credentialEntity = " + credentialEntity?.credential
            )
            Observable.just(Objects.requireNonNull<CredentialEntity>(credentialEntity))
                .map { cred -> ByteArrayInputStream(cred.credential.toByteArray()) }
                .subscribeOn(Schedulers.io())
                .subscribe { speechService.setCredential(it) }

            speechService.subscribe(observer!!)
        }
    }

    override fun onCleared() {
        super.onCleared()
        event = null
        if (disposable != null) {
            disposable!!.dispose()
            disposable = null
        }
        observer = null
    }

    companion object {
        private val TAG = MyViewModel::class.java.simpleName
    }
}
