package com.mainpackage.foodies.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.mainpackage.foodies.R
import com.mainpackage.foodies.fragment.LoginFragment


class LoginRegisterActivity : AppCompatActivity() {

    @SuppressLint("IntentWithNullActionLaunch")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_register)

        val sharedPreferences = getSharedPreferences(
            getString(R.string.shared_preferences),
            Context.MODE_PRIVATE
        )

        //If already logged in once, directly open Dashboard
        if (sharedPreferences.getBoolean("isLoggedIn", false)) {
            val intent = Intent(this, Dashboard::class.java)
            startActivity(intent)
            finish()
        } else {
            openLoginFragment()
        }
    }

    private fun openLoginFragment() {
        supportFragmentManager.beginTransaction().replace(
            R.id.frameLayout,
            LoginFragment(this)
        ).commit()

        supportActionBar?.title = "DashBoard"
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        when (supportFragmentManager.findFragmentById(R.id.frameLayout)) {
            !is LoginFragment -> openLoginFragment()
            else -> super.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                openLoginFragment()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
