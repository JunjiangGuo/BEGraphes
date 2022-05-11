package org.insa.graphs.model;

public class Label implements Comparable <Label>{
	private Node sommet_courant ;
	private boolean marque;
	private float cout;
	private Label father;
	private boolean inTas;
	
	public Label(Node noeud) {
		this.sommet_courant = noeud;
		this.marque = false;
		this.father = null;
		this.cout = Float.POSITIVE_INFINITY;
		this.inTas = false;
	}
	public Node getNode() {
		return this.sommet_courant;
	}
	
	public float getCost() {
		return this.cout;
	}
	public Label getfather() {
		return this.father;
	}
	public boolean getmarque() {
		return this.marque;
	}
	public Node getsommetcourant() {
		return this.sommet_courant;
	}
	public void setfather(Label father) {
		this.father = father;
	}
	public void setMarque() {
		this.marque = true;
	}
	public boolean inTas() {
		return this.inTas;
	}
	
	public void setCost(float cost) {
		this.cout = cost;
	}
	
	public void setInTas() {
		this.inTas = true;
	}
	
	public int compareTo(Label other) {
		if(this.getCost()<other.getCost()) {
			return -1;
		}
		if(this.getCost()==other.getCost()) {
			return 0;
		}
		else {
			return 1;
		}
	}
}