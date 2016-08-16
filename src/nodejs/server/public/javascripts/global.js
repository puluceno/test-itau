$(document).ready(function() {

	$('#top-users-by-followers').on('click', loadTopUsers);
	$('#tag-counters').on('click', loadTagCounters);
	$('#tag-counters-by-date').on('click', loadTagCountersByDate);

});

function loadTopUsers(event){
	var tableContent = '<thead><th>User</th><th>Followers</th></thead><tbody>';

	$.getJSON('/api/top-users', function( data ) {
		$.each(data, function(){
			tableContent += '<tr>';
			tableContent += '<td>' + this.name + '</td>';
			tableContent += '<td>' + this.followers + '</td>';
			tableContent += '</tr>';
		});

		tableContent += '<tbody>';
		$('#content table').html(tableContent);
	});
};

function loadTagCounters(event){
	var tableContent = '<thead><th>Tag</th><th>Count</th></thead><tbody>';

	$.getJSON('/api/tag-counter-by-lang', function( data ) {
		$.each(data, function(){
			tableContent += '<tr>';
			tableContent += '<td>' + this.tag + '</td>';
			tableContent += '<td>' + this.count + '</td>';
			tableContent += '</tr>';
		});

		tableContent += '<tbody>';
		$('#content table').html(tableContent);
	});
};

function loadTagCountersByDate(event){
	var tableContent = '<thead><th>Date</th><th>Count</th></thead><tbody>';

	$.getJSON('/api/tag-counter-by-date', function( data ) {
		$.each(data, function(){
			tableContent += '<tr>';
			tableContent += '<td>' + this.date + '</td>';
			tableContent += '<td>' + this.count + '</td>';
			tableContent += '</tr>';
		});

		tableContent += '<tbody>';
		$('#content table').html(tableContent);
	});
};
