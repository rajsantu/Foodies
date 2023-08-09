package com.mainpackage.foodies.fragment


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat

import com.mainpackage.foodies.R
import com.mainpackage.foodies.activity.Dashboard
import com.mainpackage.foodies.utils.ConnectionManager
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject

@Suppress("DEPRECATION")
class LoginFragment(val contextParam: Context) : Fragment() {

    private lateinit var txtSignUp: TextView
    private lateinit var etMobileNumber: EditText
    private lateinit var etPassword: EditText
    private lateinit var txtForgotPassword: TextView
    private lateinit var btnLogin: Button
    lateinit var fragmentLoginLayout: RelativeLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_login, container, false)

        etMobileNumber = view.findViewById(R.id.etMobileNumber)
        etPassword = view.findViewById(R.id.etPassword)
        txtForgotPassword = view.findViewById(R.id.txtForgotPassword)
        txtSignUp = view.findViewById(R.id.txtSignUp)
        btnLogin = view.findViewById(R.id.btnLogin)
        fragmentLoginLayout = view.findViewById(R.id.fragment_login_layout)

        fragmentLoginLayout.visibility = View.INVISIBLE
        txtSignUp.paintFlags = Paint.UNDERLINE_TEXT_FLAG

        btnLogin.setOnClickListener {

            if (etMobileNumber.text.isBlank() || etMobileNumber.text.length != 10) {
                etMobileNumber.error = "Enter valid Mobile Number"
            } else {
                if (etPassword.text.isBlank() || etPassword.text.length <= 4) {
                    etPassword.error = "Enter valid Password"
                } else {
                    loginUserFun()
                }
            }
        }

        txtForgotPassword.setOnClickListener {
            openForgotPasswordInputFragment()
        }

        txtSignUp.setOnClickListener {
            openRegisterFragment()
        }

        return view
    }

    private fun openForgotPasswordInputFragment() {
        val transaction = fragmentManager?.beginTransaction()

        transaction?.replace(
            R.id.frameLayout,
            ForgotPasswordInputFragment(contextParam)
        )
        transaction?.commit()
    }


    private fun openRegisterFragment() {

        val transaction = fragmentManager?.beginTransaction()

        transaction?.replace(
            R.id.frameLayout,
            RegisterFragment(contextParam)
        )
        transaction?.commit()
    }


    private fun loginUserFun() {

        val sharedPreferences = contextParam.getSharedPreferences(
            getString(R.string.shared_preferences),
            Context.MODE_PRIVATE
        )
        if (ConnectionManager().checkConnectivity(activity as Context)) {
            try {

                val loginUser = JSONObject()
                loginUser.put("mobile_number", etMobileNumber.text)
                loginUser.put("password", etPassword.text)

                val queue = Volley.newRequestQueue(activity as Context)
                val url = "http://" + getString(R.string.ip_address) + "/v2/login/fetch_result"
                val jsonObjectRequest = object : JsonObjectRequest(
                    Method.POST,
                    url,
                    loginUser,
                    Response.Listener {

                        val response = it.getJSONObject("data")
                        val success = response.getBoolean("success")
                        if (success) {
                            val data = response.getJSONObject("data")
                            sharedPreferences.edit().putBoolean("isLoggedIn", true).apply()
                            sharedPreferences.edit().putString("user_id", data.getString("user_id")).apply()
                            sharedPreferences.edit().putString("name", data.getString("name")).apply()
                            sharedPreferences.edit().putString("email", data.getString("email")).apply()
                            sharedPreferences.edit().putString("mobile_number", data.getString("mobile_number")).apply()
                            sharedPreferences.edit().putString("address", data.getString("address")).apply()

                            Toast.makeText(
                                contextParam,
                                "Welcome " + data.getString("name"),
                                Toast.LENGTH_SHORT
                            ).show()

                            userSuccessfullyLoggedIn()

                        } else {

                            val responseMessageServer = response.getString("errorMessage")
                            Toast.makeText(
                                contextParam,
                                responseMessageServer.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        fragmentLoginLayout.visibility = View.INVISIBLE
                    },
                    Response.ErrorListener {

                        fragmentLoginLayout.visibility = View.INVISIBLE

                        Toast.makeText(
                            contextParam,
                            "Some Error occurred!!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = getString(R.string.token)
                        return headers
                    }
                }
                queue.add(jsonObjectRequest)

            } catch (e: JSONException) {

                Toast.makeText(
                    contextParam,
                    "Some unexpected error occurred!!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {

            val alterDialog = androidx.appcompat.app.AlertDialog.Builder(activity as Context)
            alterDialog.setTitle("No Internet")
            alterDialog.setMessage("Check Internet Connection!")
            alterDialog.setPositiveButton("Open Settings") { _, _ ->
                val settingsIntent = Intent(Settings.ACTION_SETTINGS)
                startActivity(settingsIntent)
            }
            alterDialog.setNegativeButton("Exit") { _, _ ->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            alterDialog.create()
            alterDialog.show()
        }
    }

    @SuppressLint("IntentWithNullActionLaunch")
    fun userSuccessfullyLoggedIn() {
        val intent = Intent(activity as Context, Dashboard::class.java)
        startActivity(intent)
        activity?.finish()
    }

    override fun onResume() {
        if (!ConnectionManager().checkConnectivity(activity as Context)) {
            val alterDialog = androidx.appcompat.app.AlertDialog.Builder(activity as Context)
            alterDialog.setTitle("No Internet")
            alterDialog.setMessage("Check Internet Connection!")
            alterDialog.setPositiveButton("Open Settings") { _, _ ->
                val settingsIntent = Intent(Settings.ACTION_SETTINGS)
                startActivity(settingsIntent)
            }
            alterDialog.setNegativeButton("Exit") { _, _ ->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            alterDialog.setCancelable(false)
            alterDialog.create()
            alterDialog.show()
        }

        super.onResume()
    }

}
