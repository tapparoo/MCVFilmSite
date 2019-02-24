package com.skilldistillery.film.database;

import java.sql.SQLException;
import java.util.List;

import com.skilldistillery.film.entities.Actor;
import com.skilldistillery.film.entities.Film;

public interface DatabaseAccessor {
  public Film findFilmById(int filmId) throws SQLException;
  public Actor findActorById(int actorId) throws SQLException;
  public List<Actor> findActorsByFilmId(int filmId) throws SQLException;
  public List<Film> findFilmByKeyword(String keyword)throws SQLException;
  public Integer addFilm(Film film) throws SQLException;
  public String deleteFilm(Film film) throws SQLException;
  public String modifyFilm(Film oldFilm, Film newFilm) throws SQLException;
}
