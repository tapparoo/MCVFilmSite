package com.skilldistillery.film.controllers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.skilldistillery.film.database.DatabaseAccessor;
import com.skilldistillery.film.entities.Film;

@Controller
public class FilmController {

	@Autowired
	private DatabaseAccessor filmDAO;

	@RequestMapping(path = "FilmModified.do")
	public ModelAndView filmModified(@ModelAttribute("result") final String result) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("/WEB-INF/filmPage.jsp");
		mv.addObject("result", result);
		return mv;
	}

	@RequestMapping(path = "GetFilmById.do", method = RequestMethod.GET)
	public ModelAndView getFilmById(@RequestParam("filmId") int filmId) {
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
		mv.setViewName("/WEB-INF/filmPage.jsp");
		return mv;
	}

	@RequestMapping(path = "GetFilmByKeyword.do", params = "keyword", method = RequestMethod.GET)
	public ModelAndView getFilmByKeyword(String keyword) {
		ModelAndView mv = new ModelAndView();
		String result = null;
		List<Film> filmList = new ArrayList<Film>();
		try {
			filmList = filmDAO.findFilmByKeyword(keyword);
		} catch (SQLException e) {
			result = "No films found";
		}
		mv.addObject("filmList", filmList);
		mv.addObject("result", result);
		mv.setViewName("/WEB-INF/filmPage.jsp");
		return mv;
	}

	@RequestMapping(path = "DeleteFilmById.do", params = "filmId", method = RequestMethod.POST)
	public String deleteFilmById(@RequestParam("filmId") int filmId, RedirectAttributes redir) {
		ModelAndView mv = new ModelAndView();
		String result = null;
		try {
			Film filmToDelete = filmDAO.findFilmById(filmId);
			result = filmDAO.deleteFilm(filmToDelete);
		} catch (SQLException e) {
			result = "Error deleting film";
		}
//		mv.addObject("result", result);
//		mv.setViewName("/WEB-INF/filmPage.jsp");
		redir.addFlashAttribute("result", result);
		redir.addFlashAttribute("filmId", filmId);
		return "redirect:FilmModified.do";
	}

	@RequestMapping(path = "AddFilm.do", method = RequestMethod.POST)
	public String modifyFilm(Film film, RedirectAttributes redir) {
		ModelAndView mv = new ModelAndView();
		String result = null;
		Film newFilm = null;
		try {
			newFilm = new Film(film.getId(), film.getTitle(), null, 0, film.getLanguageId(), 0, null);
			int id = filmDAO.addFilm(newFilm);
			if (film.getId() == 0) {
				newFilm.setId(id);
			}
			result = "Successfully added film";
		} catch (SQLException e) {
			e.printStackTrace();
			result = "Error trying to modify film";
		}
		redir.addFlashAttribute("result", result);
		redir.addFlashAttribute("film", newFilm);
		return "redirect:FilmModified.do";
	}

	@RequestMapping(path = "ModifyFilm.do", params = { "filmId", "title", "description", "releaseYear", "languageId",
			"length", "rating" }, method = RequestMethod.POST)
	public ModelAndView modifyFilm(int filmId, String title, String description, int releaseYear, int languageId,
			int length, String rating) {
		ModelAndView mv = new ModelAndView();
		String result = null;
		Film newFilm = null;
		try {
			newFilm = new Film(0, title, description, releaseYear, languageId, length, rating);
			Film oldFilm = filmDAO.findFilmById(filmId);
			result = filmDAO.modifyFilm(oldFilm, newFilm);
		} catch (Exception e) {
			result = "Error trying to modify film";
		}
		mv.addObject("newFilm", newFilm);
		mv.addObject("result", result);
		mv.setViewName("/WEB-INF/filmPage.jsp");
		return mv;
	}
}
