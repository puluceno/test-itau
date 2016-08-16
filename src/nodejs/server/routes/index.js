var express = require('express');
var router = express.Router();

/* GET home page. */
router.get('/', function(req, res, next) {
  res.render('index', { title: 'Home' });
});

router.get('/top-user-followers', function(req, res, next) {
  res.render('top-user-followers', { title: 'Top Users By Followers' });
});

router.get('/tag-counters', function(req, res, next) {
  res.render('tag-counters', { title: 'Tags By Lang' });
});

router.get('/tag-aggregated', function(req, res, next) {
  res.render('tag-aggregated', { title: 'Tags Aggregated' });
});

module.exports = router;
