package com.example.qrcodeapp.mainActivity.pages.accountPage

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.qrcodeapp.R
import com.example.qrcodeapp.createQRActivity.CreateQRMainActivity
import com.example.qrcodeapp.database.CurrentDataHandler
import com.example.qrcodeapp.database.databases.QrDatabase
import com.example.qrcodeapp.database.entities.CreatedCodes
import com.example.qrcodeapp.database.viewModels.CreatedCodesViewModel
import com.example.qrcodeapp.database.viewModels.factories.CreatedCodesViewModelFactory
import com.example.qrcodeapp.mainActivity.MainActivity
import com.example.qrcodeapp.mainActivity.Page
import com.example.qrcodeapp.mainActivity.pages.accountPage.ui.theme.QRCodeAppTheme
import kotlinx.coroutines.launch

class CreatedCodesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dao = QrDatabase.getInstance(application).createdCodesDao
        val viewModelFactory = CreatedCodesViewModelFactory(dao)
        val ccvm = ViewModelProvider(this, viewModelFactory).get(CreatedCodesViewModel::class.java)

        setContent {
            QRCodeAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CreatedCodesPage(ccvm = ccvm)
                }
            }
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun CreatedCodesPage(ccvm: CreatedCodesViewModel?) {
    val headerWeight = 1f
    val contentWeight = 12f

    val context = LocalContext.current

    val createdCodes = remember {
        mutableStateOf<List<CreatedCodes>?>(null)
    }

    val scope = rememberCoroutineScope()

    scope.launch {
        createdCodes.value =
            CurrentDataHandler.getActiveUser()?.userLogin?.let { ccvm?.getAllCodes(it) }
    }

    suspend fun deleteCode(id:Int){
        ccvm?.deleteCode(id)
        createdCodes.value =
            CurrentDataHandler.getActiveUser()?.userLogin?.let { ccvm?.getAllCodes(it) }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .weight(headerWeight)

            ) {
                Button(
                    onClick = {
                        CurrentDataHandler.setMainActivityPage(Page.ACCOUNT)
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

                Box(
                    modifier = Modifier
                        .weight(5f)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Созданные QR-коды",
                        fontSize = 20.sp
                    )
                }
            }

            Column(modifier = Modifier
                .weight(contentWeight)
                .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(5.dp)) {

                if (createdCodes.value?.isEmpty() == true) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(5.dp)
                    ) {

                        Text(
                            textAlign = TextAlign.Center,
                            text = "Вы ещё не создали ни одного QR-кода :(",
                            fontSize = 25.sp
                        )

                        Button(modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .shadow(1.dp)
                            .height(45.dp)
                            .width(200.dp),
                            shape = RectangleShape,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent,
                                contentColor = Color.Black
                            ),
                            onClick = {
                                val intent = Intent(context, CreateQRMainActivity::class.java)
                                ContextCompat.startActivity(context, intent, null)
                            }) {
                            Text(text = "Перейти к созданию")
                        }
                    }
                }else{
                    createdCodes.value?.forEach { item ->
                        CodeBox(qrCode = item.code, content = item.content, qrId = item.id,
                            deleteAction = {
                                scope.launch {
                                    deleteCode(item.id)
                                }
                            })
                    }
                }
            }
        }
    }
}

@Composable
fun CodeBox(qrCode:ByteArray,
            content:String,
            qrId:Int,
            deleteAction:()->Unit){

    val context = LocalContext.current
    val scope = rememberCoroutineScope()


    fun qrCode(): ImageBitmap {
        return  BitmapFactory.decodeByteArray(qrCode, 0, qrCode.size).asImageBitmap()
    }

    Button(modifier = Modifier
        .shadow(3.dp)
        .padding(3.dp),
        shape = RectangleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Color.Black
        ),
        onClick = {
            val intent = Intent(context, QrCodeInspectActivity::class.java)
            intent.putExtra("qrCodeId", qrId)
            ContextCompat.startActivity(context, intent, null)
        }) {
        Row(horizontalArrangement = Arrangement.spacedBy(15.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)) {

            Image(modifier = Modifier.weight(1f),
                bitmap = qrCode(), contentDescription = null)

            Text(modifier = Modifier.weight(5f), text = content)

            Button(modifier = Modifier
                .weight(2f),
            shape = RectangleShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.Black
            ),
                onClick = {
                scope.launch {
                    deleteAction()
                }
            }) {
                Image(modifier = Modifier
                    .width(15.dp)
                    .height(15.dp),
                    painter = painterResource(id = R.drawable.close),
                    contentDescription = null)
            }
        }
    }

}


@Preview(showSystemUi = true, showBackground = true)
@Composable
fun CreatedCodesPagePreview() {
    QRCodeAppTheme {
        CreatedCodesPage(null)
    }
}