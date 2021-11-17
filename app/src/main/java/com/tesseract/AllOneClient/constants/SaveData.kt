package com.tesseract.AllOneClient.constants

import android.content.Context
import android.content.SharedPreferences
import com.tesseract.AllOneClient.R

object SaveData {

    var isSignInFragment:Boolean=true
    var isConfirmFragmnet:Boolean=true
    var isCurrentFragment: Boolean = true
    var isCurrentPaymentFragment: Boolean = true
    var isPostFragment: Boolean = true


    fun setLanguage(context: Context, lan: String?) {
        val sharedrefrence =
            context.getSharedPreferences("setLanguageForHeader", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedrefrence.edit()
        editor.putString(context.getString(R.string.setLanguageForHeader), lan).apply()
    }

    fun getLanguage(context: Context): String? {
        val sharedrefrence =
            context.getSharedPreferences("setLanguageForHeader", Context.MODE_PRIVATE)
        return sharedrefrence.getString(context.getString(R.string.setLanguageForHeader), "")
    }


    fun setIsUzbek(context: Context, isLogIn: Boolean) {
        val sharedrefrence = context.getSharedPreferences("isUzbek", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedrefrence.edit()
        editor.putBoolean(context.getString(R.string.usUzbek), isLogIn).apply()
    }

    fun getUzbek(context: Context): Boolean {
        val sharedrefrence = context.getSharedPreferences("isUzbek", Context.MODE_PRIVATE)
        return sharedrefrence.getBoolean(context.getString(R.string.usUzbek), false)
    }

    fun setIsEnglish(context: Context, isLogIn: Boolean) {
        val sharedrefrence = context.getSharedPreferences("isEnglish", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedrefrence.edit()
        editor.putBoolean(context.getString(R.string.isEnglishLanguage), isLogIn).apply()
    }

    fun getEnglish(context: Context): Boolean {
        val sharedrefrence = context.getSharedPreferences("isEnglish", Context.MODE_PRIVATE)
        return sharedrefrence.getBoolean(context.getString(R.string.isEnglishLanguage), false)
    }


    fun setIsRussian(context: Context, isLogIn: Boolean) {
        val sharedrefrence = context.getSharedPreferences("isRussian", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedrefrence.edit()
        editor.putBoolean(context.getString(R.string.isRussianLanguage), isLogIn).apply()
    }

    fun getRussian(context: Context): Boolean {
        val sharedrefrence = context.getSharedPreferences("isRussian", Context.MODE_PRIVATE)
        return sharedrefrence.getBoolean(context.getString(R.string.isRussianLanguage), false)
    }


    fun addData(context: Context, token: String) {
        val sharedrefrence = context.getSharedPreferences("Token", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedrefrence.edit()
        editor.putString(context.getString(R.string.token), token).apply()
    }

    fun getData(context: Context): String? {
        val sharedrefrence = context.getSharedPreferences("Token", Context.MODE_PRIVATE)
        return sharedrefrence.getString(context.getString(R.string.token), "")
    }

    fun loginUser(context: Context, isLogIn: Boolean) {
        val sharedrefrence = context.getSharedPreferences("login", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedrefrence.edit()
        editor.putBoolean(context.getString(R.string.loggedInUser), isLogIn).apply()
    }

    fun getLoginUser(context: Context): Boolean {
        val sharedrefrence = context.getSharedPreferences("login", Context.MODE_PRIVATE)
        return sharedrefrence.getBoolean(context.getString(R.string.loggedInUser), false)
    }

    fun formatPhone(amount: String): String {
        var formattedString = ""
        if (amount.contains(".")) {
            val length = amount.split(".")[0].length
            val integerPart = amount.split(".")[0]
            if (length <= 2) {
                formattedString = amount
            } else {
                var counter = 0
                val value = StringBuilder()
                for (i in (length - 1) downTo 0) {
                    val emptyString = " "
                    if (counter % 3 == 0) {
                        value.append(emptyString)
                        value.append(integerPart[i])
                    } else {
                        value.append(integerPart[i])
                    }
                    counter++
                }
                formattedString = value.toString().reversed().trim() + "." + amount.split(".")[1]
            }
        } else {
            val length = amount.length
            if (length <= 2) {
                formattedString = amount
            } else {
                var counter: Int = 0
                val value = StringBuilder()
                for (i in (length - 1) downTo 0) {

                    val emptyString: String = " "
                    if (counter % 3 == 0) {
                        value.append(emptyString)
                        value.append(amount[i])
                    } else {
                        value.append(amount[i])
                    }
                    counter++
                }
                formattedString = value.toString().reversed().trim()
            }
        }

        return formattedString
    }

    fun formatCard(cardNumber: String): String {
        var formattedString = ""
        val length = cardNumber.length
        formattedString = if (length <= 3) {
            cardNumber
        } else {
            val value = StringBuilder()
            for ((counter, i) in ((length - 1) downTo 0).withIndex()) {
                val emptyString: String = " "
                if (counter % 4 == 0) {
                    value.append(emptyString)
                    value.append(cardNumber[i])
                } else {
                    value.append(cardNumber[i])
                }
            }
            value.toString().reversed().trim()
        }
        return formattedString
    }

    fun formatExpDate(cardExp:String):String{
        var formattedString=""
        formattedString=cardExp.substring(0,2)+"/"+cardExp.substring(2, cardExp.length)

        return formattedString
    }


    fun saveUserId(context: Context, id: Int) {
        val sharedrefrence = context.getSharedPreferences("id", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedrefrence.edit()
        editor.putInt(context.getString(R.string.saveUserId), id).apply()
    }

    fun getUserId(context: Context): Int {
        val sharedrefrence = context.getSharedPreferences("id", Context.MODE_PRIVATE)
        return sharedrefrence.getInt(context.getString(R.string.saveUserId), -1)
    }


    fun savePhone(context: Context, phone: String?) {
        val sharedrefrence = context.getSharedPreferences("savePhone", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedrefrence.edit()
        editor.putString(context.getString(R.string.savePhone), phone).apply()

    }

    fun getPhone(context: Context): String? {
        val sharedrefrence = context.getSharedPreferences("savePhone", Context.MODE_PRIVATE)
        return sharedrefrence.getString(context.getString(R.string.savePhone), "")
    }

    fun savePhone1(context: Context, phone: String?) {
        val sharedrefrence = context.getSharedPreferences("savePhone1", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedrefrence.edit()
        editor.putString(context.getString(R.string.savePhone1), phone).apply()

    }

    fun getPhone1(context: Context): String? {
        val sharedrefrence = context.getSharedPreferences("savePhone1", Context.MODE_PRIVATE)
        return sharedrefrence.getString(context.getString(R.string.savePhone1), "")
    }


    fun saveName(context: Context, name: String?) {
        val sharedrefrence = context.getSharedPreferences("saveName", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedrefrence.edit()
        editor.putString(context.getString(R.string.saveName), name).apply()

    }

    fun getName(context: Context): String? {
        val sharedrefrence = context.getSharedPreferences("saveName", Context.MODE_PRIVATE)
        return sharedrefrence.getString(context.getString(R.string.saveName), "")
    }

    fun saveNameOnly(context: Context, name: String?) {
        val sharedrefrence = context.getSharedPreferences("saveNameOnly", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedrefrence.edit()
        editor.putString(context.getString(R.string.saveNameOnly), name).apply()

    }

    fun getNameOnly(context: Context): String? {
        val sharedrefrence = context.getSharedPreferences("saveNameOnly", Context.MODE_PRIVATE)
        return sharedrefrence.getString(context.getString(R.string.saveNameOnly), "")
    }

    fun saveGender(context: Context, gender: String?) {

        val sharedrefrence = context.getSharedPreferences("saveGender", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedrefrence.edit()
        editor.putString(context.getString(R.string.saveGender), gender).apply()

    }

    fun getGender(context: Context): String? {
        val sharedrefrence = context.getSharedPreferences("saveGender", Context.MODE_PRIVATE)
        return sharedrefrence.getString(context.getString(R.string.saveGender), "")
    }

    fun saveBirthdate(context: Context, birthdate: String?) {
        val sharedrefrence = context.getSharedPreferences("saveBirthdate", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedrefrence.edit()
        editor.putString(context.getString(R.string.saveBirthdate), birthdate).apply()

    }

    fun getBirthdate(context: Context): String? {
        val sharedrefrence = context.getSharedPreferences("saveBirthdate", Context.MODE_PRIVATE)
        return sharedrefrence.getString(context.getString(R.string.saveBirthdate), "")
    }

    fun saveBalance(context: Context, balance: String?) {

        val sharedrefrence = context.getSharedPreferences("saveBalance", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedrefrence.edit()
        editor.putString(context.getString(R.string.saveBalance), balance).apply()

    }

    fun getBalance(context: Context): String? {
        val sharedrefrence = context.getSharedPreferences("saveBalance", Context.MODE_PRIVATE)
        return sharedrefrence.getString(context.getString(R.string.saveBalance), "")
    }

    fun createdTime(context: Context, time: String?) {

        val sharedrefrence = context.getSharedPreferences("createdTime", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedrefrence.edit()
        editor.putString(context.getString(R.string.createdTime), time).apply()
    }

    fun getCreatedTime(context: Context): String? {
        val sharedrefrence = context.getSharedPreferences("createdTime", Context.MODE_PRIVATE)
        return sharedrefrence.getString(context.getString(R.string.createdTime), "")
    }
}