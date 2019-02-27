<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">
<!-- Bootstrap core CSS -->
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css"
	integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO"
	crossorigin="anonymous">
<link rel="stylesheet" href="css/style.css">

<title>MVC Film Site</title>
</head>
<body>
	<div id="background"></div>

	<div class="container">
		<br>
		<div class="row">

			<!--  Form Controls -->
			<div class="col-sm-4 left">
				<h3>Controls</h3>
				<hr>
				<div class="row">
					<!--  Search Dropdown-->
					<div class="dropdown col-9">
						<button class="btn btn-secondary dropdown-toggle" type="button"
							id="search" data-toggle="dropdown" aria-haspopup="true"
							aria-expanded="false">Search</button>
						<div class="dropdown-menu">
							<form action="GetFilmById.do" method="GET" class="px-4 py-3">
								<div class="input-group row">
									<label for="filmID" class="label-align-left col-12">Search
										by ID</label> <input name="filmId" type="text"
										class="form-control col-6"
										aria-label="Text input with radio button"> <br>
									<button type="submit" class="btn btn-primary col-6">Search</button>
								</div>
							</form>

							<form action="GetFilmByKeyword.do" method="GET" class="px-4 py-3">
								<div class="input-group row">
									<label for="keyword" class="label-align-left col-12">Search
										by Keyword</label> <input name="keyword" type="text"
										class="form-control col-6"
										aria-label="Text input with radio button"> <br>
									<button type="submit" class="btn btn-primary col-6">Search</button>
								</div>
							</form>

						</div>
					</div>
					<div class="col-sm-auto">
						<a href="/MVCFilmSite" class="btn btn-outline-secondary btn-sm">Reset</a>
					</div>
				</div>
				<hr>

				<!--  "Add Film" Dropdown/Form -->
				<div class="dropdown">
					<button class="btn btn-secondary dropdown-toggle" type="button"
						id="dropdownMenuButton" data-toggle="dropdown"
						aria-haspopup="true" aria-expanded="false">Add Film</button>
					<div class="dropdown-menu">
						<form action="AddFilm.do" method="POST" class="px-4 py-3">
							<div class="form-group">
								<label for="title">Title</label> <input type="text"
									class="form-control" name="title">
							</div>
							<div class="form-group">
								<label class="hasToolTip" for="languageID" data-toggle="tooltip"
									data-html="true"
									title="<div style='text-align:left'>1 - English<br>2 - Italian<br>3 - Japanese<br>4 - Mandarin<br>5 - French<br>6 - German</div>">Language
									ID: </label> <input type="text" class="form-control col-4"
									name="languageId" value="1">
							</div>
							<div class="form-group">
								<label for="id">Film ID (Optional)</label> <input type="text"
									class="form-control col-4" name="id" value="0">
							</div>
							<button type="submit" class="btn btn-primary">Submit new
								film</button>
						</form>
					</div>
				</div>
				<hr>
				<!--  List of films returned by keyword search go right here.  
					  Clicking on one will populate the form fields to the right -->
				<div>
					<ul class="list-group">
						<c:forEach var="film" items="${filmList}">
							<li class='list-group-item'><a
								href="<c:url value="GetFilmById.do?filmId=${film.id}"/>">ID:
									<c:out value="${ film.id}" />
							</a><br> Title: <c:out value="${ film.title}" /><br>
								Description: <c:out value="${ film.description}" /><br>
						</c:forEach>
					</ul>
				</div>
			</div>

			<!--  Film Details -->
			<div class="col-sm-8 right">
				<h3>Film Data</h3>
				<hr>
				<form action="ModifyFilm.do" method="POST">
					<div class="form-group form-inline row">
						<label for="filmID" class="control-label col-3">Film ID</label> <input
							type="text" class="form-control form-control-sm col-1" name="id"
							value="${film.id }" readonly="readonly"> <label for="title"
							class="control-label col-3 offset-1">Title</label> <input
							type="text" class="form-control form-control-sm col-4"
							name="title" value="${film.title }">
					</div>
					<div class="form-group form-inline row">
						<label class="control-label col-3">Year</label> <input type="text"
							class="form-control form-control-sm col-1" name="releaseYear"
							value="${film.releaseYear }"> <label for="categories"
							class="control-label col-3 offset-1">Category</label> <input
							type="text" class="form-control form-control-sm col-4"
							name="categories" value="${film.category }">
					</div>
					<div class="form-group form-inline row">
						<label for="rating" class="control-label col-3">Rating</label> <input
							type="text" class="form-control form-control-sm col-1"
							name="rating" value="${film.rating }"> <label
							for="features" class="control-label col-3 offset-1">Special
							Features</label>
						<textarea class="form-control form-control-sm col-4"
							name="features"></textarea>
					</div>
					<div class="form-group form-inline row">
						<label for="language" class="control-label col-3">Language<br>
							(${film.language })</label> 
							<input type="text"
							class="form-control form-control-sm col-1" name="languageId"
							value="${film.languageId }">
							<label for="description"
							class="control-label col-3 offset-1">Description</label>
						<textarea class="form-control form-control-sm col-4"
							name="description">${film.description }</textarea>
					</div>
					<div class="form-group form-inline row">
						<label for="length" class="control-label col-3">Length</label> <input
							type="text" class="form-control form-control-sm col-1"
							name="length" value="${film.length }"><label for="cast"
							class="control-label col-3 offset-1">Cast</label>
						<textarea class="form-control form-control-sm col-4" readonly="readonly">${film.actorList }</textarea>
					</div>
					<div class="form-group form-inline row">
						<label for="rentalDuration" class="control-label col-3">Rental
							Duration</label> <input type="text"
							class="form-control form-control-sm col-1" name="rentalDuration"
							value="${film.rentalDuration }">
						<div class="col-4"></div>
					</div>
					<div class="form-group form-inline row">
						<label for="rentalRate" class="control-label col-3">Rental
							Rate</label> <input type="text"
							class="form-control form-control-sm col-1" name="rentalRate"
							value="${film.rentalRate }">
					</div>
					<div class="form-group form-inline row">
						<label for="replacementCost" class="control-label col-3">Replacement
							Cost</label> <input type="text"
							class="form-control form-control-sm col-1" name="replacementCost"
							value="${film.replacementCost }">
						<!--  Save/Delete buttons-->
						<div class="col-sm-2 offset-sm-4 col-md-2 offset-md-4">
							<button type="submit" class="btn btn-primary">Save
								Changes</button>
						</div>
						<div class="col-sm-3 col-md-2">
							<button type="submit" class="btn btn-danger" formmethod="POST"
								formaction="DeleteFilmById.do">Delete Film</button>
						</div>
					</div>
					<div class='row float-right'>
						<p style="color: red; font-weight: bold;">
							<c:out value="${result}" />
						</p>
					</div>
				</form>
			</div>
		</div>
	</div>


	<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"
		integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo"
		crossorigin="anonymous"></script>
	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js"
		integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49"
		crossorigin="anonymous"></script>
	<script
		src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js"
		integrity="sha384-ChfqqxuZUCnJSK3+MXmPNIyE6ZbWh2IMqE241rYiqJxyMiZ6OW/JmZQ5stwEULTy"
		crossorigin="anonymous"></script>
	<script src="scripts/script.js"></script>
</body>
</html>