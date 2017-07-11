package io.adev.itpark

import android.app.Application
import android.content.Context

var appContext: Context? = null

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = this
    }

}







//class App : Application() {
//
//    companion object {
//        var db: Db? = null
//    }
//
//    override fun onCreate() {
//        super.onCreate()
//        db = Room.databaseBuilder(this, Db::class.java, "db").build()
//    }
//
//}