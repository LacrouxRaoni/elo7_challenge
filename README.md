# SpaceExplorer API

O `SpaceExplorer` é uma API rest que permite cadastrar planetas e sondas em um banco de dados. Feito isso, é possível explorar o planeta utilizando as sondas cadastradas mudando sua posição na órbita do planeta. 


## Sobre

A API foi desenvolvida utilizando Java, Springboot e Postgres: 
 ```
 + Java11
+ Maven
	- Dependências:
		- JPA
		- Hibernate
		- SpringBoot FrameWork
		- H2 (ambiente Heroku)
+ Postgresql (ambiente Local)
```

## Instalação

Para rodar a aplicação em seu ambiente é necessário ter instalados:


+  <a href="https://docs.docker.com/engine/install/">Docker</a>
+  <a href="https://docs.docker.com/compose/install/">Docker-compose</a>
+  <a href="https://www.postgresql.org/download/">Postgresql</a>
+  <a href="https://www.postman.com/downloads/">Postman</a>


apoś a instalação, clone o repositório para sua máquina e na pasta raíz do projeto, basta digitar `docker-compose up` na linha de comando e o docker fará a instalação das dependências necessárias.

Quando a aplicação estiver online, basta utilizar o Postman para chamar as requisições através do endereço:

`http://localhost:8080/space-explorer/`

Caso não queira instalar a aplicação, é possível utilizar através da plataforma Heroku através do link `(disponível até final de Outubro/2022)`:

`https://pure-fjord-43404.herokuapp.com/space-explorer/`


## Métodos
Requisições para a API devem seguir os padrões:
| Método | Descrição |
|---|---|
| `GET` | Retorna informações de um ou mais planetas e sondas. |
| `POST` | Utilizado para criar um novo planeta ou sonda. |
| `PUT` | Atualiza dados de um um planeta ou sonda e altera a posição da sonda. |
| `DELETE` | Remove planetas e sondas do banco de dados. |


## Respostas

| Código | Descrição |
|---|---|
| `200` | Requisição executada com sucesso (success).|
| `201` | Dados registrados com sucesso.|
| `400` | Erros de validação ou os campos informados não existem no sistema.|


## Requisições para planeta.

### Listar planetas [GET]
Para solicitar todos os planetas cadastrados no banco de dados, basta utilizar o endereço (`planet/all`).
A API irá retornar todos os planetas cadastrados no banco de dados. 

+ Request
    ```
	http://localhost:8080/space-explorer/planet/all
    ```

+ Response 200

    + Body

			PlanetInfo{
				planetName= Terra
				planetName= Marte
				planetName= Venus
            }

### Listar um planeta [GET]
Para solicitar os dados de um único planeta cadastrado, basta digitar o nome do planeta no body em formato Json.
A API irá retornar os dados do planeta e todas as sondas cadastradas relacionadas a ele. 

+ Request
	
			
			http://localhost:8080/space-explorer/planet/all

	+ Body

			{
				"planetName": "Terra"
            }


+ Response 200

    + Body

			PlanetInfo{
				id= 1
				planetName= Terra
				width= 5
				height= 5
				explorerAmountInPlanet= 3
				explorerAmountLimit= 25
				explorers in planet: 
				explorer= sonda1
				explorer= sonda2
				explorer= sonda3
            }


### Cadastrar um novo planeta [POST]
É possível registrar novos planetas no banco de dados. Para isto, é necessário enviar no body o nome do planeta, sua largura e altura. Caso o nome do planeta já existir no banco de dados, a API irá retornar informando que não pode ser criado um planeta com o mesmo nome. Em caso de sucesso, a API retorna uma mensagem que o planeta foi registrado. 

+ Request
	
			
			http://localhost:8080/space-explorer/planet

	+ Body

			{
				"planetName": "Venus",
				"width": 6,
				"height": 3	
            }

+ Response 201 (created)

    + Body

            {
               Planet created with success
            }



### Editar o nome de um planeta [PUT]
Para editar o nome de um planeta já registrado, deve ser solicitado um put informando o nome do planeta a ser editado e também o novo nome. A API deverá retornar um json com o nome atualizado e os dados relativos ao planeta.

+ Request
	
			
			http://localhost:8080/space-explorer/planet

	+ Body

			{
				"planetName": "Venus",
				"newPlanetName": "Jupiter"
            }

+ Response 200

    + Body

        	PlanetInfo{
			id= 3
			planetName= Jupiter
			width= 6
			height= 3
			explorerAmountInPlanet= 0
			explorerAmountLimit= 18
			explorers in planet: 
			}


### Apagar um planeta [DELETE]
Para apagar um planeta do banco de dados, é necessário informar o nome do planeta em Json. A API irá deletar o planeta e todas as sondas registradas para o planeta e depois, retornar uma mensagem que o planeta foi deletado com sucesso. 

+ Request
	
			
			http://localhost:8080/space-explorer/planet

	+ Body

			{
				"planetName": "Venus",
            }

+ Response 200

    + Body

			{
				Planet deleted with success
			}


## Requisições para sonda


### Listar sondas [GET]
Para solicitar todas as sondas cadastradas no banco de dados, basta utilizar o endereço (explorer/all).
A API irá retornar todas as sondas cadastradas no banco de dados. 

+ Request
    ```
	http://localhost:8080/space-explorer/explorer/all
    ```

+ Response 200

    + Body

			Explorers{
			ExplorerName: sonda1
			ExplorerName: sonda2
			ExplorerName: sonda3
			}
            

### Listar uma sonda [GET]
Para solicitiar os dados de uma única sonda cadastrada, basta digitar o nome da sonda no body em formato Json.
A API irá retornar os dados da sonda cadastrada.

+ Request
	
			
			http://localhost:8080/space-explorer/explorer

	+ Body

			{
				"explorerName": "sonda1"
            }


+ Response 200

    + Body

			Explorer{
			id= 4
			explorerName= sonda1
			axis x= 1
			axis y= 2
			cardinal= NORTH
			planetName= Terra
			}


### Cadastrar uma nova sonda [POST]
É possível cadastrar novas sondas no banco de dados desde que já tenha um Planeta cadastrado e o mesmo não tenha excedido o número de sondas permitidas. Para isto, é necessário enviar no body o nome do planeta, o nome da sonda, a posição em que a sonda está direcionada (NORTH, SOUTH, WEST, EAST) e sua posição no eixo x e y. Uma sonda não pode ser registrada na mesma posição de outra sonda, nem conter o mesmo nome de uma sonda já registrada. A API irá retornar uma mensagem em caso de sucesso. 

+ Request
	
			
			http://localhost:8080/space-explorer/explorer

	+ Body

			{
				"planetName": "Terra",
				"explorerName": "sonda4",
				"direction": "WEST",
				"x": 1,
				"y": 1
            }

+ Response 201 (created)

    + Body

            {
               Explorer added with success
            }



### Editar o nome de uma sonda [PUT]
Para editar o nome de uma sonda já cadastrada, deve ser solicitado um put em (`explorer/name`) informando o nome da sonda a ser editada e também o novo nome. A API deverá retornar um json com o nome atualizado e os dados relativos à sonda.

+ Request
	
			
			http://localhost:8080/space-explorer/explorer/name

	+ Body

			{
				"explorerName": "sonda4",
				"newExplorerName": "sonda10"
            }

+ Response 200

    + Body

        	Explorer{
			id= 7
			explorerName= sonda10
			axis x= 1
			axis y= 1
			cardinal= WEST
			planetName= Terra
			}


### Mover uma sonda [PUT]
Para mover uma sonda, deve ser solicitado um put em (`explorer/move`) informando o nome da sonda e os movimentos a serem executados:

	- L: girar à esquerda.
	- R: girar à direita.
	- M: movimentar para a direção em que a sonda está apontando.

Caso haja uma outra sonda no caminho, a API deve abortar as instruções. Caso seja um movimento válido, a API retorna um JSON informando a nova posição da sonda cadastrada no banco de dados.

+ Request
	
			
			http://localhost:8080/space-explorer/explorer/move

	+ Dados atuais:

			Explorer{
			id= 5
			explorerName= sonda2
			axis x= 4
			axis y= 3
			cardinal= EAST
			planetName= Terra
			}

	+ Body

			{
				"explorerName": "sonda2",
				"movement": "LMM"
            }

+ Response 200

    + Body

        	Explorer{
			id= 5
			explorerName= sonda2
			axis x= 4
			axis y= 5
			cardinal= NORTH
			planetName= Terra
			}



### Apagar uma sonda [DELETE]
Para apagar uma sonda do banco de dados, é necessário informar o nome da sonda em Json. A API irá deletar a sonda e enviar uma mensagem de sucesso.

+ Request
	
			
			http://localhost:8080/space-explorer/explorer

	+ Body

			{
				"explorerName": "sonda10"
            }

+ Response 200

    + Body

			{
				Explorer deleted with success
			}



