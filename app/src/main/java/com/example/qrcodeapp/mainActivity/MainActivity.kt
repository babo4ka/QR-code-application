package com.example.qrcodeapp.mainActivity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.qrcodeapp.R
import com.example.qrcodeapp.mainActivity.pages.mainPage.MainPageActivity
import com.example.qrcodeapp.ui.theme.QRCodeAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QRCodeAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainActivityPage("Android")
                }
            }
        }
    }
}

@Composable
fun MainActivityPage(name: String, modifier: Modifier = Modifier) {
    Box(modifier = Modifier
        .fillMaxSize()
    ){
        Column(
            Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween) {

            MainPageActivity().MainPage(name = "fe")

            Row (horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .height(80.dp)
                    .fillMaxWidth()){

                Box(modifier = Modifier
                    .background(Color.Red)
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .weight(1f)){
                    MenuButton(text = "главная")
                }
                Box(modifier = Modifier
                    .background(Color.DarkGray)
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .weight(1f)){
                    MenuButton(text = "сканер")
                }
                Box(modifier = Modifier
                    .background(Color.Green)
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .weight(1f)){
                    MenuButton(text = "моё")
                }
            }
        }
    }
}




@Composable
fun MenuButton(text: String){
    TextButton(onClick = { /*TODO*/ }) {
        Column {
           Text(text = text)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MainActivityPreview() {
    QRCodeAppTheme {
        MainActivityPage("Android")
    }
}