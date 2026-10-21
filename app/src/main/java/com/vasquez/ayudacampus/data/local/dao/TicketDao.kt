package com.vasquez.ayudacampus.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.vasquez.ayudacampus.data.local.entity.Comentario
import com.vasquez.ayudacampus.data.local.entity.Ticket
import com.vasquez.ayudacampus.data.local.relation.TicketConDetalle
import kotlinx.coroutines.flow.Flow
import com.vasquez.ayudacampus.model.ConteoCategoria

@Dao
interface TicketDao {
    // --- Escritura ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTicket(t: Ticket): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarComentario(c: Comentario)

    @Query("UPDATE tickets SET estado = :estado WHERE id = :id")
    suspend fun actualizarEstado(id: Int, estado: String)

    @Query("UPDATE tickets SET prioridad = :prioridad WHERE id = :id")
    suspend fun actualizarPrioridad(id: Int, prioridad: String)

    // --- Cerrar y Reabrir ---
    @Query("UPDATE tickets SET estado = 'CERRADO', calificacion = :calificacion, fechaCierre = :fechaCierre WHERE id = :id")
    suspend fun cerrar(id: Int, calificacion: Int, fechaCierre: Long)

    @Query("UPDATE tickets SET estado = 'ABIERTO', calificacion = NULL, fechaCierre = NULL WHERE id = :id")
    suspend fun reabrir(id: Int)

    @Query("SELECT COUNT(*) FROM tickets WHERE categoriaId = :categoriaId")
    suspend fun contarPorCategoriaId(categoriaId: Int): Int

    // --- Lectura (Requieren @Transaction por usar @Relation) ---
    @Transaction
    @Query("SELECT * FROM tickets WHERE clienteId = :clienteId ORDER BY fecha DESC")
    fun observarPorCliente(clienteId: Int): Flow<List<TicketConDetalle>>

    @Transaction
    @Query("SELECT * FROM tickets ORDER BY CASE prioridad WHEN 'ALTA' THEN 0 WHEN 'MEDIA' THEN 1 ELSE 2 END, fecha DESC")
    fun observarTodos(): Flow<List<TicketConDetalle>>

    @Transaction
    @Query("SELECT * FROM tickets WHERE id = :id")
    fun observarUno(id: Int): Flow<TicketConDetalle>

    // --- Panel / Estadísticas ---
    @Query("SELECT COUNT(*) FROM tickets WHERE estado = :estado")
    fun contarPorEstado(estado: String): Flow<Int>

    @Query("SELECT c.nombre AS categoriaNombre, COUNT(t.id) AS total FROM categorias c LEFT JOIN tickets t ON c.id = t.categoriaId GROUP BY c.id")
    fun contarPorCategoria(): Flow<List<ConteoCategoria>>

    @Query("SELECT COUNT(*) FROM tickets WHERE prioridad = 'ALTA' AND estado = 'ABIERTO' AND fecha < :limite")
    fun contarAltasSinAtender(limite: Long): Flow<Int>

    @Query("SELECT AVG(fechaCierre - fecha) FROM tickets WHERE estado = 'CERRADO' AND fechaCierre IS NOT NULL")
    fun obtenerTiempoPromedioResolucion(): Flow<Double?>

    @Query("SELECT COUNT(*) FROM tickets WHERE estado != 'CERRADO' AND estado != 'RESUELTO' AND (CASE prioridad WHEN 'ALTA' THEN :ahora - fecha > 14400000 WHEN 'MEDIA' THEN :ahora - fecha > 86400000 ELSE :ahora - fecha > 259200000 END)")
    fun contarTicketsFueraSLA(ahora: Long): Flow<Int>
}
