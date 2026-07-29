package com.cliniflow.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.abs

private val CoresAvatar = listOf(
    Color(0xFF0F9D8C), // teal
    Color(0xFF3F72AF), // azul
    Color(0xFFB5566C), // rosa queimado
    Color(0xFF8C6D1F), // âmbar escuro
    Color(0xFF5B7B4F), // verde oliva
    Color(0xFF6B5B95), // roxo suave
)

private fun corPara(chave: String): Color {
    val indice = abs(chave.hashCode()) % CoresAvatar.size
    return CoresAvatar[indice]
}

@Composable
fun Avatar(
    nome: String,
    sobrenome: String = "",
    tamanho: Dp = 48.dp
) {
    val iniciais = "${nome.firstOrNull() ?: ""}${sobrenome.firstOrNull() ?: ""}".uppercase()
    val cor = corPara(nome + sobrenome)

    Box(
        modifier = Modifier.size(tamanho).clip(CircleShape).background(cor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = iniciais,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = (tamanho.value * 0.38).sp
        )
    }
}