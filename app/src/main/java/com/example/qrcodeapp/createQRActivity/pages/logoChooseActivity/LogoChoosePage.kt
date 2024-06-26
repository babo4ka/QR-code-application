package com.example.qrcodeapp.createQRActivity.pages.logoChooseActivity

import android.content.res.AssetManager
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.qrcodeapp.createQRActivity.pages.logoChooseActivity.ui.theme.QRCodeAppTheme
import com.example.qrcodeapp.database.CurrentDataHandler


@Composable
fun LogoChoosePage(action: (String) -> Unit) {
    val context = LocalContext.current
    val assets = context.resources.assets

    val activeLogo = remember {
        mutableStateOf("")
    }

    val premiumLogos = listOf("android.png", "minecraft.png", "pinterest.png", "tiktok.png", "xbox.png")

    val firstRow = listOf("telegram.png", "minecraft.png", "apple.png")
    val secondRow = listOf("burger.png", "music.png", "android.png", "student.png")
    val thirdRow = listOf("pinterest.png", "steam.png", "xbox.png", "yandex.png")
    val fourthRow = listOf("firefox.png", "tiktok.png")

    fun checkForPremuim(logoId:Int, row:Int):Boolean{
        val user = CurrentDataHandler.getActiveUser()

        return if(user == null || !user.premium){
            when(row){
                1 -> firstRow[logoId] !in premiumLogos
                2 -> secondRow[logoId] !in premiumLogos
                3 -> thirdRow[logoId] !in premiumLogos
                4 -> fourthRow[logoId] !in premiumLogos
                else -> false
            }
        }else{
            true
        }
    }


    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            if(CurrentDataHandler.getActiveUser() != null){
                Row(
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)

                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        NoLogoButton(
                            action = {
                                action("")
                                activeLogo.value = ""
                            },
                            active = activeLogo.value
                        )
                    }

                    firstRow.forEachIndexed{ id, item ->
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth()
                                .weight(1f)
                        ) {
                            LogoButton(
                                action = {
                                    action(item)
                                    activeLogo.value = item
                                },
                                enabled = checkForPremuim(id, 1),
                                logoName = item,
                                active = activeLogo.value,
                                assets = assets
                            )
                        }
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    secondRow.forEachIndexed { id, item ->
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth()
                                .weight(1f)
                        ) {
                            LogoButton(
                                action = {
                                    action(item)
                                    activeLogo.value = item
                                },
                                logoName = item,
                                active = activeLogo.value,
                                assets = assets,
                                enabled = checkForPremuim(id, 2)
                            )
                        }
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    thirdRow.forEachIndexed { id, item ->
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth()
                                .weight(1f)
                        ) {
                            LogoButton(
                                action = {
                                    action(item)
                                    activeLogo.value = item
                                },
                                logoName = item,
                                active = activeLogo.value,
                                assets = assets,
                                enabled = checkForPremuim(id, 3)
                            )
                        }
                    }

                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    fourthRow.forEachIndexed { id, item ->
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth()
                                .weight(1f)
                        ) {
                            LogoButton(
                                action = {
                                    action(item)
                                    activeLogo.value = item
                                },
                                logoName = item,
                                active = activeLogo.value,
                                assets = assets,
                                enabled = checkForPremuim(id, 4)
                            )
                        }
                    }

                    Spacer(modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth()
                        .weight(1f))
                    Spacer(modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth()
                        .weight(1f))

                }



            }else{
                Text(modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = "Выбирать логотип могут только авторизированные пользователи. " +
                        "Без авторизации всегда будет использоваться логотип приложения.")
            }

        }

    }
}


@Composable
fun LogoButton(
    action: () -> Unit,
    logoName: String,
    active: String,
    enabled:Boolean,
    assets: AssetManager
) {

    val logoFile = assets.open(logoName)
    val byteArray = logoFile.readBytes()
    val logoImg = BitmapFactory
        .decodeByteArray(byteArray, 0, byteArray.size)
        .asImageBitmap()

    Button(modifier = Modifier
        .shadow(1.dp)
        .fillMaxSize(),
        shape = RectangleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (active == logoName) Color.Black else Color.White
        ),
        enabled = enabled,
        onClick = { action() }) {

        Image(
            bitmap = logoImg, contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )
    }

}

@Composable
fun NoLogoButton(
    active: String,
    action: () -> Unit
) {


    Button(modifier = Modifier
        .shadow(1.dp)
        .fillMaxSize(),
        shape = RectangleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (active == "") Color.Black else Color.White,
            contentColor = if (active == "") Color.White else Color.Black
        ),
        onClick = { action() }) {

        Text(text = "Без лого")
    }

}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun LogoChoosePagePreview() {
    QRCodeAppTheme {
        LogoChoosePage(action = {})
    }
}