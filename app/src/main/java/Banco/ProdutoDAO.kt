package Banco

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update


@Dao

interface ProdutoDAO {

    @Insert
    suspend fun inserir(produto: Produto)

    @Query("SELECT * FROM produtos")
    suspend fun buscarTodos() : List<Produto>;

    @Query("SELECT * FROM produtos WHERE id = :id")
    suspend fun buscarPorId(id: Int): Produto?

    @Update
    suspend fun atualizar(produto: Produto)

    @Delete
    suspend fun deletar(produto: Produto)
}