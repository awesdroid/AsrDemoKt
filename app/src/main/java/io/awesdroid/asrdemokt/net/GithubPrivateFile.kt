package io.awesdroid.asrdemokt.net

/**
 * @author awesdroid
 */
class GithubPrivateFile {

    private val _links: Links? = null
    val content: String? = null
    private val download_url: String? = null

    val encoding: String? = null
    private val get_url: String? = null
    private val html_url: String? = null
    private val name: String? = null
    private val path: String? = null
    private val sha: String? = null
    private val size: String? = null
    private val type: String? = null
    private val url: String? = null

    internal inner class Links {
        var git: String? = null
        var html: String? = null
        var self: String? = null
    }

    override fun toString(): String {
        return (GithubPrivateFile::class.java.simpleName + "{"
                + "name=" + name
                + ", html_url=" + html_url
                + "}")
    }
}
