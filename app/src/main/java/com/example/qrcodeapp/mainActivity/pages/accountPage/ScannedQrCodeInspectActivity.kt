package com.example.qrcodeapp.mainActivity.pages.accountPage

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.qrcodeapp.R
import com.example.qrcodeapp.database.databases.QrDatabase
import com.example.qrcodeapp.database.entities.ScannedCodes
import com.example.qrcodeapp.database.viewModels.ScannedCodesViewModel
import com.example.qrcodeapp.database.viewModels.factories.ScannedCodesViewModelFactory
import com.example.qrcodeapp.mainActivity.pages.accountPage.ui.theme.QRCodeAppTheme
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date

class ScannedQrCodeInspectActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val qrCodeId = intent.getIntExtra("qrCodeId", -1)

        val dao = QrDatabase.getInstance(application).scannedCodesDao
        val viewModelFactory = ScannedCodesViewModelFactory(dao)
        val scvm = ViewModelProvider(this, viewModelFactory).get(ScannedCodesViewModel::class.java)

        setContent {
            QRCodeAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ScannedQrCodeInspect(qrCodeId = qrCodeId, scvm = scvm)
                }
            }
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ScannedQrCodeInspect(
    qrCodeId: Int,
    scvm: ScannedCodesViewModel?
) {
    val headerWeight = 1f
    val contentWeight = 5f
    val footerWeight = 10f

    val context = LocalContext.current

    val scope = rememberCoroutineScope()

    val qrCodeObj = remember {
        mutableStateOf<ScannedCodes?>(null)
    }

    scope.launch {
        qrCodeObj.value = scvm?.getCode(qrCodeId)
    }

    @SuppressLint("SimpleDateFormat")
    @Composable
    fun ContentBox(sc:ScannedCodes?, modifier:Modifier){

        val date = sc?.date?.let { Date(it.toLong()) }
        val format = SimpleDateFormat("dd.MM.yyyy HH:mm")

        val dateStr = date?.let { format.format(it) }

        Column(verticalArrangement = Arrangement.spacedBy(5.dp),
            horizontalAlignment = Alignment.Start,
            modifier = modifier.fillMaxSize()){
            Text(textAlign = TextAlign.Left,
                //modifier = modifier,
                text = "Дата сканирования:\n ${dateStr}"
            )

            if (sc != null) {
                Text(textAlign = TextAlign.Left,
                    //modifier = modifier,
                    text = "Содержимое: \n ${sc.content}"
                )
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp)
    ) {

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
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
                        val intent = Intent(context, ScannedCodesActivity::class.java)
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

                Spacer(modifier = Modifier.weight(4f))
            }


            qrCodeObj.value?.let {
                ContentBox(sc = it, modifier = Modifier.weight(footerWeight))
            }


        }
    }
}

@Preview(showBackground = true)
@Composable
fun ScannedQrInspectPreview() {
    QRCodeAppTheme {
        ScannedQrCodeInspect(-1, null)
    }
}