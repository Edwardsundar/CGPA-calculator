package com.demo.cgpaapp

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.demo.cgpaapp.Constants.REQUEST_CODE
import com.demo.cgpaapp.presentation.MainScreen
import com.demo.cgpaapp.presentation.Nav
import com.demo.cgpaapp.presentation.SubjectListing
import com.demo.cgpaapp.ui.theme.CGPAAPPTheme
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        askPermission()
        setContent {
            CGPAAPPTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Nav.MainScreen.root
                    ){
                        composable(route = Nav.MainScreen.root){
                            MainScreen(navController)
                        }
                        composable(route = Nav.GetScoreScreen.root+"/{department}/{regulation}"){
                            SubjectListing()
                        }
                    }
                }
            }
        }
    }
    fun askPermission(){
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),REQUEST_CODE)
    }
//
//    fun createGPAPDF(){
//        val storagePermission = Manifest.permission.WRITE_EXTERNAL_STORAGE
//        val permissionStatus = ContextCompat.checkSelfPermission(this , storagePermission)
//        if (permissionStatus != PackageManager.PERMISSION_GRANTED){
//            askPermission()
//        }
//        if (permissionStatus != PackageManager.PERMISSION_GRANTED){
//            makeToast("Please Accept Storage Permission For Create the PDF :-)")
//            return
//        }
//
//        val document = PdfDocument()
//        //page info consist width and height of the page
//        val pageInfo = PdfDocument.PageInfo.Builder(1080,1920,1).create()
//        val page = document.startPage(pageInfo)
//
//        val canvas = page.canvas
//        val paint = Paint()
//        paint.setColor(Color.BLACK)
//        paint.textSize = 0.5f
//
//        canvas.drawText("Edward" , 500f , 900f , paint)
//        document.finishPage(page)
//
//        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
//        val file = File(downloadsDir , "Android Pdf.pdf")
//        val fos = FileOutputStream(file)
//        document.writeTo(fos)
//
//        document.close()
//        fos.close()
//
//        makeToast("Success fully saved")
//     }
//
//    fun makeToast(text:String){
//        Toast.makeText(this , text , Toast.LENGTH_SHORT).show()
//    }
}
