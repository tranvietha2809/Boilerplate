package com.example.boilerplate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.commit
import com.example.boilerplate.databinding.ActivityMainBinding
import com.example.boilerplate.ui.login.LoginFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var activityMainBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)
        setUpFragment()
    }

    private fun setUpFragment() {
        supportFragmentManager.commit {
            supportActionBar?.hide()
            val mLoginFragment = LoginFragment()
            replace(activityMainBinding.fragmentContainerView.id, mLoginFragment)
            setReorderingAllowed(true)
            addToBackStack("login")
        }
    }
}