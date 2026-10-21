
package com.example.myapplication

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import java.util.Locale

private val VinhoCatalogo = Color(0xFF9D2438)
private val FundoCatalogo = Color(0xFFFFF8F5)

@Composable
fun CatalogScreen(
    onAdicionarClick: () -> Unit,
    onProdutoClick: (Int) -> Unit,
    pesquisaInicial: String = "",
    categoriaInicial: String = "Todos"
) {
    var pesquisa by remember(pesquisaInicial) {
        mutableStateOf(pesquisaInicial)
    }

    var categoriaSelecionada by remember(categoriaInicial) {
        mutableStateOf(categoriaInicial)
    }

    var ordenarPorNome by remember {
        mutableStateOf(true)
    }

    // Lista carregada do banco
    var produtos by remember {
        mutableStateOf<List<Produto>>(emptyList())
    }

    var carregando by remember {
        mutableStateOf(true)
    }

    var excluindoId by remember {
        mutableStateOf<Int?>(null)
    }

    val context = LocalContext.current
    val produtoDao = remember(context) {
        AppDatabase.getDatabase(context).produtoDao()
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
                "Erro ao buscar produtos",
                e
            )

            Toast.makeText(
                context,
                "Erro ao carregar produtos!",
                Toast.LENGTH_SHORT
            ).show()

        } finally {
            carregando = false
        }
    }

    val categorias = (
            listOf("Todos", "Eletrônicos", "Casa", "Beleza") +
                    produtos.map { it.categoria }
            ).distinct()

    val produtosFiltrados = produtos
        .filter { produto ->

            val correspondePesquisa =
                produto.nome.contains(
                    pesquisa,
                    ignoreCase = true
                ) ||
                        produto.categoria.contains(
                            pesquisa,
                            ignoreCase = true
                        )

            val correspondeCategoria =
                categoriaSelecionada == "Todos" ||
                        produto.categoria.equals(
                            categoriaSelecionada,
                            ignoreCase = true
                        )

            correspondePesquisa && correspondeCategoria
        }
        .let { lista ->
            if (ordenarPorNome) {
                lista.sortedBy { it.nome.lowercase() }
            } else {
                lista.sortedBy { it.preco }
            }
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FundoCatalogo)
            .padding(horizontal = 20.dp, vertical = 18.dp)
    ) {

        Text(
            text = "Catálogo",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Encontre o que você procura",
            fontSize = 14.sp,
            color = Color.DarkGray
        )

        Spacer(Modifier.height(18.dp))

        OutlinedTextField(
            value = pesquisa,
            onValueChange = { pesquisa = it },
            placeholder = {
                Text("Buscar por nome ou categoria")
            },
            leadingIcon = {
                Icon(
                    Icons.Default.Search,
                    contentDescription = null
                )
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(15.dp),
            singleLine = true
        )

        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categorias.forEach { categoria ->
                FilterChip(
                    selected = categoriaSelecionada == categoria,
                    onClick = {
                        categoriaSelecionada = categoria
                    },
                    label = {
                        Text(categoria)
                    }
                )
            }
        }

        Spacer(Modifier.height(14.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "${produtosFiltrados.size} produto(s)",
                fontSize = 14.sp,
                color = Color.DarkGray
            )

            TextButton(
                onClick = {
                    ordenarPorNome = !ordenarPorNome
                }
            ) {
                Icon(
                    Icons.Default.Sort,
                    contentDescription = null,
                    tint = VinhoCatalogo
                )

                Spacer(Modifier.width(5.dp))

                Text(
                    text = if (ordenarPorNome) "Nome" else "Preço",
                    color = VinhoCatalogo
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = onAdicionarClick,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = VinhoCatalogo
            )
        ) {
            Icon(
                Icons.Default.Add,
                contentDescription = null
            )

            Spacer(Modifier.width(8.dp))

            Text("Adicionar produto")
        }

        Spacer(Modifier.height(14.dp))

        if (carregando) {

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = VinhoCatalogo
                )
            }

        } else if (produtosFiltrados.isEmpty()) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 35.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Nenhum produto encontrado.",
                    color = Color.Gray
                )

                TextButton(
                    onClick = {
                        pesquisa = ""
                        categoriaSelecionada = "Todos"
                    }
                ) {
                    Text("Limpar filtros")
                }
            }

        } else {

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {

                items(
                    items = produtosFiltrados,
                    key = { it.id }
                ) { produto ->

                    Card(
                        onClick = {
                            onProdutoClick(produto.id)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(17.dp),
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
                                .padding(15.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {

                            Box(
                                modifier = Modifier
                                    .size(55.dp)
                                    .background(
                                        Color(0xFFF9E8EB),
                                        RoundedCornerShape(12.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.ShoppingBag,
                                    contentDescription = null,
                                    tint = VinhoCatalogo,
                                    modifier = Modifier.size(27.dp)
                                )
                            }

                            Column(
                                modifier = Modifier.weight(1f)
                            ) {

                                Text(
                                    text = produto.nome,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )

                                Text(
                                    text = produto.categoria,
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )

                                Spacer(Modifier.height(4.dp))

                                Text(
                                    text = String.format(
                                        Locale.forLanguageTag("pt-BR"),
                                        "R$ %.2f",
                                        produto.preco
                                    ),
                                    color = VinhoCatalogo,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                            }

                            IconButton(
                                enabled = excluindoId == null,
                                onClick = {
                                    excluindoId = produto.id

                                    CoroutineScope(Dispatchers.IO).launch {
                                        try {
                                            produtoDao.deletar(produto)

                                            val lista = produtoDao.buscarTodos()

                                            withContext(Dispatchers.Main) {
                                                produtos = lista

                                                DadosProdutos.produtos.clear()
                                                DadosProdutos.produtos.addAll(lista)

                                                DadosCarrinho.remover(produto.id)

                                                excluindoId = null

                                                Toast.makeText(
                                                    context,
                                                    "Produto removido!",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }

                                        } catch (e: Exception) {
                                            Log.e(
                                                "RoomProduto",
                                                "Erro ao excluir produto",
                                                e
                                            )

                                            withContext(Dispatchers.Main) {
                                                excluindoId = null

                                                Toast.makeText(
                                                    context,
                                                    "Erro ao excluir produto!",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                    }
                                }
                            ) {

                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Excluir produto",
                                    tint = VinhoCatalogo
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
