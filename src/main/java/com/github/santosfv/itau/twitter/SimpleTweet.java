package com.github.santosfv.itau.twitter;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import com.google.common.base.Objects;

import java.io.Serializable;
import java.util.Date;

@Table(name = "simple_tweet")
public class SimpleTweet implements Serializable {

	private static final long serialVersionUID = 1L;

	@PartitionKey
	@Column(name = "id")
	private Long id;
	@Column(name = "tag")
	private String tag;
	@Column(name = "user_id")
	private Long userId;
	@Column(name = "user_name")
	private String userName;
	@Column(name = "followers")
	private Integer followers;
	@Column(name = "lang")
	private String lang;
	@Column(name = "created_at")
	private Date createdAt;

	/**
	 * Cassandra driver eyes only
	 */
	@Deprecated
	public SimpleTweet() {
	}

	public SimpleTweet(Long id, String tag, Long userId, String userName, Integer followers, String lang, Date createdAt) {
		this.id = id;
		this.tag = tag;
		this.userId = userId;
		this.userName = userName;
		this.followers = followers;
		this.lang = lang;
		this.createdAt = createdAt;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getFollowers() {
		return followers;
	}

	public void setFollowers(Integer followers) {
		this.followers = followers;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this)
				.add("id", id)
				.add("tag", tag)
				.add("userId", userId)
				.add("userName", userName)
				.add("followers", followers)
				.add("lang", lang)
				.add("createdAt", createdAt)
				.toString();
	}
}
