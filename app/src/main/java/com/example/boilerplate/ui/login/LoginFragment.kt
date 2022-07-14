package com.example.boilerplate.ui.login

import android.R.attr.data
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.boilerplate.MainActivity
import com.example.boilerplate.databinding.FragmentLoginBinding
import com.example.boilerplate.viewmodels.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@AndroidEntryPoint
class LoginFragment : Fragment() {

    lateinit var binding: FragmentLoginBinding
    private val viewModel : LoginViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        binding.btnLogin.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO){
                viewModel.doLogin(username = binding.etEmail.text.toString(), password = binding.etEmail.text.toString())
            }
        }
    }

    fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    when(it.isLoading) {
                        true -> (activity as MainActivity).activityMainBinding.progressLogin.visibility = View.VISIBLE
                        false -> (activity as MainActivity).activityMainBinding.progressLogin.visibility = View.GONE
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    if(it.errorMessage != null) {
                        Toast.makeText(
                            activity, it.errorMessage,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }
}