package it.polito.tdp.country.model;

import it.polito.tdp.country.db.CountryDAO;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.traverse.DepthFirstIterator;

public class Model {

	private List<Country> countries ;
	
	private SimpleGraph<Country, DefaultEdge> graph ;
	
	/**
	 * Create a new model
	 */
	public Model() {
		this.countries = new LinkedList<Country>() ;
		this.graph = new SimpleGraph<Country, DefaultEdge>(DefaultEdge.class) ;
	}
	
	/**
	 * Get an instance of the Country Bordering Graph
	 * @return the graph
	 */
	public SimpleGraph<Country, DefaultEdge> getGraph() {
		return graph;
	}

	/**
	 * Return the list of all countries
	 * @return the countries
	 */
	public List<Country> getCountries() {
		return countries;
	}

	/**
	 * Load all countries from the database. Must be called before other methods.
	 */
	public void loadCountries() {
		
		CountryDAO dao = new CountryDAO() ;
		countries.clear();
		countries.addAll( dao.loadAllCountries() ) ; 
		
	}
	
	/**
	 * Build the connectivity graph among countries. Must be called before
	 * any method that needs connectivity information.
	 */
	public void buildGraph() {
		
		Graphs.addAllVertices(this.graph, this.countries) ;
		
		CountryDAO dao = new CountryDAO() ;

		for(Country c1 : graph.vertexSet() ) {
			List<Country> bordering = dao.getBorderingCountries(c1, 1, 2006) ;
			
			for( Country c2: bordering ) {
				
				graph.addEdge(c1, c2) ;
				
			}
		}
		
		// System.out.println(graph.toString()) ;
	}
	
	
	/**
	 * Calcola tutti gli stati raggiungibili (via terra) dallo stato specificato
	 * @param start lo stato di partenza
	 * @return la lista di stati raggiungibili (in ordine di BFV)
	 */
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
	
	/**
	 * Calcola l'albero della visita in ampiezza del grafo
	 * @param start vertice di partenza
	 * @return l'albero BFV, in cui ad ogni vertice raggiungibile è associato il vertice che lo precede nell'albero
	 */
	public Map<Country, Country> getCammini(Country start) {
		
		// Esplora il grafo in ampiezza, partendo dal nodo start
		BreadthFirstIterator<Country, DefaultEdge> visita =
				new BreadthFirstIterator<Country, DefaultEdge>(this.graph, start) ;

		// regista il listener (che terrà traccia dell'albero di visita)
		VisitListener listener = new VisitListener(graph) ;
		visita.addTraversalListener( listener );
		
		// esegui la visita del grafo (senza fare nulla, farà tutto il listener)
		while( visita.hasNext() ) {
			visita.next() ;
		}
		
		// restituisci il grafo di visita
		return listener.getFather() ;
	}
	
	/**
	 * Calcola il cammino più breve tra due {@link Country} e lo restituisce sotto forma di lista.
	 * Nel caso in cui non esista un cammino, ritorna una lista vuota (zero elementi). 
	 * @param countryStart la Country di partenza
	 * @param countryEnd la Country di arrivo
	 * @return la lista di Country corrispondente al cammino più breve. 
	 * Nel caso in cui il cammino non esista, è una lista vuota. Altrimenti, il primo elemento della lista
	 * è sempre countryStart, e l'ultimo è sempre countryEnd.
	 */
	public List<Country> shortestPathOld(Country countryStart, Country countryEnd) {
		Map<Country, Country> father = getCammini(countryStart);

		if( !father.keySet().contains(countryEnd))
			return null ; // il camminio non esiste (non raggiungibile)

		List<Country> path = new LinkedList<Country>() ;
		
		Country c = countryEnd ;
		while (c!=null) {
			path.add(0,c) ;
			c = father.get(c) ;
		}
		return path;
	}
	
	public List<Country> shortestPath(Country countryStart, Country countryEnd) { 
		DijkstraShortestPath<Country, DefaultEdge> dijkstra =
				new DijkstraShortestPath<Country, DefaultEdge>(graph, countryStart, countryEnd) ;
		GraphPath<Country, DefaultEdge> path = dijkstra.getPath() ;
		
		if(path==null)
			return null ;
		
		List<Country> vertici = Graphs.getPathVertexList(path) ;
		
		return vertici;

		/*
		List<Country> vertici = new LinkedList<Country>() ;
		

		Country previous = path.getStartVertex() ;
		vertici.add(previous) ;
		
		for( DefaultEdge e: path.getEdgeList() ) {
			
			Country next = 
					Graphs.getOppositeVertex(path.getGraph(), e, previous) ;
			vertici.add(next) ;
			
			previous = next ;	
		}
		
		return vertici ;
			
		*/
		
	}


	/**
	 * Some test methods for the class Model
	 * @param args
	 */
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

	
}
