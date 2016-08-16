var express = require('express');
var router = express.Router();

/* GET top five users by followers listing. */
router.get('/top-users', function(req, res, next) {
	var client = req.client;
	var query = 'SELECT user_name, followers FROM top_user_by_followers';
	client.execute(query, function(err, result) {
		if(err){
			console.log(err);
			return;
		}

		var users = new Array();

		for(i=0; i<result.rows.length; i++){
			users.push({name: result.rows[i].user_name, followers: result.rows[i].followers});
		}

		users.sort(function(userA, userB){ return (userA.followers - userB.followers) *-1 });
		res.send(users);
	});
});

/* GET tag counters by lang listing. */
router.get('/tag-counter-by-lang', function(req, res, next) {
	var client = req.client;
	var query = 'SELECT tag, count FROM tag_counter_by_lang';
	client.execute(query, function(err, result) {
		if(err){
			console.log(err);
			return;
		}

		var tags = new Array();

		for(i=0; i<result.rows.length; i++){
			tags.push({tag: result.rows[i].tag, count: result.rows[i].count});
		}

		tags.sort(function(tagA, tagB){ return (tagA.count - tagB.count) *-1 });
		res.send(tags);
	});
});

/* GET tag counters by lang listing. */
router.get('/tag-counter-by-date', function(req, res, next) {
	var client = req.client;
	var query = 'SELECT * FROM posts_by_date';
	client.execute(query, function(err, result) {
		if(err){
			console.log(err);
			return;
		}

		var tags = new Array();

		for(i=0; i<result.rows.length; i++){
			tags.push({date: new Date(result.rows[i].date), count: result.rows[i].count});
		}

		tags.sort(function(d1, d2){return (d1.date.getTime() - d2.date.getTime()) * -1});
		res.send(tags);
	});
});

module.exports = router;
