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
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun AdicionarEnderecoScreen(
    onVoltar: () -> Unit
) {
    var rua by remember { mutableStateOf("") }
    var numero by remember { mutableStateOf("") }
    var bairro by remember { mutableStateOf("") }
    var cidade by remember { mutableStateOf("") }
    var complemento by remember { mutableStateOf("") }

    var salvando by remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current

    val enderecoDao = remember(context) {
        AppDatabase.getDatabase(context).enderecoDao()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Text(
            text = "Adicionar Endereço",
            style = MaterialTheme.typography.headlineSmall
        )

        OutlinedTextField(
            value = rua,
            onValueChange = { rua = it },
            label = { Text("Rua") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = numero,
            onValueChange = { numero = it },
            label = { Text("Número") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = bairro,
            onValueChange = { bairro = it },
            label = { Text("Bairro") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = cidade,
            onValueChange = { cidade = it },
            label = { Text("Cidade") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = complemento,
            onValueChange = { complemento = it },
            label = { Text("Complemento (opcional)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {

                if (
                    rua.isBlank() ||
                    numero.isBlank() ||
                    bairro.isBlank() ||
                    cidade.isBlank()
                ) {
                    Toast.makeText(
                        context,
                        "Preencha os campos obrigatórios!",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {

                    salvando = true

                    val endereco = Endereco(
                        id = 0,
                        rua = rua.trim(),
                        numero = numero.trim(),
                        bairro = bairro.trim(),
                        cidade = cidade.trim(),
                        complemento = complemento.trim()
                    )

                    CoroutineScope(Dispatchers.IO).launch {

                        try {
                            enderecoDao.inserir(endereco)

                            val lista = enderecoDao.buscarTodos()

                            withContext(Dispatchers.Main) {

                                DadosEnderecos.enderecos.clear()
                                DadosEnderecos.enderecos.addAll(lista)

                                salvando = false

                                Toast.makeText(
                                    context,
                                    "Endereço cadastrado!",
                                    Toast.LENGTH_SHORT
                                ).show()

                                onVoltar()
                            }

                        } catch (e: Exception) {

                            Log.e(
                                "RoomEndereco",
                                "Erro ao cadastrar endereço",
                                e
                            )

                            withContext(Dispatchers.Main) {

                                salvando = false

                                Toast.makeText(
                                    context,
                                    "Erro ao cadastrar endereço!",
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
                Text("Salvar Endereço")
            }
        }
    }
}
