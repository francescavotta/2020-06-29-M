package it.polito.tdp.imdb.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.imdb.model.Actor;
import it.polito.tdp.imdb.model.Arco;
import it.polito.tdp.imdb.model.Director;
import it.polito.tdp.imdb.model.Movie;

public class ImdbDAO {
	
	public List<Actor> listAllActors(){
		String sql = "SELECT * FROM actors";
		List<Actor> result = new ArrayList<Actor>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Actor actor = new Actor(res.getInt("id"), res.getString("first_name"), res.getString("last_name"),
						res.getString("gender"));
				
				result.add(actor);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Movie> listAllMovies(){
		String sql = "SELECT * FROM movies";
		List<Movie> result = new ArrayList<Movie>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Movie movie = new Movie(res.getInt("id"), res.getString("name"), 
						res.getInt("year"), res.getDouble("rank"));
				
				result.add(movie);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public List<Director> listAllDirectors(){
		String sql = "SELECT * FROM directors";
		List<Director> result = new ArrayList<Director>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Director director = new Director(res.getInt("id"), res.getString("first_name"), res.getString("last_name"));
				
				result.add(director);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Director> getVertici(int anno, Map<Integer, Director> mapId) {
		String sql = "SELECT DISTINCT md.director_id "
				+ "FROM movies m, movies_directors md "
				+ "WHERE m.id = md.movie_id AND "
				+ "m.year = ?";
		List<Director> result = new ArrayList<Director>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				Director d = mapId.get(res.getInt("md.director_id"));
				if(d != null)
					result.add(d);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}
	
	public List<Arco> getArchi(int anno, Map<Integer, Director> mapId, List<Director> vertici){
		String sql = "SELECT DISTINCT md.director_id,  md2.director_id, COUNT(*) AS pesoParziale "
				+ "FROM movies m, movies_directors md, movies_directors md2, movies m2, roles r1, roles r2 "
				+ "WHERE "
				+ "m.year = ? AND m2.year = ? AND "
				+ "m.id = md.movie_id AND "
				+ "m.id = r1.movie_id AND "
				+ "m2.id = r2.movie_id AND "
				+ "m2.id = md2.movie_id AND "
				+ "md.director_id <> md2.director_id AND "
				+ "r1.actor_id = r2.actor_id AND "
				+ "r1.movie_id = md.movie_id AND "
				+ "r2.movie_id = md2.movie_id "
				+ "GROUP BY md.director_id, md2.director_id";
		
		List<Arco> result = new ArrayList<Arco>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			st.setInt(2, anno);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				Director d1 = mapId.get(res.getInt("md.director_id"));
				Director d2 = mapId.get(res.getInt("md2.director_id"));
				if(d1 != null && d2!=null && vertici.contains(d1) && vertici.contains(d2)) {
					Arco a = new Arco(d1,d2, res.getInt("pesoParziale"));
					result.add(a);
				}
	
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	
	
	
}
