package com.alican.data.utils

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat


fun Context.loadJSONFromAssets(fileName: String): String {
    return applicationContext.assets.open(fileName).bufferedReader().use { reader ->
        reader.readText()
    }
}

fun formatPhoneNumber(phoneNumber: String): String {
    // Telefon numarasındaki tüm rakam ve + karakterlerini temizle
    val cleanedNumber = phoneNumber.replace(Regex("[^0-9]"), "")

    // Temizlenmiş numarayı formatla
    val formattedNumber = StringBuilder()

    // Numaranın geri kalanını parantez içine al
    if (cleanedNumber.length >= 4) {
        formattedNumber.append("(").append(cleanedNumber.substring(1, 4)).append(")")
    }

    if (cleanedNumber.length >= 7) {
        formattedNumber.append(cleanedNumber.substring(4, 7))
    }

    if (cleanedNumber.length >= 9) {
        formattedNumber.append("-").append(cleanedNumber.substring(7, 9))
    }

    if (cleanedNumber.length >= 11) {
        formattedNumber.append("-").append(cleanedNumber.substring(9, 11))
    }

    return formattedNumber.toString()
}

fun Context.openChrome(url: String) {
    val urlIntent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse(url)
    )
    this.startActivity(urlIntent)
}

fun restartApp(context: Context) {
    val packageManager: PackageManager = context.packageManager
    val intent: Intent = packageManager.getLaunchIntentForPackage(context.packageName)!!
    val componentName: ComponentName = intent.component!!
    val restartIntent: Intent = Intent.makeRestartActivityTask(componentName)
    context.startActivity(restartIntent)
    Runtime.getRuntime().exit(0)
}


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun hasNotificationPermission(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.POST_NOTIFICATIONS
    ) == PackageManager.PERMISSION_GRANTED
}

