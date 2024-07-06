package com.example.mad_project.screens

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import com.example.movieappmad24.components.Bars.SimpleTopAppBar
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@Composable
fun SummaryScreen(
    navController: NavController,
    summaryJson: String
) {
    val summaryData = JSONObject(summaryJson)

    val origin = summaryData.getString("startLocation")
    val destination = summaryData.getString("destination")
    val flight = summaryData.optJSONObject("selectedFlight")
    val sights = summaryData.optJSONArray("selectedSights")

    val context = LocalContext.current
    val activity = context as? Activity

    Scaffold(
        topBar = {
            SimpleTopAppBar(title = "Summary", navController = navController)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Trip Summary", style = MaterialTheme.typography.headlineLarge)

            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "From: $origin")
            Text(text = "To: $destination")

            flight?.let {
                Text(text = "Selected Flight:")
                Text(text = "Airline: ${it.getString("airline")}")
                Text(text = "Departure: ${it.getString("departure_at")}")
                Text(text = "Return: ${it.getString("return_at")}")
                Text(text = "Price: ${it.getString("price")} €")
                Text(text = "Flight Number: ${it.getString("flight_number")}")
            }

            sights?.let {
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Selected Sights:")
                for (i in 0 until it.length()) {
                    val sight = it.getJSONObject(i)
                    Text(text = "${i + 1}. ${sight.getString("name")}")
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    val pdfFile = generatePdf(context, summaryData)
                    sharePdf(context, pdfFile)
                    activity?.finish()
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp)
            ) {
                Text("Finish and Share")
            }
        }
    }
}

fun generatePdf(context: Context, summaryData: JSONObject): File {
    val pdfDocument = PdfDocument()
    val paint = Paint()
    val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
    val page = pdfDocument.startPage(pageInfo)
    val canvas: Canvas = page.canvas

    // Drawing the content on the PDF
    var yPosition = 50f
    canvas.drawText("Trip Summary", 40f, yPosition, paint)
    yPosition += 30f
    canvas.drawText("From: ${summaryData.getString("startLocation")}", 40f, yPosition, paint)
    yPosition += 30f
    canvas.drawText("To: ${summaryData.getString("destination")}", 40f, yPosition, paint)

    val flight = summaryData.optJSONObject("selectedFlight")
    flight?.let {
        yPosition += 30f
        canvas.drawText("Selected Flight:", 40f, yPosition, paint)
        yPosition += 20f
        canvas.drawText("Airline: ${it.getString("airline")}", 40f, yPosition, paint)
        yPosition += 20f
        canvas.drawText("Departure: ${it.getString("departure_at")}", 40f, yPosition, paint)
        yPosition += 20f
        canvas.drawText("Return: ${it.getString("return_at")}", 40f, yPosition, paint)
        yPosition += 20f
        canvas.drawText("Price: ${it.getString("price")} €", 40f, yPosition, paint)
        yPosition += 20f
        canvas.drawText("Flight Number: ${it.getString("flight_number")}", 40f, yPosition, paint)
    }

    val sights = summaryData.optJSONArray("selectedSights")
    sights?.let {
        yPosition += 30f
        canvas.drawText("Selected Sights:", 40f, yPosition, paint)
        for (i in 0 until it.length()) {
            yPosition += 20f
            val sight = it.getJSONObject(i)
            canvas.drawText("${i + 1}. ${sight.getString("name")}", 40f, yPosition, paint)
        }
    }

    pdfDocument.finishPage(page)

    val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "TripSummary.pdf")
    try {
        pdfDocument.writeTo(FileOutputStream(file))
        Toast.makeText(context, "PDF saved to ${file.absolutePath}", Toast.LENGTH_LONG).show()
    } catch (e: IOException) {
        e.printStackTrace()
        Toast.makeText(context, "Error saving PDF: ${e.message}", Toast.LENGTH_LONG).show()
    } finally {
        pdfDocument.close()
    }
    return file
}

fun sharePdf(context: Context, pdfFile: File) {
    val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", pdfFile)
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "application/pdf"
        putExtra(Intent.EXTRA_STREAM, uri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    context.startActivity(Intent.createChooser(shareIntent, "Share PDF"))
}
