package com.example.my_app

import android.app.ActivityManager
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorManager
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.media.AudioDeviceInfo
import android.media.AudioManager
import android.net.wifi.WifiManager
import android.os.BatteryManager
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.provider.Settings
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import android.util.DisplayMetrics
import android.view.WindowManager
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import java.io.File
import java.net.Inet4Address
import java.net.NetworkInterface
import java.util.Collections
import kotlin.math.pow
import kotlin.math.sqrt

class MainActivity : FlutterActivity() {
    private val CHANNEL = "com.example.device_info_pro/native"

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL).setMethodCallHandler { call, result ->
            when (call.method) {
                "getMemoryInfo" -> result.success(getMemoryInfo())
                "getStorageInfo" -> result.success(getStorageInfo())
                "getBatteryInfo" -> result.success(getBatteryInfo())
                "getDisplayInfo" -> result.success(getDisplayInfo())
                "getCpuInfo" -> result.success(getCpuInfo())
                "getKernelInfo" -> result.success(getKernelInfo())
                "getSensorsList" -> result.success(getSensorsList())
                "getCameraInfo" -> result.success(getCameraInfo())
                "getAudioInfo" -> result.success(getAudioInfo())
                "getSimInfo" -> result.success(getSimInfo())
                "getNetworkInfoExtra" -> result.success(getNetworkInfoExtra())
                else -> result.notImplemented()
            }
        }
    }

    private fun getMemoryInfo(): Map<String, Any> {
        val memoryInfo = ActivityManager.MemoryInfo()
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        activityManager.getMemoryInfo(memoryInfo)

        val totalRam = memoryInfo.totalMem
        val availRam = memoryInfo.availMem
        val usedRam = totalRam - availRam

        return mapOf(
            "totalRam" to totalRam,
            "availRam" to availRam,
            "usedRam" to usedRam,
            "lowMemory" to memoryInfo.lowMemory,
            "threshold" to memoryInfo.threshold
        )
    }

    private fun getStorageInfo(): Map<String, Any> {
        val path = Environment.getDataDirectory()
        val stat = StatFs(path.path)
        val blockSize = stat.blockSizeLong
        val totalBlocks = stat.blockCountLong
        val availBlocks = stat.availableBlocksLong

        val totalInternal = totalBlocks * blockSize
        val freeInternal = availBlocks * blockSize
        val usedInternal = totalInternal - freeInternal

        var totalExternal: Long = 0
        var freeExternal: Long = 0
        var usedExternal: Long = 0
        var hasExternal = false

        try {
            val extState = Environment.getExternalStorageState()
            if (extState == Environment.MEDIA_MOUNTED || extState == Environment.MEDIA_MOUNTED_READ_ONLY) {
                val extPath = Environment.getExternalStorageDirectory()
                val extStat = StatFs(extPath.path)
                val extBlockSize = extStat.blockSizeLong
                val extTotalBlocks = extStat.blockCountLong
                val extAvailBlocks = extStat.availableBlocksLong

                totalExternal = extTotalBlocks * extBlockSize
                freeExternal = extAvailBlocks * extBlockSize
                usedExternal = totalExternal - freeExternal
                hasExternal = totalExternal > 0 && totalExternal != totalInternal
            }
        } catch (e: Exception) {
            hasExternal = false
        }

        return mapOf(
            "totalInternal" to totalInternal,
            "freeInternal" to freeInternal,
            "usedInternal" to usedInternal,
            "totalExternal" to totalExternal,
            "freeExternal" to freeExternal,
            "usedExternal" to usedExternal,
            "hasExternal" to hasExternal
        )
    }

    private fun getBatteryInfo(): Map<String, Any> {
        val intentFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        val batteryStatus: Intent? = registerReceiver(null, intentFilter)

        val level = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
        val scale = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
        val batteryPct = if (level != -1 && scale != -1) (level * 100 / scale.toFloat()).toInt() else -1

        val status = batteryStatus?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
        val isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL

        val statusStr = when (status) {
            BatteryManager.BATTERY_STATUS_CHARGING -> "Charging"
            BatteryManager.BATTERY_STATUS_DISCHARGING -> "Discharging"
            BatteryManager.BATTERY_STATUS_FULL -> "Full"
            BatteryManager.BATTERY_STATUS_NOT_CHARGING -> "Not Charging"
            else -> "Unknown"
        }

        val chargePlug = batteryStatus?.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1) ?: -1
        val chargingSource = when (chargePlug) {
            BatteryManager.BATTERY_PLUGGED_AC -> "AC Charger"
            BatteryManager.BATTERY_PLUGGED_USB -> "USB Port"
            BatteryManager.BATTERY_PLUGGED_WIRELESS -> "Wireless"
            else -> if (isCharging) "Connected" else "None"
        }

        val health = batteryStatus?.getIntExtra(BatteryManager.EXTRA_HEALTH, -1) ?: -1
        val healthStr = when (health) {
            BatteryManager.BATTERY_HEALTH_GOOD -> "Good"
            BatteryManager.BATTERY_HEALTH_OVERHEAT -> "Overheat"
            BatteryManager.BATTERY_HEALTH_DEAD -> "Dead"
            BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE -> "Over Voltage"
            BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE -> "Unspecified Failure"
            BatteryManager.BATTERY_HEALTH_COLD -> "Cold"
            else -> "Unknown"
        }

        val tempTenths = batteryStatus?.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0) ?: 0
        val tempCelsius = tempTenths / 10.0

        val voltage = batteryStatus?.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0) ?: 0
        val technology = batteryStatus?.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY) ?: "Unknown"

        return mapOf(
            "level" to batteryPct,
            "isCharging" to isCharging,
            "status" to statusStr,
            "chargingSource" to chargingSource,
            "health" to healthStr,
            "temperature" to tempCelsius,
            "voltage" to voltage,
            "technology" to technology
        )
    }

    private fun getDisplayInfo(): Map<String, Any> {
        val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val displayMetrics = DisplayMetrics()
        val display = windowManager.defaultDisplay
        display.getRealMetrics(displayMetrics)

        val widthPx = displayMetrics.widthPixels
        val heightPx = displayMetrics.heightPixels
        val densityDpi = displayMetrics.densityDpi
        val density = displayMetrics.density
        val xdpi = displayMetrics.xdpi
        val ydpi = displayMetrics.ydpi

        val refreshRate = display.refreshRate

        val widthInches = if (xdpi > 0) widthPx / xdpi else 0f
        val heightInches = if (ydpi > 0) heightPx / ydpi else 0f
        val screenSizeInches = sqrt(widthInches.toDouble().pow(2.0) + heightInches.toDouble().pow(2.0))

        var brightness = -1
        try {
            brightness = Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS)
        } catch (e: Exception) {
            brightness = -1
        }

        return mapOf(
            "widthPx" to widthPx,
            "heightPx" to heightPx,
            "densityDpi" to densityDpi,
            "density" to density,
            "xdpi" to xdpi.toDouble(),
            "ydpi" to ydpi.toDouble(),
            "refreshRate" to refreshRate.toDouble(),
            "screenSizeInches" to screenSizeInches,
            "brightness" to brightness
        )
    }

    private fun getCpuInfo(): Map<String, Any> {
        val cores = Runtime.getRuntime().availableProcessors()
        val abis = Build.SUPPORTED_ABIS.toList()
        val hardware = Build.HARDWARE

        var processorName = "Unknown"
        try {
            val file = File("/proc/cpuinfo")
            if (file.exists()) {
                file.forEachLine { line ->
                    if (line.startsWith("Hardware") || line.startsWith("Processor") || line.startsWith("model name")) {
                        val parts = line.split(":")
                        if (parts.size > 1) {
                            processorName = parts[1].trim()
                        }
                    }
                }
            }
        } catch (e: Exception) {
            processorName = hardware
        }
        if (processorName == "Unknown") {
            processorName = hardware
        }

        return mapOf(
            "cores" to cores,
            "architecture" to abis,
            "hardware" to hardware,
            "processorName" to processorName
        )
    }

    private fun getKernelInfo(): String {
        return System.getProperty("os.version") ?: "Unknown"
    }

    private fun getSensorsList(): List<Map<String, Any>> {
        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val sensors = sensorManager.getSensorList(Sensor.TYPE_ALL)
        val sensorListMap = mutableListOf<Map<String, Any>>()

        for (sensor in sensors) {
            sensorListMap.add(
                mapOf(
                    "name" to sensor.name,
                    "vendor" to sensor.vendor,
                    "version" to sensor.version,
                    "type" to sensor.type,
                    "stringType" to sensor.stringType,
                    "maxRange" to sensor.maximumRange.toDouble(),
                    "resolution" to sensor.resolution.toDouble(),
                    "power" to sensor.power.toDouble(),
                    "minDelay" to sensor.minDelay
                )
            )
        }

        return sensorListMap
    }

    private fun getCameraInfo(): Map<String, Any> {
        val cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        var cameraCount = 0
        var hasFront = false
        var hasRear = false
        val cameraDetailsList = mutableListOf<Map<String, Any>>()

        try {
            val cameraIds = cameraManager.cameraIdList
            cameraCount = cameraIds.size

            for (id in cameraIds) {
                val chars = cameraManager.getCameraCharacteristics(id)
                val facing = chars.get(CameraCharacteristics.LENS_FACING)

                val facingStr = when (facing) {
                    CameraCharacteristics.LENS_FACING_FRONT -> {
                        hasFront = true
                        "Front"
                    }
                    CameraCharacteristics.LENS_FACING_BACK -> {
                        hasRear = true
                        "Rear"
                    }
                    else -> "External"
                }

                val mapConfig = chars.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
                val sizesList = mutableListOf<String>()
                var maxMegapixels = 0.0

                if (mapConfig != null) {
                    val sizes = mapConfig.getOutputSizes(android.graphics.ImageFormat.JPEG) ?: emptyArray()
                    for (size in sizes) {
                        val mp = (size.width.toLong() * size.height.toLong()) / 1_000_000.0
                        if (mp > maxMegapixels) {
                            maxMegapixels = mp
                        }
                        if (sizesList.size < 5) {
                            sizesList.add("${size.width}x${size.height} (${String.format("%.1f", mp)} MP)")
                        }
                    }
                }

                val flashAvailable = chars.get(CameraCharacteristics.FLASH_INFO_AVAILABLE) ?: false
                val afModes = chars.get(CameraCharacteristics.CONTROL_AF_AVAILABLE_MODES) ?: intArrayOf()
                val hasAutoFocus = afModes.size > 1

                val fpsRanges = chars.get(CameraCharacteristics.CONTROL_AE_AVAILABLE_TARGET_FPS_RANGES)
                var maxFps = 30
                fpsRanges?.forEach { range ->
                    if (range.upper > maxFps) {
                        maxFps = range.upper
                    }
                }

                cameraDetailsList.add(
                    mapOf(
                        "id" to id,
                        "facing" to facingStr,
                        "resolutions" to sizesList,
                        "maxMegapixels" to String.format("%.1f MP", maxMegapixels),
                        "hasFlash" to flashAvailable,
                        "hasAutoFocus" to hasAutoFocus,
                        "maxFps" to maxFps
                    )
                )
            }
        } catch (e: Exception) {
            // Permission or camera service error
        }

        return mapOf(
            "cameraCount" to cameraCount,
            "hasFront" to hasFront,
            "hasRear" to hasRear,
            "cameras" to cameraDetailsList
        )
    }

    private fun getAudioInfo(): Map<String, Any> {
        val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        var hasSpeaker = packageManager.hasSystemFeature(PackageManager.FEATURE_AUDIO_OUTPUT)
        var hasMic = packageManager.hasSystemFeature(PackageManager.FEATURE_MICROPHONE)

        val outputDevices = mutableListOf<String>()
        val inputDevices = mutableListOf<String>()

        val devices = audioManager.getDevices(AudioManager.GET_DEVICES_ALL)
        for (device in devices) {
            val name = getAudioDeviceTypeName(device.type)
            if (device.isSink && !outputDevices.contains(name)) {
                outputDevices.add(name)
            }
            if (device.isSource && !inputDevices.contains(name)) {
                inputDevices.add(name)
            }
        }

        if (outputDevices.isEmpty()) {
            if (hasSpeaker) outputDevices.add("Built-in Speaker")
        }
        if (inputDevices.isEmpty()) {
            if (hasMic) inputDevices.add("Built-in Microphone")
        }

        return mapOf(
            "hasSpeaker" to hasSpeaker,
            "hasMicrophone" to hasMic,
            "outputDevices" to outputDevices,
            "inputDevices" to inputDevices
        )
    }

    private fun getAudioDeviceTypeName(type: Int): String {
        return when (type) {
            AudioDeviceInfo.TYPE_BUILTIN_SPEAKER -> "Built-in Speaker"
            AudioDeviceInfo.TYPE_BUILTIN_EARPIECE -> "Built-in Earpiece"
            AudioDeviceInfo.TYPE_BUILTIN_MIC -> "Built-in Microphone"
            AudioDeviceInfo.TYPE_WIRED_HEADSET -> "Wired Headset"
            AudioDeviceInfo.TYPE_WIRED_HEADPHONES -> "Wired Headphones"
            AudioDeviceInfo.TYPE_BLUETOOTH_A2DP -> "Bluetooth Audio (A2DP)"
            AudioDeviceInfo.TYPE_BLUETOOTH_SCO -> "Bluetooth Handsfree (SCO)"
            AudioDeviceInfo.TYPE_USB_DEVICE -> "USB Audio Device"
            AudioDeviceInfo.TYPE_USB_HEADSET -> "USB Headset"
            else -> "Audio Device ($type)"
        }
    }

    private fun getSimInfo(): Map<String, Any> {
        val telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        var simStateStr = when (telephonyManager.simState) {
            TelephonyManager.SIM_STATE_READY -> "Ready"
            TelephonyManager.SIM_STATE_ABSENT -> "Absent"
            TelephonyManager.SIM_STATE_PIN_REQUIRED -> "PIN Required"
            TelephonyManager.SIM_STATE_PUK_REQUIRED -> "PUK Required"
            TelephonyManager.SIM_STATE_NETWORK_LOCKED -> "Network Locked"
            else -> "Unknown"
        }

        var carrierName = "Restricted by Android"
        try {
            val simOperatorName = telephonyManager.simOperatorName
            if (!simOperatorName.isNullOrEmpty()) {
                carrierName = simOperatorName
            } else {
                val netOperatorName = telephonyManager.networkOperatorName
                if (!netOperatorName.isNullOrEmpty()) {
                    carrierName = netOperatorName
                }
            }
        } catch (e: Exception) {
            carrierName = "Restricted by Android"
        }

        var countryIso = "Unknown"
        try {
            val iso = telephonyManager.simCountryIso
            if (!iso.isNullOrEmpty()) {
                countryIso = iso.uppercase()
            }
        } catch (e: Exception) {
            countryIso = "Unknown"
        }

        var simCount = 1
        try {
            val subscriptionManager = getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager
            simCount = subscriptionManager.activeSubscriptionInfoCountMax
            if (simCount <= 0) simCount = 1
        } catch (e: Exception) {
            simCount = 1
        }

        val networkTypeStr = getNetworkTypeName(telephonyManager)

        return mapOf(
            "simState" to simStateStr,
            "carrierName" to carrierName,
            "countryIso" to countryIso,
            "simCount" to simCount,
            "networkType" to networkTypeStr
        )
    }

    private fun getNetworkTypeName(telephonyManager: TelephonyManager): String {
        return try {
            when (telephonyManager.networkType) {
                TelephonyManager.NETWORK_TYPE_NR -> "5G (NR)"
                TelephonyManager.NETWORK_TYPE_LTE -> "4G (LTE)"
                TelephonyManager.NETWORK_TYPE_HSPAP,
                TelephonyManager.NETWORK_TYPE_HSPA,
                TelephonyManager.NETWORK_TYPE_UMTS -> "3G"
                TelephonyManager.NETWORK_TYPE_GPRS,
                TelephonyManager.NETWORK_TYPE_EDGE -> "2G"
                else -> "Cellular"
            }
        } catch (e: Exception) {
            "Cellular"
        }
    }

    private fun getNetworkInfoExtra(): Map<String, Any> {
        val macAddress = "Restricted by Android" // Android 6.0+ restrictions

        var ipv4 = "Unavailable"
        var ipv6 = "Unavailable"

        try {
            val interfaces = Collections.list(NetworkInterface.getNetworkInterfaces())
            for (intf in interfaces) {
                val addrs = Collections.list(intf.inetAddresses)
                for (addr in addrs) {
                    if (!addr.isLoopbackAddress) {
                        val hostAddress = addr.hostAddress ?: continue
                        val isIPv4 = addr is Inet4Address
                        if (isIPv4) {
                            if (ipv4 == "Unavailable") ipv4 = hostAddress
                        } else {
                            val delim = hostAddress.indexOf('%')
                            ipv6 = if (delim < 0) hostAddress.uppercase() else hostAddress.substring(0, delim).uppercase()
                        }
                    }
                }
            }
        } catch (e: Exception) {
            // Ignore
        }

        var bluetoothStatus = "Disabled"
        try {
            val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
            if (bluetoothAdapter == null) {
                bluetoothStatus = "Not Supported"
            } else if (bluetoothAdapter.isEnabled) {
                bluetoothStatus = "Enabled"
            } else {
                bluetoothStatus = "Disabled"
            }
        } catch (e: Exception) {
            bluetoothStatus = "Permission Required"
        }

        return mapOf(
            "macAddress" to macAddress,
            "ipv4" to ipv4,
            "ipv6" to ipv6,
            "bluetoothStatus" to bluetoothStatus
        )
    }
}
