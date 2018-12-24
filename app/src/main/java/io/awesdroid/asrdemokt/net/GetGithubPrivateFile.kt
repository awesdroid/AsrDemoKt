package io.awesdroid.asrdemokt.net

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

/**
 * @author awesdroid
 */
interface GetGithubPrivateFile {
    @GET
    fun getCall(@Url url: String): Call<GithubPrivateFile>
}
