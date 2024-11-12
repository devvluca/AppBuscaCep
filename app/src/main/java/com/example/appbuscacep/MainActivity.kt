package com.example.appbuscacep

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.example.appbuscacep.ui.theme.AppBuscaCepTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppBuscaCepTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    CepScreen()
                }
            }
        }
    }
}

@Composable
fun CepScreen() {
    val context = LocalContext.current
    val dbHelper = DatabaseHelper(context)
    var cepInput by remember { mutableStateOf(TextFieldValue("")) }
    var message by remember { mutableStateOf("") }
    var enderecos by remember { mutableStateOf(listOf<Endereco>()) }

    LaunchedEffect(true) {
        enderecos = dbHelper.obterEnderecos()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = cepInput,
            onValueChange = { cepInput = it },
            label = { Text("Digite o CEP") }
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            val cep = cepInput.text
            if (cep.isNotBlank()) {
                val call = RetrofitClient.apiService.buscarEndereco(cep)
                call.enqueue(object : Callback<EnderecoViaCep> {
                    override fun onResponse(call: Call<EnderecoViaCep>, response: Response<EnderecoViaCep>) {
                        if (response.isSuccessful) {
                            val endereco = response.body()
                            endereco?.let {
                                dbHelper.inserirEndereco(it.cep, it.localidade, it.bairro, it.logradouro)
                                message = "Endereço salvo com sucesso!"
                                enderecos = dbHelper.obterEnderecos()
                            }
                        } else {
                            message = "Erro ao buscar endereço."
                        }
                    }

                    override fun onFailure(call: Call<EnderecoViaCep>, t: Throwable) {
                        message = "Falha na requisição: ${t.message}"
                    }
                })
            } else {
                message = "Por favor, digite um CEP válido."
            }
        }) {
            Text("Buscar e Salvar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botão para resetar o histórico de endereços
        Button(onClick = {
            dbHelper.resetDatabase()  // Chama o reset completo do banco de dados
            enderecos = emptyList()   // Atualiza a interface para mostrar o histórico vazio
            message = "Histórico resetado com sucesso!"
        }) {
            Text("Resetar Histórico")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(message)

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(enderecos) { endereco ->
                Text(
                    text = "CEP: ${endereco.cep}\nCidade: ${endereco.cidade}\nBairro: ${endereco.bairro}\nRua: ${endereco.rua}",
                    modifier = Modifier.padding(8.dp)
                )
                Divider()
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "enderecos.csv")
            exportarParaCSV(context, enderecos)
            message = "Arquivo CSV gerado com sucesso!"
        }) {
            Text("Exportar para CSV")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "enderecos.csv")
            compartilharCSV(context, file)
        }) {
            Text("Compartilhar CSV")
        }
    }
}

// Função para exportar os dados em CSV
fun exportarParaCSV(context: Context, enderecos: List<Endereco>) {
    val fileName = "enderecos.csv"
    val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), fileName)

    try {
        val outputStream = FileOutputStream(file)
        val writer = OutputStreamWriter(outputStream)

        writer.write("CEP,Cidade,Bairro,Rua\n")

        for (endereco in enderecos) {
            writer.write("${endereco.cep},${endereco.cidade},${endereco.bairro},${endereco.rua}\n")
        }

        writer.close()
        outputStream.close()

    } catch (e: Exception) {
        e.printStackTrace()
    }
}

// Função para compartilhar o arquivo CSV
fun compartilharCSV(context: Context, file: File) {
    val uri = FileProvider.getUriForFile(
        context,
        context.applicationContext.packageName + ".provider",
        file
    )

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/csv"
        putExtra(Intent.EXTRA_STREAM, uri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }

    context.startActivity(Intent.createChooser(intent, "Compartilhar arquivo"))
}
