
package com.example.myapplication

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale
import com.example.myapplication.R

private val VinhoHome = Color(0xFF9D2438)
private val FundoHome = Color(0xFFFFF8F5)

@Composable
fun HomeScreen(
    onCatalogoClick: () -> Unit,
    onPesquisarClick: (String) -> Unit,
    onCategoriaClick: (String) -> Unit,
    onProdutoClick: (Int) -> Unit
) {
    var pesquisa by remember {
        mutableStateOf("")
    }

    var produtos by remember {
        mutableStateOf<List<Produto>>(emptyList())
    }

    var carregando by remember {
        mutableStateOf(true)
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
                "Erro ao carregar produtos da Home",
                e
            )
        } finally {
            carregando = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FundoHome)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 22.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.logo_mp),
                contentDescription = "Logo Mercado Preso",
                modifier = Modifier.size(58.dp),
                contentScale = ContentScale.Fit
            )

            Column {
                Text(
                    text = "MERCADO PRESO",
                    color = VinhoHome,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )

                Text(
                    text = "Olá! O que vamos encontrar?",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF222222)
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        OutlinedTextField(
            value = pesquisa,
            onValueChange = {
                pesquisa = it
            },
            placeholder = {
                Text("Buscar no catálogo")
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null
                )
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        onPesquisarClick(pesquisa.trim())
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Pesquisar",
                        tint = VinhoHome
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = androidx.compose.foundation.shape.RoundedCornerShape(15.dp),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onPesquisarClick(pesquisa.trim())
                }
            )
        )

        Spacer(Modifier.height(22.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(
                containerColor = VinhoHome
            )
        ) {
            Column(
                modifier = Modifier.padding(22.dp)
            ) {
                Text(
                    text = "UM MUNDO DE ESCOLHAS",
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )

                Spacer(Modifier.height(10.dp))

                Text(
                    text = "Boas ofertas,\nsem complicação.",
                    color = Color.White,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 32.sp
                )

                Spacer(Modifier.height(12.dp))

                Text(
                    text = "Explore produtos e encontre algo para você.",
                    color = Color(0xFFFFE2E7),
                    fontSize = 14.sp,
                    lineHeight = 21.sp
                )

                Spacer(Modifier.height(18.dp))

                Button(
                    onClick = onCatalogoClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = VinhoHome
                    )
                ) {
                    Text("Explorar catálogo")

                    Spacer(Modifier.width(8.dp))

                    Icon(
                        imageVector = Icons.Default.ShoppingBag,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        Spacer(Modifier.height(26.dp))

        Text(
            text = "Explore por categoria",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF222222)
        )

        Spacer(Modifier.height(14.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CategoriaHome(
                nome = "Eletrônicos",
                simbolo = "📱",
                modifier = Modifier.weight(1f)
            ) {
                onCategoriaClick("Eletrônicos")
            }

            CategoriaHome(
                nome = "Casa",
                simbolo = "🏠",
                modifier = Modifier.weight(1f)
            ) {
                onCategoriaClick("Casa")
            }

            CategoriaHome(
                nome = "Beleza",
                simbolo = "✨",
                modifier = Modifier.weight(1f)
            ) {
                onCategoriaClick("Beleza")
            }
        }

        Spacer(Modifier.height(26.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Em destaque",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            TextButton(
                onClick = onCatalogoClick
            ) {
                Text(
                    text = "Ver todos",
                    color = VinhoHome
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        if (carregando) {
            CircularProgressIndicator(
                color = VinhoHome
            )
        } else {
            val destaques = produtos.take(3)

            if (destaques.isEmpty()) {
                Text("Nenhum produto disponível.")
            } else {
                destaques.forEach { produto ->
                    Card(
                        onClick = {
                            onProdutoClick(produto.id)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(17.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 2.dp
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(58.dp)
                                    .background(
                                        Color(0xFFF9E8EB),
                                        androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ShoppingBag,
                                    contentDescription = null,
                                    tint = VinhoHome,
                                    modifier = Modifier.size(28.dp)
                                )
                            }

                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = produto.nome,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )

                                Text(
                                    text = produto.categoria,
                                    color = Color.Gray,
                                    fontSize = 12.sp
                                )

                                Text(
                                    text = String.format(
                                        Locale.forLanguageTag("pt-BR"),
                                        "R$ %.2f",
                                        produto.preco
                                    ),
                                    color = VinhoHome,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = null,
                                tint = VinhoHome
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoriaHome(
    nome: String,
    simbolo: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = modifier.height(100.dp),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(17.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = simbolo,
                fontSize = 27.sp
            )

            Spacer(Modifier.height(9.dp))

            Text(
                text = nome,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
