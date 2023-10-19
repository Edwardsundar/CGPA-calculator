package com.demo.cgpaapp.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.TextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import com.demo.cgpaapp.data.Points
import com.demo.cgpaapp.data.Subject
import com.demo.cgpaapp.pdf.CreatePdf
import com.demo.cgpaapp.ui.theme.Green
import com.demo.cgpaapp.ui.theme.Orange
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SubjectListing(
    viewModel:CGPAViewModel = hiltViewModel()
){
    val semState = viewModel.state.value.semesterDetails.allSem

    var showCGPA by remember {
        mutableStateOf(false)
    }
    var semIndex by remember {
        mutableIntStateOf(0)
    }
    val context = LocalContext.current

    var showAlertDialog by remember {
        mutableStateOf(false)
    }
    var regNo by remember {
        mutableStateOf("")
    }
    var name by remember {
        mutableStateOf("")
    }
    var clgName by remember {
        mutableStateOf("")
    }

    var isProgress by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(true){
        isProgress = true
        delay(3000)
        isProgress = false
    }

    if (showAlertDialog){
        Dialog(
            onDismissRequest = {showAlertDialog = false}
        ){
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                shape = RoundedCornerShape(15.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(18.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){

                    TextField(
                        value = regNo,
                        onValueChange = {
                            regNo = it
                        },
                        placeholder = {
                            Text(text = "Register No" , color = Color.LightGray)
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp , bottom = 10.dp)
                    ){
                        Text(
                            text = "If you Want",
                            color = Color.Black,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    TextField(
                        value = name,
                        onValueChange = {
                            name = it
                        },
                        modifier = Modifier
                            .fillMaxWidth(),
                        placeholder = {
                            Text(text = "Name..." , color = Color.LightGray)
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    TextField(
                        value = clgName,
                        onValueChange = {
                            clgName = it
                        },
                        modifier = Modifier
                            .fillMaxWidth(),
                        placeholder = {
                            Text(text = "Collage Name" , color = Color.LightGray)
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ){
                        PDFButton(text = "CGPA") {
                            viewModel.viewModelScope.launch {
                                showAlertDialog = false
                                isProgress = true
                                viewModel.onEvent(ScreenEvents.CalculateCGPA)
                                CreatePdf.createCGPAPDF(
                                    allSem = semState,
                                    cgpa = viewModel.state.value.CGPA,
                                    context = context ,
                                    regNo = regNo,
                                    name = name ,
                                    collageName = clgName
                                )
                                isProgress = false
                            }
                        }
                        PDFButton(text = "GPA") {
                            viewModel.viewModelScope.launch {
                                showAlertDialog = false
                                viewModel.onEvent(ScreenEvents.CalculateGPA(sem = semIndex))
                                isProgress = true
                                CreatePdf.createGPAPDF(
                                    context = context,
                                    semesters = semState.get(semIndex),
                                    sem = semIndex + 1,
                                    regNo = regNo,
                                    name = name,
                                    collageName = clgName
                                )
                                isProgress = false
                            }
                        }
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 18.dp)
    ) {

        LazyRow(modifier = Modifier.padding(top = 20.dp) ,
            horizontalArrangement = Arrangement.SpaceEvenly) {
            items(semState.size){index->
                Spacer(modifier = Modifier.width(20.dp))
                BottomOptions(text = "Sem ${index+1}"  ,if(semIndex == index) Red else Orange){
                    semIndex = index
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .padding(8.dp)
                .weight(1f)
        ) {
            item {
                semState.get(semIndex).listOfSubjects.forEachIndexed { subIndex, subject ->
                    SubjectBox(
                        subject = subject,
                        viewModel = viewModel,
                        sem = semIndex,
                        sub = subIndex
                    )
                    Spacer(modifier = Modifier.height(18.dp))
                }
            }
        }
        Row(
            modifier = Modifier
                .background(Color.Transparent)
                .padding(10.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ){
            BottomOptions(text = "Sem${semIndex+1} GPA") {
                viewModel.onEvent(ScreenEvents.CalculateGPA(semIndex))
                showCGPA = true
            }
            Spacer(modifier = Modifier.width(5.dp))
            BottomOptions(text = "CGPA"){
                viewModel.onEvent(ScreenEvents.CalculateCGPA)
                showCGPA = true
            }
            Spacer(modifier = Modifier.width(5.dp))
            BottomOptions(text = "PDF"){
                showAlertDialog = true
            }
        // warning: The following options were not recognized by any processor:
        // '[dagger.fastInit, dagger.hilt.android.internal.disableAndroidSuperclassValidation,
        // dagger.hilt.android.internal.projectType, dagger.hilt.internal.useAggregatingRootProcessor, kapt.kotlin.generated]'
        }
    }
    if (showCGPA){
        Dialog(
            onDismissRequest = {showCGPA = false}
        ){
            Text(
                text = "Your Cgpa is = ${viewModel.state.value.CGPA}\n " +
                        "Your Sem${semIndex+1} GPA = ${semState.get(semIndex).GPA}",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                color = Color.White,
                modifier = Modifier
                    .background(Color.Transparent.copy(alpha = 0.6f))
            )
        }
    }
    if(isProgress){
        Column(modifier = Modifier.fillMaxSize().background(Color.Transparent) ,
            verticalArrangement = Arrangement.Center ,
            horizontalAlignment = Alignment.CenterHorizontally){
            CircularProgressIndicator(
                modifier = Modifier.padding(16.dp),
                strokeWidth = 5.dp
            )
        }
    }
}


@Composable
fun PDFButton(text: String , onClick: () -> Unit){
    Column{
        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clip(RoundedCornerShape(16.dp))
                .clickable {
                    onClick()
                }
                .background(Orange),
            contentAlignment = Alignment.Center
        ) {
            Text(text = text , modifier = Modifier.padding(10.dp))
        }
    }
}
@Composable
fun BottomOptions(
    text : String,
    color: Color = Orange,
    onClick: ()-> Unit
){
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable {
                onClick()
            }
            .background(color)
    ) {
        Text(
            text = text,
            modifier = Modifier
                .padding(10.dp)
        )
    }
}

@Composable
fun SubjectBox(
    subject : Subject,
    modifier: Modifier = Modifier,
    viewModel : CGPAViewModel,
    sem : Int,
    sub : Int
){
    var isClicked by remember {
        mutableStateOf(false)
    }
    Card(
        backgroundColor = Color.Black,
        shape = RoundedCornerShape(20.dp),
        elevation = 8.dp
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(start = 12.dp)
                .fillMaxWidth()
                .clickable {
                    isClicked = !isClicked
                }
        ) {
            Box(
                modifier = Modifier
                    .width(8.dp)
                    .height(34.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Green),
            )
            Spacer(Modifier.width(4.dp))
            Column(
                modifier = Modifier
                    .padding(12.dp)
            ) {
                if (isClicked){
                    Row(
                        modifier = Modifier
                            .width(275.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(color = MaterialTheme.colorScheme.background)
                            .padding(5.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Points.values().forEach {point->
                            GradeSelectionItem(
                                grade = point
                            ){selectedPoint->
                                isClicked = !isClicked
                                viewModel.onEvent(
                                    ScreenEvents.UpdateDetails(
                                        gradePoints = selectedPoint,
                                        credit = subject.credit,
                                        subject = sub,
                                        sem = sem
                                    )
                                )
                            }
                        }
                    }
                }
                else{
                    Text(
                        text = subject.title,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                        color = Color.White,
                        maxLines = 1,
                        modifier = Modifier
                            .width(275.dp),
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    text = subject.code,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White,
                    )
            }
            Text(
                text = subject.grade.point,
                color = Color.White,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold)
            )
        }
    }
}

@Composable
fun GradeSelectionItem(
    grade : Points,
    onClick : (Points)-> Unit
){
    Box(
        modifier = Modifier
            .size(35.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Green)
            .clickable {
                       onClick(grade)
            },
        contentAlignment = Alignment.Center
    ){
        Text(
            text = grade.point,
            color = Color.White,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold)
        )
    }
}
/*
        ScrollableTabRow(
            selectedTabIndex = selectedTabIndex
        ) {
            semState.forEachIndexed { index, subjects ->
                Tab(
                    selected = index == selectedTabIndex,
                    onClick = {
                        selectedTabIndex = index
                    },
                    text = {
                        Text(
                            text = "Sem ${index + 1}"
                        )
                    }
                )
            }
        }
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ){

        }
 */