# AppBuscaCep

**AppBuscaCep** é um aplicativo Android desenvolvido em Kotlin, que busca e armazena dados de endereços a partir da API ViaCEP. O app permite realizar consultas por CEP, exibir resultados salvos, exportar e compartilhar os dados em formato CSV, e limpar o histórico de endereços.

## 📋 Funcionalidades

- **Busca de Endereço por CEP**: Utiliza a API ViaCEP para buscar informações de endereço com base em um CEP inserido pelo usuário.
- **Armazenamento de Endereços**: Salva os dados obtidos no banco de dados local SQLite.
- **Exibição de Histórico**: Exibe o histórico de buscas em uma lista na tela principal.
- **Exportação para CSV**: Gera um arquivo CSV com os endereços armazenados.
- **Compartilhamento de CSV**: Permite compartilhar o arquivo CSV gerado.
- **Limpeza de Histórico**: Botão para limpar todo o histórico de endereços armazenados.

## 🚀 Tecnologias Utilizadas

- **Kotlin**: Linguagem de programação principal.
- **Jetpack Compose**: Toolkit para desenvolvimento de UI no Android.
- **SQLite**: Banco de dados local para armazenamento dos endereços.
- **Retrofit**: Biblioteca para fazer requisições HTTP à API ViaCEP.

## ⚙️ Configuração e Execução

1. Clone o repositório:
   
   git clone https://github.com/(SeuUsuário)/AppBuscaCep.git

3. Abra o projeto no **Android Studio**.

4. Compile e execute o aplicativo em um dispositivo físico ou emulador Android.

## 📂 Estrutura do Projeto

- **MainActivity**: Contém a interface e lógica de interação com o usuário.
- **DatabaseHelper**: Gerencia as operações de armazenamento e consulta ao banco de dados SQLite.
- **RetrofitClient**: Configuração do Retrofit para conexão com a API ViaCEP.
- **Modelos de Dados**: Modelos para representar os dados de endereço.

## 📄 Documentação de API

Este aplicativo utiliza a **API ViaCEP** para buscar dados de endereços. Saiba mais sobre a API [aqui](https://viacep.com.br).





