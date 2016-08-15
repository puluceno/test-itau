package com.github.santosfv.itau.twitter;


import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.Mapper.Option;
import com.datastax.driver.mapping.MappingManager;
import com.github.santosfv.itau.cassandra.CassandraConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class TweetRepository {

	private static final Logger LOGGER = LoggerFactory.getLogger(TweetRepository.class);
	private final CassandraConnection connection;
	private final Mapper<SimpleTweet> mapper;

	public TweetRepository(CassandraConnection connection) {
		this.connection = connection;
		MappingManager mappingManager = new MappingManager(connection.getSession());
		mapper = mappingManager.mapper(SimpleTweet.class);
		mapper.setDefaultSaveOptions(Option.saveNullFields(false));
	}

	public void saveTweet(SimpleTweet tweet) {
		LOGGER.debug("Saving simple tweet: {}", tweet);
		mapper.save(tweet);
	}

	public List<SimpleTweet> listAll(){
		Select statement = QueryBuilder
				.select()
				.all()
				.from("simple_tweet");

		ResultSet resultSet = connection.getSession().execute(statement);

		return mapper.map(resultSet).all();
	}
}
