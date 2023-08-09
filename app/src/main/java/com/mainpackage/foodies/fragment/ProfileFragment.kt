package com.mainpackage.foodies.fragment


import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.mainpackage.foodies.R

class ProfileFragment(private val contextParam: Context) : Fragment() {

    private lateinit var txtName: TextView
    private lateinit var txtEmail: TextView
    private lateinit var txtMobileNumber: TextView
    private lateinit var txtAddress: TextView

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        txtName = view.findViewById(R.id.txtName)
        txtEmail = view.findViewById(R.id.txtEmail)
        txtMobileNumber = view.findViewById(R.id.txtMobileNumber)
        txtAddress = view.findViewById(R.id.txtAddress)

        val sharedPreferences = contextParam.getSharedPreferences(
            getString(R.string.shared_preferences),
            Context.MODE_PRIVATE
        )

        txtName.text = sharedPreferences.getString("name", "")
        txtEmail.text = sharedPreferences.getString("email", "")
        txtMobileNumber.text = "+91-" + sharedPreferences.getString("mobile_number", "")
        txtAddress.text = sharedPreferences.getString("address", "")

        return view
    }

}
