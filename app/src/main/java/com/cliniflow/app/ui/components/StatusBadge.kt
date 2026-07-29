package com.cliniflow.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class BadgeTipo { SUCESSO, ATENCAO, ERRO, NEUTRO, INFO }

@Composable
fun StatusBadge(texto: String, tipo: BadgeTipo) {
    val (corFundo, corTexto) = coresPara(tipo)
    Text(
        text = texto,
        color = corTexto,
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(corFundo)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    )
}

private fun coresPara(tipo: BadgeTipo): Pair<Color, Color> = when (tipo) {
    BadgeTipo.SUCESSO -> Color(0xFFCDEFD9) to Color(0xFF1B6B39)
    BadgeTipo.ATENCAO -> Color(0xFFFBE7C6) to Color(0xFF8C6D1F)
    BadgeTipo.ERRO -> Color(0xFFF8D3D3) to Color(0xFFB3261E)
    BadgeTipo.NEUTRO -> Color(0xFFE3E8E7) to Color(0xFF44504E)
    BadgeTipo.INFO -> Color(0xFFD3E4F5) to Color(0xFF2C5C8C)
}