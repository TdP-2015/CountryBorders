package it.polito.tdp.country.model;

import it.polito.tdp.country.db.CountryDAO;

import java.util.LinkedList;
import java.util.List;

import org.jgrapht.Graphs;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

public class Model {
	
	private List<Country> countries ;
	
	private UndirectedGraph<Country, DefaultEdge> connectivity ;
	
	/**
	 * @return the connectivity
	 */
	public UndirectedGraph<Country, DefaultEdge> getConnectivity() {
		return connectivity;
	}

	public Model() {
		this.countries = new LinkedList<Country>() ;
		
		this.connectivity = new SimpleGraph<Country, DefaultEdge>(DefaultEdge.class) ;
		
	}

	/**
	 * @return the countries
	 */
	public List<Country> getCountries() {
		return countries;
	}

	public void loadCountries() {
		CountryDAO dao = new CountryDAO() ;
		countries.clear();
		countries.addAll(dao.getCountries()) ;
	}
	
	public void buildGraph() {
		
		CountryDAO dao = new CountryDAO() ;

		Graphs.addAllVertices(connectivity, countries) ;
		
		for(Country c: countries) {
			List<Country> others = dao.getConnectedCountries(c) ;
			
			for( Country c2: others ) {
				connectivity.addEdge(c, c2) ;
			}
		}
		
		System.out.println(connectivity.toString()) ; 
		
	}
	
	public static void main(String [] args) {
		Model m = new Model() ;
		
		m.loadCountries();
		
		/*for( Country c: m.getCountries() ) {
			System.out.println(c.toString()) ;
		}*/
		
		m.buildGraph();
	}
	
}
