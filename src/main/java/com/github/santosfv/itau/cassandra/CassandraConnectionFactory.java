package com.github.santosfv.itau.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory to create CassandraConnection. The creation should be done on foreachrdd.
 * For configuration settings see: DefaultCassandraConfiguration.
 */
public class CassandraConnectionFactory {

	private static final Logger LOGGER = LoggerFactory.getLogger(CassandraConnectionFactory.class);

	/**
	 * Creates a connection with Cassandra. Cluster and Session are created.
	 *
	 * @return CassandraConnection
	 */
	public static CassandraConnection create() {
		Cluster.Builder builder = Cluster.builder()
				.addContactPoints("localhost")
				.withPort(9042);

		LOGGER.info("Connecting to Cassandra Cluster");
		Cluster cluster = builder.build();
		LOGGER.info("Creating Cassandra Session");
		Session session = cluster.connect("itau");

		return new CassandraConnection(session, cluster);
	}
}
