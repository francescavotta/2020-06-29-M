package it.polito.tdp.imdb.model;

import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {
	
	List<Director> direttori;
	Map<Integer, Director> mapId;
	ImdbDAO dao;
	Graph <Director, DefaultWeightedEdge> grafo;
	
	public Model() {
		dao = new ImdbDAO();
	}
	
	public String creaGrafo(int anno) {
		direttori = dao.listAllDirectors();
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		for(Director d: direttori) {
			mapId.put(d.getId(), d);
		}
		
		//creo i vertici
		List<Director> vertici = dao.getVertici(anno, mapId);
		Graphs.addAllVertices(grafo, vertici);
		
		return String.format("Il grafo è stato creato con %d vertici e %d archi", grafo.vertexSet().size(), grafo.edgeSet().size() );
	}
	

}
