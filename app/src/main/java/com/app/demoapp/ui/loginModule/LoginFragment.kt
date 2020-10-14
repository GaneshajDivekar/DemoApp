package com.app.demoapp.ui.loginModule

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.navigation.findNavController
import com.app.demoapp.R
import com.app.demoapp.databinding.FragmentLoginBinding
import com.app.demoapp.presentation.base.BaseFragment
import com.app.demoapp.ui.mainModule.MainActivity
import com.app.demoapp.utils.DialogUtils
import com.app.demoapp.utils.SessionManger
import com.app.demoapp.utils.SessionManger.Companion.PREF_FILE_NAME
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.common.api.Status
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class LoginFragment : BaseFragment<FragmentLoginBinding, LoginViewModel>(), LoginNavigator,
    GoogleApiClient.OnConnectionFailedListener {
    val loginViewModel: LoginViewModel by viewModel()
    var fragmentLoginBinding: FragmentLoginBinding? = null
    private val TAG = MainActivity::class.java.simpleName
    private val RC_SIGN_IN = 7

    private var mGoogleApiClient: GoogleApiClient? = null
    var sessionManger: SessionManger? = null

    override fun getLayoutId(): Int {
        return R.layout.fragment_login
    }

    override fun getViewModel(): LoginViewModel {
        return loginViewModel
    }

    override fun setUp(view: View, savedInstanceState: Bundle?) {
        fragmentLoginBinding = getViewDataBinding()
        fragmentLoginBinding?.loginViewModel = this

        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()

        mGoogleApiClient = (activity as MainActivity)?.mGoogleApiClient
        (activity as MainActivity).toolbarTitle.setText("G+ Login")

        fragmentLoginBinding!!.btnSingIn.setSize(SignInButton.SIZE_STANDARD)
        fragmentLoginBinding!!.btnSingIn.setScopes(gso.scopeArray)
        sessionManger = SessionManger(requireContext(), PREF_FILE_NAME)
        fragmentLoginBinding?.btnSingIn!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                clickOnLogin(p0!!)
            }

        })


    }

    override fun clickOnLogin(view: View) {

        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
        startActivityForResult(signInIntent, RC_SIGN_IN)

        //
    }



    private fun handleSignInResult(result: GoogleSignInResult) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess)
        if (result.isSuccess) {
            // Signed in successfully, show authenticated UI.
            val acct = result.signInAccount
            Log.e(TAG, "display name: " + acct!!.displayName)
            val personName = acct.displayName
            val personPhotoUrl = acct.photoUrl.toString()
            val email = acct.email
            Log.e(
                TAG, "Name: " + personName + ", email: " + email
                        + ", Image: " + personPhotoUrl
            )
            Toast.makeText(requireContext(),"App Login Successfully", Toast.LENGTH_SHORT).show()
            view?.findNavController()?.navigate(R.id.action_loginFragment_to_notesListFragmet)
            sessionManger?.setCurrentUserLoggedInMode(true)

        } else {
            // Signed out, show unauthenticated UI.
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            handleSignInResult(result!!)
        }
    }

    override fun onStart() {
        super.onStart()
        val opr =
            Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient)
        if (opr.isDone) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in")
            val result = opr.get()
            handleSignInResult(result)
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            showProgressDialog()
            opr.setResultCallback(object :
                ResultCallback<GoogleSignInResult?> {
                override fun onResult(p0: GoogleSignInResult) {
                    hideProgressDialog()
                    handleSignInResult(p0!!)
                }
            })
        }
    }

    override fun onConnectionFailed(@NonNull connectionResult: ConnectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:$connectionResult")
    }

    private fun showProgressDialog() {
        DialogUtils.startProgressDialog(requireContext())
    }

    private fun hideProgressDialog() {
        DialogUtils.stopProgressDialog()
    }



}