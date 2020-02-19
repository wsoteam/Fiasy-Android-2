package com.wsoteam.diet.presentation.auth

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.facebook.FacebookSdk.getApplicationContext
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.wsoteam.diet.R
import com.wsoteam.diet.utils.InputValidation
import com.wsoteam.diet.utils.hideKeyboard
import kotlinx.android.synthetic.main.fragment_reset_pass.*
import android.view.Gravity
import android.widget.TextView
import androidx.annotation.ColorInt


class ResetPassFragment : Fragment(R.layout.fragment_reset_pass) {


    private val emailValidator = InputValidation.EmailValidation(R.string.auth_user_email_missmatch)
    private lateinit var mAuth: FirebaseAuth
    private var internetBad: InternetBad? = null

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

                if(error == null) email.isErrorEnabled = false
            }
        })

        resetPass.isActivated = false
        resetPass.setOnClickListener {
            if (resetPass.isActivated && hasNetwork(context)) {
                resetPassword()
                emailEdit?.hideKeyboard()
            } else{
                val error = emailValidator.validate(emailEdit)
                if (error != null) email.error = error
            }
        }
    }

    private fun resetPassword() {

        stateProgress()
        mAuth.sendPasswordResetEmail(emailEdit.text.toString())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("kkk", "Email sent." + emailEdit.text.toString())
//                        showToastMessage(getString(R.string.forgot_pass_check_email))
                        showToastMessage("Письмо отправлено на указанный адресс электронной почты. Проверьте почту.")
//                        this.fragmentManager?.popBackStack()

                        stateDone()
                    } else {
                        Log.d("kkk", "Error")
                        stateStart()
                    }
                }.addOnFailureListener { e ->
                    if (e is FirebaseAuthInvalidUserException) {

                        when (e.errorCode) {
                            "ERROR_USER_NOT_FOUND" -> showToastMessage("Введенный Email адрес не зарегистрирован или заблокирован. Пройдите регистрацию.", Color.parseColor("#73cc0808"))

                        }

                        Log.d("kkk", "e.errorCode = ${e.errorCode}  --- ${e.localizedMessage}")
                        //                        showToastMessage(getString(R.string.forgot_pass_wrong_user))
                    }

                    Log.d("kkk", "e.errorCode = ${e.message}")
                }
    }

    private fun showToastMessage(charSequence: CharSequence){
        showToastMessage(charSequence, Color.parseColor("#73000000"))
    }

    private fun showToastMessage(charSequence: CharSequence, @ColorInt color: Int){
        val toast = Toast.makeText(getApplicationContext(),
                charSequence,
                Toast.LENGTH_LONG)
        toast.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 100)
        toast.view.backgroundTintList = ColorStateList.valueOf(color)
        val text: TextView = toast.view.findViewById(android.R.id.message)
        text.setTextColor(Color.WHITE)

        toast.show()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (activity is InternetBad) {
            internetBad = activity as InternetBad?
        }
    }

    override fun onDetach() {
        super.onDetach()
        internetBad = null
    }

    private fun hasNetwork(context: Context?): Boolean {
        if (context == null) return false
        val activeNetwork = ContextCompat.getSystemService(context, ConnectivityManager::class.java)!!
                .activeNetworkInfo


        val reesult =  activeNetwork != null && activeNetwork.isConnected

        return  if (reesult){
            true
        }else{
            internetBad?.show()
            false
        }
    }

    private fun stateStart(){
        progressBar2.visibility = View.GONE
        imageCheckMark.visibility = View.GONE
        btnTxt.visibility = View.VISIBLE
        btnTxt.text = getString(R.string.reset_send)
    }

    private fun stateProgress(){
        progressBar2.visibility = View.VISIBLE
        imageCheckMark.visibility = View.GONE
        btnTxt.visibility = View.GONE
    }

    private fun stateDone(){
        progressBar2.visibility = View.GONE
        imageCheckMark.visibility = View.VISIBLE
        btnTxt.visibility = View.VISIBLE
        btnTxt.text = "ОТПРАВЛЕННО"
    }
}