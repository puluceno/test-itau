package com.github.santosfv.itau.spark;

import com.github.santosfv.itau.cassandra.CassandraConnection;
import com.github.santosfv.itau.cassandra.CassandraConnectionFactory;
import com.github.santosfv.itau.twitter.SimpleTweet;
import com.github.santosfv.itau.twitter.TweetRepository;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

public class ItauApplication {

	public static void main(String[] args) {
		try (
				CassandraConnection connection = CassandraConnectionFactory.create();
				JavaSparkContext context = new JavaSparkContext()) {

			TweetRepository repository = new TweetRepository(connection);
			JavaRDD<SimpleTweet> rdd = context
					.parallelize(repository.listAll())
					.cache();

			// Top five users wit more followers
			new TopUsersByFollowers(connection).process(rdd);
			// Post count for lang=pt
			new PostCountByTagsAndLang(connection).process(rdd, "pt");
			// Post count by date/hour
			new PostCounterByDateHour(connection).process(rdd);
		}
	}
}
