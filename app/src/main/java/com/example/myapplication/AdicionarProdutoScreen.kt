
package com.example.myapplication

import android.util.Log
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun AdicionarProdutoScreen(
    onVoltar: () -> Unit
) {
    var nome by remember { mutableStateOf("") }
    var preco by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var salvando by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val produtoDao = remember(context) {
        AppDatabase.getDatabase(context).produtoDao()
    }

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
            value = nome,
            onValueChange = { nome = it },
            label = { Text("Nome do produto") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = preco,
            onValueChange = { preco = it },
            label = { Text("Preço") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            ),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = categoria,
            onValueChange = { categoria = it },
            label = { Text("Categoria") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                val valor = preco.replace(",", ".").toDoubleOrNull()

                if (
                    nome.isBlank() ||
                    categoria.isBlank() ||
                    valor == null ||
                    !valor.isFinite() ||
                    valor <= 0
                ) {
                    Toast.makeText(
                        context,
                        "Preencha os campos corretamente!",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {
                    salvando = true

                    val produto = Produto(
                        id = 0,
                        nome = nome.trim(),
                        preco = valor,
                        categoria = categoria.trim()
                    )

                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            produtoDao.inserir(produto)

                            val lista = produtoDao.buscarTodos()

                            withContext(Dispatchers.Main) {
                                DadosProdutos.produtos.clear()
                                DadosProdutos.produtos.addAll(lista)

                                salvando = false

                                Toast.makeText(
                                    context,
                                    "Produto cadastrado!",
                                    Toast.LENGTH_SHORT
                                ).show()

                                onVoltar()
                            }

                        } catch (e: Exception) {
                            Log.e(
                                "RoomProduto",
                                "Erro ao inserir produto",
                                e
                            )

                            withContext(Dispatchers.Main) {
                                salvando = false

                                Toast.makeText(
                                    context,
                                    "Erro ao cadastrar produto!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
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
