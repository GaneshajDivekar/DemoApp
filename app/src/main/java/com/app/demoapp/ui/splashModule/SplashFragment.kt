package com.app.demoapp.ui.splashModule

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.navigation.findNavController
import com.app.demoapp.R
import com.app.demoapp.databinding.FragmentSplashBinding
import com.app.demoapp.presentation.base.BaseFragment
import com.app.demoapp.utils.SessionManger
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashFragment : BaseFragment<FragmentSplashBinding, SplashViewModel>(), SplashNavigator {
    private var handler: Handler? = null
    val splashViewModel: SplashViewModel by viewModel()
    var fragmentSplashBinding: FragmentSplashBinding? = null
    var sessionManger: SessionManger? = null

    override fun getLayoutId(): Int {
        return R.layout.fragment_splash
    }

    override fun getViewModel(): SplashViewModel {
        return splashViewModel

    }

    override fun setUp(view: View, savedInstanceState: Bundle?) {
        fragmentSplashBinding = getViewDataBinding()
        sessionManger = SessionManger(requireContext(), SessionManger.PREF_FILE_NAME)
        handler = Handler()
        handler!!.postDelayed({
            if (sessionManger!!.getCurrentUserLoggedInMode() == false) {
                view.findNavController().navigate(R.id.action_homeFragment_to_loginFragmet)
            } else {
                view.findNavController().navigate(R.id.action_splashFragment_to_notesListFragment)
            }
        }, 2000)


    }
}