package it.polito.tdp.country.db;

import it.polito.tdp.country.model.Country;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class CountryDAO {
	
	public List<Country> loadAllCountries() {
		
		String sql = 
				"SELECT ccode,StateAbb,StateNme " +
				"FROM country " +
				"ORDER BY StateAbb " ;

		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet rs = st.executeQuery() ;
			
			List<Country> list = new LinkedList<Country>() ;
			
			while( rs.next() ) {
				
				Country c = new Country(
						rs.getInt("ccode"),
						rs.getString("StateAbb"), 
						rs.getString("StateNme")) ;
				
				list.add(c) ;
			}
			
			conn.close() ;
			
			return list ;
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null ;
	}
	
	public List<Country> getBorderingCountries(Country c, int contType, int year) {
		String sql = 
				"SELECT CCode, StateAbb, StateNme " +
				"FROM country, contiguity " +
				"WHERE country.CCode = contiguity.state2no " +
				"AND contiguity.state1no = ? " +
				"AND contiguity.conttype <= ? " +
				"AND contiguity.year = ? ";

		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			st.setInt(1, c.getcCode()) ;
			st.setInt(2, contType) ;
			st.setInt(3, year);
			
			ResultSet rs = st.executeQuery() ;
			
			List<Country> list = new LinkedList<Country>() ;
			
			while( rs.next() ) {
				
				Country c2 = new Country(
						rs.getInt("ccode"),
						rs.getString("StateAbb"), 
						rs.getString("StateNme")) ;
				
				list.add(c2) ;
			}
			
			conn.close() ;
			
			return list ;
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null ;
	}

}
