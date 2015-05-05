package it.polito.tdp.country.model;

import java.util.HashMap;
import java.util.Map;

import org.jgrapht.event.ConnectedComponentTraversalEvent;
import org.jgrapht.event.EdgeTraversalEvent;
import org.jgrapht.event.TraversalListener;
import org.jgrapht.event.VertexTraversalEvent;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

public class VisitListener implements TraversalListener<Country, DefaultEdge> {

	private Map<Country,Country> father =
			new HashMap<Country,Country>() ;
	
	public Map<Country, Country> getFather() {
		return father;
	}

	private boolean firstSeen = false ;
	
	private SimpleGraph<Country, DefaultEdge> graph ;
	
	public VisitListener(SimpleGraph<Country, DefaultEdge> graph) {
		this.graph = graph ;
	}
	
	@Override
	public void connectedComponentFinished(ConnectedComponentTraversalEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void connectedComponentStarted(ConnectedComponentTraversalEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void edgeTraversed(EdgeTraversalEvent<Country, DefaultEdge> e) {
		// TODO Auto-generated method stub
		System.out.println(e.getEdge()) ;
		
		DefaultEdge edge = e.getEdge() ;
		
		Country vertexA = graph.getEdgeSource(edge) ;
		Country vertexB = graph.getEdgeTarget(edge) ;
		
		if( ! father.keySet().contains(vertexA) ) {
			father.put(vertexA, vertexB) ;
		} else if( ! father.keySet().contains(vertexB) ) {
			father.put(vertexB, vertexA) ;
		}

	}

	@Override
	public void vertexTraversed(VertexTraversalEvent<Country> e) {
		// TODO Auto-generated method stub
		System.out.println( e.getVertex() ) ;
		
		if(!firstSeen) {
			father.put(e.getVertex(), null) ;
			firstSeen = true ;
		}
		
	}

	@Override
	public void vertexFinished(VertexTraversalEvent<Country> e) {
		// TODO Auto-generated method stub
		
	}

	
}
