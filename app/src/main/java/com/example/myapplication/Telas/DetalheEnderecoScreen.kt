package com.example.myapplication.Telas

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.myapplication.ui.enderecos.EnderecoViewModel

@Composable
fun DetalheEnderecoScreen(
    enderecoId: Int,
    viewModel: EnderecoViewModel,
    onVoltar: () -> Unit
) {
    val vinho = Color(0xFF9D2438)
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var salvando by remember { mutableStateOf(false) }

    LaunchedEffect(enderecoId) { viewModel.carregarDetalheEndereco(enderecoId) }

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        when {
            uiState.carregandoDetalhe -> CircularProgressIndicator(color = vinho)
            uiState.enderecoDetalhe == null -> {
                Text(if (uiState.erroDetalhe) "Erro ao carregar endereço." else "Endereço não encontrado.")
                Button(
                    onClick = onVoltar,
                    colors = ButtonDefaults.buttonColors(containerColor = vinho)
                ) { Text("Voltar") }
            }
            else -> {
                val endereco = uiState.enderecoDetalhe!!
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(endereco.rua, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        HorizontalDivider()
                        Text("Número: ${endereco.numero}")
                        Text("Bairro: ${endereco.bairro}")
                        Text("Cidade: ${endereco.cidade}")
                        Text("Complemento: " + endereco.complemento.ifBlank { "Não informado" })
                    }
                }

                Text("Editar Complemento", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                OutlinedTextField(
                    value = uiState.complemento,
                    onValueChange = viewModel::onComplementoChange,
                    label = { Text("Complemento do endereço") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    enabled = !salvando
                )
                Button(
                    onClick = {
                        salvando = true
                        viewModel.onSaveComplemento { sucesso ->
                            salvando = false
                            Toast.makeText(
                                context,
                                if (sucesso) "Endereço atualizado!" else "Erro ao atualizar endereço!",
                                Toast.LENGTH_SHORT
                            ).show()
                            if (sucesso) onVoltar()
                        }
                    },
                    enabled = !salvando,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = vinho, contentColor = Color.White)
                ) { Text(if (salvando) "Salvando..." else "Salvar Alteração") }
            }
        }
    }
}
