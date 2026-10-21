
package com.example.myapplication

import android.util.Log
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun EnderecosScreen(
    onAdicionarClick: () -> Unit,
    onEnderecoClick: (Int) -> Unit,
    onVoltar: () -> Unit
) {
    val vinho = Color(0xFF9D2438)
    val context = LocalContext.current

    val enderecoDao = remember(context) {
        AppDatabase.getDatabase(context).enderecoDao()
    }

    // Lista auxiliar sincronizada com o Room
    val enderecos = DadosEnderecos.enderecos

    var carregando by remember {
        mutableStateOf(true)
    }

    var removendoId by remember {
        mutableStateOf<Int?>(null)
    }

    LaunchedEffect(Unit) {
        carregando = true

        try {
            val lista = withContext(Dispatchers.IO) {
                enderecoDao.buscarTodos()
            }

            enderecos.clear()
            enderecos.addAll(lista)

        } catch (e: Exception) {
            Log.e(
                "RoomEndereco",
                "Erro ao buscar endereços",
                e
            )

            Toast.makeText(
                context,
                "Erro ao carregar endereços!",
                Toast.LENGTH_SHORT
            ).show()

        } finally {
            carregando = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Endereços cadastrados",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onAdicionarClick,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = vinho,
                contentColor = Color.White
            )
        ) {
            Text("Adicionar Endereço")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (carregando) {

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = vinho
                )
            }

        } else if (enderecos.isEmpty()) {

            Text(
                text = "Nenhum endereço cadastrado.",
                color = Color.Gray
            )

        } else {

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {

                items(
                    items = enderecos,
                    key = { it.id }
                ) { endereco ->

                    Card(
                        onClick = {
                            onEnderecoClick(endereco.id)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 2.dp
                        )
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Column(
                                modifier = Modifier.weight(1f)
                            ) {

                                Text(
                                    text = endereco.rua,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )

                                Spacer(
                                    modifier = Modifier.height(4.dp)
                                )

                                Text(
                                    text = "Nº ${endereco.numero}"
                                )

                                Text(
                                    text = endereco.bairro
                                )

                                Text(
                                    text = endereco.cidade,
                                    color = Color.Gray
                                )
                            }

                            IconButton(
                                enabled = removendoId == null,
                                onClick = {
                                    removendoId = endereco.id

                                    CoroutineScope(Dispatchers.IO).launch {

                                        try {
                                            enderecoDao.deletar(endereco)

                                            val lista = enderecoDao.buscarTodos()

                                            withContext(Dispatchers.Main) {

                                                enderecos.clear()
                                                enderecos.addAll(lista)

                                                removendoId = null

                                                Toast.makeText(
                                                    context,
                                                    "Endereço removido!",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }

                                        } catch (e: Exception) {

                                            Log.e(
                                                "RoomEndereco",
                                                "Erro ao remover endereço",
                                                e
                                            )

                                            withContext(Dispatchers.Main) {

                                                removendoId = null

                                                Toast.makeText(
                                                    context,
                                                    "Erro ao remover endereço!",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                    }
                                }
                            ) {

                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Remover endereço",
                                    tint = vinho
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
