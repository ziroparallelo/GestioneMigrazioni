package it.polito.tdp.borders.model;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;

public class Simulatore {
	
	//1. CODA

	private PriorityQueue<Event> queue;
	
	//2. Parametri 
	
	private int nInizialeMigranti;
	private Country nazioneIniziale;
	
	//3. Output della simulazione
	
	private int nPassi;
	private Map<Country, Integer> persone;
	
	//4. Stato del mondo

	private Graph<Country, DefaultEdge> grafo;
	//Map persone (è l'informazione su cui lui deve lavorare ogni volta)

	public Simulatore(Graph<Country, DefaultEdge> grafo) {
		super();
		this.grafo = grafo;
	}
	
	public void init(Country partenza, int migranti) {
		
		this.nazioneIniziale = partenza;
		this.nInizialeMigranti = migranti;
		
		//inizializzare la struttura dati per tenere traccia dei numeri di stanziali
		//in ogni stato per ogni step
		
//		Inizializzandola qui sono sicuro che quando l'utente crea una nuova simulazione
//		non riprenderò i vecchi dati
		this.persone = new HashMap<Country, Integer>();
		for(Country c : this.grafo.vertexSet()) {
			this.persone.put(c, 0);
		}
		
		this.queue = new PriorityQueue<>();
		
		this.queue.add(new Event(1, 
				this.nazioneIniziale, 
				this.nInizialeMigranti));
		
	}
	
	public void run() {
		while (!this.queue.isEmpty()) {
			Event e = this.queue.poll();
//			System.out.println(e);
			processEvent(e);
		}
	}

	private void processEvent(Event e) {
		
		//Ho solo un tipo di evento, non ho bisogno di uno switch
		
		int stanziali = e.getPersone() / 2; //(arrotonda per difetto)
		int migranti = e.getPersone() - stanziali;
		
		int confinanti = this.grafo.degreeOf(e.getNazione());
		int gruppiMigranti = migranti / confinanti;
		
		stanziali += migranti % confinanti;
		
		
		this.persone.put(e.getNazione(),
				this.persone.get(e.getNazione()) + stanziali);
		
		//Qui aggiorno sempre il numero di passi
		//Devo creare dei getter per passi e persone(map)
		this.nPassi = e.getTime();
		
		
		if(gruppiMigranti != 0) {
		for(Country vicino : 
			Graphs.neighborListOf(this.grafo, e.getNazione())) {
			this.queue.add(new Event(e.getTime()+1, vicino, gruppiMigranti));
		}
		}
	}
	
	public int getnPassi() {
		return nPassi;
	}

	public Map<Country, Integer> getPersone() {
		return persone;
	}
	
	
	
	
	
	
	
	
	
	
	
}
