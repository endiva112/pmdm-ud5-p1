package com.example.ud5_p1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.ud5_p1.ui.theme.Ud5_p1Theme

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                SensorScreen()
            }
        }
    }
}

@Composable
fun SensorScreen() {
    // TODO: Aquí declararás tus estados para los sensores
    // Ejemplo:
    // var luzAmbiente by remember { mutableFloatStateOf(0f) }
    // var acelerometroX by remember { mutableFloatStateOf(0f) }
    // var acelerometroY by remember { mutableFloatStateOf(0f) }
    // var acelerometroZ by remember { mutableFloatStateOf(0f) }
    // var sensorLuzDisponible by remember { mutableStateOf(true) }
    // var sensorAcelerometroDisponible by remember { mutableStateOf(true) }

    // TODO: Aquí registrarás tus sensores usando DisposableEffect o LaunchedEffect
    // Recuerda limpiar los recursos cuando el composable salga de la composición

    // Color de fondo según luminosidad (ampliación opcional)
    val backgroundColor = determinarColorFondo(0f) // TODO: Pasar valor real de luz

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

            // Sección Sensor de Luz
            SensorCard(
                titulo = "Sensor de Luminosidad"
            ) {
                // TODO: Verificar si el sensor está disponible
                // if (!sensorLuzDisponible) {
                //     Text("Sensor de luz no disponible en este dispositivo")
                // } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "0.0 lux", // TODO: Usar valor real
                        fontSize = 32.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = interpretarLuminosidad(0f), // TODO: Usar valor real
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                // }
            }

            // Sección Acelerómetro
            SensorCard(
                titulo = "Acelerómetro"
            ) {
                // TODO: Verificar si el sensor está disponible
                // if (!sensorAcelerometroDisponible) {
                //     Text("Acelerómetro no disponible en este dispositivo")
                // } else {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ValorEje("X", 0f) // TODO: Usar valor real
                    ValorEje("Y", 0f) // TODO: Usar valor real
                    ValorEje("Z", 0f) // TODO: Usar valor real
                }
                // }
            }
        }
    }
}

@Composable
fun SensorCard(
    titulo: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = titulo,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            content()
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

fun interpretarLuminosidad(lux: Float): String {
    return when {
        lux < 10 -> "Oscuro"
        lux < 50 -> "Penumbra"
        lux < 500 -> "Luz normal"
        lux < 10000 -> "Bien iluminado"
        else -> "Muy iluminado"
    }
}

// Ampliación opcional: cambiar color de fondo según luminosidad
fun determinarColorFondo(lux: Float): Color {
    return when {
        lux < 10 -> Color(0xFF1A1A2E) // Muy oscuro
        lux < 50 -> Color(0xFF2D2D44) // Oscuro
        lux < 500 -> Color(0xFFE8E8E8) // Normal
        lux < 10000 -> Color(0xFFF5F5F5) // Claro
        else -> Color(0xFFFFFFFF) // Muy claro
    }
}