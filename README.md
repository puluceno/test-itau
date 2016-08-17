# Test Itau


## Container

O entrypoint do container por padrão efetua as seguintes ações:

 * Inicia o Cassandra;
 * Cria o schema no Cassandra;
 * Executa o job que faz a busca das tags no Twitter e persiste os resultados no Cassandra;
 * Executa o job Spark que faz as devidas agregações e persiste os resultados no Cassandra;
 * Inicia o servidor NodeJS.

Efetuar pull do container:

```shell
docker pull santosfv/itau-test
```

Iniciar container:

```shell
docker run -p 3000:3000 santosfv/itau-test 
```

Pode haver problemas ao criar o schema devido o cassandra não ter terminado seu startup, para tal basta configurar a variável de ambiente ``CASSANDRA_STARTUP_TIME``:

```shell
docker run -p 3000:3000 -e CASSANDRA_STARTUP_TIME=120 santosfv/itau-test 
```

## Server NodeeJS

Rest endpoints:

* **/**: Pagina HTML para exibição dos resultados objtidos pelo job Spark.
* **/api/top-users**: retorna os top 5 usuários com maior número de seguidores.
* **/api/tag-counter-by-lang**: contagem do número de tags filtradas pela lingua ``pt``.
* **/api//tag-counter-by-date**: contagem do número de tags agregadas por hora e data.

## Development

Detalhamento da execução dos jobs individualmente:

### Criar schema no cassandra

```shell
cqlsh -f /etc/cassandra/cqlsh -f default-db-model.cql --request-timeout=60
```

### Efetuar busca das tags no Twitter

```shell
./gradlew run -PmainClass="com.github.santosfv.itau.twitter.TwitterTagSearcher" -PrunArgs="#brasil,#brazil,#brasil2016,#brazil2016,#jogosolimpicos,#olimpiadas,#olimpiadas2016,#olympics,#rio2016,#riodejaneiro"
```

### Gerar agregações vis job Spark

```shell
./gradlew run
```
