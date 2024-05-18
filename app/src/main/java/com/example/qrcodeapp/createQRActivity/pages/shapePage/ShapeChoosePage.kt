package com.example.qrcodeapp.createQRActivity.pages.shapePage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.qrcodeapp.createQRActivity.pages.colorsAndBackgroundPage.ColorButton
import com.example.qrcodeapp.createQRActivity.pages.colorsAndBackgroundPage.ColorChoosePage
import com.example.qrcodeapp.createQRActivity.pages.shapePage.ui.theme.QRCodeAppTheme
import qrcode.color.Colors

class ShapeChoosePage : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QRCodeAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ShapeChoosePage()
                }
            }
        }
    }
}

@Composable
fun ShapeChoosePage(action: (shape:String)->Unit){

    val active = remember{
        mutableStateOf("Круги")
    }

    Box(modifier = Modifier.fillMaxSize()){
        Column(
            Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(5.dp)) {
            Row (horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)){

                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth()
                        .weight(1f)
                ){
                    ShapeButton(action = {
                        active.value = "Круги"
                        action("Круги") }, text= "Круги", active = active.value)
                }

                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth()
                        .weight(1f)
                ){
                    ShapeButton(action = {
                        active.value = "Квадраты"
                        action("Квадраты") }, text= "Квадраты", active = active.value)
                }

                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth()
                        .weight(1f)
                ){
                    ShapeButton(action = {
                        active.value = "Закругленные квадраты"
                        action("Закругленные квадраты") }, text= "Закругленные квадраты", active = active.value)
                }

            }

        }
    }
}


@Composable
fun ShapeButton(action:()->Unit,
                text:String,
                active:String){
    Button(modifier = Modifier
        .shadow(1.dp)
        .fillMaxSize(),
        shape = RectangleShape,
        colors = ButtonDefaults.buttonColors(
            contentColor = if(text == active) Color.White else Color.Black,
            containerColor = if(text == active) Color.Black else Color.White
        ),
        onClick = { action() }) {
        Text(text = text)
    }
}


@Preview(showSystemUi = true, showBackground = true)
@Composable
fun ShapeChoosePagePreview(){
    ShapeChoosePage(action = {})
}