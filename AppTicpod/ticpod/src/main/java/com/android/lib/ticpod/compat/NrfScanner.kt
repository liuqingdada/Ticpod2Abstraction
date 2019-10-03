package com.android.lib.ticpod.compat

import android.bluetooth.BluetoothAdapter
import android.os.Build
import com.android.lib.ticpod.FastPairConstant
import com.android.lib.ticpod.fastble.utils.LogUtil
import no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerCompat
import no.nordicsemi.android.support.v18.scanner.ScanCallback
import no.nordicsemi.android.support.v18.scanner.ScanSettings

/**
 * Created by andy
 * 2019-07-26.
 * Email: 1239604859@qq.com
 */
class NrfScanner(
    private val bluetoothAdapter: BluetoothAdapter,
    private val scanCallback: ScanCallback
) {
    companion object {
        private const val TAG = "NrfScanner"

        private const val REPORT_DELAY: Long = FastPairConstant.SOURCE.BLE_SCAN_REPORT_DELAY
    }

    fun startLeCompatScan() {
        if (bluetoothAdapter.isEnabled) {
            val settings = ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .setReportDelay(REPORT_DELAY)
                .setUseHardwareBatchingIfSupported(false)
                .build()

            val scanner = BluetoothLeScannerCompat.getScanner()
            scanner.startScan(null, settings, scanCallback)
            LogUtil.d(TAG, "startLeCompatScan: ")
        }
    }

    fun stopLeCompatScan() {
        val scanner = BluetoothLeScannerCompat.getScanner()
        scanner.stopScan(scanCallback)
        LogUtil.d(TAG, "stopLeCompatScan: ")
    }

    fun getNativeCallback(scanCallback: ScanCallback): android.bluetooth.le.ScanCallback? {
        val scanner = BluetoothLeScannerCompat.getScanner()
        return when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> getRealCallback(
                scanner,
                scanner.javaClass.superclass?.superclass,
                scanCallback
            )
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> getRealCallback(
                scanner,
                scanner.javaClass.superclass,
                scanCallback
            )
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> getRealCallback(
                scanner,
                scanner.javaClass,
                scanCallback
            )
            else -> null
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun getRealCallback(
        scanner: BluetoothLeScannerCompat,
        clazz: Class<*>?,
        scanCallback: ScanCallback
    ): android.bluetooth.le.ScanCallback? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                val feildWrappers = clazz?.getDeclaredField("wrappers")
                feildWrappers?.isAccessible = true
                val map = feildWrappers?.get(scanner)
                if (map != null) {
                    val wrappers = map as Map<ScanCallback, *>
                    val wrapper = wrappers[scanCallback]
                    val fieldCallback = wrapper?.javaClass?.getDeclaredField("nativeCallback")
                    fieldCallback?.isAccessible = true
                    val nativeCallback = fieldCallback?.get(wrapper)
                    return nativeCallback as? android.bluetooth.le.ScanCallback
                }
            } catch (e: Exception) {
                LogUtil.e(TAG, "getNativeCallback: ", e)
            }
        }
        return null
    }
}