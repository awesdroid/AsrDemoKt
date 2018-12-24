package io.awesdroid.asrdemokt.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.util.Log
import io.awesdroid.asrdemokt.db.MyDatabase
import io.awesdroid.asrdemokt.db.entity.CredentialEntity

/**
 * @author awesdroid
 */
class MyRepository internal constructor(database: MyDatabase?) {

    private var credential: MediatorLiveData<CredentialEntity>? = null

    init {
        setCredential(database)
    }

    private fun setCredential(database: MyDatabase?) {
        Log.d(TAG, "setCredential(): databse = $database")
        credential = MediatorLiveData()
        credential!!.addSource(database!!.credentialDao().loadCredential(0)) { credentialEntity ->
            Log.d(TAG, "addSource(): " + credentialEntity)
            if (credentialEntity != null) {
                credential!!.postValue(credentialEntity)
            }
        }

    }

    fun getCredential(): LiveData<CredentialEntity>? {
        return credential
    }

    companion object {
        private val TAG = MyRepository::class.java.simpleName
    }
}
