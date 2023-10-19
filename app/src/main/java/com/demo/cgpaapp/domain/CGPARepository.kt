package com.demo.cgpaapp.domain

import com.demo.cgpaapp.data.DepartmentEntity

interface CGPARepository {
    fun getSemester(): DepartmentEntity
}