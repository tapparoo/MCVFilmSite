package com.skilldistillery.film.entities;

import java.util.List;

import com.skilldistillery.film.database.JDBCFilmDAOImpl;

public class Film {
	private int id, releaseYear, languageId, length, rentalDuration;
	private double rentalRate, replacementCost;
	private String title, description, rating;
	private List<Actor> actorList;

	public Film() {
	}

	public Film(int id, String title, String description, int releaseYear, int languageId, int length, String rating,
			List<Actor> actorList, int rentalDuration, double rentalRate, double replacementCost) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.releaseYear = releaseYear;
		this.languageId = languageId;
		this.length = length;
		this.rating = rating;
		this.actorList = actorList;
		this.rentalDuration = rentalDuration;
		this.rentalRate = rentalRate;
		this.replacementCost = replacementCost;
	}

	public String toString() {
		StringBuilder filmInfo = new StringBuilder();
		filmInfo.append("ID: " + this.id);
		filmInfo.append("\nTitle: " + this.title);
		try {
			filmInfo.append("\nCategory: " + this.getCategory());
		} catch (Exception e) {
			filmInfo.append("\nCategory: Error retrieving category");
		}
		filmInfo.append("\nDescription: " + this.description);
		filmInfo.append("\nYear: " + this.releaseYear);
		try {
			filmInfo.append("\nLanguage: " + this.getLanguage());
		} catch (Exception e) {
			filmInfo.append("\nLanguage: Error retrieving language");
		}
		filmInfo.append("\nLength: " + this.length + " minutes");
		filmInfo.append("\nRating: " + this.rating);
		int i = 0;
		if (actorList != null) {
			for (Actor actor : actorList) {

				if (i == 0) {
					filmInfo.append("\nCast: ");
				} else
					filmInfo.append(", ");

				filmInfo.append(actor);
				i++;
			}
		}
		filmInfo.append("\nRental duration: " + this.rentalDuration);
		filmInfo.append("\nRental rate: " + this.rentalRate);
		filmInfo.append("\nReplacement cost: $" + this.replacementCost);
		return filmInfo.toString();
	}

	public String getLanguage() {
		JDBCFilmDAOImpl dao = new JDBCFilmDAOImpl();
		String language = dao.getLanguageFromId(this.languageId);
		return language;
	}

	public String getCategory() {
		JDBCFilmDAOImpl dao = new JDBCFilmDAOImpl();
		String category = dao.getCategoryFromId(this.id);
		return category;
	}

	public List<Actor> getActorList() {
		return actorList;
	}

	public void setActorList(List<Actor> actorList) {
		this.actorList = actorList;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getReleaseYear() {
		return releaseYear;
	}

	public void setReleaseYear(int releaseYear) {
		this.releaseYear = releaseYear;
	}

	public int getLanguageId() {
		return languageId;
	}

	public void setLanguageId(int languageId) {
		this.languageId = languageId;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public double getRentalRate() {
		return rentalRate;
	}

	public int getRentalDuration() {
		return rentalDuration;
	}

	public double getReplacementCost() {
		return replacementCost;
	}
	public void setRentalRate(double rate) {
		this.rentalRate = rate;
	}
	
	public void setRentalDuration(int duration) {
		this.rentalDuration = duration;
	}
	
	public void setReplacementCost(double cost) {
		this.replacementCost = cost;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + id;
		result = prime * result + languageId;
		result = prime * result + length;
		result = prime * result + ((rating == null) ? 0 : rating.hashCode());
		result = prime * result + releaseYear;
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Film other = (Film) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (id != other.id)
			return false;
		if (languageId != other.languageId)
			return false;
		if (length != other.length)
			return false;
		if (rating == null) {
			if (other.rating != null)
				return false;
		} else if (!rating.equals(other.rating))
			return false;
		if (releaseYear != other.releaseYear)
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}

}