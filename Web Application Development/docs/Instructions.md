# Instruções

## Correr os seguintes sql scripts na base de dados: 
	"./code/jvm/ProjectIssuesManager/sql/create_tables.sql",

## Configurar a string para o datasource no ficheiro:
	"./src/main/resources/application.properties"
	ex:
		app.dbConnString=jdbc:postgresql://localhost:5432/basedados?user=postgres&password=somepassword
	
	Em que:
		basedados é o nome da base de dados.
		postgres é o nome do utilizador.
		somepassword é a password do utilizador.
	
