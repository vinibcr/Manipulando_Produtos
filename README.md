Aplicativo de Gerenciamento de Produtos
Visão Geral

Este projeto é um aplicativo Android para gerenciamento de produtos, que permite adicionar, visualizar e excluir produtos de um banco de dados SQLite local e também buscar produtos de uma API externa. O aplicativo utiliza o padrão MVVM (Model-View-ViewModel) para estruturar o código e o RecyclerView para exibir uma lista de produtos.
Estrutura do Projeto
1. Fragmento de Manipulação de Produtos (Manipulacao)

Localização: com.example.myapplication.ui.Manipulacao

    Classe Manipulacao: Este fragmento gerencia a interface de usuário para adicionar, listar e excluir produtos.
        Método onCreateView: Configura a interface do usuário, incluindo o RecyclerView para exibir a lista de produtos e um botão para adicionar novos produtos.
        Método getDataFromSQLite: Recupera a lista de produtos do banco de dados SQLite.
        Método insertProduct: Insere um novo produto no banco de dados.
        Método deleteProduct: Remove um produto do banco de dados.

2. Helper do Banco de Dados (MyDatabaseHelper)

Localização: com.example.myapplication.data

    Classe MyDatabaseHelper: Gerencia o banco de dados SQLite.
        Método copyDatabase: Copia o banco de dados pré-populado do diretório de assets para o armazenamento interno do dispositivo.
        Método insertProduct: Insere um novo produto na tabela produtos.
        Método deleteProduct: Remove um produto da tabela produtos.
        Método getNextId: Obtém o próximo ID disponível para a inserção de um novo produto.

3. Adaptador do RecyclerView (MyAdapter)

Localização: com.example.myapplication.ui.Manipulacao

    Classe MyAdapter: Adaptador para exibir produtos em um RecyclerView.
        Método onCreateViewHolder: Infla o layout de item para o RecyclerView.
        Método onBindViewHolder: Preenche os dados no layout do item.
        Método getItemCount: Retorna o número de itens na lista de dados.

4. Fragmento de Galeria (GalleryFragment)

Localização: com.example.myapplication.ui.gallery

    Classe GalleryFragment: Fragmento que busca e exibe produtos de uma API externa usando a biblioteca Fuel para requisições HTTP.
        Método onCreateView: Configura o RecyclerView e inicializa a lista de produtos.
        Método fetchProducts: Faz uma requisição GET para a API externa e atualiza a lista de produtos com a resposta.

Layouts

    Layout do Fragmento de Manipulação: Inclui campos de texto para entrada de dados do produto, um botão para adicionar produtos e um RecyclerView para listar produtos.
    Layout do Item do RecyclerView: Define a aparência de cada item na lista de produtos.

Dependências

    Fuel: Biblioteca para fazer requisições HTTP.
    AndroidX: Biblioteca padrão para componentes da interface do usuário e gerenciamento de banco de dados.
