
package com.example.myapplication

import com.example.myapplication.Banco.Produto
import androidx.compose.runtime.mutableStateListOf

data class ItemCarrinho(
    val produto: Produto,
    val quantidade: Int
)

object DadosCarrinho {

    val itens = mutableStateListOf<ItemCarrinho>()

    fun adicionar(produto: Produto, quantidade: Int) {

        val indice = itens.indexOfFirst {
            it.produto.id == produto.id
        }

        if (indice >= 0) {
            val itemAtual = itens[indice]

            itens[indice] = itemAtual.copy(
                quantidade = itemAtual.quantidade + quantidade
            )
        } else {
            itens.add(ItemCarrinho(produto, quantidade))
        }
    }

    fun remover(produtoId: Int) {
        val indice = itens.indexOfFirst {
            it.produto.id == produtoId
        }

        if (indice >= 0) {
            itens.removeAt(indice)
        }
    }

    fun calcularTotal(): Double {
        return itens.sumOf {
            it.produto.preco * it.quantidade
        }
    }
}
