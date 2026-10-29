package com.example.myapplication.Telas

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.myapplication.ui.enderecos.EnderecoViewModel

@Composable
fun EnderecosScreen(
    viewModel: EnderecoViewModel,
    onAdicionarClick: () -> Unit,
    onEnderecoClick: (Int) -> Unit,
    onVoltar: () -> Unit
) {
    val vinho = Color(0xFF9D2438)
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var removendoId by remember { mutableStateOf<Int?>(null) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Endereços cadastrados", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onAdicionarClick,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = vinho, contentColor = Color.White)
        ) { Text("Adicionar Endereço") }
        Spacer(modifier = Modifier.height(16.dp))

        if (uiState.carregandoLista) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = vinho)
            }
        } else if (uiState.erroLista) {
            Text("Erro ao carregar endereços.", color = Color.Gray)
        } else if (uiState.listaDeEnderecos.isEmpty()) {
            Text("Nenhum endereço cadastrado.", color = Color.Gray)
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(items = uiState.listaDeEnderecos, key = { it.id }) { endereco ->
                    Card(
                        onClick = { onEnderecoClick(endereco.id) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(endereco.rua, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Nº ${endereco.numero}")
                                Text(endereco.bairro)
                                Text(endereco.cidade, color = Color.Gray)
                            }
                            IconButton(
                                enabled = removendoId == null,
                                onClick = {
                                    removendoId = endereco.id
                                    viewModel.onDelete(endereco) { sucesso ->
                                        removendoId = null
                                        Toast.makeText(
                                            context,
                                            if (sucesso) "Endereço removido!" else "Erro ao remover endereço!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = "Remover endereço", tint = vinho)
                            }
                        }
                    }
                }
            }
        }
    }
}
