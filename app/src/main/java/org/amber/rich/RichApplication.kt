package org.amber.rich

import android.app.Application
import android.content.Context
import org.amber.rich.data.RichDataBaseHolder

/**
 * create by colin
 * 2023/5/4
 */
class RichApplication : Application() {

    companion object {
        lateinit var appContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        RichDataBaseHolder.initDatabase(this)
        appContext = applicationContext
        // CodeScannerManager.init(this)
    }
}