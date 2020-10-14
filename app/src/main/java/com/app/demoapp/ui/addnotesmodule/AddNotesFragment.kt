package com.app.demoapp.ui.addnotesmodule

import android.os.Bundle
import android.view.View
import com.app.demoapp.R
import com.app.demoapp.databinding.FragmentAddNotesBinding
import com.app.demoapp.presentation.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddNotesFragment:BaseFragment<FragmentAddNotesBinding,AddNotesViewModel>(),AddNotesNavigator{
    var fragmentAddNotesBinding:FragmentAddNotesBinding?=null
    val addNotesViewModel:AddNotesViewModel by viewModel()
    override fun getLayoutId(): Int {
       return  R.layout.fragment_add_notes
    }

    override fun getViewModel(): AddNotesViewModel {
        return  addNotesViewModel
    }

    override fun setUp(view: View, savedInstanceState: Bundle?) {
        fragmentAddNotesBinding=getViewDataBinding()

    }

}