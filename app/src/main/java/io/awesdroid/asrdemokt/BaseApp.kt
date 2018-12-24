package io.awesdroid.asrdemokt

import android.app.Application

/**
 * @author awesdroid
 */
class BaseApp : Application() {
    init {
        instance = this
    }

    companion object {

        lateinit var instance: BaseApp
    }
}