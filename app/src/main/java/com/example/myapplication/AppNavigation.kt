
package com.example.myapplication

import com.example.myapplication.Telas.AdicionarEnderecoScreen
import com.example.myapplication.Telas.AdicionarProdutoScreen
import com.example.myapplication.Telas.CarrinhoScreen
import com.example.myapplication.Telas.CatalogScreen
import com.example.myapplication.Telas.DetalheEnderecoScreen
import com.example.myapplication.Telas.DetalheProdutoScreen
import com.example.myapplication.Telas.EnderecosScreen
import com.example.myapplication.Telas.HomeScreen
import com.example.myapplication.Telas.ProfileScreen
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.compose.*
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {

    val navController = rememberNavController()
    val telaAtual by navController.currentBackStackEntryAsState()
    val rotaAtual = telaAtual?.destination?.route

    val tituloTela = when (rotaAtual) {
        Rotas.INICIO -> "Início"
        Rotas.CATALOGO -> "Catálogo"
        Rotas.CARRINHO -> "Carrinho"
        Rotas.PERFIL -> "Meu Perfil"
        Rotas.ADICIONAR_PRODUTO -> "Adicionar Produto"
        Rotas.DETALHE_PRODUTO -> "Detalhes do Produto"
        Rotas.ENDERECOS -> "Meus Endereços"
        Rotas.ADICIONAR_ENDERECO -> "Adicionar Endereço"
        Rotas.DETALHE_ENDERECO -> "Detalhes do Endereço"
        else -> "App MP"
    }

    val telasPrincipais = listOf(
        Rotas.INICIO,
        Rotas.CATALOGO,
        Rotas.CARRINHO,
        Rotas.PERFIL
    )

    val mostrarVoltar =
        rotaAtual != null && rotaAtual !in telasPrincipais

    var pesquisaCatalogo by remember {
        mutableStateOf("")
    }

    var categoriaCatalogo by remember {
        mutableStateOf("Todos")
    }

    fun abrirCatalogo(
        pesquisa: String = "",
        categoria: String = "Todos"
    ) {
        pesquisaCatalogo = pesquisa
        categoriaCatalogo = categoria

        navController.navigate(Rotas.CATALOGO) {
            launchSingleTop = true
        }
    }

    Scaffold(


        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Mercado Preso",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Text(
                            text = "Seu mercado, do seu jeito",
                            fontSize = 11.sp,
                            color = Color(0xFFFFDDE3)
                        )
                    }
                },

                navigationIcon = {
                    if (mostrarVoltar) {
                        IconButton(
                            onClick = {
                                navController.popBackStack()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Voltar",
                                tint = Color.White
                            )
                        }
                    }
                },

                actions = {
                    IconButton(
                        onClick = {
                            abrirCatalogo()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Pesquisar produtos",
                            tint = Color.White
                        )
                    }

                    IconButton(
                        onClick = {
                            if (rotaAtual != Rotas.CARRINHO) {
                                navController.navigate(Rotas.CARRINHO) {
                                    popUpTo(Rotas.INICIO) {
                                        inclusive = false
                                    }
                                    launchSingleTop = true
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Abrir carrinho",
                            tint = Color.White
                        )
                    }

                    IconButton(
                        onClick = {
                            if (rotaAtual != Rotas.PERFIL) {
                                navController.navigate(Rotas.PERFIL) {
                                    popUpTo(Rotas.INICIO) {
                                        inclusive = false
                                    }
                                    launchSingleTop = true
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Abrir perfil",
                            tint = Color.White
                        )
                    }
                },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF9D2438)
                )
            )
        },




        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFF9D2438),
                contentColor = Color.White
            ) {
                val abas = listOf(
                    Triple("Início", Rotas.INICIO, Icons.Default.Home),
                    Triple("Catálogo", Rotas.CATALOGO, Icons.Default.List),
                    Triple("Carrinho", Rotas.CARRINHO, Icons.Default.ShoppingCart),
                    Triple("Perfil", Rotas.PERFIL, Icons.Default.Person)
                )

                abas.forEach { (nome, rota, icone) ->
                    NavigationBarItem(
                        selected = rotaAtual == rota,

                        onClick = {
                            if (rotaAtual != rota) {

                                if (rota == Rotas.CATALOGO) {
                                    pesquisaCatalogo = ""
                                    categoriaCatalogo = "Todos"
                                }

                                navController.navigate(rota) {
                                    popUpTo(Rotas.INICIO) {
                                        inclusive = false
                                    }
                                    launchSingleTop = true
                                }
                            }
                        },

                        icon = {
                            Icon(
                                imageVector = icone,
                                contentDescription = nome
                            )
                        },

                        label = {
                            Text(nome)
                        },

                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            selectedTextColor = Color.White,
                            indicatorColor = Color(0xFFB94A60),
                            unselectedIconColor = Color(0xFFFFDDE3),
                            unselectedTextColor = Color(0xFFFFDDE3)
                        )
                    )
                }
            }
        },

        ) { padding ->

        NavHost(
            navController = navController,
            startDestination = Rotas.INICIO,
            modifier = Modifier.padding(padding)
        ) {

            composable(Rotas.INICIO) {
                HomeScreen(
                    onCatalogoClick = {
                        abrirCatalogo()
                    },

                    onPesquisarClick = { pesquisa ->
                        abrirCatalogo(pesquisa = pesquisa)
                    },

                    onCategoriaClick = { categoria ->
                        abrirCatalogo(categoria = categoria)
                    },

                    onProdutoClick = { id ->
                        navController.navigate(
                            Rotas.detalheProduto(id)
                        )
                    }
                )
            }

            composable(Rotas.CATALOGO) {
                CatalogScreen(
                    pesquisaInicial = pesquisaCatalogo,
                    categoriaInicial = categoriaCatalogo,

                    onAdicionarClick = {
                        navController.navigate(
                            Rotas.ADICIONAR_PRODUTO
                        )
                    },

                    onProdutoClick = { id ->
                        navController.navigate(
                            Rotas.detalheProduto(id)
                        )
                    }
                )
            }

            composable(Rotas.ADICIONAR_PRODUTO) {
                AdicionarProdutoScreen(
                    onVoltar = {
                        navController.popBackStack()
                    }
                )
            }

            composable(
                route = Rotas.DETALHE_PRODUTO,
                arguments = listOf(
                    navArgument("id") {
                        type = NavType.IntType
                    }
                )
            ) { backStackEntry ->

                val id = backStackEntry.arguments
                    ?.getInt("id") ?: -1

                DetalheProdutoScreen(
                    produtoId = id,

                    onVoltar = {
                        navController.popBackStack()
                    },

                    onAdicionarCarrinho = { produto, quantidade ->
                        DadosCarrinho.adicionar(
                            produto,
                            quantidade
                        )

                        navController.navigate(Rotas.CARRINHO) {
                            popUpTo(Rotas.INICIO) {
                                inclusive = false
                            }
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable(Rotas.CARRINHO) {
                CarrinhoScreen(
                    onCatalogoClick = {
                        abrirCatalogo()
                    },
                    onProdutoClick = { id ->
                        navController.navigate(
                            Rotas.detalheProduto(id)
                        )
                    }
                )
            }

            composable(Rotas.PERFIL) {
                ProfileScreen(
                    onEnderecosClick = {
                        navController.navigate(
                            Rotas.ENDERECOS
                        )
                    },

                    onCatalogoClick = {
                        abrirCatalogo()
                    }
                )
            }

            composable(Rotas.ENDERECOS) {
                EnderecosScreen(
                    onAdicionarClick = {
                        navController.navigate(
                            Rotas.ADICIONAR_ENDERECO
                        )
                    },

                    onEnderecoClick = { id ->
                        navController.navigate(
                            Rotas.detalheEndereco(id)
                        )
                    },

                    onVoltar = {
                        navController.popBackStack()
                    }
                )
            }

            composable(Rotas.ADICIONAR_ENDERECO) {
                AdicionarEnderecoScreen(
                    onVoltar = {
                        navController.popBackStack()
                    }
                )
            }

            composable(
                route = Rotas.DETALHE_ENDERECO,
                arguments = listOf(
                    navArgument("id") {
                        type = NavType.IntType
                    }
                )
            ) { backStackEntry ->

                val id = backStackEntry.arguments
                    ?.getInt("id") ?: -1

                DetalheEnderecoScreen(
                    enderecoId = id,

                    onVoltar = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}
