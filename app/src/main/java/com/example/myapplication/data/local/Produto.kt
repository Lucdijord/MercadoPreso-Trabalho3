package com.example.myapplication.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "produtos")
data class Produto (
    @PrimaryKey(autoGenerate = true)

    val id: Int = 0,
    val nome: String,
    val preco: Double,
    val categoria: String
)

