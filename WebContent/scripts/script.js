$('.dropdown-menu a').click(function() {
	$('#dropdownMenuButton').text($(this).text());
});

$(function() {
	$('[data-toggle="tooltip"]').tooltip()
})