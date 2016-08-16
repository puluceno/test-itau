############################################################
# Dockerfile For Itau Test
# Based on Debian Wheezy
############################################################

FROM debian:wheezy

# Avoid problems with gclibc arenas
ENV MALLOC_ARENA_MAX=4

# Update the sources list and install base packages
RUN apt-get update && apt-get install -y tar less vim wget unzip software-properties-common python procps

# Install Java 8
RUN echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | /usr/bin/debconf-set-selections \
	&& echo "deb http://ppa.launchpad.net/webupd8team/java/ubuntu trusty main" | tee /etc/apt/sources.list.d/webupd8team-java.list \
	&& echo "deb-src http://ppa.launchpad.net/webupd8team/java/ubuntu trusty main" | tee -a /etc/apt/sources.list.d/webupd8team-java.list \
	&& apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv-keys EEA14886 \
	&& apt-get update && apt-get install -y oracle-java8-installer

ENV JAVA_HOME /usr/lib/jvm/java-8-oracle

# Cassandra Installation
ENV CASSANDRA_VERSION 3.7
RUN echo "deb http://www.apache.org/dist/cassandra/debian 37x main" | tee  /etc/apt/sources.list.d/cassandra.list \
	&& echo "deb-src http://www.apache.org/dist/cassandra/debian 37x main" | tee -a /etc/apt/sources.list.d/cassandra.list \
	&& apt-key adv --keyserver pgp.mit.edu --recv-keys 0353B12C \
	&& apt-get update && apt-get install -y cassandra="$CASSANDRA_VERSION"

# Spark Installation
ENV SPARK_HOME=/opt/spark
ENV SPARK_VERSION=2.0.0

# Spark install
RUN mkdir -p ${SPARK_HOME} \
    && wget -O /tmp/spark.tgz http://d3kbcqa49mib13.cloudfront.net/spark-${SPARK_VERSION}-bin-hadoop2.7.tgz \
	&& tar -xf /tmp/spark.tgz -C ${SPARK_HOME} --strip-components=1 \
	&& rm -f /tmp/spark.tgz

# NodeJS install
ENV NODE_JS_HOME=/opt/nodejs
ENV NODE_JS_VERSION=v4.4.7
ENV PATH=${PATH}:${NODE_JS_HOME}/bin

RUN mkdir -p $NODE_JS_HOME \
	&& wget -O /tmp/nodejs.tar.xz http://nodejs.org/dist/${NODE_JS_VERSION}/node-${NODE_JS_VERSION}-linux-x64.tar.xz \
	&& tar -xf /tmp/nodejs.tar.xz -C ${NODE_JS_HOME} --strip-components=1 \
	&& rm -f /tmp/nodejs.tar.xz

ADD resources/ /
ADD . /sources

# Setup NodeJS server
RUN cd /sources/src/nodejs/server && npm install

ENTRYPOINT ["/sbin/start.sh"]