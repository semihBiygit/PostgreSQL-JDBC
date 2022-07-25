package com.bilgeadam.postgresqltutorial;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class App {

	private final String url = "jdbc:postgresql://localhost/dvdrental";
	private final String user = "postgres";
	private final String password = "asdsaddas";

	public Connection connect() {

		Connection conn = null;

		try {
			conn = DriverManager.getConnection(url, user, password);
			System.out.println("Connected to PostgreSQL Server successfully.");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn;
	}

	public int getActorCount() {

		String sql = "SELECT COUNT(*) FROM actor;";
		int count = 0;

		try (Connection conn = connect();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {
			rs.next();
			count = rs.getInt("count");

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return count;

	}

	public void getActors() {
		String sql = "SELECT actor_id, first_name, last_name FROM actor;";

		try (Connection conn = connect();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {
			displayActor(rs);
		} catch (SQLException e) {
			e.getMessage();
		}

	}

	private void displayActor(ResultSet rs) throws SQLException {
		while (rs.next()) {
			System.out.println(
					rs.getString("actor_id") + "-" + rs.getString("first_name") + "\t" + rs.getString("last_name"));
		}

	}

	private void findActorByID(int actorId) {

		String sql = "SELECT actor_id,first_name,last_name FROM actor WHERE actor_id = ? ;";
		try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setInt(1, actorId);
			ResultSet rs = pstmt.executeQuery();
			displayActor(rs);

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public long insertActor(Actor actor) {
		String sql = "INSERT INTO actor (first_name,last_name) VALUES (?,?);";
		long id = 0;

		Connection conn = connect();
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, actor.getFirstName());
			pstmt.setString(2, actor.getLastName());
			int affectedRows = pstmt.executeUpdate();

			if (affectedRows > 0) {
				try (ResultSet rs = pstmt.getGeneratedKeys()) {
					if (rs.next()) {
						id = rs.getLong(1);
					}
				}

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return id;
	}

	public void insertActors(List<Actor> list) {
		String sql = "INSERT INTO actor (first_name,last_name) VALUES (?,?);";

		try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql);) {
			int count = 0;
			for (Actor actor : list) {
				pstmt.setString(1, actor.getFirstName());
				pstmt.setString(2, actor.getLastName());

				pstmt.addBatch();
				count++;

				if (count % 100 == 0 || count == list.size()) {
					pstmt.executeBatch();
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		App app = new App();
//		app.connect();
//		System.out.println(app.getActorCount());
		app.getActors();
		app.findActorByID(69);
//		app.insertActor(new Actor("Semih", "Biygit"));

	}
}
