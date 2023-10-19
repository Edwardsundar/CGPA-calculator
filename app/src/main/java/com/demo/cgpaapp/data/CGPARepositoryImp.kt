package com.demo.cgpaapp.data

import com.demo.cgpaapp.domain.CGPARepository

class CGPARepositoryImp: CGPARepository {
    override fun getSemester(): DepartmentEntity {
        return DepartmentEntity(allSem = emptyList() )
    }
}