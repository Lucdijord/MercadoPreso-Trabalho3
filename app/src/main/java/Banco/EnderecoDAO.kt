package Banco

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface EnderecoDAO {
    @Insert
    suspend fun inserir(endereco: Endereco)

    @Query("SELECT * FROM enderecos")
    suspend fun buscarTodos(): List<Endereco>

    @Query("SELECT * FROM enderecos WHERE id = :id")
    suspend fun buscarPorId(id: Int): Endereco?

    @Update
    suspend fun atualizar(endereco: Endereco)

    @Delete
    suspend fun deletar(endereco: Endereco)
}