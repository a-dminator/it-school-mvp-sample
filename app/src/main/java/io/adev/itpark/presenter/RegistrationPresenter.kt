package io.adev.itpark.presenter

import android.app.Activity
import io.adev.itpark.presenter.contract.RegistrationContract
import io.adev.itpark.presenter.navigator.RegistrationNavigator

class RegistrationPresenter(
        private val view: RegistrationContract.View,
        activity: Activity) : RegistrationContract.Presenter {

    private val navigator = RegistrationNavigator(activity)

    override fun onRegistrationClick(fName: String, lName: String, login: String, password: String) {
        navigator.toLogin()
    }

}