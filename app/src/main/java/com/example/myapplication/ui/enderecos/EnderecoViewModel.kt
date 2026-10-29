package com.example.myapplication.ui.enderecos

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.Endereco
import com.example.myapplication.data.repository.EnderecoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EnderecoViewModel(
    private val repository: EnderecoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EnderecoUiState())
    val uiState: StateFlow<EnderecoUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAllEnderecos()
                .flowOn(Dispatchers.IO)
                .catch { erro ->
                    Log.e("EnderecoViewModel", "Erro ao carregar endereços", erro)
                    _uiState.update { it.copy(carregandoLista = false, erroLista = true) }
                }
                .collect { enderecos ->
                    _uiState.update {
                        it.copy(listaDeEnderecos = enderecos, carregandoLista = false, erroLista = false)
                    }
                }
        }
    }

    fun onRuaChange(valor: String) { _uiState.update { it.copy(rua = valor) } }
    fun onNumeroChange(valor: String) { _uiState.update { it.copy(numero = valor) } }
    fun onBairroChange(valor: String) { _uiState.update { it.copy(bairro = valor) } }
    fun onCidadeChange(valor: String) { _uiState.update { it.copy(cidade = valor) } }
    fun onComplementoChange(valor: String) { _uiState.update { it.copy(complemento = valor) } }

    fun limparCampos() {
        _uiState.update { it.copy(
            rua = "", numero = "", bairro = "", cidade = "", complemento = "", enderecoEmEdicao = null
        ) }
    }

    fun carregarDetalheEndereco(id: Int) {
        _uiState.update {
            it.copy(enderecoDetalhe = null, carregandoDetalhe = true, erroDetalhe = false)
        }
        viewModelScope.launch {
            try {
                val endereco = withContext(Dispatchers.IO) { repository.buscarPorId(id) }
                _uiState.update {
                    it.copy(
                        enderecoDetalhe = endereco,
                        carregandoDetalhe = false,
                        complemento = endereco?.complemento.orEmpty()
                    )
                }
            } catch (erro: Exception) {
                Log.e("EnderecoViewModel", "Erro ao buscar endereço", erro)
                _uiState.update {
                    it.copy(enderecoDetalhe = null, carregandoDetalhe = false, erroDetalhe = true)
                }
            }
        }
    }

    fun onDelete(endereco: Endereco, onResult: (Boolean) -> Unit = {}) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) { repository.deletar(endereco) }
                onResult(true)
            } catch (erro: Exception) {
                Log.e("EnderecoViewModel", "Erro ao excluir endereço", erro)
                onResult(false)
            }
        }
    }

    fun onSave(onResult: (Boolean) -> Unit = {}) {
        val state = _uiState.value
        if (state.rua.isBlank() || state.numero.isBlank() ||
            state.bairro.isBlank() || state.cidade.isBlank()) {
            onResult(false)
            return
        }
        val endereco = Endereco(
            rua = state.rua.trim(),
            numero = state.numero.trim(),
            bairro = state.bairro.trim(),
            cidade = state.cidade.trim(),
            complemento = state.complemento.trim()
        )
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) { repository.inserir(endereco) }
                limparCampos()
                onResult(true)
            } catch (erro: Exception) {
                Log.e("EnderecoViewModel", "Erro ao cadastrar endereço", erro)
                onResult(false)
            }
        }
    }

    fun onSaveComplemento(onResult: (Boolean) -> Unit = {}) {
        val state = _uiState.value
        val endereco = state.enderecoDetalhe ?: run {
            onResult(false)
            return
        }
        val atualizado = endereco.copy(complemento = state.complemento.trim())
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) { repository.atualizar(atualizado) }
                _uiState.update { it.copy(enderecoDetalhe = atualizado) }
                onResult(true)
            } catch (erro: Exception) {
                Log.e("EnderecoViewModel", "Erro ao atualizar endereço", erro)
                onResult(false)
            }
        }
    }
}
