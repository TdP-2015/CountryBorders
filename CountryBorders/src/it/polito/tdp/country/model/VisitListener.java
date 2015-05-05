package it.polito.tdp.country.model;

import java.util.HashMap;
import java.util.Map;

import org.jgrapht.event.ConnectedComponentTraversalEvent;
import org.jgrapht.event.EdgeTraversalEvent;
import org.jgrapht.event.TraversalListener;
import org.jgrapht.event.VertexTraversalEvent;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

/**
 * Listener per il calcolo dell'albero di visita.
 * 
 * @author Fulvio Corno
 *
 */
public class VisitListener implements TraversalListener<Country, DefaultEdge> {

	/**
	 * La relazione vertice->vertice che registra l'albero di visita (memorizzato
	 * al rovescio, dai figli verso la radice)
	 */
	private Map<Country,Country> father =
			new HashMap<Country,Country>() ;
	

	/**
	 * È già stato visitato almeno un vertice?
	 */
	private boolean firstSeen = false ;
	
	/**
	 * Il grafo sul quale viene effettuata la visita
	 */
	private SimpleGraph<Country, DefaultEdge> graph ;
	
	/**
	 * Inizializza il listener per una nuova visita
	 * @param graph Il grafo su cui è definito l'iteratore
	 */
	public VisitListener(SimpleGraph<Country, DefaultEdge> graph) {
		this.graph = graph ;
	}
	
	/**
	 * Ritorna la Map che codifica l'albero di visita
	 * @return la relazione vertice figlio -> vertice padre
	 */
	public Map<Country, Country> getFather() {
		return father;
	}

	// Unused
	@Override
	public void connectedComponentFinished(ConnectedComponentTraversalEvent e) {		
	}

	// Unused
	@Override
	public void connectedComponentStarted(ConnectedComponentTraversalEvent e) {		
	}

	/**
	 * Analizza gli archi che vengono progressivamente attraversati dall'algoritmo di visita.
	 * Per ogni arco, determina se uno dei due estremi è un vertice "nuovo" (non ancora visitato),
	 * ed in caso affermativo salva l'informazione che tale vertice è stato "scoperto" a partire
	 * dal vertice che si trova all'estremo opposto dell'arco.
	 * 
	 * Dato una arco [A,B]:
	 *  - se sia A che B sono già stati visitati, non fare nulla
	 *  - se A è nuovo e B è già stato visitato
	 *  	allora ricorda che A si raggiunge da B (e quindi father(A)=B)
	 *  - se B è nuovo e A è già stato visitato
	 *  	allora ricorda che B si raggiunge da A (e quindi father(B)=A)
	 *  - non può succedere che A e B siano entrambi nuovi
	 */
	@Override
	public void edgeTraversed(EdgeTraversalEvent<Country, DefaultEdge> e) {
		// System.out.println(e.getEdge()) ;
		
		DefaultEdge edge = e.getEdge() ;
		
		Country vertexA = graph.getEdgeSource(edge) ;
		Country vertexB = graph.getEdgeTarget(edge) ;
		
		if( ! father.keySet().contains(vertexA) ) {
			father.put(vertexA, vertexB) ;
		} else if( ! father.keySet().contains(vertexB) ) {
			father.put(vertexB, vertexA) ;
		}

	}

	/**
	 * Registra il primo vertice visitato, in modo che venga inizializzata
	 * la mappa 'father' con la conoscenza della radice dell'albero di visita
	 */
	@Override
	public void vertexTraversed(VertexTraversalEvent<Country> e) {
		//System.out.println( e.getVertex() ) ;
		
		if(!firstSeen) {
			father.put(e.getVertex(), null) ;
			firstSeen = true ;
		}
	}

	// Unused
	@Override
	public void vertexFinished(VertexTraversalEvent<Country> e) {		
	}

	
}
