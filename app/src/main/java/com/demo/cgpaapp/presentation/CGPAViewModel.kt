package com.demo.cgpaapp.presentation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.cgpaapp.Constants
import com.demo.cgpaapp.data.Departments
import com.demo.cgpaapp.data.Regulations
import com.demo.cgpaapp.domain.CGPARepository
import com.demo.cgpaapp.domain.Mapper
import com.demo.cgpaapp.domain.SemesterDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CGPAViewModel @Inject constructor(
    private val repository : CGPARepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    lateinit var state : MutableState<ScreenState>
    init {
        val semesterDetails = createMySemester()
        state = mutableStateOf(
            value = ScreenState(semesterDetails =semesterDetails))
    }
    private fun createMySemester():SemesterDetails{
        val reg = when(savedStateHandle.get<String>(Constants.REGULATION)?.toInt() ?: 0){
            0-> Regulations.SEVENTEEN
            1-> Regulations.TWENTY_ONE
            else-> Regulations.SEVENTEEN
        }
        val dep = when(savedStateHandle.get<String>(Constants.DEPARTMENT)?.toInt() ?: 0){
            0-> Departments.BE_EEE
            1-> Departments.BE_ECE
            2-> Departments.BE_MECH
            else -> Departments.BE_CSE
        }
        Constants.course = dep.department
        return Mapper.getSelectedSemSub(regulations = reg, departments = dep)
    }
    fun onEvent(screenEvents: ScreenEvents){
        when(screenEvents){
            is ScreenEvents.CalculateGPA -> {
                calculateGPA(screenEvents.sem)
            }
            ScreenEvents.CalculateCGPA->{
                var attendedSem = 0
                var sumOfGPA = 0f
                state.value.semesterDetails.allSem.forEachIndexed { index, semester ->
                    calculateGPA(index)
                    if (semester.GPA > 0f){
                        sumOfGPA += semester.GPA
                        attendedSem++
                    }
                }
                val formattedCGPA = String.format("%.2f",sumOfGPA / attendedSem.toFloat()).toFloat()
                state.value = state.value.copy(
                    CGPA = formattedCGPA
                )
            }

            is ScreenEvents.UpdateDetails -> {
                state.value.semesterDetails
                    .allSem.get(screenEvents.sem)
                    .listOfSubjects
                    .get(screenEvents.subject)
                    .grade = screenEvents.gradePoints
            }
        }
    }
    private fun calculateGPA(semester : Int){
        var obtainGradeAndCredit = 0
        var obtainCredit = 0
        val semesters =state.value.semesterDetails.allSem.get(semester)
        semesters.listOfSubjects.forEach { subject->
            if (subject.grade.score != 0){
                obtainCredit += subject.credit
                obtainGradeAndCredit += (subject.credit * subject.grade.score)
            }
        }
        val formattedGPA = String.format("%.2f",(obtainGradeAndCredit / obtainCredit.toFloat())).toFloat()
        semesters.GPA = formattedGPA
        
    }
}