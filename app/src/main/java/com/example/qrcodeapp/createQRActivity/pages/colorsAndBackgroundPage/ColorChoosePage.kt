package com.example.qrcodeapp.createQRActivity.pages.colorsAndBackgroundPage

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
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.qrcodeapp.database.CurrentDataHandler
import qrcode.QRCode
import qrcode.QRCodeBuilder
import qrcode.color.Colors


@Composable
fun ColorChoosePage(action: (color: Int) -> Unit, type: String) {

    val context = LocalContext.current
    val assets = context.resources.assets

    val premiumColors = listOf(Colors.MAGENTA, Colors.CYAN, Colors.ORANGE, Colors.DEEP_PINK)

    val firstRowColors =
        listOf(Color(Colors.BLACK), Color(Colors.RED), Color(Colors.GREEN), Color(Colors.YELLOW))
    val secondRowColors =
        listOf(Color(Colors.BLUE), Color(Colors.MAGENTA), Color(Colors.CYAN), Color(Colors.WHITE))
    val thirdRowColors =
        listOf(Color(Colors.ORANGE), Color(Colors.DEEP_PINK), Color(Colors.DODGER_BLUE))


    val firstRowColorsToChange = listOf(Colors.BLACK, Colors.RED, Colors.GREEN, Colors.YELLOW)
    val secondRowColorsToChange = listOf(Colors.BLUE, Colors.MAGENTA, Colors.CYAN, Colors.WHITE)
    val thirdRowColorsToChange = listOf(Colors.ORANGE, Colors.DEEP_PINK, Colors.DODGER_BLUE)

    fun checkForPremium(colorId: Int, row:Int): Boolean {
        val user = CurrentDataHandler.getActiveUser()


        return if(user == null || !user.premium){
            when(row){
                1 -> firstRowColorsToChange[colorId] !in premiumColors
                2 -> secondRowColorsToChange[colorId] !in premiumColors
                3 -> thirdRowColorsToChange[colorId] !in premiumColors
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
            Row(
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {

                firstRowColors.forEachIndexed { id, item ->
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        ColorButton(
                            action = action,
                            color = item,
                            colorToChange = firstRowColorsToChange[id],
                            assets = assets,
                            type = type,
                            enabled = checkForPremium(id, 1)
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

                secondRowColors.forEachIndexed { id, item ->
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        ColorButton(
                            action = action,
                            color = item,
                            colorToChange = secondRowColorsToChange[id],
                            assets = assets,
                            type = type,
                            enabled = checkForPremium(id, 2)
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

                thirdRowColors.forEachIndexed { id, item ->
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        ColorButton(
                            action = action,
                            color = item,
                            colorToChange = thirdRowColorsToChange[id],
                            assets = assets,
                            type = type,
                            enabled = checkForPremium(id, 3)
                        )
                    }
                }

                Spacer(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth()
                        .weight(1f)
                )

            }

        }
    }
}

@Composable
fun ColorButton(
    action: (colorToChange: Int) -> Unit,
    color: Color,
    colorToChange: Int,
    assets: AssetManager,
    type: String,
    enabled: Boolean
) {

    val logoFile = assets.open("qr_icon.png")
    val byteArray = logoFile.readBytes()
    val logoImg = BitmapFactory
        .decodeByteArray(byteArray, 0, byteArray.size)
        .asImageBitmap()

    Button(modifier = Modifier
        .shadow(1.dp)
        .fillMaxSize(),
        enabled = enabled,
        shape = RectangleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (type == "content") Color(Colors.LIGHT_GRAY) else color,
            contentColor = color
        ),
        onClick = { action(colorToChange) }) {

        if (type == "content") {
            Icon(
                modifier = Modifier.fillMaxSize(),
                bitmap = logoImg, contentDescription = null
            )
        }
    }

}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun ColorChoosePagePreview() {
    ColorChoosePage(action = {}, type = "content")
}