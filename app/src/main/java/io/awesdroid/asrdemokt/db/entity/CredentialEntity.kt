package io.awesdroid.asrdemokt.db.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * @auther Awesdroid
 */
@Entity(tableName = "credential")
class CredentialEntity(var credential: String) {
    @PrimaryKey
    var id: Int = 0
}
//class CredentialEntity : Credential {
//    @PrimaryKey
//    var id: Int = 0
//
//    override var credential: String? = null
//
//    companion object {
//
//        fun create(credential: String): CredentialEntity {
//            val credentialEntity = CredentialEntity()
//            credentialEntity.credential = credential
//            return credentialEntity
//        }
//    }
//}
