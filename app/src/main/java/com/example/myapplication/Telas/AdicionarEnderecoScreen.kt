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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.myapplication.ui.enderecos.EnderecoViewModel

@Composable
fun AdicionarEnderecoScreen(
    viewModel: EnderecoViewModel,
    onVoltar: () -> Unit
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var salvando by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { viewModel.limparCampos() }

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Adicionar Endereço", style = MaterialTheme.typography.headlineSmall)
        OutlinedTextField(
            value = uiState.rua, onValueChange = viewModel::onRuaChange,
            label = { Text("Rua") }, modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = uiState.numero, onValueChange = viewModel::onNumeroChange,
            label = { Text("Número") }, modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = uiState.bairro, onValueChange = viewModel::onBairroChange,
            label = { Text("Bairro") }, modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = uiState.cidade, onValueChange = viewModel::onCidadeChange,
            label = { Text("Cidade") }, modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = uiState.complemento, onValueChange = viewModel::onComplementoChange,
            label = { Text("Complemento (opcional)") }, modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                if (uiState.rua.isBlank() || uiState.numero.isBlank() ||
                    uiState.bairro.isBlank() || uiState.cidade.isBlank()) {
                    Toast.makeText(context, "Preencha os campos obrigatórios!", Toast.LENGTH_SHORT).show()
                } else {
                    salvando = true
                    viewModel.onSave { sucesso ->
                        salvando = false
                        Toast.makeText(
                            context,
                            if (sucesso) "Endereço cadastrado!" else "Erro ao cadastrar endereço!",
                            Toast.LENGTH_SHORT
                        ).show()
                        if (sucesso) onVoltar()
                    }
                }
            },
            enabled = !salvando,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF9D2438), contentColor = Color.White
            )
        ) { Text(if (salvando) "Salvando..." else "Salvar Endereço") }
    }
}
