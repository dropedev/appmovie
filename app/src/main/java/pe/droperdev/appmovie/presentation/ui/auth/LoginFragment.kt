package pe.droperdev.appmovie.presentation.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_login.*
import pe.droperdev.appmovie.R
import pe.droperdev.appmovie.data.repository.AuthRepositoryImpl
import pe.droperdev.appmovie.domain.model.UserModel
import pe.droperdev.appmovie.presentation.Resource
import pe.droperdev.appmovie.presentation.ui.main.MainActivity

class LoginFragment : Fragment(R.layout.fragment_login), View.OnClickListener {

    private val authViewModel by viewModels<AuthViewModel> {
        AuthViewModelFactory(
            AuthRepositoryImpl()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        eventUI()
    }

    private fun eventUI() {
        btn_log_in.setOnClickListener(this)
    }

    private fun getUserName(): String {
        return et_username.text.toString()
    }

    private fun getPassword(): String {
        return et_password.text.toString()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_log_in -> {
                authViewModel.login(getUserName(), getPassword())
                    .observe(viewLifecycleOwner, loginObserver)
            }
        }
    }

    private val loginObserver = Observer<Resource<UserModel?>> {
        when (it) {
            is Resource.Loading -> {
                progress_bar.visibility = VISIBLE
                btn_log_in.visibility = GONE
            }
            is Resource.Success -> {
                progress_bar.visibility = GONE
                btn_log_in.visibility = VISIBLE
                goToMain()
            }
            is Resource.Failure -> {
                progress_bar.visibility = GONE
                btn_log_in.visibility = VISIBLE
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun goToMain() {
        startActivity(Intent(requireContext(), MainActivity::class.java))
        activity?.finish()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }


}