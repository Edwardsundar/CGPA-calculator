package com.demo.cgpaapp.data

data class Semesters(
    val listOfSubjects : List<Subject>,
    var GPA : Float = 0f
)

data class Regulation(
    val _2017 : DepartmentEntity,
    val _2021 : DepartmentEntity
)

data class DepartmentEntity(
    val allSem : List<Semesters>,
    var CGPA : Float = 0f
)

data class Subject(
    val code: String,
    val title: String,
    val credit: Int,
    var grade : Points = Points.U
)

