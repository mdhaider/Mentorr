package com.geckosoftlabs.mentorr.features.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import com.geckosoftlabs.mentorr.databinding.ActivityLoginBinding
import androidx.lifecycle.repeatOnLifecycle
import com.geckosoftlabs.mentorr.features.home.MainRepository
import com.geckosoftlabs.mentorr.features.home.MainViewModel
import com.geckosoftlabs.mentorr.features.home.SomeViewModelFactory
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.flow.collect

private const val TAG = "LoginActivity"

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: MainViewModel
    lateinit var systemVerification: String
    //if code sending failed, will use to resend
    private var forceResendingToken: PhoneAuthProvider.ForceResendingToken? = null
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: MainViewModel by viewModels { SomeViewModelFactory(MainRepository(applicationContext)) }
        this.viewModel = viewModel

        firebaseAuth = FirebaseAuth.getInstance()

        val mCallBacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(p0, p1)
                systemVerification = p0
                forceResendingToken = p1
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
                .setCallbacks(mCallBacks)     // OnVerificationStateChangedCallbacks
                .build()
            PhoneAuthProvider.verifyPhoneNumber(options)
        }

        binding.btnVerify.setOnClickListener {
            if (binding.etEnterOtp.text?.isNotEmpty() == true) {
                signIn_with_credential(binding.etEnterOtp.text.toString())
            }
        }

        binding.btnSendOtp.setOnClickListener {
            val options = forceResendingToken?.let { it1 ->
                PhoneAuthOptions.newBuilder(firebaseAuth)
                    .setPhoneNumber(binding.etPhoneNum.text.toString())
                    .setTimeout(60L, TimeUnit.SECONDS)
                    .setActivity(this)
                    .setCallbacks(mCallBacks)
                    .setForceResendingToken(it1)
                    .build()
            }

            if (options != null) {
                PhoneAuthProvider.verifyPhoneNumber(options)
            }
        }
    }

    private fun signIn_with_credential(code: String) {
        val credential = PhoneAuthProvider.getCredential(systemVerification, code)
        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this) {
            Log.e(TAG, "signIn: Hey  you  digned  in")

        }.addOnFailureListener { e->
            Toast.makeText(this, "Sign  in with ${credential.smsCode} failed:(", Toast.LENGTH_LONG).show()
        }
    }

    /*private fun getMemo() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                getMemoFlow().collect { memo ->
                    Log.d("memo", memo)
                }
            }
        }
    }
*/
   /* private fun getMemoFlow() = dataStore.data
        .map { preferences ->
            preferences[MEMO_KEY] ?: ""
        }*/

    private fun setBtnMemo() {
        viewModel.setMemo("loggedout")
    }
}