package com.vasquez.ayudacampus.data.local.entity

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "categorias")
data class Categoria(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val icono: String, // pc, code, wifi, key, printer, tag
    val activa: Boolean = true
)