package io.adev.itpark.model.entity

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

@Dao
interface UserDao {

    @Query("SELECT * FROM users")
    fun all(): List<User>

    @Query("SELECT * FROM users WHERE id >= :minId")
    fun allMinId(minId: Int): List<User>

    @Insert
    fun insert(user: User)

}