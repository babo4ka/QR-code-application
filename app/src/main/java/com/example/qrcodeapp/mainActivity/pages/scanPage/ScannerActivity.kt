package com.example.qrcodeapp.mainActivity.pages.scanPage

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.qrcodeapp.mainActivity.pages.scanPage.ui.theme.QRCodeAppTheme


@Composable
fun ScannerPage(action:()->Unit) {

    Button(onClick = { action() }) {
        Text("Scan!")
    }
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    QRCodeAppTheme {
        ScannerPage(){

        }
    }
}