package com.example.myapplication.data.repository

import com.example.myapplication.data.local.Endereco
import com.example.myapplication.data.local.EnderecoDAO
import kotlinx.coroutines.flow.Flow

class EnderecoRepository(
    private val enderecoDao: EnderecoDAO
) {
    fun getAllEnderecos(): Flow<List<Endereco>> =
        enderecoDao.observarTodos()

    suspend fun buscarPorId(id: Int): Endereco? =
        enderecoDao.buscarPorId(id)

    suspend fun inserir(endereco: Endereco) {
        enderecoDao.inserir(endereco)
    }

    suspend fun atualizar(endereco: Endereco) {
        enderecoDao.atualizar(endereco)
    }

    suspend fun deletar(endereco: Endereco) {
        enderecoDao.deletar(endereco)
    }
}