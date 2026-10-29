package com.example.myapplication.data.local

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

