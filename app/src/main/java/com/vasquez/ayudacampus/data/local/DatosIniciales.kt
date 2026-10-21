package com.vasquez.ayudacampus.data.local

import com.vasquez.ayudacampus.data.local.entity.Categoria
import com.vasquez.ayudacampus.data.local.entity.Ticket
import com.vasquez.ayudacampus.data.local.entity.Usuario
import com.vasquez.ayudacampus.data.local.entity.Comentario

object DatosIniciales {
    suspend fun sembrar(db: AyudaDatabase) {
        if (db.usuarioDao().contar() == 0) {
            // 1. Categorías (5)
            val cat1 = Categoria(id = 1, nombre = "Hardware", icono = "pc", activa = true)
            val cat2 = Categoria(id = 2, nombre = "Software", icono = "code", activa = true)
            val cat3 = Categoria(id = 3, nombre = "Red/Wi-fi", icono = "wifi", activa = true)
            val cat4 = Categoria(id = 4, nombre = "Cuenta / Acceso", icono = "key", activa = true)
            val cat5 = Categoria(id = 5, nombre = "Impresion", icono = "printer", activa = true)
            db.categoriaDao().insertarTodas(listOf(cat1, cat2, cat3, cat4, cat5))

            // 2. Usuarios (2: Cliente y Soporte)
            val userCliente = Usuario(
                id = 1,
                nombre = "Juan Perez",
                correo = "juan@campus.test",
                clave = "1234",
                rol = "CLIENTE"
            )
            val userSoporte = Usuario(
                id = 2,
                nombre = "María Soto",
                correo = "soporte@campus.test",
                clave = "admin",
                rol = "ADMIN"
            )
            db.usuarioDao().insertarTodos(listOf(userCliente, userSoporte))

            // 3. Tickets (6 de ejemplo con diferentes estados y fechas relativas)
            val ahora = System.currentTimeMillis()
            val hora = 3600 * 1000L

            val t1 = Ticket(
                id = 1,
                clienteId = 1,
                categoriaId = 1,
                titulo = "Sin internet en Pabellón B",
                descripcion = "No conecta la red Wi-Fi de alumnos.",
                ubicacion = "Pabellón B - Aula 204",
                prioridad = "ALTA",
                estado = "ABIERTO",
                fecha = ahora - 2 * hora,
                fechaCierre = null,
                calificacion = null
            )
            val t2 = Ticket(
                id = 2,
                clienteId = 1,
                categoriaId = 2,
                titulo = "PC no enciende",
                descripcion = "La computadora de la mesa 4 no da video.",
                ubicacion = "Lab C - 302",
                prioridad = "MEDIA",
                estado = "EN_PROCESO",
                fecha = ahora - 5 * hora,
                fechaCierre = null,
                calificacion = null
            )
            val t3 = Ticket(
                id = 3,
                clienteId = 1,
                categoriaId = 3,
                titulo = "Instalación de IDE",
                descripcion = "Necesito Android Studio instalado.",
                ubicacion = "Remoto",
                prioridad = "BAJA",
                estado = "ABIERTO",
                fecha = ahora - 10 * hora,
                fechaCierre = null,
                calificacion = null
            )
            val t4 = Ticket(
                id = 4,
                clienteId = 1,
                categoriaId = 4,
                titulo = "No puedo acceder al Campus",
                descripcion = "Error de contraseña.",
                ubicacion = "Virtual",
                prioridad = "ALTA",
                estado = "RESUELTO",
                fecha = ahora - 24 * hora,
                fechaCierre = ahora - 2 * hora,
                calificacion = 5
            )
            val t5 = Ticket(
                id = 5,
                clienteId = 1,
                categoriaId = 5,
                titulo = "Atasco de papel en impresora",
                descripcion = "Impresora del piso 2 bloqueada.",
                ubicacion = "Biblioteca",
                prioridad = "MEDIA",
                estado = "CERRADO",
                fecha = ahora - 48 * hora,
                fechaCierre = ahora - 20 * hora,
                calificacion = 4
            )
            val t6 = Ticket(
                id = 6,
                clienteId = 1,
                categoriaId = 1,
                titulo = "Cable de red dañado",
                descripcion = "Puerto Ethernet flojo.",
                ubicacion = "Lab A",
                prioridad = "BAJA",
                estado = "ABIERTO",
                fecha = ahora - hora,
                fechaCierre = null,
                calificacion = null)

            db.ticketDao().insertarTicket(t1)
            db.ticketDao().insertarTicket(t2)
            db.ticketDao().insertarTicket(t3)
            db.ticketDao().insertarTicket(t4)
            db.ticketDao().insertarTicket(t5)
            db.ticketDao().insertarTicket(t6)

            val c1 = Comentario(
                id = 1,
                ticketId = 1,
                autorId = 2,
                autorNombre = "María Soporte",
                autorRol = "ADMIN",
                texto = "Estamos revisando la conexion",
                fecha = ahora - hora
            )
            val c2 = Comentario(
                id = 2,
                ticketId = 2,
                autorId = 2,
                autorNombre = "María Soto",
                autorRol = "ADMIN",
                texto = "Estamos revisando la conexión Wi-Fi.",
                fecha = ahora - 4 * hora
            )
            val c3 = Comentario(
                id = 3,
                ticketId = 4,
                autorId = 1,
                autorNombre = "Juan Pérez",
                autorRol = "CLIENTE",
                texto = "Reiniciamos el equipo; por favor, prueba de nuevo.",
                fecha = ahora - 3 * hora
            )
            val c4 = Comentario(
                id = 4,
                ticketId = 4,
                autorId = 2,
                autorNombre = "María Soto",
                autorRol = "ADMIN",
                texto = "Ya pude ingresar al campus. ¡Gracias!",
                fecha = ahora - 2 * hora
            )

            db.ticketDao().insertarComentario(c1)
            db.ticketDao().insertarComentario(c2)
            db.ticketDao().insertarComentario(c3)
            db.ticketDao().insertarComentario(c4)
        }
    }
}