package com.example.qrcodeapp.database

import com.example.qrcodeapp.createQRActivity.QrType
import com.example.qrcodeapp.database.entities.User
import com.example.qrcodeapp.mainActivity.Page

class CurrentDataHandler {


    companion object{
        private var activeUser: User? = null

        private var qrTypeChoosed: QrType = QrType.TEXT

        private var textEntered: String = ""

        private var mainActivityPage: Page = Page.MAIN

        fun setActiveUser(user:User?){
            activeUser = user
        }

        fun getActiveUser(): User?{
            return activeUser
        }

        fun setQrTypeChoosed(qrType:QrType){
            qrTypeChoosed = qrType
        }

        fun getQrTypeChoosed():QrType{
            return qrTypeChoosed
        }

        fun setTextEntered(text:String){
            textEntered = text
        }

        fun getTextEntered():String{
            return textEntered
        }

        fun setMainActivityPage(page:Page){
            mainActivityPage = page
        }

        fun getMainActivityPage():Page{
            return mainActivityPage
        }
    }
}