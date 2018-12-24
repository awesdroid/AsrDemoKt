package io.awesdroid.asrdemokt.net

import io.awesdroid.asrdemokt.model.Account
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * @author awesdroid
 */
class BasicAuthInterceptor internal constructor(account: Account) : Interceptor {

    private val credentials: String

    init {
        this.credentials = Credentials.basic(account.user, account.password)
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val authenticatedRequest = request.newBuilder()
            .header("Authorization", credentials).build()
        return chain.proceed(authenticatedRequest)
    }

}
