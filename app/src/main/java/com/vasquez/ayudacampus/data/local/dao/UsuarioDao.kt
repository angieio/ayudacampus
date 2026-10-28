package com.vasquez.ayudacampus.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vasquez.ayudacampus.data.local.entity.Usuario

@Dao
interface UsuarioDao {
    @Query("SELECT * FROM usuarios WHERE correo = :correo AND clave = :clave")
    suspend fun login(correo: String, clave: String): Usuario?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTodos(lista: List<Usuario>)

    @Query("SELECT COUNT(*) FROM usuarios")
    suspend fun contar(): Int

    @Query("SELECT * FROM usuarios WHERE id = :id LIMIT 1")
    suspend fun obtenerPorId(id: Int): Usuario?

    @Query("SELECT * FROM usuarios WHERE rol = :rol ORDER BY id LIMIT 1")
    suspend fun obtenerPorRol(rol: String): Usuario?

    @Query("SELECT * FROM usuarios WHERE correo = :correo LIMIT 1")
    suspend fun obtenerPorCorreo(correo: String): Usuario?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(usuario: Usuario): Long

    @androidx.room.Update
    suspend fun actualizar(usuario: Usuario)
}
