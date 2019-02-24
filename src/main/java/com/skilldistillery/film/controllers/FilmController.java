package com.skilldistillery.film.controllers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.skilldistillery.film.database.DatabaseAccessor;
import com.skilldistillery.film.entities.Film;

@Controller
public class FilmController {

	@Autowired
	private DatabaseAccessor filmDAO;

//	public void setFilmDAO(DatabaseAccessor filmDAO) {
//		this.filmDAO = filmDAO;
//	}

	@RequestMapping(path = "GetFilmById.do", params = "filmId", method = RequestMethod.GET)
	public ModelAndView getFilmById(int filmId) {
		ModelAndView mv = new ModelAndView();
		Film film = null;
		String result = null;
		try {
			film = filmDAO.findFilmById(filmId);
		} catch (SQLException e) {
			result = "No films found";
		}
		mv.addObject("film", film);
		mv.addObject("result", result);
		mv.setViewName("filmPage.jsp");
		return mv;
	}
	
	@RequestMapping(path = "GetFilmByKeyword.do", params = "keyword", method = RequestMethod.GET)
	public ModelAndView getFilmByKeyword(String keyword) {
		ModelAndView mv = new ModelAndView();
		Film film = null;
		String result = null;
		List<Film> filmList = new ArrayList();
		try {
			filmList = filmDAO.findFilmByKeyword(keyword);
		} catch (SQLException e) {
			result = "No films found";
		}
		mv.addObject("filmList", filmList);
		mv.addObject("result", result);
		mv.setViewName("filmPage");
		return mv;
	}
	
	@RequestMapping(path = "DeleteFilmById.do", params = "filmId", method = RequestMethod.POST)
	public ModelAndView deleteFilmById(int filmId) {
		ModelAndView mv = new ModelAndView();
		Film film = null;
		String result = null;
		try {
			Film filmToDelete = filmDAO.findFilmById(filmId);
			filmDAO.deleteFilm(filmToDelete);
			result = "Film successfully deleted!";
		} catch (SQLException e) {
			// TODO handle this better
			e.printStackTrace();
		}
		mv.addObject("result", result);
		mv.setViewName("filmPage");
		return mv;
	}
//	@RequestMapping(path = "ModifyFilm.do", params = {"filmId", "title", "description", "releaseYear", "languageId", "length", "rating"}, method = RequestMethod.POST)
//	public ModelAndView modifyFilm(int filmId, String title, String description, int releaseYear, int languageId, int length, String rating) {
////TODO: write a bunch of code here
//		mv.addObject("newFilm", newFilm);
//		mv.addObject("result", result);
//		mv.setViewName("filmPage");
//		return mv;
//	}
}
