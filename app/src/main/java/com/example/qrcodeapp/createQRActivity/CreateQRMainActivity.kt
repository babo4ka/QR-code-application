package com.example.qrcodeapp.createQRActivity

import android.content.Intent
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
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.qrcodeapp.R
import com.example.qrcodeapp.createQRActivity.ui.theme.QRCodeAppTheme
import com.example.qrcodeapp.mainActivity.MainActivity
import java.io.File

class CreateQRMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QRCodeAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CreateQRActivityPage("Android")
                }
            }
        }
    }
}

@Composable
fun CreateQRActivityPage(name: String, modifier: Modifier = Modifier) {


    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize().padding(horizontal = 10.dp)) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)

            ) {
                Button(
                    onClick = {
                        val intent = Intent(context, MainActivity::class.java)
                        ContextCompat.startActivity(context, intent, null)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.Black
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.back),
                        modifier = Modifier.fillMaxSize(),
                        contentDescription = null
                    )
                }
                //Spacer(modifier = Modifier.weight(1f))

                Box(modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ){
                    Text(text = "Создать",
                        fontSize = 20.sp)
                }

                //Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = { /*TODO*/ },
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.Black,
                        containerColor = Color.Green
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "Создать")
                }
            }

            Box(modifier = Modifier.fillMaxWidth()){

            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun CreateQRActivityPagePreview() {
    QRCodeAppTheme {
        CreateQRActivityPage("Android")
    }
}