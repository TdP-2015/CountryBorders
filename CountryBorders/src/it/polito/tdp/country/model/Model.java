package it.polito.tdp.country.model;

import it.polito.tdp.country.db.CountryDAO;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.traverse.DepthFirstIterator;

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
			List<Country> bordering = dao.getBorderingCountries(c1, 1, 2006) ;
			
			for( Country c2: bordering ) {
				
				graph.addEdge(c1, c2) ;
				
			}
		}
		
		System.out.println(graph.toString()) ;
	}
	
	
	public List<Country> getRaggiungibili(Country start) {
		
		BreadthFirstIterator<Country, DefaultEdge> visita =
				new BreadthFirstIterator<Country, DefaultEdge>(this.graph, start) ;
		
		List<Country> vicini = new LinkedList<Country>() ;
		
		while( visita.hasNext() ) {
			Country c = visita.next() ;
			vicini.add(c) ;
		}
				
		return vicini ;
		
	}
	
	public Map<Country, Country> getCammini(Country start) {
		
		BreadthFirstIterator<Country, DefaultEdge> visita =
				new BreadthFirstIterator<Country, DefaultEdge>(this.graph, start) ;

		VisitListener listener = new VisitListener(graph) ;
		visita.addTraversalListener( listener );
		
		while( visita.hasNext() ) {
			visita.next() ;
		}
		
		return listener.getFather() ;

		
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
		
		// Quali nazioni confinano con l'Italia?
		Country italia = null;
		for (Country c: m.getCountries()) {
			if(c.getStateAbb().equals("ITA")) {
				italia = c ;
				break ;
			}
		}
		
		List<Country> confinanti = 
				Graphs.neighborListOf(m.getGraph(), italia) ;
		
		System.out.println("STATI CONFINANTI") ;
		for(Country c: confinanti) {
			System.out.println(c.toString()) ;
		}
		
		// Quali nazioni sono raggiungibili via terra dall'Italia?

		List<Country> raggiungibili = 
				m.getRaggiungibili(italia) ;

		System.out.println("STATI RAGGIUNGIBILI VIA TERRA") ;
		for(Country c: raggiungibili) {
			System.out.println(c.toString()) ;
		}

	}

	public SimpleGraph<Country, DefaultEdge> getGraph() {
		return graph;
	}

	public List<Country> getCountries() {
		return countries;
	}

	public List<Country> shortestPath(Country countryStart, Country countryEnd) {
		getCammini(countryStart);

		
		return null;
	}

}
