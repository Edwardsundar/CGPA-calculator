package com.demo.cgpaapp.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.demo.cgpaapp.data.ListOfDepartment

@Composable
fun MainScreen(navController: NavController) {
    var dep by remember {
        mutableStateOf(0)
    }
    var regulation by remember {
        mutableStateOf(0)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .offset(y = 200.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            TextWithIcon(
                title = ListOfDepartment.all[dep],
                list = ListOfDepartment.all,
                modifier = Modifier.width(200.dp)
            ){ index->
                dep = index
            }
            Spacer(modifier = Modifier.width(10.dp))
            TextWithIcon(
                title = ListOfDepartment.regulation[regulation],
                list = ListOfDepartment.regulation,
                modifier = Modifier.fillMaxWidth(1f)
            ){ index->
                regulation = index
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(18.dp))
                .background(color = Blue)
                .width(200.dp)
                .border(
                    width = 2.dp,
                    color = Color.White,
                    shape = RoundedCornerShape(18.dp)
                )
                .clickable {
                    navController.navigate(Nav.GetScoreScreen.root + "/$dep/$regulation")
                },
            contentAlignment = Alignment.Center
        ){
            Text(
                text = "Submit",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(8.dp)
            )
        }
    }
}

@Composable
fun TextWithIcon(
    modifier: Modifier = Modifier,
    list : List<String> = emptyList(),
    title : String,
    onItemClick : (Int)->Unit
){
    var isClicked by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = modifier
            .border(width = 2.dp , color = Color.White , shape = RoundedCornerShape(10.dp))
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    isClicked = true
                }
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                textAlign = TextAlign.Start,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth(0.8f),
                maxLines = 1
            )
            Icon(
                imageVector = if (isClicked) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = null,
            )
        }
        DropdownMenu(
            expanded = isClicked,
            onDismissRequest = { isClicked = false}
        ){
            list.forEachIndexed { index, s ->
                DropdownMenuItem(
                    text = {
                        Text(text = s)
                    },
                    onClick = {
                        onItemClick(index)
                        isClicked = false
                    }
                )
            }
        }
    }
}
