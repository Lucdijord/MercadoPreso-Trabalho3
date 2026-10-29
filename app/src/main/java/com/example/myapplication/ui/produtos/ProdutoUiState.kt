package com.example.myapplication.ui.produtos

import com.example.myapplication.data.local.Produto

data class ProdutoUiState(
    val listaDeProdutos: List<Produto> = emptyList(),
    val carregando: Boolean = true,
    val nome: String = "",
    val preco: String = "",
    val categoria: String = "",
    val produtoEmEdicao: Produto? = null,
    val produtoDetalhe: Produto? = null,
    val carregandoDetalhe: Boolean = false,
    val erroDetalhe: Boolean = false
) {
    val textoBotao: String
        get() = if (produtoEmEdicao == null) "Adicionar Produto" else "Atualizar Produto"
}
