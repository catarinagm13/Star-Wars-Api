# star-wars-api

Api Rest que permite disponibiliza as seguintes funcionalidades:

- Adicionar um planeta (com nome, clima e terreno)
- Listar planetas do banco de dados
- Listar planetas da API do Star Wars
- Buscar por nome no banco de dados
- Buscar por ID no banco de dados
- Remover planeta

Para as funcionalidades que acessam a API externa esse aplicativo acessa os dados da api aberta [https://swapi.dev/](https://swapi.dev/)

Correr:
docker build -t starwars .
docker run -d -p 8080:8080 starwars

A aplicação ira subir no caminho [http://localhost:8080](http://localhost:8080)

Operações:

- Adicionar um planeta (com nome, clima e terreno)

> [POST] http://localhost:8080/planeta

- Listar planetas do banco de dados

> [GET] http://localhost:8080/planeta

- Listar planetas da API do Star Wars

> [GET] http://localhost:8080/lista-api-externa

> [GET] http://localhost:8080/planeta/lista-api-externa-paginado?pagina=<numero>

- Buscar por nome no banco de dados

> [GET] http://localhost:8080/planeta/nome/{nome}

- Buscar por ID no banco de dados

> [GET] http://localhost:8080/planeta/{id}

- Remover planeta

> [DELETE] http://localhost:8080/planeta/{id}


####Exemplo requisição cadastro Planeta####

```json
{
    "nome":"Terra",
    "clima":"ameno",
    "terreno":"rochoso"
}

```

####Exemplo retorno busca de dados Planetas####
```json
{
    "total": 1,
    "proxima": null,
    "anterior": null,
    "planetas": [
        {
            "id": 1,
            "nome": "Terra",
            "clima": "ameno",
            "terreno": "rochoso",
            "numeroAparicoes": 0
        }
    ]
}

```



