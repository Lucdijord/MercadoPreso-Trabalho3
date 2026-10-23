# Mercado Preso - Trabalho 3

Aplicativo Android desenvolvido em Kotlin com Jetpack Compose. Nesta etapa do trabalho foi utilizado o Room para salvar os produtos e endereços no banco de dados local.

## Requisitos

- Android Studio
- Emulador Android ou celular com Android 7.0 ou superior


## Dependências

O projeto utiliza as seguintes tecnologias:

- Kotlin 2.2.10
- Jetpack Compose
- Navigation Compose
- Room 2.8.5
- SQLite
- Coroutines
- KSP 2.3.12

As dependências já estão configuradas nos arquivos Gradle. Não é necessário instalar o Room ou o SQLite separadamente.

## Como executar

1. Clone o repositório:


  git clone https://github.com/Lucdijord/MercadoPreso-Trabalho3


1. Abra a pasta do projeto no Android Studio.
2. Aguarde a sincronização do Gradle.
3. Clique em Run para executar o aplicativo.

## Banco de dados

O aplicativo utiliza Room com SQLite para armazenar produtos e endereços.

O banco é criado automaticamente quando o aplicativo é executado pela primeira vez. Não é necessário configurar nenhum banco de dados manualmente.

Os produtos e endereços continuam salvos mesmo depois de fechar o aplicativo.

O carrinho funciona apenas durante a execução do aplicativo, sem persistência dos itens.

## Integrantes

- Luciano Diniz Javorski
- Matheus Alves de Lima
- Marianne Cristina Bueno
