package com.demo.cgpaapp.presentation

import com.demo.cgpaapp.domain.SemesterDetails

data class ScreenState(
    val semesterDetails: SemesterDetails,
    val CGPA : Float = 0f
)