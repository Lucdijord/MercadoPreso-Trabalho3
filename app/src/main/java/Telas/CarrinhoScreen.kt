package Telas

import Banco.AppDatabase
import Banco.DadosProdutos
import Banco.Produto
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.DadosCarrinho
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale

@Composable
fun CarrinhoScreen(
    onCatalogoClick: () -> Unit,
    onProdutoClick: (Int) -> Unit
) {
    val itens = DadosCarrinho.itens
    val formatoBrasil = Locale.forLanguageTag("pt-BR")
    val vinho = Color(0xFF9D2438)

    val context = LocalContext.current

    val produtoDao = remember(context) {
        AppDatabase.getDatabase(context).produtoDao()
    }

    var produtos by remember {
        mutableStateOf<List<Produto>>(emptyList())
    }

    var carregando by remember {
        mutableStateOf(true)
    }

    LaunchedEffect(Unit) {
        carregando = true

        try {
            val lista = withContext(Dispatchers.IO) {
                produtoDao.buscarTodos()
            }

            produtos = lista

            DadosProdutos.produtos.clear()
            DadosProdutos.produtos.addAll(lista)

        } catch (e: Exception) {
            Log.e(
                "RoomProduto",
                "Erro ao carregar recomendações",
                e
            )
        } finally {
            carregando = false
        }
    }

    val produtosRecomendados = produtos.filter { produto ->
        itens.none { item ->
            item.produto.id == produto.id
        }
    }

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        val alturaDisponivel = maxHeight

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = "Meu Carrinho",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            if (itens.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(
                                min = alturaDisponivel * 0.38f
                            ),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = null,
                            modifier = Modifier.size(55.dp),
                            tint = Color.Gray
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text("Seu carrinho está vazio!")

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = onCatalogoClick,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = vinho
                            )
                        ) {
                            Text("Ver produtos")
                        }
                    }
                }
            } else {
                items(
                    items = itens,
                    key = { it.produto.id }
                ) { item ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFF5F5F5)
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
                                    text = item.produto.nome,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = "Quantidade: ${item.quantidade}"
                                )

                                Text(
                                    text = String.format(
                                        formatoBrasil,
                                        "R$ %.2f",
                                        item.produto.preco * item.quantidade
                                    ),
                                    color = vinho,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            IconButton(
                                onClick = {
                                    DadosCarrinho.remover(
                                        item.produto.id
                                    )
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Remover do carrinho",
                                    tint = vinho
                                )
                            }
                        }
                    }
                }

                item {
                    HorizontalDivider()

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Total:",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = String.format(
                                formatoBrasil,
                                "R$ %.2f",
                                DadosCarrinho.calcularTotal()
                            ),
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = vinho
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = onCatalogoClick,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = vinho
                        )
                    ) {
                        Text("Continuar comprando")
                    }
                }
            }

            if (carregando) {
                item {
                    CircularProgressIndicator(
                        color = vinho
                    )
                }
            } else if (produtosRecomendados.isNotEmpty()) {

                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider()
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Você também pode gostar",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )

                        TextButton(
                            onClick = onCatalogoClick
                        ) {
                            Text("Ver mais")
                        }
                    }
                }

                items(
                    items = produtosRecomendados.chunked(2)
                ) { linha ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        linha.forEach { produto ->
                            Card(
                                onClick = {
                                    onProdutoClick(produto.id)
                                },
                                modifier = Modifier.weight(1f),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.White
                                ),
                                elevation = CardDefaults.cardElevation(
                                    defaultElevation = 2.dp
                                )
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(85.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.ShoppingBag,
                                            contentDescription = null,
                                            modifier = Modifier.size(50.dp),
                                            tint = Color(0xFFB94A60)
                                        )
                                    }

                                    Text(
                                        text = produto.nome,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 2
                                    )

                                    Text(
                                        text = String.format(
                                            formatoBrasil,
                                            "R$ %.2f",
                                            produto.preco
                                        ),
                                        color = vinho,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Text(
                                        text = "Ver produto →",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = vinho
                                    )
                                }
                            }
                        }

                        if (linha.size == 1) {
                            Spacer(
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }
}
