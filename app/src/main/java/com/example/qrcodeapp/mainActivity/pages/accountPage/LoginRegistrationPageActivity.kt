package com.example.qrcodeapp.mainActivity.pages.accountPage

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qrcodeapp.database.CurrentDataHandler
import com.example.qrcodeapp.database.entities.User
import com.example.qrcodeapp.database.viewModels.UserViewModel
import com.example.qrcodeapp.mainActivity.pages.accountPage.ui.theme.QRCodeAppTheme
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LoginRegistrationPage(modifier: Modifier,
    uvm: UserViewModel?,
    action:(User?)->Unit) {
    val pagerState = rememberPagerState(pageCount = { 2 })
    val scope = rememberCoroutineScope()

    Box(
        modifier = modifier
    ) {
        Column(
            Modifier
                .fillMaxWidth()
        ) {


            val tabs = listOf("Вход", "Регистрация")
            var tabIndex by remember { mutableIntStateOf(0) }

            TabRow(
                modifier = Modifier.height(50.dp),
                selectedTabIndex = tabIndex
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(selected = tabIndex == index,
                        onClick = {
                            tabIndex = index
                            scope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        }) {
                        Text(
                            text = title,
                            fontSize = 20.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.W600
                        )
                    }
                }
            }

            HorizontalPager(
                state = pagerState) { page ->
                when (page) {
                    0 -> {
                        LoginPage(uvm = uvm, action = action)
                        tabIndex = 0
                    }

                    1 -> {
                        RegistrationPage(uvm = uvm, action = action)
                        tabIndex = 1
                    }
                }
            }


        }
    }
}


@Composable
fun LoginPage(uvm: UserViewModel?, action:(User?)->Unit) {
    var userName by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    val context = LocalContext.current

    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(28.dp)
    ) {
        Column(
            Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            InputField(value = userName,
                label = "Логин",
                placeholder = "Введите логин",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                onValChange = {
                    userName = it
                })

            InputField(value = password,
                label = "Пароль",
                placeholder = "Введите пароль",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                onValChange = {
                    password = it
                })

            Spacer(Modifier.height(10.dp))

            TextButton(modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .shadow(1.dp)
                .height(45.dp)
                .width(100.dp),
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Black
                ),
                onClick = {
                    scope.launch {
                        val user = uvm?.getUser(userName)
                        if (user == null) {
                            val toast = Toast.makeText(
                                context, "No account with entered username",
                                Toast.LENGTH_SHORT
                            )
                            toast.show()
                        } else {
                            if (user.password != password) {
                                val toast = Toast.makeText(
                                    context, "Incorrect password!!",
                                    Toast.LENGTH_SHORT
                                )
                                toast.show()
                            } else {
                                CurrentDataHandler.setActiveUser(user)
                            }
                        }
                        action(user)
                    }
                }
            ) {
                Text(text = "Войти")
            }

        }
    }
}

@Composable
fun RegistrationPage(uvm: UserViewModel?, action:(User?)->Unit) {
    var userName by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var confirmPassword by remember {
        mutableStateOf("")
    }

    var name by remember {
        mutableStateOf("")
    }

    val context = LocalContext.current

    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(28.dp)
    ) {
        Column(
            Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            InputField(value = userName,
                label = "Логин",
                placeholder = "Придумайте логин",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                onValChange = {
                    userName = it
                })

            InputField(value = name,
                label = "Имя",
                placeholder = "Придумайте имя",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                onValChange = {
                    name = it
                })

            InputField(value = password,
                label = "Пароль",
                placeholder = "Придумайте пароль",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                onValChange = {
                    password = it
                })

            InputField(value = confirmPassword,
                label = "Повторите пароль",
                placeholder = "Повторите пароль",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                onValChange = {
                    confirmPassword = it
                })

            Spacer(Modifier.height(10.dp))

            TextButton(modifier = Modifier
                .align(Alignment.CenterHorizontally)

                .shadow(1.dp)
                .height(45.dp)
                .width(150.dp),
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Black
                ),
                onClick = {
                    scope.launch {
                        val u = uvm?.getUser(userName)
                        if (u != null) {
                            val toast = Toast.makeText(
                                context,
                                "User $userName already exists",
                                Toast.LENGTH_SHORT
                            )
                            toast.show()
                        } else {
                            if (password != confirmPassword) {
                                val toast = Toast.makeText(
                                    context,
                                    "Passwords aren't match",
                                    Toast.LENGTH_SHORT
                                )
                                toast.show()
                            } else {
                                uvm?.addUser(userName, password, name)
                                val user = uvm?.getUser(userName)
                                if (user != null) {
                                    CurrentDataHandler.setActiveUser(user)
                                }
                                action(user)
                            }
                        }
                    }

                }
            ) {
                Text(text = "Создать аккаунт")
            }
        }
    }
}


@Composable
fun InputField(
    value: String,
    label: String,
    placeholder: String,
    onValChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions
) {
    OutlinedTextField(
//        colors = TextFieldDefaults.colors(
//            unfocusedLabelColor = Color.White,
//            focusedTextColor = Color.White,
//            focusedPlaceholderColor = Color.White,
//            focusedLabelColor = Color.White,
//            unfocusedContainerColor = Color.DarkGray,
//            focusedContainerColor = Color.DarkGray
//        ),
        keyboardOptions = keyboardOptions,
        value = value,
        onValueChange = { onValChange(it.take(18)) },
        label =
        {
            Text(text = label)
        },
        placeholder = {
            Text(text = placeholder)
        },
        singleLine = true
    )
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun LoginRegistrationPreview() {
    QRCodeAppTheme {
        LoginRegistrationPage(modifier = Modifier
            .fillMaxSize(),
            null, action = {})
    }
}