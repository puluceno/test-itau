package com.github.santosfv.itau.spark;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.github.santosfv.itau.cassandra.CassandraConnection;
import com.github.santosfv.itau.twitter.SimpleTweet;
import com.github.santosfv.itau.util.DateConversionUtils;
import org.apache.spark.api.java.JavaRDD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Tuple2;

import java.time.LocalDateTime;

public class PostCounterByDateHour {

	private static final Logger LOGGER = LoggerFactory.getLogger(PostCounterByDateHour.class);
	private final CassandraConnection connection;
	private final PreparedStatement insert;

	public PostCounterByDateHour(CassandraConnection connection) {
		this.connection = connection;
		insert = connection.getSession().prepare("INSERT INTO posts_by_date(date, count)"
				+ "VALUES(:date, :count)");
	}

	public void process(JavaRDD<SimpleTweet> rdd) {
		LOGGER.info("Processing count tweets by date and hour");

		rdd
				.mapToPair(tweet -> new Tuple2<>(DateConversionUtils.convertDateAndTruncate(tweet.getCreatedAt()), 1L))
				.reduceByKey((count1, count2) -> count1 + count2)
				.collect()
				.stream()
				.forEach(this::saveCounts);
	}

	private void saveCounts(Tuple2<LocalDateTime, Long> tuple) {
		BoundStatement statement = insert.bind()
				.setTimestamp("date", DateConversionUtils.toDate(tuple._1()))
				.setLong("count", tuple._2());

		connection.getSession().execute(statement);
	}
}
