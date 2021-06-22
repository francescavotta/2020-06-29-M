package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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
	List<Director> vertici;
	
	public Model() {
		dao = new ImdbDAO();
	}
	
	public String creaGrafo(int anno) {
		direttori = dao.listAllDirectors();
		mapId = new HashMap<Integer, Director>();
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		for(Director d: direttori) {
			mapId.put(d.getId(), d);
		}
		
		//creo i vertici
		vertici = dao.getVertici(anno, mapId);
		Graphs.addAllVertices(grafo, vertici);
		
		//creo gli archi
		for(Arco a : dao.getArchi(anno, mapId, vertici)) {
			if(grafo.containsEdge(a.getD1(), a.getD2())) {
				DefaultWeightedEdge e = grafo.getEdge(a.getD1(), a.getD2());
				int peso = (int) grafo.getEdgeWeight(e);
				//peso += a.getPeso();
				a.setPeso(peso);
				grafo.setEdgeWeight(e, peso);	
			}else {
				Graphs.addEdge(grafo, a.getD1(), a.getD2(), a.getPeso());
			}
		}
		return String.format("Il grafo è stato creato con %d vertici e %d archi \n", grafo.vertexSet().size(), grafo.edgeSet().size() );
	}

	public List<Director> getVertici() {
		
		return vertici;
	}
	
	public List<Arco> getAdiacenti(Director d){
		List <Arco> result = new ArrayList<>();
		
		for(Director dd: Graphs.neighborListOf(grafo, d)) {
			DefaultWeightedEdge e = grafo.getEdge(d, dd);
			
			Arco arco = new Arco(d, dd, (int) grafo.getEdgeWeight(e) );
			result.add(arco);
		}
		Collections.sort(result);
		return result;
		
	}
	

}
