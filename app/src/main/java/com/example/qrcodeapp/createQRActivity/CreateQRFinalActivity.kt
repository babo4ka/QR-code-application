package com.example.qrcodeapp.createQRActivity

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.example.qrcodeapp.PremiumReminder
import com.example.qrcodeapp.R
import com.example.qrcodeapp.Reminder
import com.example.qrcodeapp.createQRActivity.pages.colorsAndBackgroundPage.ColorChoosePage
import com.example.qrcodeapp.createQRActivity.pages.logoChooseActivity.LogoChoosePage
import com.example.qrcodeapp.createQRActivity.pages.shapePage.ShapeChoosePage
import com.example.qrcodeapp.createQRActivity.ui.theme.QRCodeAppTheme
import com.example.qrcodeapp.database.CurrentDataHandler
import com.example.qrcodeapp.database.databases.QrDatabase
import com.example.qrcodeapp.database.viewModels.CreatedCodesViewModel
import com.example.qrcodeapp.database.viewModels.factories.CreatedCodesViewModelFactory
import com.example.qrcodeapp.mainActivity.MainActivity
import com.example.qrcodeapp.mainActivity.Page
import com.google.zxing.BarcodeFormat
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.coroutines.launch
import qrcode.color.Colors
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import kotlin.math.cos
import kotlin.math.sin

class CreateQRFinalActivity : ComponentActivity() {
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
                    QRCodeCreator(ccvm = ccvm){
                        saveQrCodeToFiles(it)
                    }
                }
            }
        }
    }


    fun saveQrCodeToFiles(content:Bitmap?){
        var fos: OutputStream? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            this.contentResolver?.also { resolver ->
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, "qr.png")
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }
                val imageUri: Uri? =
                    resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                fos = imageUri?.let { resolver.openOutputStream(it) }
            }
        }else{
            val dir = File(Environment.getExternalStorageDirectory().toString() + "/qrCodes")
            dir.mkdirs()

            val file = File(dir, "qr.jpg")
            fos = FileOutputStream(file)
        }

        fos?.let { content?.compress(Bitmap.CompressFormat.JPEG, 100, it) }

        fos?.flush()
        fos?.close()

        Toast.makeText(this, "QR-код сохранён на устройстве!", Toast.LENGTH_SHORT).show()
    }

}

@Composable
fun QRCodeCreator(ccvm: CreatedCodesViewModel?, saveQrToFilesAction:(Bitmap?)->Unit) {

    val context = LocalContext.current
    val assets = context.resources.assets
    val scope = rememberCoroutineScope()

    val qrColor = remember {
        mutableStateOf(Colors.WHITE)
    }

    val qrBackColor = remember {
        mutableStateOf(Colors.BLACK)
    }

    val qrShape = remember {
        mutableStateOf("Круги")
    }

    val qrLogo = remember {
        mutableStateOf("")
    }

    val saveBtnGradient = Brush.linearGradient(
        colorStops = arrayOf(
            0.0f to Color.hsv(119f, .76f, .65f),
            0.8f to Color.hsv(190f, .79f, .94f)
        ),
        start = Offset(0f, 0f),
        end = Offset(500f, 500f)
    )

    val defaultLogoFile = assets.open("app_logo.png")
    val defaultLogoByteArray = defaultLogoFile.readBytes()

//    val qrBuilder = remember {
//        mutableStateOf(
//            QRCode.ofCircles()
//                .withSize(15)
//                .withBackgroundColor(Colors.BLACK)
//                .withColor(qrColor.value)
//                .withLogo(defaultLogoByteArray, 128, 128, true)
//        )
//    }

    val activeCustomizeOption = remember {
        mutableStateOf("Цвет")
    }


    val headerWeight = 1f
    val contentWeight = 12f
    val footerWeight = 5f




    fun qrCode(): ImageBitmap {
//        val bytes = qrToBytes()
//        return bytes.size.let { BitmapFactory.decodeByteArray(bytes, 0, it).asImageBitmap() }
        val textEntered = CurrentDataHandler.getTextEntered()
        val text = when(CurrentDataHandler.getQrTypeChoosed()){
            QrType.TEXT -> if(textEntered == "") "zero" else textEntered
            QrType.LINK -> "https://${if(textEntered == "") "zero" else textEntered}"
            QrType.TG -> "https://t.me/${if(textEntered == "") "zero" else textEntered}"
            else -> if(textEntered == "") "zero" else textEntered
        }

        val s = 1000
        val bitMatrix: BitMatrix =
            QRCodeWriter().encode(text, BarcodeFormat.QR_CODE, s, s)

        val bmp = Bitmap.createBitmap(s, s, Bitmap.Config.ARGB_8888)


        for (x in 0 until s) {
            for (y in 0 until s) {
                bmp.setPixel(x, y, if (bitMatrix[x, y]) qrColor.value else qrBackColor.value)
            }
        }


        if(CurrentDataHandler.getActiveUser() == null){
            val logoBmp = BitmapFactory.decodeByteArray(defaultLogoByteArray, 0, defaultLogoByteArray.size)
            val xPos = (s - logoBmp.width) / 2
            val yPos = (s - logoBmp.height) / 2

            val mergedBitmap = Bitmap.createBitmap(s, s, Bitmap.Config.ARGB_8888)
            val defaultLogoCanvas = Canvas(mergedBitmap)
            defaultLogoCanvas.drawBitmap(bmp, 0f, 0f, null)
            defaultLogoCanvas.drawBitmap(logoBmp, xPos.toFloat(), yPos.toFloat(), null)

            return mergedBitmap.asImageBitmap()
        }else{
            if (qrLogo.value != ""){
                val logoFile = assets.open(qrLogo.value)
                val logoByteArray = logoFile.readBytes()

                val logoBmp = BitmapFactory.decodeByteArray(logoByteArray, 0, logoByteArray.size)
                val xPos = (s - logoBmp.width) / 2
                val yPos = (s - logoBmp.height) / 2

                val mergedBitmap = Bitmap.createBitmap(s, s, Bitmap.Config.ARGB_8888)
                val logoCanvas = Canvas(mergedBitmap)
                logoCanvas.drawBitmap(bmp, 0f, 0f, null)
                logoCanvas.drawBitmap(logoBmp, xPos.toFloat(), yPos.toFloat(), null)

                return mergedBitmap.asImageBitmap()
            }else{
                return bmp.asImageBitmap()
            }
        }
    }



    fun qrToBytes(): ByteArray {
//        val text = when(CurrentDataHandler.getQrTypeChoosed()){
//            QrType.TEXT -> CurrentDataHandler.getTextEntered()
//            QrType.LINK -> "https://${CurrentDataHandler.getTextEntered()}"
//            QrType.TG -> "https://t.me/${CurrentDataHandler.getTextEntered()}"
//            else -> ({}).toString()
//        }
//
//        return qrBuilder.value.build(text).render().getBytes()

        val stream = ByteArrayOutputStream()
        qrCode().asAndroidBitmap().compress(Bitmap.CompressFormat.PNG, 90, stream)
        return stream.toByteArray()
    }

    fun saveQrToFiles(){
        val bytes = qrToBytes()
        saveQrToFilesAction(BitmapFactory.decodeByteArray(bytes, 0, bytes.size))
    }

    fun saveQrCodeToDb() {
        val bytes = qrToBytes()
        val user = CurrentDataHandler.getActiveUser()

        val textEntered = CurrentDataHandler.getTextEntered()
        val text = when(CurrentDataHandler.getQrTypeChoosed()){
            QrType.TEXT -> if(textEntered == "") "zero" else textEntered
            QrType.LINK -> "https://${if(textEntered == "") "zero" else textEntered}"
            QrType.TG -> "https://t.me/${if(textEntered == "") "zero" else textEntered}"
            else -> if(textEntered == "") "zero" else textEntered
        }

        if (user != null) {
            scope.launch {
                ccvm?.addCode(user, bytes, text)
                Toast.makeText(context, "QR-код сохранён в истории", Toast.LENGTH_SHORT)
                    .show()
            }

        }
    }

//    fun rebuildQr() {
//        var builder: QRCodeBuilder? = null
//
//        when (qrShape.value) {
//            "Круги" -> builder = QRCode.ofCircles()
//            "Квадраты" -> builder = QRCode.ofSquares()
//            "Закругленные квадраты" -> builder = QRCode.ofRoundedSquares()
//        }
//
//        if (qrLogo.value != "") {
//            val logoFile = assets.open(qrLogo.value)
//            val byteArray = logoFile.readBytes()
//
//
//            if (builder != null) {
//                builder = builder.withLogo(byteArray, 128, 128, true)
//            }
//        }else{
//            if (builder != null) {
//                builder = builder.withLogo(defaultLogoByteArray, 128, 128, true)
//            }
//        }
//
//        if (builder != null) {
//            qrBuilder.value = builder
//                .withBackgroundColor(qrBackColor.value)
//                .withColor(qrColor.value)
//        }
//    }

    fun getContentUri(): Uri?{
        val bytes = qrToBytes()
        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

        val imgsFolder = File(context.cacheDir, "imgs")

        var contentUri:Uri? = null

        imgsFolder.mkdirs()

        val file = File(imgsFolder, "shared_image.png")
        val fos = FileOutputStream(file)

        bitmap.compress(Bitmap.CompressFormat.PNG, 50, fos)
        fos.flush()
        fos.close()
        contentUri = FileProvider.getUriForFile(context, "com.example.qrcodeapp.fileprovider", file)

        return contentUri
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
            if(CurrentDataHandler.getActiveUser() == null){
                Reminder()
            }

            if(CurrentDataHandler.getActiveUser() != null
                && !CurrentDataHandler.getActiveUser()?.premium!!){
                PremiumReminder()
            }


            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(35.dp)
                    //.weight(headerWeight)

            ) {
                Button(
                    onClick = {
                        val intent = Intent(context, CreateQRMainActivity::class.java)
                        startActivity(context, intent, null)
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
                        .weight(2f)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Создать",
                        fontSize = 15.sp
                    )
                }

                //Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = {
                        val contentUri = getContentUri()
                        val intent= Intent()
                        intent.action=Intent.ACTION_SEND
                        intent.putExtra(Intent.EXTRA_STREAM,contentUri)
                        intent.type="image/png"
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        startActivity(context, Intent.createChooser(intent,"Share To:"), null)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    )
                ) {
                    Image(modifier = Modifier
                        .width(20.dp)
                        .height(20.dp),
                        painter = painterResource(id = R.drawable.share),
                        contentDescription = null)
                }

                Button(
                    onClick = {
                        saveQrCodeToDb()
                        saveQrToFiles()
                        CurrentDataHandler.setTextEntered("")
                        CurrentDataHandler.setQrTypeChoosed(QrType.TEXT)
                        CurrentDataHandler.setMainActivityPage(Page.MAIN)
                        val intent = Intent(context, MainActivity::class.java)
                        startActivity(context, intent, null)
                    },
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.Black,
                        containerColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .weight(2f)
                        .background(
                            brush = saveBtnGradient,
                            shape = RoundedCornerShape(20)
                        )
                ) {
                    Text(text = "Сохранить", fontSize = 13.sp)
                }
            }

            Row(horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(contentWeight)
            ) {

                Image(bitmap = qrCode(), contentDescription = null)
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(footerWeight)
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
                            .height(35.dp)
                            //.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth()
                                .weight(1f)
                        ) {
                            ChooseOptionToCustomizeBtn(
                                action = { activeCustomizeOption.value = "Цвет" },
                                text = "Цвет",
                                active = activeCustomizeOption.value
                            )
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth()
                                .weight(1f)
                        ) {
                            ChooseOptionToCustomizeBtn(
                                action = { activeCustomizeOption.value = "Фон" },
                                text = "Фон",
                                active = activeCustomizeOption.value
                            )
                        }

//                        Box(
//                            modifier = Modifier
//                                .fillMaxHeight()
//                                .fillMaxWidth()
//                                .weight(1f)
//                        ) {
//                            ChooseOptionToCustomizeBtn(
//                                action = { activeCustomizeOption.value = "Форма" },
//                                text = "Форма",
//                                active = activeCustomizeOption.value
//                            )
//                        }

                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth()
                                .weight(1f)
                        ) {
                            ChooseOptionToCustomizeBtn(
                                action = { activeCustomizeOption.value = "Лого" },
                                text = "Лого",
                                active = activeCustomizeOption.value
                            )
                        }
                    }

                    Box(modifier = Modifier.weight(4f)) {
                        when (activeCustomizeOption.value) {
                            "Цвет" -> {
                                ColorChoosePage(type="content",
                                    action = {
                                        qrColor.value = it
                                        //rebuildQr()
                                    })
                            }

                            "Фон" -> {
                                ColorChoosePage(type="back",
                                    action = {
                                        qrBackColor.value = it
                                        //rebuildQr()
                                    }
                                )
                            }

//                            "Форма" -> {
//                                ShapeChoosePage {
//                                    qrShape.value = it
//                                    //rebuildQr()
//                                }
//                            }

                            "Лого" -> {
                                LogoChoosePage {
                                    qrLogo.value = it
                                    //rebuildQr()
                                }
                            }
                        }
                    }
                }
            }
        }

    }

}


@Composable
fun ChooseOptionToCustomizeBtn(
    action: () -> Unit,
    text: String,
    active: String
) {
    Button(
        modifier = Modifier.fillMaxSize(),
        onClick = { action() },
        colors = ButtonDefaults.buttonColors(
            containerColor = if (active == text) Color.Black else Color.Transparent,
            contentColor = if (active == text) Color.White else Color.Black
        )
    ) {

        Text(text = text)
    }
}


@Preview(showSystemUi = true, showBackground = true)
@Composable
fun CreatorPreview() {
    QRCodeAppTheme {
        QRCodeCreator(ccvm = null){

        }
    }
}