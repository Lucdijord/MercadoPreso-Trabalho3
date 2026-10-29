package com.example.myapplication.ui.enderecos

import com.example.myapplication.data.local.Endereco

data class EnderecoUiState(
    val listaDeEnderecos: List<Endereco> = emptyList(),
    val carregandoLista: Boolean = true,
    val erroLista: Boolean = false,
    val enderecoDetalhe: Endereco? = null,
    val carregandoDetalhe: Boolean = false,
    val erroDetalhe: Boolean = false,
    val rua: String = "",
    val numero: String = "",
    val bairro: String = "",
    val cidade: String = "",
    val complemento: String = "",
    val enderecoEmEdicao: Endereco? = null
) {
    val textoBotao: String
        get() = if (enderecoEmEdicao == null) "Adicionar Endereço" else "Atualizar Endereço"
}
