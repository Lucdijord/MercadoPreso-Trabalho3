package com.example.myapplication.data.repository

import com.example.myapplication.data.local.Produto
import com.example.myapplication.data.local.ProdutoDAO
import kotlinx.coroutines.flow.Flow

class ProdutoRepository(
    private val produtoDao: ProdutoDAO
) {

    fun getAllProdutos(): Flow<List<Produto>> =
        produtoDao.observarTodos()

    suspend fun buscarPorId(id: Int): Produto? =
        produtoDao.buscarPorId(id)

    suspend fun inserir(produto: Produto) {
        produtoDao.inserir(produto)
    }

    suspend fun atualizar(produto: Produto) {
        produtoDao.atualizar(produto)
    }

    suspend fun deletar(produto: Produto) {
        produtoDao.deletar(produto)
    }
}