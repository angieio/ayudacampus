package com.vasquez.ayudacampus.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "comentarios",
    foreignKeys = [
        ForeignKey(
            entity = Ticket::class,
            parentColumns = ["id"],
            childColumns = ["ticketId"],
            onDelete = ForeignKey.CASCADE
        )
    ])
data class Comentario(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val ticketIt: String,
    val autorId: Int,
    val autorNombre: String,
    val autorRol: String,
    val fecha: Long
)