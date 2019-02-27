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
	public ModelAndView filmModified(@ModelAttribute("result") final String result, @ModelAttribute("film") final Film film) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("/WEB-INF/filmPage.jsp");
		mv.addObject("result", result);
		mv.addObject("film", film);
		return mv;
	}

	@RequestMapping(path = "GetFilmById.do", method = RequestMethod.GET)
	public ModelAndView getFilmById(@RequestParam("filmId") int filmId) {
		ModelAndView mv = new ModelAndView();
		Film film = null;
		String result = null;
		try {
			film = filmDAO.findFilmById(filmId);
			if(film == null) {
				result = "No films found with ID: " + filmId;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		mv.addObject("film", film);
		mv.addObject("result", result);
		mv.setViewName("/WEB-INF/filmPage.jsp");
		return mv;
	}

	@RequestMapping(path = "GetFilmByKeyword.do", method = RequestMethod.GET)
	public ModelAndView getFilmByKeyword(@RequestParam("keyword") String keyword) {
		ModelAndView mv = new ModelAndView();
		String result = null;
		List<Film> filmList = null;
		System.out.println(filmDAO);
		try {
			filmList = filmDAO.findFilmByKeyword(keyword);
			if(filmList == null) {
				result = "No films found with keyword: " + keyword;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		mv.addObject("filmList", filmList);
		mv.addObject("result", result);
		mv.setViewName("/WEB-INF/filmPage.jsp");
		return mv;
	}

	@RequestMapping(path = "DeleteFilmById.do", method = RequestMethod.POST)
	public String deleteFilmById(@RequestParam("id") int filmId, RedirectAttributes redir) {
		String result = null;
		try {
			Film filmToDelete = filmDAO.findFilmById(filmId);
			result = filmDAO.deleteFilm(filmToDelete);
		} catch (SQLException e) {
			result = "Error deleting film";
		}
		redir.addFlashAttribute("result", result);
		redir.addFlashAttribute("filmId", filmId);
		return "redirect:FilmModified.do";
	}

	@RequestMapping(path = "AddFilm.do", method = RequestMethod.POST)
	public String addFilm(Film film, RedirectAttributes redir) {
		String result = null;
		Film newFilm = null;
		try {
			newFilm = new Film(film.getId(), film.getTitle(), null, 0, film.getLanguageId(), 0, null, null, 0, 0, 0);
			int id = filmDAO.addFilm(newFilm);
			if (film.getId() == 0) {
				newFilm.setId(id);
			}
			redir.addFlashAttribute("film", filmDAO.findFilmById(id));
			result = "Successfully added film";
		} catch (SQLException e) {
			e.printStackTrace();
			result = "Error trying to modify film";
		}
		redir.addFlashAttribute("result", result);
		return "redirect:FilmModified.do";
	}

	@RequestMapping(path = "ModifyFilm.do", method = RequestMethod.POST)
	public String modifyFilm(Film film, RedirectAttributes redir) {
		String result = null;
		Film newFilm = null;
		try {
			newFilm = new Film(film.getId(), film.getTitle(), film.getDescription(), film.getReleaseYear(), film.getLanguageId(), film.getLength(), film.getRating(), film.getActorList(), film.getRentalDuration(), film.getRentalRate(), film.getReplacementCost());
			Film oldFilm = filmDAO.findFilmById(film.getId());
			result = filmDAO.modifyFilm(oldFilm, newFilm);
		} catch (SQLException e) {
			e.printStackTrace();
			result = "Error trying to modify film";
		}
		redir.addFlashAttribute("result", result);
		redir.addFlashAttribute("film", newFilm);
		return "redirect:FilmModified.do";
	}
}
