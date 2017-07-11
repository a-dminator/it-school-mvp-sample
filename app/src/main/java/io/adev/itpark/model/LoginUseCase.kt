package io.adev.itpark.model

import com.google.gson.JsonParser
import io.adev.itpark.model.base.UseCase
import io.adev.itpark.model.entity.User
import io.reactivex.Observable
import okhttp3.Request

class LoginUseCase : UseCase<User, LoginUseCase.Criteria>() {

    class Criteria(
            val login: String,
            val password: String)

    override fun buildObservable(criteria: Criteria?): Observable<User> {
        return Observable.create { emitter ->

            val request = Request.Builder()
                    .url("http://207.254.71.167:8083/park/users/login?login=${criteria!!.login}&password=${criteria.password}")
                    .build()

            val response = okHttpClient.newCall(request)
                    .execute()
                    .body().string()

            val responseJson = JsonParser().parse(response).asJsonObject["response"].asJsonObject

            val user =
                    User().apply {
                        id    = responseJson["id"].asLong
                        fName = responseJson["fName"].asString
                        lName = responseJson["lName"].asString
                        login = responseJson["login"].asString
                        photo = responseJson["photo"].asString
                    }

            emitter.onNext(user)
        }
    }

}