package io.awesdroid.asrdemokt.db.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import io.awesdroid.asrdemokt.db.entity.CredentialEntity

/**
 * @auther Awesdroid
 */
@Dao
interface CredentialDao {

    @Query("select * from credential where id = :id")
    fun loadCredential(id: Int): LiveData<CredentialEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(credentialEntities: List<CredentialEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCredential(credentialEntity: CredentialEntity)
}
