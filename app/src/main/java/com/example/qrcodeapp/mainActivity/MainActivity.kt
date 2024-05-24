package com.example.qrcodeapp.mainActivity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.example.qrcodeapp.R
import com.example.qrcodeapp.database.CurrentDataHandler
import com.example.qrcodeapp.database.databases.QrDatabase
import com.example.qrcodeapp.database.viewModels.ScannedCodesViewModel
import com.example.qrcodeapp.database.viewModels.UserViewModel
import com.example.qrcodeapp.database.viewModels.factories.ScannedCodesViewModelFactory
import com.example.qrcodeapp.database.viewModels.factories.UserViewModelFactory
import com.example.qrcodeapp.mainActivity.pages.accountPage.AccountPage
import com.example.qrcodeapp.mainActivity.pages.mainPage.MainPage
import com.example.qrcodeapp.mainActivity.pages.scanPage.ScannerPage
import com.example.qrcodeapp.ui.theme.QRCodeAppTheme
import com.google.zxing.qrcode.QRCodeWriter
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import java.util.Date

class MainActivity : ComponentActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dao = QrDatabase.getInstance(application).userDao
        val viewModelFactory = UserViewModelFactory(dao)
        val uvm = ViewModelProvider(this, viewModelFactory).get(UserViewModel::class.java)

        val scDao = QrDatabase.getInstance(application).scannedCodesDao
        val scViewModelFactory = ScannedCodesViewModelFactory(scDao)
        val scvm = ViewModelProvider(this, scViewModelFactory).get(ScannedCodesViewModel::class.java)

        val scanLauncher = registerForActivityResult(ScanContract()){
                result ->
            if(result.contents == null){

            }else{
                val date = Date().time.toString()
                CurrentDataHandler.getActiveUser()
                    ?.let { scvm.addCode(it.userLogin, result.contents, date) }

                Toast.makeText(this, "data: ${result.contents}", Toast.LENGTH_SHORT).show()
            }
        }

        fun scan(){
            scanLauncher.launch(ScanOptions()
                .setDesiredBarcodeFormats(ScanOptions.QR_CODE))
        }

        setContent {
            QRCodeAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainActivityPage(uvm = uvm, scanAction = {
                        scan()
                    })
                }
            }
        }
    }


}


@Composable
fun MainActivityPage(uvm:UserViewModel?, scanAction:()->Unit) {

    val page = remember {
        mutableStateOf(CurrentDataHandler.getMainActivityPage())
    }

    fun pageName():String{
        return when(page.value){
            Page.MAIN -> "главная"
            Page.SCANNER -> "сканер"
            Page.ACCOUNT -> "кабинет"
        }
    }

    val active = remember {
        mutableStateOf(pageName())
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            when (page.value) {
                Page.MAIN -> {
                    MainPage(modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                    )
                }

                Page.SCANNER -> {
                    ScannerPage(modifier = Modifier
                        .fillMaxSize()
                        .weight(1f), action = {
                        scanAction()
                    })
                }
                Page.ACCOUNT -> {
                    AccountPage(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        uvm = uvm
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .height(60.dp)
                    .fillMaxWidth()
                    .shadow(1.dp)
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    MenuButton(text = "главная", painterResource(id = R.drawable.home),
                        active = active.value,
                        action = {
                            page.value = Page.MAIN
                            active.value = "главная"
                        })
                }
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    MenuButton(text = "сканер", painterResource(id = R.drawable.scanner),
                        active = active.value,
                        action = {
                            page.value = Page.SCANNER
                            active.value = "сканер"
                        })
                }
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    MenuButton(text = "кабинет", painterResource(id = R.drawable.account),
                        active = active.value,
                        action = {
                            page.value = Page.ACCOUNT
                            active.value = "кабинет"
                        })
                }
            }
        }
    }
}


@Composable
fun MenuButton(
    text: String,
    painterResourse: Painter,
    action: () -> Unit,
    active: String
) {

    val logoSize = 25.dp

    Button(modifier = Modifier.fillMaxSize(),
        shape = RectangleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = if (active == text) Color.Black else Color.Gray
        ),
        onClick = { action() }) {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                painter = painterResourse,
                contentDescription = null,
                modifier = Modifier
                    .width(logoSize)
                    .height(logoSize)
            )


            Text(
                text = text,
                fontSize = 15.sp
            )
        }

    }
}


@Preview(showSystemUi = true, showBackground = true)
@Composable
fun MainActivityPreview() {
    QRCodeAppTheme {
        MainActivityPage(null){

        }
    }
}