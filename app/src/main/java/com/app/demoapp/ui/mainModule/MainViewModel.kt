package com.app.demoapp.ui.mainModule

import android.app.Application
import com.app.demoapp.presentation.base.BaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class MainViewModel(application: Application) : BaseViewModel(application) {
    private val uiScope = CoroutineScope(Dispatchers.Main)


}
