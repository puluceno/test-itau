#!/bin/bash
set -e

CASSANDRA_STARTUP_TIME="${CASSANDRA_STARTUP_TIME:-30}"
CQL_REQUEST_TIMEOUT="${CQL_REQUEST_TIMEOUT:-60}"

function info(){
	echo "####################################"
	echo "#  $1"
	echo "####################################"
}

info "Iniciando o cassandra"
cassandra -R
sleep $CASSANDRA_STARTUP_TIME

info "Criando o database"
cqlsh -f /etc/cassandra/default-db-model.cql --request-timeout=$CQL_REQUEST_TIMEOUT

info "Carregando a base do Twitter"
cd /sources && ./gradlew run -PmainClass="com.github.santosfv.itau.twitter.TwitterTagSearcher" -PrunArgs="#brasil,#brazil,#brasil2016,#brazil2016,#jogosolimpicos,#olimpiadas,#olimpiadas2016,#olympics,#rio2016,#riodejaneiro"

info "Executando o Job do Spark"
cd /sources && ./gradlew run

info "Iniciando o server NodeJs"
cd /sources/src/nodejs/server
exec npm start
