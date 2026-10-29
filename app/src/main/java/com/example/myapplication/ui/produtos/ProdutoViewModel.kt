package com.example.myapplication.ui.produtos

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.Produto
import com.example.myapplication.data.repository.ProdutoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProdutoViewModel(
    private val repository: ProdutoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProdutoUiState())
    val uiState: StateFlow<ProdutoUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAllProdutos()
                .flowOn(Dispatchers.IO)
                .catch { erro ->
                    Log.e("ProdutoViewModel", "Erro ao carregar produtos", erro)
                    _uiState.update { it.copy(carregando = false) }
                }
                .collect { produtos ->
                    _uiState.update { it.copy(listaDeProdutos = produtos, carregando = false) }
                }
        }
    }

    fun onNomeChange(nome: String) {
        _uiState.update { it.copy(nome = nome) }
    }

    fun onPrecoChange(preco: String) {
        _uiState.update { it.copy(preco = preco) }
    }

    fun onCategoriaChange(categoria: String) {
        _uiState.update { it.copy(categoria = categoria) }
    }

    fun onEdit(produto: Produto) {
        _uiState.update {
            it.copy(
                produtoEmEdicao = produto,
                nome = produto.nome,
                preco = produto.preco.toString(),
                categoria = produto.categoria
            )
        }
    }

    fun carregarProduto(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val produto = repository.buscarPorId(id)
            if (produto != null) onEdit(produto)
        }
    }

    fun carregarDetalheProduto(id: Int) {
        _uiState.update {
            it.copy(produtoDetalhe = null, carregandoDetalhe = true, erroDetalhe = false)
        }
        viewModelScope.launch {
            try {
                val produto = withContext(Dispatchers.IO) {
                    repository.buscarPorId(id)
                }
                _uiState.update {
                    it.copy(produtoDetalhe = produto, carregandoDetalhe = false)
                }
            } catch (erro: Exception) {
                Log.e("ProdutoViewModel", "Erro ao buscar detalhes do produto", erro)
                _uiState.update {
                    it.copy(produtoDetalhe = null, carregandoDetalhe = false, erroDetalhe = true)
                }
            }
        }
    }

    fun onDelete(produto: Produto, onResult: (Boolean) -> Unit = {}) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) { repository.deletar(produto) }
                onResult(true)
            } catch (erro: Exception) {
                Log.e("ProdutoViewModel", "Erro ao excluir produto", erro)
                onResult(false)
            }
        }
    }

    fun onSave(onResult: (Boolean) -> Unit = {}) {
        val state = _uiState.value
        val valor = state.preco.replace(',', '.').toDoubleOrNull()
        if (state.nome.isBlank() || state.categoria.isBlank() || valor == null || !valor.isFinite() || valor <= 0) {
            onResult(false)
            return
        }

        val produto = state.produtoEmEdicao?.copy(
            nome = state.nome.trim(), preco = valor, categoria = state.categoria.trim()
        ) ?: Produto(nome = state.nome.trim(), preco = valor, categoria = state.categoria.trim())

        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    if (state.produtoEmEdicao == null) repository.inserir(produto)
                    else repository.atualizar(produto)
                }
                limparCampos()
                onResult(true)
            } catch (erro: Exception) {
                Log.e("ProdutoViewModel", "Erro ao salvar produto", erro)
                onResult(false)
            }
        }
    }

    fun limparCampos() {
        _uiState.update {
            it.copy(nome = "", preco = "", categoria = "", produtoEmEdicao = null)
        }
    }
}
