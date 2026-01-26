package com.universidade.project_form.ui.componentes

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp

@Composable
fun GraficoCategorias(gastos: Map<String, Double>) {

    if (gastos.isEmpty()) return

    val scrollState = rememberScrollState()
    val larguraBarra = 180f

    Row(
        modifier = Modifier
            .horizontalScroll(scrollState)
            .height(200.dp)
            .padding(start = 10.dp)
    ) {
        Canvas(
            modifier = Modifier
                .width((gastos.size * larguraBarra * 2).dp)
                .height(200.dp)
        ) {
            val maxValor = gastos.values.maxOrNull() ?: 1.0

            gastos.values.forEachIndexed { index, valor ->
                val x = index * larguraBarra * 1.4f
                val barraAltura = size.height * (valor / maxValor).toFloat() * 0.7f

                // Desenha a barra
                drawRect(
                    color = Color(0xFF6650a4),
                    topLeft = Offset(x, size.height * 0.7f - barraAltura),
                    size = Size(larguraBarra, barraAltura)
                )

                // Desenha o nome da categoria
                drawContext.canvas.nativeCanvas.apply {
                    drawText(
                        gastos.keys.toList()[index],
                        x + larguraBarra / 2,
                        size.height - 4f,
                        android.graphics.Paint().apply {
                            textAlign = android.graphics.Paint.Align.CENTER
                            textSize = 28f
                            color = android.graphics.Color.BLACK
                        }
                    )
                }
            }
        }
    }
}
