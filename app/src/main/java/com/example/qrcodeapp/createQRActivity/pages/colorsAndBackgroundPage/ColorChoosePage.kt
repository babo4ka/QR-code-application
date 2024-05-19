package com.example.qrcodeapp.createQRActivity.pages.colorsAndBackgroundPage

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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import qrcode.color.Colors


@Composable
fun ColorChoosePage(action: (color:Int)->Unit){

    val firstRowColors = listOf(Color.Black, Color.Red, Color.Green, Color.Yellow)
    val secondRowColors = listOf(Color.Blue, Color.Magenta, Color.Cyan, Color.White)

    val firstRowColorsToChange = listOf(Colors.BLACK, Colors.RED, Colors.GREEN, Colors.YELLOW)
    val secondRowColorsToChange = listOf(Colors.BLUE, Colors.MAGENTA, Colors.CYAN, Colors.WHITE)


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

                firstRowColors.forEachIndexed{ id, item ->
                    Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth()
                        .weight(1f)
                    ){
                        ColorButton(action = action,
                            color = item,
                            colorToChange = firstRowColorsToChange[id])
                    }
                }

            }

            Row (horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)){

                secondRowColors.forEachIndexed{ id, item ->
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth()
                            .weight(1f)
                    ){
                        ColorButton(action = action,
                            color = item,
                            colorToChange = secondRowColorsToChange[id])
                    }
                }

            }

        }
    }
}

@Composable
fun ColorButton(
    action: (colorToChange:Int) -> Unit,
    color: Color,
    colorToChange:Int
) {
    Button(modifier = Modifier
        .shadow(1.dp)
        .fillMaxSize(),
        shape = RectangleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = color
        ),
        onClick = { action(colorToChange) }) {

    }

}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun ColorChoosePagePreview(){
    ColorChoosePage(action = {})
}