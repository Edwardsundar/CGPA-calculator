package com.demo.cgpaapp.presentation

import com.demo.cgpaapp.data.Points

sealed class ScreenEvents{

    data class CalculateGPA(val sem : Int) : ScreenEvents()
    data class UpdateDetails( val gradePoints : Points  , val credit : Int , val subject : Int , val sem : Int) : ScreenEvents()
    object CalculateCGPA : ScreenEvents()

}