package com.demo.cgpaapp.presentation

sealed class Nav(val root: String){
    object MainScreen: Nav("MainScreen")
    object GetScoreScreen: Nav("GetScoreScreen")
}
