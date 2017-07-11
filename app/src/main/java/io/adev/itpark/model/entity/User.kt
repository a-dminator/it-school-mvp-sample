package io.adev.itpark.model.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "users")
class User {

    @PrimaryKey
    var id: Long = 0

    var fName: String = ""
    var lName: String = ""
    var login: String = ""
    var photo: String = ""

}