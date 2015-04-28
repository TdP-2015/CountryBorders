package it.polito.tdp.country.model;

import it.polito.tdp.country.db.CountryDAO;

import java.util.LinkedList;
import java.util.List;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

public class Model {

	private List<Country> countries ;
	
	private SimpleGraph<Country, DefaultEdge> graph ;
	
	public Model() {
		this.countries = new LinkedList<Country>() ;
		this.graph = new SimpleGraph<Country, DefaultEdge>(DefaultEdge.class) ;
	}
	
	public void loadCountries() {
		
		CountryDAO dao = new CountryDAO() ;
		countries.clear();
		countries.addAll( dao.loadAllCountries() ) ; 
		
	}
	
	public void buildGraph() {
		
		Graphs.addAllVertices(this.graph, this.countries) ;
		
		CountryDAO dao = new CountryDAO() ;

		for(Country c1 : graph.vertexSet() ) {
			List<Country> bordering = dao.getBorderingCountries(c1, 1) ;
			
			for( Country c2: bordering ) {
				
				graph.addEdge(c1, c2) ;
				
			}
		}
		
		//System.out.println(graph.toString()) ;
	}
	
	
	public static void main(String[] args) {
		
		Model m = new Model() ;
		
		m.loadCountries();
		
		/*
		for(Country c: m.getCountries()) {
			System.out.println(c.toString()) ;
		}
		*/
		
		m.buildGraph(); 

		System.out.format("Graph: %d vertices, %d edges\n",
				m.getGraph().vertexSet().size(),
				m.getGraph().edgeSet().size()) ;
	}

	public SimpleGraph<Country, DefaultEdge> getGraph() {
		return graph;
	}

	public List<Country> getCountries() {
		return countries;
	}

}
