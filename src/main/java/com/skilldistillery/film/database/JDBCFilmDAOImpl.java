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
public class JDBCFilmDAOImpl implements DatabaseAccessor {
	private static final String USER = "student";
	private static final String PASS = "student";
	private static final String URL = "jdbc:mysql://localhost:3306/sdvid?useSSL=false";

	// Discover JDBC Driver
	public JDBCFilmDAOImpl() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Film findFilmById(int filmId) {
		Film film = null;
		Connection conn = null;
		
		try {
			conn = DriverManager.getConnection(URL, USER, PASS);
			String sql = "SELECT * FROM film WHERE id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, filmId);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				film = new Film(rs.getInt("id"), rs.getString("title"), rs.getString("description"),
						rs.getInt("release_year"), rs.getInt("language_id"), rs.getInt("length"), rs.getString("rating"),
						findActorsByFilmId(filmId), rs.getInt("rental_duration"), rs.getDouble("rental_rate"),
						rs.getDouble("replacement_cost"));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return film;
	}

	public List<Film> findFilmByKeyword(String keyword) {
		List<Film> filmList = null;
		Connection conn = null;
		
		try {
			conn = DriverManager.getConnection(URL, USER, PASS);
			String sql = "SELECT * FROM film WHERE title LIKE ? OR description LIKE ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, "%" + keyword + "%");
			stmt.setString(2, "%" + keyword + "%");
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				if (filmList == null) {
					filmList = new ArrayList<>();
				}

				filmList.add(new Film(rs.getInt("id"), rs.getString("title"), rs.getString("description"),
						rs.getInt("release_year"), rs.getInt("language_id"), rs.getInt("length"), rs.getString("rating"),
						findActorsByFilmId(rs.getInt("id")), rs.getInt("rental_duration"), rs.getDouble("rental_rate"),
						rs.getDouble("replacement_cost")));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return filmList;
	}

	public Actor findActorById(int actorId) {
		Actor actor = null;
		Connection conn = null;

		try {
			conn = DriverManager.getConnection(URL, USER, PASS);
			String sql = "SELECT * FROM actor WHERE id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, actorId);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				actor = new Actor(rs.getInt("id"), rs.getString("first_name"), rs.getString("last_name"));
			}
			
			rs.close();
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return actor;
	}

	public List<Actor> findActorsByFilmId(int filmId) {
		List<Actor> actorList = null;
		Connection conn = null;
		
		try {
			conn = DriverManager.getConnection(URL, USER, PASS);
			String sql = "SELECT actor.first_name, actor.last_name FROM actor JOIN film_actor ON actor.id = film_actor.actor_id JOIN film ON film_actor.film_id = film.id WHERE film.id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, filmId);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				if (actorList == null) {
					actorList = new ArrayList<>();
				}

				actorList.add(new Actor(rs.getString("first_name"), rs.getString("last_name")));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return actorList;
	}

	public String getCategoryFromId(int filmId) throws SQLException {
		String category = null;

		Connection conn = DriverManager.getConnection(URL, USER, PASS);
		String sql = "SELECT name FROM category JOIN film_category ON film_category.category_id = category.id JOIN film ON film.id = film_category.film_id WHERE film.id = ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setInt(1, filmId);
		ResultSet rs = stmt.executeQuery();

		if (rs.next()) {
			category = rs.getString("name");
		}

		rs.close();
		stmt.close();
		conn.close();

		return category;
	}

	public String getLanguageFromId(int languageId) {
		String language = null;
		Connection conn = null;

		try {
			conn = DriverManager.getConnection(URL, USER, PASS);
			String sql = "SELECT * FROM language WHERE id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, languageId);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				language = rs.getString("name");
			}
			
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return language;
	}

	@Override
	public Integer addFilm(Film film) {
		Connection conn = null;
		String sql = "INSERT INTO film (title, description, release_year, language_id, length, rating, id, rental_duration, rental_rate, replacement_cost) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		int key = 0; // New Film's ID (or 0 if add failed)

		try {
			conn = DriverManager.getConnection(URL, USER, PASS);
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

			if (keys.next()) {
				key = keys.getInt(1);
			}
			
			keys.close();
			st.close();
		} catch (SQLException e) {
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException e2) {
					e2.printStackTrace();
				}
			}
		} finally {
			try {
				conn.commit();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return key;
	}

	@Override
	public String deleteFilm(Film film){
		String result = null;
		Connection conn = null;
		String sql = "DELETE FROM film WHERE id = ?";

		try {
			conn = DriverManager.getConnection(URL, USER, PASS);
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
					e2.printStackTrace();
				}
			}
			return result;
		} finally {
			try {
				conn.commit();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}

	public String modifyFilm(Film oldFilm, Film newFilm) {
		String result = null;
		Connection conn = null;
		String sql = "UPDATE film SET title = ?, description = ?, release_year = ?, language_id = ?, length = ?, rating = ?, rental_duration = ?, rental_rate = ?, replacement_cost = ? WHERE id = ?";
		
		try {
			conn = DriverManager.getConnection(URL, USER, PASS);
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
					e2.printStackTrace();
				}
			}
		} finally {
			try {
				conn.commit();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}
}
