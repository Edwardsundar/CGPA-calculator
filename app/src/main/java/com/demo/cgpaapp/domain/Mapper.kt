package com.demo.cgpaapp.domain

import com.demo.cgpaapp.data.Departments
import com.demo.cgpaapp.data.ListOfDepartment
import com.demo.cgpaapp.data.Regulations

object Mapper {
    fun getSelectedSemSub(
        regulations: Regulations ,
        departments: Departments
    ):SemesterDetails{
        val dep = when(departments){
            Departments.BE_EEE -> ListOfDepartment.beEee
            Departments.BE_ECE -> TODO()
            Departments.BE_CSE -> TODO()
            Departments.BE_MECH -> TODO()
        }

        return SemesterDetails( allSem = dep.allSem )
    }
}