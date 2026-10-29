package com.example.myapplication.Telas

import com.example.myapplication.data.local.Produto
import com.example.myapplication.ui.produtos.ProdutoViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale

@Composable
fun DetalheProdutoScreen(
    produtoId: Int,
    viewModel: ProdutoViewModel,
    onVoltar: () -> Unit,
    onAdicionarCarrinho: (Produto, Int) -> Unit
) {
    val vinho = Color(0xFF9D2438)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val produto = uiState.produtoDetalhe
    val carregando = uiState.carregandoDetalhe
    var quantidade by remember(produtoId) { mutableStateOf(1) }

    LaunchedEffect(produtoId) {
        viewModel.carregarDetalheProduto(produtoId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        if (carregando) {

            CircularProgressIndicator(
                color = vinho
            )

        } else if (produto == null) {

            Text(if (uiState.erroDetalhe) "Erro ao carregar produto." else "Produto não encontrado.")

            Button(
                onClick = onVoltar,
                colors = ButtonDefaults.buttonColors(
                    containerColor = vinho
                )
            ) {
                Text("Voltar")
            }

        } else {

            val produtoAtual = produto!!
            val total = produtoAtual.preco * quantidade

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
                        text = produtoAtual.nome,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )

                    HorizontalDivider()

                    Text(
                        text = "Categoria: ${produtoAtual.categoria}",
                        color = Color.Gray
                    )

                    Text(
                        text = String.format(
                            Locale.forLanguageTag("pt-BR"),
                            "R$ %.2f",
                            produtoAtual.preco
                        ),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = vinho
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            onAdicionarCarrinho(
                                produtoAtual,
                                quantidade
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = vinho,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Adicionar ao Carrinho")
                    }
                }
            }

            Text(
                text = "Quantidade",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                OutlinedButton(
                    onClick = {
                        if (quantidade > 1) {
                            quantidade--
                        }
                    },
                    enabled = quantidade > 1,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = vinho
                    )
                ) {
                    Text("-")
                }

                Text(
                    text = quantidade.toString(),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                OutlinedButton(
                    onClick = {
                        quantidade++
                    },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = vinho
                    )
                ) {
                    Text("+")
                }
            }

            HorizontalDivider()

            Text(
                text = "Valor total",
                fontSize = 18.sp
            )

            Text(
                text = String.format(
                    Locale.forLanguageTag("pt-BR"),
                    "R$ %.2f",
                    total
                ),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = vinho
            )
        }
    }
}
