package io.awesdroid.asrdemokt.db

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import android.util.Log
import com.google.gson.Gson
import io.awesdroid.asrdemokt.BaseApp
import io.awesdroid.asrdemokt.R
import io.awesdroid.asrdemokt.db.dao.CredentialDao
import io.awesdroid.asrdemokt.db.entity.CredentialEntity
import io.awesdroid.asrdemokt.model.Account
import io.awesdroid.asrdemokt.net.Net
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.io.InputStream
import java.io.InputStreamReader


/**
 * @author awesdroid
 */
@Database(entities = [CredentialEntity::class], version = 1)
abstract class MyDatabase : RoomDatabase() {

    abstract fun credentialDao(): CredentialDao

    companion object {
        private val TAG = MyDatabase::class.java.simpleName

        private const val DB_NAME = "credential.db"

        private var instance: MyDatabase? = null

        internal fun getInstance(context: BaseApp): MyDatabase? {
            if (instance == null) {
                synchronized(MyDatabase::class.java) {
                    if (instance == null) {
                        instance = buildDatabase(context.applicationContext)
                    }
                }
            }
            return instance
        }

        private fun buildDatabase(context: Context): MyDatabase {
            return Room.databaseBuilder(context, MyDatabase::class.java, DB_NAME)
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        Log.d(TAG, "onCreate(): db = " + context.getDatabasePath(DB_NAME))
                        Observable.just(R.raw.account)
                            .observeOn(Schedulers.io())
                            .map<InputStream> { id -> context.resources.openRawResource(id!!) }
                            .map { s -> InputStreamReader(s, "UTF-8") }
                            .map { isr -> Gson().fromJson(isr, Account::class.java) }
                            .map { accountEntity -> Net.getCredential(accountEntity).get() }
                            .map(::CredentialEntity)
                            .subscribe { credentialEntity ->
                                instance!!.runInTransaction {
                                    instance!!.credentialDao().insertCredential(credentialEntity)
                                }
                            }
                    }
                })
                .build()
        }
    }
}
