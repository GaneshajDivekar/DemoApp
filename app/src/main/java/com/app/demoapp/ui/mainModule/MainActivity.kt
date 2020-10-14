package com.app.demoapp.ui.mainModule


import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import com.app.demoapp.R
import com.app.demoapp.databinding.ActivityMainBinding
import com.app.demoapp.presentation.base.BaseActivity
import com.app.demoapp.utils.SessionManger
import com.app.demoapp.utils.SessionManger.Companion.PREF_FILE_NAME
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.common.api.Status
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(), MainNavigator,
    GoogleApiClient.OnConnectionFailedListener {

    var activityMainBinding: ActivityMainBinding? = null
    val mainViewModel: MainViewModel by viewModel()
    var mGoogleApiClient: GoogleApiClient? = null
    var mainNavigator: MainNavigator? = null
    var sessionManger: SessionManger? = null


    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun getViewModel(): MainViewModel {
        return mainViewModel
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = getViewDataBinding()
        mainNavigator = this
        setSupportActionBar(activityMainBinding!!.toolbar);
        sessionManger = SessionManger(this, PREF_FILE_NAME)
        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()

        mGoogleApiClient = GoogleApiClient.Builder(this)
            .enableAutoManage(this, this)
            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
            .build()

    }


    override fun setUp(savedInstanceState: Bundle?) {

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.logout ->
                logoutfromAccount()

            else -> {
            }
        }
        return false
    }

    private fun logoutfromAccount() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
            object : ResultCallback<Status?> {
                override fun onResult(p0: Status) {
                    if (sessionManger!!.getCurrentUserLoggedInMode() == true) {
                        sessionManger?.setCurrentUserLoggedInMode(false)
                        sessionManger?.clearAll()
                        finish()

                    } else {
                        Toast.makeText(this@MainActivity, "First Login app", Toast.LENGTH_SHORT)
                            .show()
                    }
                }


            })
    }

    override fun onConnectionFailed(p0: ConnectionResult) {

    }

    override fun onStart() {
        super.onStart()
        if (mGoogleApiClient != null) mGoogleApiClient!!.connect()
    }

    override fun onStop() {
        super.onStop()
        if (mGoogleApiClient != null && mGoogleApiClient!!.isConnected) {
            mGoogleApiClient!!.stopAutoManage(this)
            mGoogleApiClient!!.disconnect()
        }
    }


}









