package com.cliniflow.app.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun proximosDias(quantidade: Int): List<Pair<String, String>> {
    val isoFormat = SimpleDateFormat("yyyy-MM-dd", Locale("pt", "BR"))
    val labelFormat = SimpleDateFormat("dd/MM", Locale("pt", "BR"))
    val base = Calendar.getInstance()
    return (0 until quantidade).map { offset ->
        val dia = base.clone() as Calendar
        dia.add(Calendar.DAY_OF_YEAR, offset)
        isoFormat.format(dia.time) to labelFormat.format(dia.time)
    }
}

fun hoje(): String {
    val isoFormat = SimpleDateFormat("yyyy-MM-dd", Locale("pt", "BR"))
    return isoFormat.format(Calendar.getInstance().time)
}