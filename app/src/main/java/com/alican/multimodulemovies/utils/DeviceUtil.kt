package com.alican.multimodulemovies.utils

import android.os.Build
import java.io.File

object DeviceUtil {

    private val GENY_FILES = arrayOf(
        "/dev/socket/genyd",
        "/dev/socket/baseband_genyd"
    )
    private val PIPES = arrayOf(
        "/dev/socket/qemud",
        "/dev/qemu_pipe"
    )
    private val X86_FILES = arrayOf(
        "ueventd.android_x86.rc",
        "x86.prop",
        "ueventd.ttVM_x86.rc",
        "init.ttVM_x86.rc",
        "fstab.ttVM_x86",
        "fstab.vbox86",
        "init.vbox86.rc",
        "ueventd.vbox86.rc"
    )
    private val ANDY_FILES = arrayOf(
        "fstab.andy",
        "ueventd.andy.rc"
    )
    private val NOX_FILES = arrayOf(
        "fstab.nox",
        "init.nox.rc",
        "ueventd.nox.rc"
    )

    val BLUE_STACKS_FILES = arrayOf(
        "/mnt/windows/BstSharedFolder",
        "/mnt/prebundledapps/bluestacks.prop"
    )

    fun isEmulator(): Boolean {
        return basicEmulatorCheck() || advancedEmulatorCheck()
    }

    private fun basicEmulatorCheck(): Boolean {
        return ((Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || (Build.BRAND.lowercase().contains("sony") && !isRadioAvailable())
                || Build.DEVICE.lowercase().contains("bluestacks")
                || Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.HARDWARE.contains("goldfish")
                || Build.HARDWARE.contains("ranchu")
                || Build.HARDWARE.contains("vbox86")
                || Build.HARDWARE.lowercase().contains("nox")
                || Build.HARDWARE.lowercase().contains("intel")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MODEL.lowercase().contains("droid4x")
                || Build.MODEL.lowercase().contains("bluestacks")
                || Build.MANUFACTURER.contains("Genymotion")
                || Build.MANUFACTURER.contains("Genymobile")
                || Build.MANUFACTURER.lowercase().contains("bluestacks")
                || Build.PRODUCT.contains("sdk_google")
                || Build.PRODUCT.contains("google_sdk")
                || Build.PRODUCT.contains("sdk")
                || Build.PRODUCT.contains("sdk_x86")
                || Build.PRODUCT.contains("sdk_gphone64_arm64")
                || Build.PRODUCT.contains("vbox86p")
                || Build.PRODUCT.contains("emulator")
                || Build.PRODUCT.contains("simulator")
                || Build.PRODUCT.lowercase().contains("nox")
                || Build.BOARD.lowercase().contains("nox")
                || Build.BOOTLOADER.lowercase().contains("nox"))
    }

    private fun advancedEmulatorCheck(): Boolean {
        return (checkFiles(GENY_FILES)
                || checkFiles(ANDY_FILES)
                || checkFiles(NOX_FILES)
                || checkFiles(X86_FILES)
                || checkFiles(PIPES)
                || checkFiles(BLUE_STACKS_FILES))
    }

    private fun checkFiles(targets: Array<String>): Boolean {
        for (pipe in targets) {
            val file = File(pipe)
            if (file.exists()) {
                return true
            }
        }
        return false
    }

    fun isRadioAvailable(): Boolean {
        val radioVersion = Build.getRadioVersion() ?: ""
        return radioVersion.isNotEmpty() && radioVersion != "1.0.0.0"
    }
}