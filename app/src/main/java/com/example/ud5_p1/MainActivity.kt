package com.example.ud5_p1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                VistaSensores()
            }
        }
    }
}

@Composable
fun VistaSensores() {
    val context = LocalContext.current

    // Sección Acelerómetro
    var x by remember { mutableStateOf(0f) }
    var y by remember { mutableStateOf(0f) }
    var z by remember { mutableStateOf(0f) }

    val sensorManager = remember {
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    val accelerometer = remember {
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }

    DisposableEffect(accelerometer) {
        if (accelerometer == null) {
            onDispose { }
        } else {
            val listener = object : SensorEventListener {
                override fun onSensorChanged(event: SensorEvent) {
                    x = event.values[0]
                    y = event.values[1]
                    z = event.values[2]
                }

                override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                    // opcional
                }
            }

            sensorManager.registerListener(
                listener,
                accelerometer,
                SensorManager.SENSOR_DELAY_UI
            )

            onDispose {
                sensorManager.unregisterListener(listener)
            }
        }
    }


    // Sección Sensor de Luz
    var lux by remember { mutableStateOf(0f) }

    val lightSensor = remember {
        sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
    }

    DisposableEffect(lightSensor) {
        if (lightSensor == null) {
            onDispose { }
        } else {
            val listener = object : SensorEventListener {
                override fun onSensorChanged(event: SensorEvent) {
                    lux = event.values[0]
                }

                override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                    // opcional
                }
            }

            sensorManager.registerListener(
                listener,
                lightSensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )

            onDispose {
                sensorManager.unregisterListener(listener)
            }
        }
    }

    val backgroundColor = determinarColorFondo(lux)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = backgroundColor
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            // Título
            Text(
                text = "Pantalla de sensores",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp)
            )

            // Sección Acelerómetro
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Acelerómetro",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    if (accelerometer == null) {
                        Text("Acelerómetro no disponible en este dispositivo")
                    } else {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            ValorEje("X", x)
                            ValorEje("Y", y)
                            ValorEje("Z", z)
                        }
                    }
                }
            }

            // Sección Sensor de Luz
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Sensor de Luminosidad",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    if (lightSensor == null) {
                        Text("Sensor de luz no disponible en este dispositivo")
                    } else {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = lux.toString() + " lux",
                                fontSize = 32.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = interpretarLuminosidad(lux),
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ValorEje(eje: String, valor: Float) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Eje $eje:",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = String.format("%.2f m/s²", valor),
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

fun determinarColorFondo(lux: Float): Color {
    return when {
        lux < 10 -> Color(0xFF1A1A2E)       // Muy oscuro
        lux < 50 -> Color(0xFF2D2D44)       // Oscuro
        lux < 500 -> Color(0xFFE8E8E8)      // Normal
        lux < 10000 -> Color(0xFFF5F5F5)    // Claro
        else -> Color(0xFFFFFFFF)           // Muy claro
    }
}

fun interpretarLuminosidad(lux: Float): String {
    return when {
        lux < 10 -> "Oscuro"
        lux < 50 -> "Penumbra"
        lux < 500 -> "Luz normal"
        lux < 10000 -> "Bien iluminado"
        else -> "Muy iluminado"
    }
}