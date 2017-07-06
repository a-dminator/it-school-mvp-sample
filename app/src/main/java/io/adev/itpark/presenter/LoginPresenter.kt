package io.adev.itpark.presenter

import android.app.Activity
import io.adev.itpark.presenter.contract.LoginContract
import io.adev.itpark.presenter.navigator.LoginNavigator

class LoginPresenter(
        private val view: LoginContract.View,
        activity: Activity) : LoginContract.Presenter {

    private val navigator = LoginNavigator(activity)

    override fun onLoginClick(login: String, password: String) {
        navigator.toMain()
    }

    override fun onRegistrationClick() {
        navigator.toRegistration()
    }

}