package io.adev.itpark

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import io.adev.itpark.model.entity.User
import io.adev.itpark.model.entity.UserDao

val db = Room.databaseBuilder(appContext!!, Db::class.java, "db").build()!!

@Database(entities = arrayOf(User::class), version = 1)
abstract class Db : RoomDatabase() {
    abstract fun userDao(): UserDao
}










