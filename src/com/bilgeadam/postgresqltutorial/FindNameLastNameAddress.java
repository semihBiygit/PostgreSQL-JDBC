package com.bilgeadam.postgresqltutorial;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
	
	public class FindNameLastNameAddress {
		private final String url = "jdbc:postgresql://localhost/dvdrental";
		private final String user = "postgres";
		private final String password = "asdsaddas";
		
	public Connection connect() {
		
			Connection conn= null;
			
			try {
				conn=DriverManager.getConnection(url, user, password);
				System.out.println("Connected to PostgreSQL server succesfully");
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			return conn;
			}
		
		
		private void findInner(int customer_id) {
			String sql = "select first_name, last_name, address from customer inner join address on customer.address_id = address.address_id where customer_id= ? ;";
			try(Connection con =connect();
					PreparedStatement pstmt = con.prepareStatement(sql)){
				
				pstmt.setInt(1, customer_id);
				ResultSet rs = pstmt.executeQuery();
				displayfind(rs);
				
			} catch (SQLException ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}
			
		}
		private void displayfind(ResultSet rs) throws SQLException {
			while(rs.next()) {
				System.out.println(rs.getString("first_name")+ "-" 
			+ rs.getString("last_name") + "\t" 
			+ rs.getString("address"));
			}
			
		}
		

		public static void main(String[] args) {
			FindNameLastNameAddress find = new FindNameLastNameAddress();
			find.findInner(1);

		}

	}
