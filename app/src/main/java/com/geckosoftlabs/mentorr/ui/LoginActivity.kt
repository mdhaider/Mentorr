package com.geckosoftlabs.mentorr.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.geckosoftlabs.mentorr.R
import com.geckosoftlabs.mentorr.databinding.ActivityLoginBinding
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit

private const val TAG = "LoginActivity"
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    lateinit var systemVerification: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        binding = ActivityLoginBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val callbacks= object: PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(p0, p1)
                systemVerification = p0
            }


            override fun onCodeAutoRetrievalTimeOut(p0: String) {
                super.onCodeAutoRetrievalTimeOut(p0)
                Log.e(TAG, "onCodeAutoRetrievalTimeOut: ${p0}")
            }

            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                Log.e(TAG, "onVerificationCompleted: ${p0.smsCode}")

            }

            override fun onVerificationFailed(p0: FirebaseException) {
                if (p0 is FirebaseAuthInvalidCredentialsException) {
                    Log.e(TAG, "onVerificationFailed: ${p0}")
                } else if (p0 is FirebaseTooManyRequestsException) {
                    Log.e(TAG, "onVerificationFailed: ${p0}")
                }
            }

        }

        binding.btnGetOtp.setOnClickListener {
            val options = PhoneAuthOptions.newBuilder(Firebase.auth)
                .setPhoneNumber(binding.etPhoneNum.text.toString())       // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(this)                 // Activity (for callback binding)
                .setCallbacks(callbacks)     // OnVerificationStateChangedCallbacks
                .build()
            PhoneAuthProvider.verifyPhoneNumber(options)
        }

        binding.btnVerify.setOnClickListener {
            if (binding.etEnterOtp.text?.isNotEmpty() == true) {
                signIn_with_credential(binding.etEnterOtp.text.toString())
            }
        }
    }

    private fun signIn_with_credential(code: String) {
        val credential = PhoneAuthProvider.getCredential(systemVerification, code)
        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this) {
            Log.e(TAG, "signIn: Hey  you  digned  in")
        }.addOnFailureListener(this) {
            Toast.makeText(this, "Sign  in with ${credential.smsCode} failed:(", Toast.LENGTH_LONG)
                .show()
        }
    }
}