CREATE KEYSPACE itau WITH REPLICATION = { 'class' : 'NetworkTopologyStrategy', 'datacenter1' : 1 };

CREATE TABLE itau.top_user_by_follwoers (
	user_id text,
	user_name text,
	followers bigint,
	PRIMARY KEY(user_id, followers)
) WITH CLUSTERING ORDER BY (followers DESC);

CREATE TABLE itau.tag_counter_by_lang (
	tag text,
	lang text,
	count bigint,
	PRIMARY KEY(tag, lang, count)
) WITH CLUSTERING ORDER BY (land ASC, count DESC);

CREATE TABLE itau.posts_by_date (
	date timestamp,
	count bigint,
	PRIMARY KEY(date, count)
) WITH CLUSTERING ORDER BY (count DESC);
