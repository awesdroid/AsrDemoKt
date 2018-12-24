package io.awesdroid.asrdemokt.net

import android.util.Base64
import android.util.Log
import io.awesdroid.asrdemokt.model.Account
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.CompletableFuture

/**
 * @author awesdroid
 */
object Net {
    private val TAG = Net::class.java.simpleName

    fun getCredential(account: Account): CompletableFuture<String> {
        Log.d(TAG, "getCredential: account = $account")

        val client = OkHttpClient.Builder()
            .addInterceptor(BasicAuthInterceptor(account))
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(account.baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val credential = retrofit.create(GetGithubPrivateFile::class.java)

        val url = String.format("repos/%s/%s", account.user, account.path)

        val call = credential.getCall(url)
        Log.d(TAG, "getCredential(): call = " + call.request())
        return CompletableFuture.supplyAsync {
            try {
                val response = call.execute()
                if ("base64" == response.body().encoding!!.toLowerCase()) {
                    val content = Base64.decode(response.body().content, Base64.DEFAULT)
                    return@supplyAsync String(content)
                } else {
                    return@supplyAsync null
                }
            } catch (e: IOException) {
                e.printStackTrace()
                return@supplyAsync null
            }
        }
    }
}
