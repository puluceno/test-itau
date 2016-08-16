package com.github.santosfv.itau.twitter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import twitter4j.Query;
import twitter4j.Query.ResultType;
import twitter4j.QueryResult;
import twitter4j.Status;

import java.util.Collections;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TwitterTagSearcherTest {

	private static final Long STATUS_ID = 1L;
	private static final Long USER_ID = 2L;
	private static final String USER_NAME = "UserName";
	private static final Integer FOLLOWERS = 10;
	private static final String LANG = "pt";
	private static final String QUERY = "query";
	private static final Date NOW = new Date();

	private TwitterTagSearcher subject;
	@Mock
	private QueryResult queryResult;
	@Mock(answer = Answers.RETURNS_DEEP_STUBS)
	private Status status;

	@Before
	public void setup() {
		subject = new TwitterTagSearcher();

		when(queryResult.getTweets()).thenReturn(Collections.singletonList(status));
		when(status.getId()).thenReturn(STATUS_ID);
		when(status.getUser().getId()).thenReturn(USER_ID);
		when(status.getUser().getName()).thenReturn(USER_NAME);
		when(status.getUser().getFollowersCount()).thenReturn(FOLLOWERS);
		when(status.getLang()).thenReturn(LANG);
		when(status.getCreatedAt()).thenReturn(NOW);
		when(queryResult.getQuery()).thenReturn(QUERY);
	}

	@Test
	public void should_return_false_when_tag_is_null() {
		assertFalse(subject.isValidTag(null));
	}

	@Test
	public void should_return_false_when_tag_is_blank() {
		assertFalse(subject.isValidTag(" "));
	}

	@Test
	public void should_return_false_when_tag_do_not_starts_with_hash() {
		assertFalse(subject.isValidTag("invalid"));
	}

	@Test
	public void should_return_true_when_tag_starts_with_hash_and_is_not_blank() {
		assertTrue(subject.isValidTag("#valid"));
	}

	@Test
	public void should_set_tag_in_query() {
		Query query = subject.createQueryForTag("tag");

		assertEquals("tag", query.getQuery());
	}

	@Test
	public void should_set_result_type_as_recent_in_query() {
		Query query = subject.createQueryForTag("tag");

		assertEquals(ResultType.recent, query.getResultType());
	}

	@Test
	public void should_set_count_in_query() {
		Query query = subject.createQueryForTag("tag");

		assertEquals(TwitterTagSearcher.COUNT, query.getCount());
	}

	@Test
	public void should_convert_tweets_to_simple_tweet_with_tag() {
		SimpleTweet tweet = subject.convertStatusToSimpleTweet(queryResult).findFirst().get();

		assertEquals(QUERY, tweet.getTag());
	}

	@Test
	public void should_convert_tweets_to_simple_tweet_with_status_id() {
		SimpleTweet tweet = subject.convertStatusToSimpleTweet(queryResult).findFirst().get();

		assertEquals(STATUS_ID, tweet.getId());
	}

	@Test
	public void should_convert_tweets_to_simple_tweet_with_user_id() {
		SimpleTweet tweet = subject.convertStatusToSimpleTweet(queryResult).findFirst().get();

		assertEquals(USER_ID, tweet.getUserId());
	}

	@Test
	public void should_convert_tweets_to_simple_tweet_with_user_name() {
		SimpleTweet tweet = subject.convertStatusToSimpleTweet(queryResult).findFirst().get();

		assertEquals(USER_NAME, tweet.getUserName());
	}

	@Test
	public void should_convert_tweets_to_simple_tweet_with_followers_count() {
		SimpleTweet tweet = subject.convertStatusToSimpleTweet(queryResult).findFirst().get();

		assertEquals(FOLLOWERS, tweet.getFollowers());
	}

	@Test
	public void should_convert_tweets_to_simple_tweet_with_lang() {
		SimpleTweet tweet = subject.convertStatusToSimpleTweet(queryResult).findFirst().get();

		assertEquals(LANG, tweet.getLang());
	}

	@Test
	public void should_convert_tweets_to_simple_tweet_with_created_at() {
		SimpleTweet tweet = subject.convertStatusToSimpleTweet(queryResult).findFirst().get();

		assertEquals(NOW, tweet.getCreatedAt());
	}
}