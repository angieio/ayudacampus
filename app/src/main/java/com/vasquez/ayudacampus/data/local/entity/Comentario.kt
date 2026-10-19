package com.vasquez.ayudacampus.data.local.entity

import androidx.room3.Entity
import androidx.room3.ForeignKey
import androidx.room3.Index
import androidx.room3.PrimaryKey

@Entity(tableName = "comentarios",
    foreignKeys = [
        ForeignKey(
            entity = Ticket::class,
            parentColumns = ["id"],
            childColumns = ["ticketId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("ticketId")])
data class Comentario(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val ticketId: Int,
    val autorId: Int,
    val autorNombre: String,
    val autorRol: String,
    val fecha: Long
)