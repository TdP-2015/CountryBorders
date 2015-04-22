package it.polito.tdp.country.db;

import it.polito.tdp.country.model.Country;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class CountryDAO {
	
	public List<Country> getCountries() {
		String sql = 
				"SELECT CCode, StateAbb, StateNme " +
				"FROM country " +
				"ORDER BY StateNme" ;
		
		Connection conn = DBConnect.getConnection();
		
		try {

			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			
			List<Country> countries = new LinkedList<Country>() ;

			
			while (res.next()) { 
				Country c = new Country(
						res.getInt("CCode"),
						res.getString("StateAbb"),
						res.getString("StateNme"));
				
				countries.add(c) ;
			}
			
			res.close();
			st.close() ;
			conn.close();

			return countries;

		} catch (SQLException e) {
			return null;
		}
	}
	
	public List<Country> getConnectedCountries(Country c1) {
		
		String sql = 
				"SELECT CCode, StateAbb, StateNme, contiguity.year " +
				"FROM country, contiguity " +
				"WHERE contiguity.state1no = ? "+
				"AND country.CCode = contiguity.state2no " +
				"ORDER BY StateNme" ;

		Connection conn = DBConnect.getConnection();
		
		try {

			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, c1.getcCode());
			
			ResultSet res = st.executeQuery();
			
			List<Country> countries = new LinkedList<Country>() ;

			
			while (res.next()) { 
				Country c2 = new Country(
						res.getInt("CCode"),
						res.getString("StateAbb"),
						res.getString("StateNme"));
				
				countries.add(c2) ;
			}
			
			res.close();
			st.close() ;
			conn.close();

			return countries;

		} catch (SQLException e) {
			return null;
		}

		
	}

}
