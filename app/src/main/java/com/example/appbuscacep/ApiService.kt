package com.example.appbuscacep

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

// Define a interface para a requisição à API ViaCEP
interface ApiService {
    @GET("{cep}/json/")
    fun buscarEndereco(@Path("cep") cep: String): Call<EnderecoViaCep>
}
