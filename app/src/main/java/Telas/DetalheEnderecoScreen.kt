package Telas

import Banco.AppDatabase
import Banco.DadosEnderecos
import Banco.Endereco
import android.util.Log
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun DetalheEnderecoScreen(
    enderecoId: Int,
    onVoltar: () -> Unit
) {
    val context = LocalContext.current
    val vinho = Color(0xFF9D2438)

    val enderecoDao = remember(context) {
        AppDatabase.getDatabase(context).enderecoDao()
    }

    var endereco by remember(enderecoId) {
        mutableStateOf<Endereco?>(null)
    }

    var complemento by remember(enderecoId) {
        mutableStateOf("")
    }

    var carregando by remember(enderecoId) {
        mutableStateOf(true)
    }

    var salvando by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(enderecoId) {
        carregando = true

        try {
            val resultado = withContext(Dispatchers.IO) {
                enderecoDao.buscarPorId(enderecoId)
            }

            endereco = resultado

            if (resultado != null) {
                complemento = resultado.complemento
            }

        } catch (e: Exception) {
            Log.e(
                "RoomEndereco",
                "Erro ao buscar endereço",
                e
            )

            Toast.makeText(
                context,
                "Erro ao carregar endereço!",
                Toast.LENGTH_SHORT
            ).show()

        } finally {
            carregando = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        if (carregando) {

            CircularProgressIndicator(
                color = vinho
            )

        } else if (endereco == null) {

            Text("Endereço não encontrado.")

            Button(
                onClick = onVoltar,
                colors = ButtonDefaults.buttonColors(
                    containerColor = vinho
                )
            ) {
                Text("Voltar")
            }

        } else {

            val enderecoAtual = endereco!!

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 2.dp
                )
            ) {

                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    Text(
                        text = enderecoAtual.rua,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )

                    HorizontalDivider()

                    Text(
                        text = "Número: ${enderecoAtual.numero}"
                    )

                    Text(
                        text = "Bairro: ${enderecoAtual.bairro}"
                    )

                    Text(
                        text = "Cidade: ${enderecoAtual.cidade}"
                    )

                    Text(
                        text = "Complemento: " +
                                enderecoAtual.complemento.ifBlank {
                                    "Não informado"
                                }
                    )
                }
            }

            Text(
                text = "Editar Complemento",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
                value = complemento,
                onValueChange = {
                    complemento = it
                },
                label = {
                    Text("Complemento do endereço")
                },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2,
                enabled = !salvando
            )

            Button(
                onClick = {

                    salvando = true

                    val enderecoAtualizado = enderecoAtual.copy(
                        complemento = complemento.trim()
                    )

                    CoroutineScope(Dispatchers.IO).launch {

                        try {
                            enderecoDao.atualizar(
                                enderecoAtualizado
                            )

                            withContext(Dispatchers.Main) {

                                // Atualiza a lista auxiliar
                                val indice =
                                    DadosEnderecos.enderecos.indexOfFirst {
                                        it.id == enderecoId
                                    }

                                if (indice >= 0) {
                                    DadosEnderecos.enderecos[indice] =
                                        enderecoAtualizado
                                }

                                endereco = enderecoAtualizado
                                salvando = false

                                Toast.makeText(
                                    context,
                                    "Endereço atualizado!",
                                    Toast.LENGTH_SHORT
                                ).show()

                                onVoltar()
                            }

                        } catch (e: Exception) {

                            Log.e(
                                "RoomEndereco",
                                "Erro ao atualizar endereço",
                                e
                            )

                            withContext(Dispatchers.Main) {

                                salvando = false

                                Toast.makeText(
                                    context,
                                    "Erro ao atualizar endereço!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                },
                enabled = !salvando,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = vinho,
                    contentColor = Color.White
                )
            ) {
                Text(
                    if (salvando) {
                        "Salvando..."
                    } else {
                        "Salvar Alteração"
                    }
                )
            }
        }
    }
}
