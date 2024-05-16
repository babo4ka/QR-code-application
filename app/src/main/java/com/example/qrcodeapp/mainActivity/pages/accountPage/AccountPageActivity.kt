package com.example.qrcodeapp.mainActivity.pages.accountPage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qrcodeapp.R


@Composable
fun AccountPage(modifier: Modifier){

    val logoSize = 50.dp
    val btnSize = 150.dp
    val txtSize = 15.sp

    Box(modifier = modifier) {
        Row(horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize()) {
            Button(modifier = Modifier
                .height(btnSize)
                .width(btnSize),
                shape = RoundedCornerShape(25.dp),
                onClick = { /*TODO*/ }) {

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(painter = painterResource(id = R.drawable.qr_logo),
                        contentDescription = null,
                        modifier = Modifier
                            .width(logoSize)
                            .height(logoSize))

                    Spacer(modifier = Modifier.height(25.dp))

                    Text(text = "Созданные QR",
                        fontSize = txtSize)
                }

            }

            Spacer(modifier = Modifier.width(25.dp))

            Button(modifier = Modifier
                .height(btnSize)
                .width(btnSize),
                shape = RoundedCornerShape(25.dp),
                onClick = { /*TODO*/ }) {

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(painter = painterResource(id = R.drawable.scanner),
                        contentDescription = null,
                        modifier = Modifier
                            .width(logoSize)
                            .height(logoSize))

                    Spacer(modifier = Modifier.height(25.dp))

                    Text(text = "Отсканированные QR",
                        fontSize = txtSize)
                }

            }
        }
    }
}


@Preview(showSystemUi = true, showBackground = true)
@Composable
fun AccountPagePrev(){
    AccountPage(modifier = Modifier.fillMaxSize())
}