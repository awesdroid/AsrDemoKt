package io.awesdroid.asrdemokt.model

/**
 * @author awesdroid
 */
data class Account(var user: String,
                   var password: String,
                   var baseUrl: String,
                   var path: String) {

    override fun toString(): String {
        return "{$user, $password, $baseUrl, $path}"
    }

    override fun equals(other: Any?): Boolean {
        return if (other is Account) {
            val a = other as Account?
            a!!.user == user &&
                    a.password == password &&
                    a.baseUrl == baseUrl &&
                    a.path == path
        } else {
            false
        }
    }

    override fun hashCode(): Int {
        return (user.hashCode()
                + 10 * password.hashCode()
                + 100 * baseUrl.hashCode()
                + 1000 * path.hashCode())
    }
}
