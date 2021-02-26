package com.skilldistillery.filmquery.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.skilldistillery.filmquery.entities.Actor;
import com.skilldistillery.filmquery.entities.Film;

public class DatabaseAccessorObject implements DatabaseAccessor {
	
	private static final String URL = "jdbc:mysql://localhost:3307/sdvid?useSSL=false";
	private static String username = "student";
	private static String password = "student";
	
	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (Exception e) {
			System.err.println(e);
		}
	}
	
	@Override
	public Film findFilmById(int filmId) {
		Film film = null;
		String  sqltxt = "SELECT * FROM film WHERE id = ?";
		
		try (Connection conn=DriverManager.getConnection(URL,username,password);
				PreparedStatement prepStmt = conn.prepareStatement(sqltxt); ) {
			prepStmt.setInt(1, filmId);
			ResultSet rs = prepStmt.executeQuery(); 
			if (rs.next()) {
				film = new Film(rs.getInt("id"), rs.getInt("language_id"), rs.getInt("rental_duration"), rs.getInt("length"), rs.getDouble("rental_rate"), 
						rs.getDouble("replacement_cost"), rs.getString("title"),rs.getString("description"), rs.getString("rating"), rs.getString("special_features"), rs.getInt("release_year"), findActorsByFilmId(filmId));
			}
		} catch (SQLException e) {
			System.err.println(e);
		}
		return film;
	}

	@Override
	public Actor findActorById(int actorId) {
		Actor actor = null;
		String  sqltxt = "SELECT * FROM actor WHERE id = ?";
		
		try (Connection conn=DriverManager.getConnection(URL,username,password);
				PreparedStatement prepStmt = conn.prepareStatement(sqltxt); ) {
			prepStmt.setInt(1, actorId);
			ResultSet rs = prepStmt.executeQuery(); 
			if (rs.next()) {
				actor = new Actor(rs.getInt("id"), rs.getString("first_name"), rs.getString("last_name"));
			}
		} catch (SQLException e) {
			System.err.println(e);
		}
		return actor;
	}

	@Override
	public List<Actor> findActorsByFilmId(int filmId) {
		List<Actor> actorsList = new ArrayList<>();
		String  sqltxt = "SELECT actor.id, actor.first_name, actor.last_name "
				+ " FROM actor JOIN film_actor ON actor.id = film_actor.actor_id "
				+ " WHERE film_actor.film_id = ?";
		
		try (Connection conn=DriverManager.getConnection(URL,username,password);
				PreparedStatement prepStmt = conn.prepareStatement(sqltxt); ) {
			prepStmt.setInt(1, filmId);
			ResultSet rs = prepStmt.executeQuery(); 
			while (rs.next()) {
				actorsList.add(findActorById(rs.getInt("id")));
			}
		} catch (SQLException e) {
			System.err.println(e);
		}
		return actorsList;
	}

}
