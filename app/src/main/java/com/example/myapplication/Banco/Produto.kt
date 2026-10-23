package com.example.myapplication.Banco

import androidx.compose.runtime.mutableStateListOf
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myapplication.DadosCarrinho

@Entity(tableName = "produtos")
data class Produto (
    @PrimaryKey(autoGenerate = true)

    val id: Int = 0,
    val nome: String,
    val preco: Double,
    val categoria: String
)

object DadosProdutos {

    val produtos = mutableStateListOf(
        Produto(1, "Fone de Ouvido", 159.90, "Eletrônicos"),
        Produto(2, "Smartwatch", 299.90, "Eletrônicos"),
        Produto(3, "Perfume", 89.90, "Beleza")
    )

    private var proximoId = 4

    fun adicionar(nome: String, preco: Double, categoria: String) {
        produtos.add(
            Produto(proximoId, nome, preco, categoria)
        )
        proximoId++
    }

    fun remover(produto: Produto) {
        produtos.remove(produto)
        DadosCarrinho.remover(produto.id)
    }
}
