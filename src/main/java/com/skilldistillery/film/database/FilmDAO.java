package com.skilldistillery.film.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.skilldistillery.film.entities.Actor;
import com.skilldistillery.film.entities.Film;

@Repository
public class FilmDAO implements DatabaseAccessor {

	private static final String URL = "jdbc:mysql://localhost:3306/sdvid?useSSL=false";

	public FilmDAO() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Film findFilmById(int filmId) throws SQLException {
		String user = "student";
		String pass = "student";
		Connection conn = DriverManager.getConnection(URL, user, pass);
		String sql = "select * from film where id = ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setInt(1, filmId);
		ResultSet rs = stmt.executeQuery();
		int id = 0, releaseYear = 0, languageId = 0, length = 0, rentalDuration = 0, rentalRate = 0,
				replacementCost = 0;
		String title = null, description = null, rating = null;
		while (rs.next()) {
			id = rs.getInt("id");
			title = rs.getString("title");
			description = rs.getString("description");
			releaseYear = rs.getInt("release_year");
			languageId = rs.getInt("language_id");
			length = rs.getInt("length");
			rating = rs.getString("rating");
			rentalDuration = rs.getInt("rental_duration");
			rentalRate = rs.getInt("rental_rate");
			replacementCost = rs.getInt("replacement_cost");
		}
		List<Actor> actorList = this.findActorsByFilmId(filmId);
		Film film = new Film(id, title, description, releaseYear, languageId, length, rating, actorList, rentalDuration,
				rentalRate, replacementCost);
		rs.close();
		stmt.close();
		conn.close();
		return film;
	}

	public List<Film> findFilmByKeyword(String keyword) throws SQLException {
		String user = "student";
		String pass = "student";
		keyword = "%" + keyword + "%";
		Connection conn = DriverManager.getConnection(URL, user, pass);
		String sql = "select * from film where title like ? or description like ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, keyword);
		stmt.setString(2, keyword);
		ResultSet rs = stmt.executeQuery();
		Film film = null;
		List<Film> filmList = new ArrayList<>();
		int id = 0, releaseYear = 0, languageId = 0, length = 0, rentalDuration = 0, rentalRate = 0,
				replacementCost = 0;
		String title = null, description = null, rating = null;
		while (rs.next()) {
			id = rs.getInt("id");
			title = rs.getString("title");
			description = rs.getString("description");
			releaseYear = rs.getInt("release_year");
			languageId = rs.getInt("language_id");
			length = rs.getInt("length");
			rating = rs.getString("rating");
			rentalDuration = rs.getInt("rental_duration");
			rentalRate = rs.getInt("rental_rate");
			replacementCost = rs.getInt("replacement_cost");
			List<Actor> actorList = this.findActorsByFilmId(id);
			film = new Film(id, title, description, releaseYear, languageId, length, rating, actorList, rentalDuration,
					rentalRate, replacementCost);
			filmList.add(film);
		}
		rs.close();
		stmt.close();
		conn.close();
		return filmList;
	}

	public Actor findActorById(int actorId) throws SQLException {
		String user = "student";
		String pass = "student";
		Connection conn = DriverManager.getConnection(URL, user, pass);
		String sql = "select * from actor where id = ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setInt(1, actorId);
		ResultSet rs = stmt.executeQuery();
		int id = 0;
		String firstName = null, lastName = null;
		while (rs.next()) {
			id = rs.getInt("id");
			firstName = rs.getString("first_name");
			lastName = rs.getString("last_name");
		}
		Actor actor = new Actor(id, firstName, lastName);
		rs.close();
		stmt.close();
		conn.close();
		return actor;
	}

	public List<Actor> findActorsByFilmId(int filmId) throws SQLException {
		String user = "student";
		String pass = "student";
		Connection conn = DriverManager.getConnection(URL, user, pass);
		String sql = "select actor.first_name, actor.last_name from actor join film_actor on actor.id = film_actor.actor_id join film on film_actor.film_id = film.id where film.id = ?;";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setInt(1, filmId);
		ResultSet rs = stmt.executeQuery();
		List<Actor> actorList = new ArrayList<>();
		String firstName = null, lastName = null;
		while (rs.next()) {
			firstName = rs.getString("first_name");
			lastName = rs.getString("last_name");
			actorList.add(new Actor(firstName, lastName));
		}
		rs.close();
		stmt.close();
		conn.close();
		return actorList;
	}

	public String getCategoryFromId(int filmId) throws SQLException {
		String user = "student";
		String pass = "student";
		Connection conn = DriverManager.getConnection(URL, user, pass);
		String sql = "select name from category \n" + "join film_category on film_category.category_id = category.id\n"
				+ "join film on film.id = film_category.film_id\n" + "where film.id = ?;";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setInt(1, filmId);
		ResultSet rs = stmt.executeQuery();
		String category = null;
		while (rs.next()) {
			category = rs.getString("name");
		}
		rs.close();
		stmt.close();
		conn.close();
		return category;
	}

	public String getLanguageFromId(int languageId) throws SQLException {
		String user = "student";
		String pass = "student";
		Connection conn = DriverManager.getConnection(URL, user, pass);
		String sql = "select * from language where id = ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setInt(1, languageId);
		ResultSet rs = stmt.executeQuery();
		String language = null;
		while (rs.next()) {
			language = rs.getString("name");
		}
		rs.close();
		stmt.close();
		conn.close();
		return language;
	}

	public Integer addFilm(Film film) throws SQLException {
		String url = "jdbc:mysql://localhost:3306/sdvid?useSSL=false";
		String user = "student";
		String pword = "student";
		String sql;

		sql = "INSERT INTO film (title, description, release_year, language_id, length, rating, id, rental_duration, rental_rate, replacement_cost)"
				+ " values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		Connection conn = null;
		int key = 0;
		try {
			conn = DriverManager.getConnection(url, user, pword);
			conn.setAutoCommit(false);
			PreparedStatement st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			st.setString(1, film.getTitle());
			st.setString(2, film.getDescription());
			st.setInt(3, film.getReleaseYear());
			st.setInt(4, film.getLanguageId());
			st.setInt(5, film.getLength());
			st.setString(6, film.getRating());
			st.setInt(7, film.getId());
			st.setInt(8, film.getRentalDuration());
			st.setDouble(9, film.getRentalRate());
			st.setDouble(10, film.getReplacementCost());
			st.executeUpdate();
			ResultSet keys = st.getGeneratedKeys();
			while (keys.next()) {
				key = keys.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			conn.commit();
		}
		return key;
	}

	public String deleteFilm(Film film) throws SQLException {
		String url = "jdbc:mysql://localhost:3306/sdvid?useSSL=false";
		String user = "student";
		String pword = "student";
		String result = null;
		String sql = "delete from film where id = ?;";
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, user, pword);
			conn.setAutoCommit(false);
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, film.getId());
			stmt.executeUpdate();
			result = "Film successfully deleted";
		} catch (SQLException e) {
			if (conn != null) {
				result = "Error trying to delete film";
				try {
					conn.rollback();
				} catch (SQLException e2) {
					result += "Error trying to rollback";
				}
			}
			return result;
		} finally {
			conn.commit();
		}
		return result;

	}

	public String modifyFilm(Film oldFilm, Film newFilm) throws SQLException {
		String url = "jdbc:mysql://localhost:3306/sdvid?useSSL=false";
		String user = "student";
		String pword = "student";
		String result = null;
		String sql = "update film set title = ?, description = ?, release_year = ?, language_id = ?, length = ?,"
				+ " rating = ?, rental_duration = ?, rental_rate = ?, replacement_cost = ? where id = ?";
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, user, pword);
			conn.setAutoCommit(false);
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, newFilm.getTitle());
			stmt.setString(2, newFilm.getDescription());
			stmt.setInt(3, newFilm.getReleaseYear());
			stmt.setInt(4, newFilm.getLanguageId());
			stmt.setInt(5, newFilm.getLength());
			stmt.setString(6, newFilm.getRating());
			stmt.setInt(7, newFilm.getRentalDuration());
			stmt.setDouble(8, newFilm.getRentalRate());
			stmt.setDouble(9, newFilm.getReplacementCost());
			stmt.setInt(10, oldFilm.getId());
			stmt.executeUpdate();
			result = "Film successfully updated";
		} catch (SQLException e) {
			e.printStackTrace();
			result = "Error trying to update film";
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException e2) {
					result += "Error trying to rollback";
				}
			}
		} finally {
			conn.commit();
		}
		return result;
	}

}
