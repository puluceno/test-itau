CREATE KEYSPACE itau WITH REPLICATION = { 'class' : 'NetworkTopologyStrategy', 'datacenter1' : 1 };

CREATE TABLE itau.top_user_by_followers (
	user_id bigint,
	user_name text,
	followers int,
	PRIMARY KEY(user_id, followers)
) WITH CLUSTERING ORDER BY (followers DESC);

CREATE TABLE itau.tag_counter_by_lang (
	tag text,
	lang text,
	count bigint,
	PRIMARY KEY(tag, lang, count)
) WITH CLUSTERING ORDER BY (lang ASC, count DESC);

CREATE TABLE itau.posts_by_date (
	date timestamp,
	count bigint,
	PRIMARY KEY(date, count)
) WITH CLUSTERING ORDER BY (count DESC);

CREATE TABLE itau.simple_tweet (
	id bigint,
	tag text,
	user_id bigint,
	user_name text,
	followers int,
	lang text,
	created_at timestamp,
	PRIMARY KEY(id)
);
