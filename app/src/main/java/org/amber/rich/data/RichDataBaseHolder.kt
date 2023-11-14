package org.amber.rich.data

import android.content.Context
import androidx.room.Room

/**
 * create by colin
 * 2023/5/7
 */
object RichDataBaseHolder {

    private lateinit var database: RichDatabase

    private const val dbName = "rich_db"

    private var isComplete = false

    fun initDatabase(context: Context) {
        database = Room.databaseBuilder(
            context.applicationContext,
            RichDatabase::class.java, dbName
        ).build()
        isComplete = true
    }

    fun getDatabase(): RichDatabase {
        if (!isComplete) {
            throw RuntimeException("The database is not init")
        }
        return database
    }
}