package com.vasquez.ayudacampus.data.repository

import com.vasquez.ayudacampus.data.local.AyudaDatabase
import com.vasquez.ayudacampus.data.local.entity.Categoria
import com.vasquez.ayudacampus.data.local.entity.Comentario
import com.vasquez.ayudacampus.data.local.entity.Ticket
import com.vasquez.ayudacampus.data.local.entity.Usuario
import com.vasquez.ayudacampus.data.local.relation.TicketConDetalle
import com.vasquez.ayudacampus.model.ConteoCategoria
import com.vasquez.ayudacampus.model.EstadoTicket
import com.vasquez.ayudacampus.model.Prioridad
import kotlinx.coroutines.flow.Flow

class AyudaRepositoryRoom(private val db: AyudaDatabase) : AyudaRepository {
    private val usuarioDao = db.usuarioDao()
    private val ticketDao = db.ticketDao()
    private val categoriaDao = db.categoriaDao()

    override suspend fun login(correo: String, clave: String): Usuario? {
        val claveInt = clave.toIntOrNull() ?: 0
        return usuarioDao.login(correo, claveInt)
    }

    override fun observarCategorias(): Flow<List<Categoria>> {
        return categoriaDao.observarTodas()
    }

    override fun observarCategoriasActivas(): Flow<List<Categoria>> {
        return categoriaDao.observarActivas()
    }

    override suspend fun guardarCategoria(c: Categoria) {
        categoriaDao.insertar(c)
    }

    override suspend fun eliminarCategoria(c: Categoria) {
        categoriaDao.eliminar(c)
    }

    override suspend fun cambiarActiva(id: Int, activa: Boolean) {
        categoriaDao.cambiarActiva(id, activa)
    }

    override suspend fun crearTicket(
        clienteId: Int,
        categoriaId: Int,
        titulo: String,
        descripcion: String,
        ubicacion: String,
        prioridad: Prioridad
    ): Long {
        val ticket = Ticket(
            clienteId = clienteId,
            categoriaId = categoriaId,
            titulo = titulo,
            descripcion = descripcion,
            ubicacion = ubicacion,
            prioridad = prioridad.name,
            estado = EstadoTicket.ABIERTO.name,
            fecha = System.currentTimeMillis(),
            fechaCierre = null,
            calificacion = null
        )
        return ticketDao.insertarTicket(ticket)
    }

    override fun observarTicketsCliente(clienteId: Int): Flow<List<TicketConDetalle>> {
        return ticketDao.observarPorCliente(clienteId)
    }

    override fun observarTodosLosTickets(): Flow<List<TicketConDetalle>> {
        return ticketDao.observarTodos()
    }

    override fun observarTicket(id: Int): Flow<TicketConDetalle?> {
        return ticketDao.observarUno(id)
    }

    override suspend fun cambiarEstado(ticketId: Int, estado: EstadoTicket) {
        ticketDao.actualizarEstado(ticketId, estado.name)
    }

    override suspend fun cambiarPrioridad(ticketId: Int, prioridad: Prioridad) {
        ticketDao.actualizarPrioridad(ticketId, prioridad.name)
    }

    override suspend fun cerrarConCalificacion(ticketId: Int, calificacion: Int) {
        ticketDao.cerrar(ticketId, calificacion, System.currentTimeMillis())
    }

    override suspend fun reabrir(ticketId: Int) {
        ticketDao.reabrir(ticketId)
    }

    override suspend fun agregarComentario(ticketId: Int, autor: Usuario, texto: String) {
        val comentario = Comentario(
            ticketId = ticketId,
            autorId = autor.id,
            autorNombre = autor.nombre,
            autorRol = autor.rol,
            texto = texto,
            fecha = System.currentTimeMillis()
        )
        ticketDao.insertarComentario(comentario)
    }

    override fun observarCantidadPorEstado(estado: EstadoTicket): Flow<Int> {
        return ticketDao.contarPorEstado(estado.name)
    }

    override fun observarCantidadPorCategoria(): Flow<List<ConteoCategoria>> {
        return ticketDao.contarPorCategoria()
    }

    override fun observarAltasSinAtender(limite: Long): Flow<Int> {
        return ticketDao.contarAltasSinAtender(limite)
    }
}