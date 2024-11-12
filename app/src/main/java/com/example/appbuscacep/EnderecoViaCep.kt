package com.example.appbuscacep

data class EnderecoViaCep(
    val cep: String,
    val logradouro: String,
    val bairro: String,
    val localidade: String,  // Cidade
    val uf: String  // Estado
)
