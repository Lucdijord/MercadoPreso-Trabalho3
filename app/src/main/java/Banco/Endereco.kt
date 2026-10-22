package Banco

import androidx.compose.runtime.mutableStateListOf
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "enderecos")
data class Endereco(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val rua: String,
    val numero: String,
    val bairro: String,
    val cidade: String,
    val complemento: String
)

object DadosEnderecos {

    val enderecos = mutableStateListOf(
        Endereco(
            1,
            "Rua das Flores",
            "123",
            "Centro",
            "Curitiba",
            "Apartamento 10"
        ),
        Endereco(
            2,
            "Avenida Brasil",
            "500",
            "Batel",
            "Curitiba",
            ""
        )
    )

    private var proximoId = 3

    fun adicionar(
        rua: String,
        numero: String,
        bairro: String,
        cidade: String,
        complemento: String
    ) {
        enderecos.add(
            Endereco(
                proximoId,
                rua,
                numero,
                bairro,
                cidade,
                complemento
            )
        )

        proximoId++
    }

    fun remover(endereco: Endereco) {
        enderecos.remove(endereco)
    }


    fun atualizarComplemento(id: Int, novoComplemento: String) {

        val indice = enderecos.indexOfFirst {
            it.id == id
        }

        if (indice >= 0) {
            enderecos[indice] = enderecos[indice].copy(
                complemento = novoComplemento
            )
        }
    }

}
