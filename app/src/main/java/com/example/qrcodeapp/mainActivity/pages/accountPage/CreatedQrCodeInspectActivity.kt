package com.example.qrcodeapp.mainActivity.pages.accountPage

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.example.qrcodeapp.R
import com.example.qrcodeapp.database.databases.QrDatabase
import com.example.qrcodeapp.database.entities.CreatedCodes
import com.example.qrcodeapp.database.viewModels.CreatedCodesViewModel
import com.example.qrcodeapp.database.viewModels.factories.CreatedCodesViewModelFactory
import com.example.qrcodeapp.mainActivity.pages.accountPage.ui.theme.QRCodeAppTheme
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class CreatedQrCodeInspectActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val qrCodeId = intent.getIntExtra("qrCodeId", -1)

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
                    CreatedQrCodeInspect(qrCodeId = qrCodeId, ccvm = ccvm) {
                        saveQrCodeToFiles(it)
                    }
                }
            }
        }

    }

    fun saveQrCodeToFiles(content: Bitmap?) {

        val dir = File(Environment.getExternalStorageDirectory().toString() + "/qrCodes")
        dir.mkdirs()
        val file = File(dir, "qr.jpg")
        val fos = FileOutputStream(file)
        content?.compress(Bitmap.CompressFormat.JPEG, 100, fos)

        fos.flush()
        fos.close()
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun CreatedQrCodeInspect(
    qrCodeId: Int,
    ccvm: CreatedCodesViewModel?,
    saveQrToFilesAction: (Bitmap?) -> Unit
) {
    println("code id $qrCodeId")
    println("vm $ccvm")
    val headerWeight = 1f
    val contentWeight = 5f
    val footerWeight = 3f

    val context = LocalContext.current

    val scope = rememberCoroutineScope()

    val qrCodeObj = remember {
        mutableStateOf<CreatedCodes?>(null)
    }

    scope.launch {
        qrCodeObj.value = ccvm?.getCode(qrCodeId)
    }

    fun qrCode(): ImageBitmap? {
        println(qrCodeObj.value)

        val bytes = qrCodeObj.value?.code

        return bytes?.size?.let {
            BitmapFactory.decodeByteArray(bytes, 0, it)
                .asImageBitmap()
        }
    }

    fun saveQrToFiles() {
        val bytes = qrCodeObj.value?.code
        saveQrToFilesAction(bytes?.size?.let { BitmapFactory.decodeByteArray(bytes, 0, it) })
    }

    fun getContentUri(): Uri?{
        val bytes = qrCodeObj.value?.code
        val bitmap = bytes?.let { BitmapFactory.decodeByteArray(bytes, 0, it.size) }

        val imgsFolder = File(context.cacheDir, "imgs")

        var contentUri: Uri? = null

        imgsFolder.mkdirs()

        val file = File(imgsFolder, "shared_image.png")
        val fos = FileOutputStream(file)

        if (bitmap != null) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 50, fos)
        }
        fos.flush()
        fos.close()
        contentUri = FileProvider.getUriForFile(context, "com.example.qrcodeapp.fileprovider", file)

        return contentUri
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
                        val intent = Intent(context, CreatedCodesActivity::class.java)
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

            qrCode()?.let {
                Image(
                    modifier = Modifier.weight(contentWeight),
                    bitmap = it, contentDescription = null
                )
            }

            qrCodeObj.value?.content?.let {
                Text(
                    modifier = Modifier.weight(footerWeight),
                    text = it
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)) {
                Button(colors = ButtonDefaults.buttonColors(
                    contentColor = Color.Black,
                    containerColor = Color.Green
                ),
                    modifier = Modifier.weight(1f),
                    onClick = {
                        saveQrToFiles()
                    }) {

                    Text(
                        text = "Сохранить",
                        fontSize = 20.sp
                    )
                }

                Button(colors = ButtonDefaults.buttonColors(
                    contentColor = Color.Black,
                    containerColor = Color.Green
                ),modifier = Modifier.weight(1f),

                    onClick = {
                        val contentUri = getContentUri()
                        val intent= Intent()
                        intent.action=Intent.ACTION_SEND
                        intent.putExtra(Intent.EXTRA_STREAM,contentUri)
                        intent.type="image/png"
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        ContextCompat.startActivity(
                            context,
                            Intent.createChooser(intent, "Share To:"),
                            null
                        )
                    }) {

                    Text(
                        text = "Поделиться",
                        fontSize = 20.sp
                    )
                }
            }
        }
    }

}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun QrCodeInspectPreview() {
    QRCodeAppTheme {
        CreatedQrCodeInspect(-1, null) {

        }
    }
}