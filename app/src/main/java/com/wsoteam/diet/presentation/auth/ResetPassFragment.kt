package com.wsoteam.diet.presentation.auth

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.wsoteam.diet.R
import com.wsoteam.diet.utils.InputValidation
import kotlinx.android.synthetic.main.fragment_reset_pass.*

class ResetPassFragment : Fragment(R.layout.fragment_reset_pass) {


    private val emailValidator = InputValidation.EmailValidation(R.string.write_email)
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mAuth = FirebaseAuth.getInstance()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backButton.setOnClickListener { fragmentManager?.popBackStack() }

        emailEdit.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val error = emailValidator.validate(emailEdit)

                Log.d("kkk", "${error?.isNotEmpty()}")

                resetPass.isActivated = error?.isEmpty() ?: true
            }
        })

        resetPass.setOnClickListener { resetPassword() }
    }

    private fun resetPassword() {

        mAuth.sendPasswordResetEmail(emailEdit.text.toString())
                .addOnCompleteListener { task ->

                    if (task.isSuccessful) {
                        Log.d("kkk", "Email sent." + emailEdit.text.toString())
        //                        showToastMessage(getString(R.string.forgot_pass_check_email))
        //                        finish()
                    } else {
                        Log.d("kkk", "Error")
                    }
                }.addOnFailureListener { e ->
                    if (e is FirebaseAuthInvalidUserException) {

                        e.errorCode

                        Log.d("kkk", "e.errorCode = ${e.errorCode}  --- ${e.localizedMessage}")
        //                        showToastMessage(getString(R.string.forgot_pass_wrong_user))
                    }


                    Log.d("kkk", e.javaClass.toString())
                }
    }
}