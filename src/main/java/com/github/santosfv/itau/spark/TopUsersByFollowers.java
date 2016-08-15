package com.github.santosfv.itau.spark;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.github.santosfv.itau.cassandra.CassandraConnection;
import com.github.santosfv.itau.twitter.SimpleTweet;
import org.apache.spark.api.java.JavaRDD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Tuple2;

public class TopUsersByFollowers {

	private static final Logger LOGGER = LoggerFactory.getLogger(TopUsersByFollowers.class);
	private final CassandraConnection connection;
	private final PreparedStatement insert;

	public TopUsersByFollowers(CassandraConnection connection) {
		this.connection = connection;
		insert = connection.getSession().prepare("INSERT INTO top_user_by_followers(user_id, user_name, followers)"
				+ "VALUES(:userId, :userName, :followers)");
	}

	public void process(JavaRDD<SimpleTweet> rdd) {
		LOGGER.info("Processing top[5] users by followers");

		rdd
				.mapToPair(tweet -> new Tuple2<>(tweet.getFollowers(), tweet))
				.sortByKey(false)
				.take(5)
				.stream()
				.map(Tuple2::_2)
				.forEach(this::saveTopUser);
	}

	private void saveTopUser(SimpleTweet tweet) {
		BoundStatement statement = insert.bind()
				.setLong("userId", tweet.getUserId())
				.setString("userName", tweet.getUserName())
				.setInt("followers", tweet.getFollowers());

		connection.getSession().execute(statement);
	}
}
