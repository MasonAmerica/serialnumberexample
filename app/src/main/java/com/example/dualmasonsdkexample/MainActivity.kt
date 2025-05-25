package com.example.dualmasonsdkexample

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.dualmasonsdkexample.ui.theme.DualMasonSDKExampleTheme
//import masonamerica.platform.DeviceIdentifiers
//import masonamerica.platform.MasonFramework

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var ser = "UNKNOWN"
        if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.O) {
            ser = android.os.Build.SERIAL
        } else {
            try {
                // This will only work if deployed to a device via a Mason Controller build.
                //var diA = MasonFramework.get(baseContext, DeviceIdentifiers::class.java)
                //ser = diA.serial

                // Use reflection to load MasonFramework and DeviceIdentifiers at runtime
                val masonFrameworkClass = Class.forName("masonamerica.platform.MasonFramework")
                val deviceIdentifiersClass = Class.forName("masonamerica.platform.DeviceIdentifiers")
                val getMethod = masonFrameworkClass.getMethod("get", android.content.Context::class.java, Class::class.java)
                val di = getMethod.invoke(null, baseContext, deviceIdentifiersClass)
                val getSerialMethod = deviceIdentifiersClass.getMethod("getSerial")
                ser = getSerialMethod.invoke(di) as? String ?: "UNKNOWN"

            } catch (e: Exception) {
                // ser is already set to UNKNOWN
            }
        }

        enableEdgeToEdge()
        setContent {
            DualMasonSDKExampleTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        serial = ser,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(serial: String, modifier: Modifier = Modifier) {
    Text(
        text = "Serial: $serial",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DualMasonSDKExampleTheme {
        Greeting("Android")
    }
}