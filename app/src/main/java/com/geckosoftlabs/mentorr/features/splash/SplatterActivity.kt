package com.geckosoftlabs.mentorr.features.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.geckosoftlabs.mentorr.MainActivity
import com.geckosoftlabs.mentorr.features.login.LoginActivity
import com.geckosoftlabs.mentorr.utils.Constants
import com.geckosoftlabs.mentorr.utils.DataStoreManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SplatterActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var dataStoreManager: DataStoreManager
    private var firebaseUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataStoreManager = DataStoreManager(this)
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseUser = firebaseAuth.currentUser
        checkUser(firebaseUser)
    }

    private fun checkUser(firebaseUser: FirebaseUser?) {
        if (firebaseUser == null) {
            setValue(false)
            goToLoginActivity()
        } else {
            setValue(true)
            goToHomeActivity()
        }
        finish()
    }

    private fun goToLoginActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
    }

    private fun goToHomeActivity() {
        val intent = Intent(this, MainActivity::class.java)
        val phone = firebaseUser?.phoneNumber
        intent.putExtra(Constants.PHONE_KEY, phone)
        startActivity(intent)
    }

    private fun setValue(loggedIn: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            dataStoreManager.saveLoggedInState(loggedIn)
        }
    }
}