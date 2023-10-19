package com.demo.cgpaapp.pdf

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.demo.cgpaapp.Constants
import com.demo.cgpaapp.data.Points
import com.demo.cgpaapp.data.Semesters
import com.demo.cgpaapp.data.Subject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import kotlin.math.floor

fun makeToast(text : String , context: Context){
    Toast.makeText(context , text, Toast.LENGTH_SHORT).show()
}



object CreatePdf{

    suspend fun createCGPAPDF(
        allSem : List<Semesters>,
        cgpa : Float,
        context: Context,
        regNo : String,
        name: String = "",
        course :String =  Constants.course,
        collageName : String = ""
    ){

        val storagePermission = Manifest.permission.WRITE_EXTERNAL_STORAGE
        val permissionStatus = ContextCompat.checkSelfPermission(context , storagePermission)
        if (permissionStatus != PackageManager.PERMISSION_GRANTED){
            makeToast("Please Accept Storage Permission For Create the PDF :-)" , context)
            return
        }

        val document = PdfDocument()
        //page info consist width and height of the page
        val pageInfo = PdfDocument.PageInfo.Builder(1080,1920,1).create()
        val page = document.startPage(pageInfo)

        val canvas = page.canvas
        val startX = 80f
        val endX = pageInfo.pageWidth - 80f
        var startY = 100f

        val textPaint = Paint()
        textPaint.color = Color.BLACK
        textPaint.textSize = 50f

        drawProfile(regNo, name, course, startX, endX, startY, canvas , collageName = collageName)
        startY += 300
        startY = CGPAGrid(startX , startY , endX , canvas , listOf(Semesters(listOf() , GPA = 100f))) + 100
        startY = CGPAGrid(startX , startY , endX , canvas , allSem) + 150
        val formattedPercentage = String.format("%.2f",cgpa * 10)
        canvas.drawText("Percentage = $formattedPercentage%         CGPA = $cgpa " ,endX - 910,  startY ,textPaint )

        document.finishPage(page)

        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(downloadsDir , "${regNo}-CGPA.pdf")
        val fos = withContext(Dispatchers.IO) {
            FileOutputStream(file)
        }
        document.writeTo(fos)

        document.close()
        withContext(Dispatchers.IO) {
            fos.close()
        }

        makeToast("CGPA is Success fully saved",context)
    }

    suspend fun CGPAGrid(startX: Float , y:Float , endX: Float , canvas: Canvas , allSem : List<Semesters> ):Float{
        val spaceY = 100
        var endY = y+ spaceY
        var startY = y
        val paint = Paint()
        paint.strokeWidth = 2f
        paint.color = Color.BLACK
        val semSpace = startX + 500
        val gpaSpace = semSpace + 150

        val textPaint = Paint()
        textPaint.color = Color.BLACK
        textPaint.textSize = 40f

        allSem.forEachIndexed { index, semesters ->
            if (semesters.GPA > -1){
                val textY = ((startY + endY) / 2)+15
                canvas.drawLine(startX , startY , endX , startY ,paint )
                canvas.drawLine(endX , startY , endX , endY ,paint )
                canvas.drawLine(endX , endY , startX , endY ,paint )
                canvas.drawLine(startX , endY , startX , startY ,paint )

                canvas.drawLine(semSpace , startY , semSpace , endY ,paint )
                canvas.drawLine(gpaSpace , startY , gpaSpace , endY ,paint )

                if (allSem.size == 1){
                    val semTextX =  ((startX + semSpace) / 2) - (90)
                    canvas.drawText("Semesters" , semTextX , textY , textPaint)

                    val gpaTextX = ((semSpace + gpaSpace) / 2) - (30)
                    canvas.drawText("GPA" , gpaTextX , textY , textPaint)

                    val percentage = "Percentage"

                    val percentageTextX = ((gpaSpace + endX) / 2) - (120)
                    canvas.drawText(percentage , percentageTextX , textY , textPaint)
                    return endY - spaceY
                }

                val semTextX =  ((startX + semSpace) / 2) - (120)
                canvas.drawText("Semester - ${index + 1}" , semTextX , textY , textPaint)

                val gpaTextX = ((semSpace + gpaSpace) / 2) - (semesters.GPA.toString().length * 20)/2
                canvas.drawText("${semesters.GPA}" , gpaTextX , textY , textPaint)
                val formattedPercentage = String.format("%.2f",semesters.GPA * 10)
                val percentage = "$formattedPercentage%"

                val percentageTextX = ((gpaSpace + endX) / 2) - (percentage.length * 20)/2
                canvas.drawText(percentage , percentageTextX , textY , textPaint)

                startY = endY
                endY += spaceY
            }
        }
        return endY - spaceY
    }

    suspend fun createGPAPDF(context: Context , semesters : Semesters , sem : Int,collageName : String = "",
                             regNo : String , name: String , course :String =  Constants.course){
        val storagePermission = Manifest.permission.WRITE_EXTERNAL_STORAGE
        val permissionStatus = ContextCompat.checkSelfPermission(context , storagePermission)
        if (permissionStatus != PackageManager.PERMISSION_GRANTED){
            makeToast("Please Accept Storage Permission For Create the PDF :-)" , context)
            return
        }

        val document = PdfDocument()
        //page info consist width and height of the page
        val pageInfo = PdfDocument.PageInfo.Builder(1080,1920,1).create()
        val page = document.startPage(pageInfo)

        val canvas = page.canvas
        val startX = 80f
        val endX = pageInfo.pageWidth - 80f
        val startY = 100f

        drawProfile(regNo = regNo ,name =  name ,course =  course,
            startX = startX , endX = endX , startY =  startY ,canvas =  canvas , sem = sem ,collageName = collageName )
        draw(startX ,endX, canvas , listOf(Subject("","",1,Points.U)), startY+300 /*400*/)
        val endY =draw(startX , endX, canvas , semesters.listOfSubjects , startY+400 /*500*/) + 100f

        val textPaint = Paint()
        textPaint.color = Color.BLACK
        textPaint.textSize = 50f

        canvas.drawText("GPA = ${semesters.GPA}" , endX - 270f  , endY , textPaint)
        //canvas.drawText("Edward" , 500f , 900f , paint)
         document.finishPage(page)

        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(downloadsDir , "${regNo}-Semester-${sem}.pdf")
        val fos = withContext(Dispatchers.IO) {
            FileOutputStream(file)
        }
        document.writeTo(fos)

        document.close()
        withContext(Dispatchers.IO) {
            fos.close()
        }

        makeToast("Success fully saved",context)

    }
    suspend fun draw(pageStart : Float, pageEnd : Float, canvas : Canvas, listOfSubjects : List<Subject>, y : Float):Float{

        val paint = Paint()
        paint.strokeWidth = 2f
        paint.color = Color.BLACK

        val textPaint = Paint()
        textPaint.color = Color.BLACK
        textPaint.textSize = 30f

        // draw grid
        val startX = pageStart
        var startY = y
        val endX = pageEnd
        var endY = 0f

        listOfSubjects.forEachIndexed { index, subject ->

            val cellHeight =  floor((subject.title.length / 20).toDouble()).toInt()
            endY = startY+ 100f

            // for vertical line
            val sNoX = startX + 80
            val subCodeX = sNoX + 160
            val subTitleX = subCodeX + 400
            val creditsX = subTitleX+100
            val gradeX = creditsX+100

            // for text
            val textSNoX =( (startX + sNoX)/2)
            val textSubCodeX = ((sNoX + subCodeX) / 2) - ((subject.code.length / 2f) * 15)
            var textSubTitleX = ((subCodeX + subTitleX) / 2) - ((subject.title.length / 2f) * 15)
            val textCreditsX = (subTitleX + creditsX) / 2 - (Math.floor(subject.credit / 10.toDouble()) * 15).toFloat()
            val textGradeX = (creditsX + gradeX) / 2 - (subject.grade.point.length / 2f) * 15
            val textGradePointX = ((gradeX + endX) / 2) - (Math.floor(subject.grade.score / 10.toDouble()) * 15).toFloat()
            var textY = ((startY+endY)/2)+15


            if (listOfSubjects.size != 1){ endY = drawText(startY, endY, subCodeX, subTitleX, subject.title, canvas) }

            textY = ((startY+endY)/2)+15
            canvas.drawLine(startX , startY , startX , endY , paint)
            canvas.drawLine(startX , startY , endX , startY , paint)
            canvas.drawLine(endX , startY , endX , endY , paint)

            canvas.drawLine(sNoX , startY , sNoX , endY , paint)
            canvas.drawLine(subCodeX , startY , subCodeX , endY , paint)
            canvas.drawLine(subTitleX , startY , subTitleX , endY , paint)
            canvas.drawLine(creditsX , startY , creditsX , endY , paint)
            canvas.drawLine(gradeX , startY , gradeX , endY , paint)


            if (listOfSubjects.size == 1){
            canvas.drawText(
                "S.NO",
                textSNoX - 37, // ((subject.code.length / 2f) * 15)
                textY,
                textPaint
            )
            canvas.drawText(
                "Code",
                textSubCodeX- 30, //((subject.code.length / 2f) * 15)
                textY,
                textPaint
            )

            canvas.drawText(
                "Subject Title",
                textSubTitleX- ((13 / 2f) * 15) ,
                textY,
                textPaint
            )
                canvas.drawText(
                    "Credit",
                    textCreditsX - 45,
                    textY,
                    textPaint
                )
            canvas.drawText(
                "Grade",
                textGradeX-  ((5 / 2f) * 15),
                textY,
                textPaint
            )
            canvas.drawText(
                "G.P",
                textGradePointX-  ((3 / 2f) * 15) ,
                textY,
                textPaint
            )
            }
            else{
                canvas.drawText(
                    (index+1).toString(),
                    textSNoX,
                    textY,
                    textPaint
                )
                canvas.drawText(
                    subject.code,
                    textSubCodeX,
                    textY,
                    textPaint
                )

                canvas.drawText(
                    (subject.credit).toString(),
                    textCreditsX,
                    textY,
                    textPaint
                )
                canvas.drawText(
                    (subject.grade.point),
                    textGradeX,
                    textY,
                    textPaint
                )
                canvas.drawText(
                    (subject.grade.score).toString(),
                    textGradePointX,
                    textY,
                    textPaint
                )
            }
            startY = endY
        }
        canvas.drawLine(startX , startY , endX , startY , paint)

        return endY
    }
    fun drawProfile(
        regNo:String ,
        name : String = "" ,
        course : String ,
        startX : Float,
        endX : Float,
        startY: Float,
        canvas: Canvas,
        sem : Int = 0,
        collageName : String = ""
        )
    {
        val endY = startY + 300
        val paint = Paint()

        paint.color = Color.GRAY
        canvas.drawRect(startX , startY , endX , endY , paint)

        paint.color = Color.BLACK
        paint.strokeWidth = 5f

        canvas.drawLine(startX , startY , startX , endY , paint)
        canvas.drawLine(startX , startY , endX , startY , paint)
        canvas.drawLine(endX , startY , endX , endY , paint)
        canvas.drawLine(startX , endY , endX , endY , paint)

        paint.color = Color.WHITE
        paint.textSize = 45f

        val startTextX = startX + 100
        var startTextY = startY + 60

        canvas.drawText(regNo , startTextX , startTextY , paint)
        if (name.isNotBlank()) {
            startTextY += 60
            canvas.drawText(name, startTextX, startTextY, paint)
        }

        paint.textSize = 30f
        if (sem != 0){
            startTextY += 50
            canvas.drawText("Semester ${sem}", startTextX, startTextY, paint)
        }
        startTextY += 50
        canvas.drawText(course , startTextX , startTextY , paint)

        if (collageName.isNotBlank()){
            startTextY += 50
            canvas.drawText(collageName, startTextX, startTextY, paint)
        }
    }
    fun drawText(startY : Float, beforeEndY: Float, startX : Float, endX : Float, title : String,canvas: Canvas):Float{
        val tempEndY = 30f
        var textSubTitleX : Float
        var textX : Float
        var textY = ((startY+beforeEndY)/2)+15
        var endY = beforeEndY

        val textPaint = Paint()
        textPaint.color = Color.BLACK
        textPaint.textSize = 30f

        val lintTitle = StringBuilder()
        val tempTitle = StringBuilder()
        title.forEachIndexed { index, c ->
            tempTitle.append(c)
            if (c == ' ' ){
                if (lintTitle.length + tempTitle.length >= 25){
                    textSubTitleX = ((startX + endX) / 2) - ((lintTitle.length / 2f) * 15)
                    canvas.drawText(
                        lintTitle.toString(),
                        textSubTitleX,
                        textY,
                        textPaint
                    )
                    endY += tempEndY
                    textY += 40
                    lintTitle.delete(0 , lintTitle.length-1)
                }
                lintTitle.append(tempTitle)
                tempTitle.delete(0 , tempTitle.length-1)
                val a = 10
            }
            else if (index == title.length-1){
                if (lintTitle.length + tempTitle.length < 25 ) {
                    lintTitle.append(tempTitle)
                    textX = ((startX + endX) / 2) - ((lintTitle.length / 2f) * 15)
                    canvas.drawText(
                        lintTitle.toString(),
                        textX,
                        textY,
                        textPaint
                    )

                }
                else if(lintTitle.length + tempTitle.length >= 25 ){
                    textX = ((startX + endX) / 2) - ((lintTitle.length / 2f) * 15)
                    canvas.drawText(
                        lintTitle.toString(),
                        textX,
                        textY,
                        textPaint
                    )
                    textX = ((startX + endX) / 2) - ((tempTitle.length / 2f) * 15)
                    endY += tempEndY
                    textY += 40
                    canvas.drawText(
                        tempTitle.toString(),
                        textX,
                        textY,
                        textPaint
                    )
                }
            }
        }
        return endY
    }
}