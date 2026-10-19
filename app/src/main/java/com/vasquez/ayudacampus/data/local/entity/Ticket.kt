package com.vasquez.ayudacampus.data.local.entity

import androidx.room3.Entity
import androidx.room3.ForeignKey
import androidx.room3.Index
import androidx.room3.PrimaryKey

@Entity(tableName = "tickets",
    foreignKeys = [
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["id"],
            childColumns = ["clienteId"]
        ),
        ForeignKey(
            entity = Categoria::class,
            parentColumns = ["id"],
            childColumns = ["categoriaId"]
        )
    ],
    indices = [Index("clienteId"), Index("categoriaId")])
data class Ticket(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val clienteId: Int,
    val categoriaId: Int,
    val titulo: String,
    val descripcion: String,
    val ubicacion: String,
    val prioridad: String, //baja, media, alta
    val estado: String, //abierto, en proceso, resuelto, cerrado
    val fecha: Long, //millis
    val fechaCierre: Long?, //preguntar
    val calificacion: Int? //preguntar
)