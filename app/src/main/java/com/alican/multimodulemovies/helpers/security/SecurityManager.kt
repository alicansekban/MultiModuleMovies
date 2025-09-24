package com.alican.multimodulemovies.helpers.security

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Debug
import com.alican.multimodulemovies.BuildConfig
import com.alican.multimodulemovies.utils.DeviceUtil
import com.alican.multimodulemovies.utils.isAppInstalled
import java.io.File
import java.security.MessageDigest

/**
 * SecurityManager provides methods to assess the security state of a device and application.
 *
 * This class includes checks for rooted devices, insecure configurations, cloned apps,
 * blacklisted applications, running on an emulator in production, and debugger attachment.
 * It also provides a method to verify the integrity and authenticity of the application
 * using its signature.
 *
 * @param context The application context used for evaluating security checks.
 */
class SecurityManager(
    private val context: Context
) {
    /**
     * Determines if the device is not secure by performing several security checks.
     *
     * The method combines various checks including:
     * - Root access detection
     * - Ability to execute the "su" binary
     * - Detection of cloned application environment
     * - Installation of blacklisted applications
     * - Emulator detection for production release
     * - Debugger attachment detection
     *
     * @return Returns true if any of the security checks indicate the device is not secure, otherwise false.
     */
    fun isDeviceNotSecure(): Boolean {
        return isDeviceRooted() || canExecuteSu() || isClonedApp() || isBlackListAppInstalled()
                || isEmulatorForProdRelease() || isDebuggerAttached()
    }

    /**
     * Checks if the device is rooted by detecting the presence of common root files
     * or test-keys in the Android build.
     *
     * This method scans for the existence of files typically associated with rooted
     * devices, such as the "su" binary or Superuser applications, and checks if the
     * build tags contain "test-keys", which are commonly found in rooted or custom
     * ROM builds.
     *
     * @return Returns true if any indications of root access are detected, otherwise false.
     */

    private fun isDeviceRooted(): Boolean {
        val buildTags = Build.TAGS
        val paths = arrayOf(
            "/system/app/Superuser.apk",
            "/system/xbin/su",
            "/system/bin/su",
            "/system/sbin/su",
            "/system/xbin/which",
            "/system/sd/xbin/su",
            "/system/bin/failsafe/su",
            "su",
            "/sbin/su",
            "/vendor/bin/su",
            "/data/local/su",
            "/su/bin/su",
            "/data/local/xbin/su",
            "/data/local/bin/su",
        )
        return paths.any { path -> File(path).exists() }
                || (buildTags != null && buildTags.contains("test-keys"))
    }

    /**
     * Checks if the "su" (superuser) binary can be executed on the device.
     *
     * This method attempts to execute the "su" binary typically used for
     * rooting purposes and determines its availability by inspecting the
     * process output. If the binary is found and executable, it implies
     * a higher likelihood of the device being rooted.
     *
     * @return Returns true if the "su" binary can be executed, indicating
     * potential root access, otherwise false.
     */
    private fun canExecuteSu(): Boolean {
        return try {
            val process = Runtime.getRuntime().exec(arrayOf("/system/xbin/which", "su"))
            process.inputStream.read() != -1
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Checks whether the application is running in a cloned environment.
     *
     * This method determines if the application is a cloned instance by
     * comparing the structure of the application's file directory path
     * with the package name and by inspecting the call stack for specific
     * activity creation patterns.
     *
     * @return Returns true if the application is detected as a cloned app, otherwise false.
     */

    private fun isClonedApp(): Boolean {
        val path = context.filesDir.path
        val pathDotCount = path.count { it == '.' }
        val appIdDotCount = context.packageName.count { it == '.' }

        if (pathDotCount > appIdDotCount) {
            return true
        }
        val stacks = Thread.currentThread().stackTrace
        for (stackTraceElement in stacks) {
            val methodName = stackTraceElement.methodName
            val className = stackTraceElement.className
            if (methodName.equals("callActivityOnCreate") && className.startsWith("com")) {
                return true
            }
        }
        return false
    }

    private val blackListAppList: List<String> = listOf(
        "com.waxmoon.ma.gp",
        "com.cmaster.cloner",
        "com.xunijun.app.gp"
    )

    /**
     * Checks whether any application from the blacklist is installed on the device.
     *
     * The method iterates over a predefined list of blacklisted application package names
     * and verifies if any of the applications are installed on the device.
     *
     * @return Returns true if at least one blacklisted application is installed, otherwise false.
     */
    private fun isBlackListAppInstalled(): Boolean {
        blackListAppList.forEach { packageName ->
            if (context.isAppInstalled(packageName)) {
                return true
            }
        }
        return false
    }

    /**
     * Determines whether the application is running on an emulator in a production release.
     *
     * This method checks for emulator characteristics only in non-debug builds to avoid any false positives
     * during debugging or development. It utilizes the `DeviceUtil.isEmulator` method for the actual emulator checks.
     *
     * @return Returns true if the application is running on an emulator in a production environment, otherwise false.
     */
    private fun isEmulatorForProdRelease(): Boolean {
        if (!BuildConfig.DEBUG) {
            return DeviceUtil.isEmulator()
        }
        return false
    }

    /**
     * Determines whether a debugger is attached to the process.
     *
     * The method checks multiple indicators to determine if*/
    private fun isDebuggerAttached(): Boolean {
        if (BuildConfig.DEBUG) {
            return false
        }
        if (Debug.isDebuggerConnected() || Debug.waitingForDebugger()) {
            return true
        }

        try {
            val debugField = Class.forName("android.os.Debug")
                .getDeclaredMethod("isDebuggerConnected")
            if (debugField.invoke(null) as Boolean) {
                return true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    /**
     * Verifies the app's signature against an expected signature to ensure the integrity and authenticity of the application.
     *
     * @param expectedSignature The expected SHA-256 hashed signature of the application in a colon-separated hexadecimal format.
     * @return Returns true if the app's signature matches the provided expected signature, otherwise false.
     */
    private fun verifyAppSignature(expectedSignature: String): Boolean {
        val packageName = context.packageName
        val pm = context.packageManager
        val packageInfo: PackageInfo =
            pm.getPackageInfo(packageName, PackageManager.GET_SIGNING_CERTIFICATES)

        val signatures =
            packageInfo.signingInfo?.apkContentsSigners

        val md = MessageDigest.getInstance("SHA-256")
        if (signatures != null) {
            for (signature in signatures) {
                val hash = md.digest(signature.toByteArray())
                val hexHash = hash.joinToString(":") { "%02x".format(it) }
                if (hexHash.equals(expectedSignature, ignoreCase = true)) {
                    return true // apk modified
                }
            }
        }
        return false
    }
}