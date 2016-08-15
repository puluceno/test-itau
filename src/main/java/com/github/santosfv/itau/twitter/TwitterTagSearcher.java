package com.github.santosfv.itau.twitter;

import com.github.santosfv.itau.cassandra.CassandraConnection;
import com.github.santosfv.itau.cassandra.CassandraConnectionFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.Query;
import twitter4j.Query.ResultType;
import twitter4j.QueryResult;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TwitterTagSearcher {

	private static final String HASH = "#";
	private static final Logger LOGGER = LoggerFactory.getLogger(TwitterTagSearcher.class);
	private static final int COUNT = 100;

	public static void main(String[] args) {

		if (args.length == 0) {
			System.out.println("Missing tag list!");
			System.exit(1);
		}

		new TwitterTagSearcher().saveTags(Arrays.asList(args[0].split(",")).stream().collect(Collectors.toSet()));
	}

	public void saveTags(Set<String> tags) {
		Twitter twitter = TwitterFactory.getSingleton();

		try (CassandraConnection connection = CassandraConnectionFactory.create()) {
			TweetRepository repository = new TweetRepository(connection);

			tags
					.stream()
					.filter(this::isValidTag)
					.map(this::createQueryForTag)
					.map(query -> {
						try {
							LOGGER.info("Performing search for tag: {}.", query.getQuery());
							return twitter.search().search(query);
						} catch (TwitterException e) {
							throw new RuntimeException(e);
						}
					})
					.flatMap(this::convertStatusToSimpleTweet)
					.forEach(repository::saveTweet);
		}
	}

	private boolean isValidTag(String tag) {
		boolean isValid = StringUtils.isNotBlank(tag) && tag.startsWith(HASH);

		if (!isValid) {
			LOGGER.warn("Discarding invalid tag {}.", tag);
		}

		return isValid;
	}

	private Query createQueryForTag(String tag) {
		Query query = new Query();
		query.setQuery(tag);
		query.setResultType(ResultType.recent);
		query.setCount(COUNT);

		return query;
	}

	private Stream<SimpleTweet> convertStatusToSimpleTweet(QueryResult result) {
		return result
				.getTweets()
				.stream()
				.map(status -> new SimpleTweet(
						status.getId(),
						result.getQuery(),
						status.getUser().getId(),
						status.getUser().getName(),
						status.getUser().getFollowersCount(),
						status.getLang(),
						status.getCreatedAt()))
				.collect(Collectors.toList())
				.stream();
	}

}
