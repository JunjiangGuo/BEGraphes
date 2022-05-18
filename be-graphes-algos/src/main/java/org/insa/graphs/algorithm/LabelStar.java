package org.insa.graphs.algorithm;


import org.insa.graphs.algorithm.shortestpath.ShortestPathData;
import org.insa.graphs.model.*;

public class LabelStar extends Label implements Comparable<Label>{
	private float inf;

	public LabelStar(Node noeud, ShortestPathData data) {
		super(noeud);

		if (data.getMode() == AbstractInputData.Mode.LENGTH) {
			this.inf = (float)Point.distance(noeud.getPoint(),data.getDestination().getPoint());
		}
		else {
			int vitesse = data.getGraph().getGraphInformation().getMaximumSpeed();
			this.inf = (float)Point.distance(noeud.getPoint(),data.getDestination().getPoint())/(vitesse*1000.0f/3600.0f);
		}
	}

	
	/* Renvoie le coût de l'origine jusqu'au noeud + coût à vol d'oiseau du noeud jusqu'à la destination */
	public float getTotalCost() {
		return this.inf+this.getCost();
	}

}
