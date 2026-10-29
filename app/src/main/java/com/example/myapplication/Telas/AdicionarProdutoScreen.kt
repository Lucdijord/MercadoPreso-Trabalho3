package com.example.myapplication.Telas

import com.example.myapplication.ui.produtos.ProdutoViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AdicionarProdutoScreen(
    viewModel: ProdutoViewModel,
    onVoltar: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var salvando by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "Adicionar Produto",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        OutlinedTextField(
            value = uiState.nome,
            onValueChange = viewModel::onNomeChange,
            label = { Text("Nome do produto") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = uiState.preco,
            onValueChange = viewModel::onPrecoChange,
            label = { Text("Preço") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            ),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = uiState.categoria,
            onValueChange = viewModel::onCategoriaChange,
            label = { Text("Categoria") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                val valor = uiState.preco.replace(",", ".").toDoubleOrNull()
                if (uiState.nome.isBlank() || uiState.categoria.isBlank() || valor == null || !valor.isFinite() || valor <= 0) {
                    Toast.makeText(context, "Preencha os campos corretamente!", Toast.LENGTH_SHORT).show()
                } else {
                    salvando = true
                    viewModel.onSave { sucesso ->
                        salvando = false
                        Toast.makeText(context, if (sucesso) "Produto cadastrado!" else "Erro ao cadastrar produto!", Toast.LENGTH_SHORT).show()
                        if (sucesso) onVoltar()
                    }
                }
            },
            enabled = !salvando,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF9D2438),
                contentColor = Color.White
            )
        ) {
            if (salvando) {
                Text("Salvando...")
            } else {
                Text("Salvar Produto")
            }
        }
    }
}
