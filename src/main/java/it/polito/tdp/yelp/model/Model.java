package it.polito.tdp.yelp.model;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;
import it.polito.tdp.yelp.db.YelpDao;


public class Model {
	private List<String> city = new ArrayList<String>();
	private List<Business> businesses;
	private YelpDao dao = new YelpDao();
	private Graph<Business, DefaultWeightedEdge> grafo;
	private List<Business> listaMigliore;
	
	public List<String> getCity() {
		this.city.addAll(this.dao.getCity());
		return this.city;
	}
	
	public String creaGrafo(String city) {
		businesses = dao.getAllBusiness(city);
		this.grafo = new SimpleWeightedGraph<Business, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(this.grafo, this.businesses);
		
		for(Business b1 : businesses ) {
			for(Business b2: businesses) {
				if(!b1.equals(b2)) {
					double peso = LatLngTool.distance(new LatLng(b1.getLatitude(), b1.getLongitude()), new LatLng(b2.getLatitude(), b2.getLongitude()), LengthUnit.KILOMETER);
					Graphs.addEdge(this.grafo, b1, b2, peso);
				}
			}
		}
		return "Grafo creato con "+this.grafo.vertexSet().size()+" vertici e "+this.grafo.edgeSet().size()+" archi\n";
	}
	
	public List<Business> getBusinesses(){
		return this.businesses;
	}
	
	public String getLocaleDistanza(Business scelto){
		List<Business> vicini = Graphs.neighborListOf(this.grafo, scelto); //perchè non è orientato
		String s="";
		double distanza=0;
		for(Business b: vicini) {	
			double dis = this.grafo.getEdgeWeight(this.grafo.getEdge(b, scelto));
			if(dis>distanza) {
				distanza=dis;
				s="LOCALE PIU' DISTANTE:\n"+b.getBusinessName()+" = "+dis;
			}
		}
		return s;
		}

//	public List<Business> cercaLista(Business b1, Business b2, double n) {//lancio ricorsione qui
//		this.listaMigliore = new ArrayList<Business>();
//		List<Business> parziale = new ArrayList<Business>();
//		List<Business> locali = new ArrayList<Business>(this.businesses);
//		locali.remove(b1);
//		locali.remove(b2);
//		parziale.add(b1);
//		cerca(parziale, locali, n);
//		listaMigliore.add(b2);
//		return listaMigliore;
//	}
//
//	private void cerca(List<Business> parziale, List<Business> locali, double n) {
//		//controllo soluzione migliore appena lanciato e guardo se la nuova parziale è meglio di quella migliore che abbiamo
//		if(parziale.size()>listaMigliore.size()) {
//			//soluzione migliore trovata
//			listaMigliore = new ArrayList<Business>(parziale);			
//		}
//		
//		for(Business b : locali) {
//			if(b.getStars()>n && !parziale.contains(b)) {
//				parziale.add(b);
//				cerca(parziale, locali, n);
//				parziale.remove(parziale.size()-1);
//			}
//		}
//		
//	} 
	
	public List<Business> cercaLista(Business b1, Business b2, double n) {//lancio ricorsione qui
		this.listaMigliore = new ArrayList<Business>();
		List<Business> parziale = new ArrayList<Business>();
		parziale.add(b1);
		cerca(parziale, b1, b1, b2, n);
		listaMigliore.add(b2);
		return listaMigliore;
	}

	private void cerca(List<Business> parziale, Business scelto, Business b1, Business b2, double n) {
		//controllo soluzione migliore appena lanciato e guardo se la nuova parziale è meglio di quella migliore che abbiamo
		if(parziale.size()>listaMigliore.size()) {
			//soluzione migliore trovata
			listaMigliore = new ArrayList<Business>(parziale);			
		}
		//aggiunto questa parte dove creo la lista basandomi sui vicini del business dove mi trovo
		List<Business> locali = new ArrayList<Business>(Graphs.neighborListOf(this.grafo, scelto));
		locali.remove(b1);
		locali.remove(b2);
		
		for(Business b : locali) {
			if(b.getStars()>=n && !parziale.contains(b)) {
				parziale.add(b);
				cerca(parziale, b, b1, b2, n);
				parziale.remove(parziale.size()-1);
			}
		}
	}
	
	public double getKM() {
		List<Business> bus = new ArrayList<>(listaMigliore);
		double km=0;
		int a=0;
		int b=1;
		while(b<bus.size()) {
			km = km+ this.grafo.getEdgeWeight(this.grafo.getEdge(bus.get(a), bus.get(b)));
			a++;
			b++;
		}
		return km;
	}
	
}
