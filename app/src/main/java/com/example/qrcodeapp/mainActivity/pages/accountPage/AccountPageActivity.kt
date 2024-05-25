package com.example.qrcodeapp.mainActivity.pages.accountPage

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.qrcodeapp.ModalDialog
import com.example.qrcodeapp.R
import com.example.qrcodeapp.createQRActivity.CreateQRMainActivity
import com.example.qrcodeapp.database.CurrentDataHandler
import com.example.qrcodeapp.database.entities.User
import com.example.qrcodeapp.database.viewModels.UserViewModel
import com.example.qrcodeapp.mainActivity.MainActivity
import com.example.qrcodeapp.mainActivity.Page
import kotlinx.coroutines.launch


@Composable
fun AccountPage(modifier: Modifier, uvm: UserViewModel?) {

    val logoSize = 50.dp
    val btnSize = 150.dp
    val txtSize = 15.sp

    val activeUser = remember {
        mutableStateOf(CurrentDataHandler.getActiveUser())
    }

    fun setActiveUser(user: User?) {
        activeUser.value = user
    }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val openModal = remember{
        mutableStateOf(false)
    }

    val dialogText = "Купить премиум-доступ, чтобы получить больше возможностей для редактирования, " +
            "а также неограниченное хранилище для Ваших QR-кодов"


    val createdBtnGradient = Brush.linearGradient(
        colorStops = arrayOf(
            0.0f to Color.Green,
            0.8f to Color.Blue
        ),
        start = Offset(0f, 0f),
        end = Offset(500f, 500f)
    )

    val scannedBtnGradient = Brush.linearGradient(
        colorStops = arrayOf(
            0.0f to Color.Yellow,
            0.8f to Color.Magenta
        ),
        start = Offset(0f, 0f),
        end = Offset(500f, 500f)
    )

    val buyPremiumBtnGradient = Brush.linearGradient(
        colorStops = arrayOf(
            0.0f to Color.Red,
            0.8f to Color.hsv(347f, 1f, .38f)
        ),
        start = Offset(500f, 0f),
        end = Offset(500f, 500f)
    )

    if (activeUser.value == null) {
        LoginRegistrationPage(modifier = modifier,
            uvm = uvm, action = {
                setActiveUser(it)
            })
    } else {
        when(openModal.value){
            true ->
                ModalDialog(title = "Купить премиум",
                    text = dialogText,
                    confirmText = "Купить премиум",
                    onDismissRequest = {
                        openModal.value = false
                    }
                ) {
                    openModal.value = false
                    CurrentDataHandler.setMainActivityPage(Page.ACCOUNT)
                    val intent = Intent(context, MainActivity::class.java)
                    ContextCompat.startActivity(context, intent, null)
                }
            else -> {}
        }

        Box(modifier = modifier) {
            Button(modifier = Modifier
                .shadow(1.dp),
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                ),
                onClick = {
                    CurrentDataHandler.setActiveUser(null)
                    setActiveUser(null)
                }) {
                Text(text = "Выйти из аккаунта")
            }
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(25.dp, Alignment.CenterVertically)
            ) {


                Text(
                    text = "Твои QR-коды, ${activeUser.value?.name}",
                    fontSize = 25.sp
                )

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(modifier = Modifier
                        .background(
                            brush = createdBtnGradient,
                            shape = RoundedCornerShape(25.dp)
                        )
                        .height(btnSize)
                        .width(btnSize),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.Black
                        ),
                        onClick = {
                            val intent = Intent(context, CreatedCodesActivity::class.java)
                            ContextCompat.startActivity(context, intent, null)
                        }) {

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                painter = painterResource(id = R.drawable.qr_logo),
                                contentDescription = null,
                                modifier = Modifier
                                    .width(logoSize)
                                    .height(logoSize)
                            )

                            Spacer(modifier = Modifier.height(25.dp))

                            Text(
                                text = "Созданные QR",
                                fontSize = txtSize,
                                textAlign = TextAlign.Center
                            )
                        }

                    }

                    Spacer(modifier = Modifier.width(25.dp))

                    Button(modifier = Modifier
                        .background(
                            brush = scannedBtnGradient,
                            shape = RoundedCornerShape(25.dp)
                        )
                        .height(btnSize)
                        .width(btnSize),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.Black
                        ),
                        onClick = {
                            val intent = Intent(context, ScannedCodesActivity::class.java)
                            ContextCompat.startActivity(context, intent, null)
                        }) {

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                painter = painterResource(id = R.drawable.scanner),
                                contentDescription = null,
                                modifier = Modifier
                                    .width(logoSize)
                                    .height(logoSize)
                            )

                            Spacer(modifier = Modifier.height(25.dp))

                            Text(
                                text = "Отсканированные QR",
                                fontSize = txtSize,
                                textAlign = TextAlign.Center
                            )
                        }

                    }
                }

                if(CurrentDataHandler.getActiveUser()?.premium != true){
                    Button(modifier = Modifier
                        .width(300.dp)
                        .height(50.dp)
                        .background(
                            brush = buyPremiumBtnGradient,
                            shape = RoundedCornerShape(20)
                        ),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.Black
                        ),

                        onClick = {
                            scope.launch {
                                uvm?.setPremium(activeUser.value!!)
                                activeUser.value = activeUser.value
                            }
                        }) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Купить премиум",
                                fontSize = 20.sp,
                                modifier = Modifier.weight(2f)
                            )

                            Button(
                                onClick = {
                                    openModal.value = true
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Transparent,
                                    contentColor = Color.Blue
                                ),
                                modifier = Modifier.weight(1f)
                            ) {
                                Image(
                                    painterResource(id = R.drawable.info),
                                    modifier = Modifier.fillMaxSize(),
                                    contentDescription = null
                                )
                            }
                        }

                    }
                }else{
                    Text(text = "У Вас имеется премиум-доступ, поздравляю!",
                        textAlign = TextAlign.Center)
                }
            }
        }
    }


}


@Preview(showSystemUi = true, showBackground = true)
@Composable
fun AccountPagePrev() {
    AccountPage(modifier = Modifier.fillMaxSize(), uvm = null)
}