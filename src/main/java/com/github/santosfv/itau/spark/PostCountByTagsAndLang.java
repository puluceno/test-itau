package com.github.santosfv.itau.spark;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.github.santosfv.itau.cassandra.CassandraConnection;
import com.github.santosfv.itau.twitter.SimpleTweet;
import org.apache.spark.api.java.JavaRDD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Tuple2;

public class PostCountByTagsAndLang {

	private static final Logger LOGGER = LoggerFactory.getLogger(PostCountByTagsAndLang.class);
	private final CassandraConnection connection;
	private final PreparedStatement insert;

	public PostCountByTagsAndLang(CassandraConnection connection) {
		this.connection = connection;
		insert = connection.getSession().prepare("INSERT INTO tag_counter_by_lang(tag, lang, count)"
				+ "VALUES(:tag, :lang, :count)");
	}

	public void process(JavaRDD<SimpleTweet> rdd, String lang) {
		LOGGER.info("Processing count tweets by lang {}.", lang);

		rdd
				.filter(tweet -> tweet.getLang().equals(lang))
				.mapToPair(tweet -> new Tuple2<>(tweet.getTag(), 1L))
				.reduceByKey((count1, count2) -> count1 + count2)
				.collect()
				.stream()
				.forEach(tuple -> saveTagsCount(tuple, lang));
	}

	private void saveTagsCount(Tuple2<String, Long> tuple, String lang) {
		BoundStatement statement = insert.bind()
				.setString("tag", tuple._1())
				.setString("lang", lang)
				.setLong("count", tuple._2());

		connection.getSession().execute(statement);
	}

}
