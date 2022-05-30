package org.insa.graphs.algorithm.utils;
import static org.junit.Assert.assertEquals;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;


import org.insa.graphs.algorithm.ArcInspector;
import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.algorithm.shortestpath.BellmanFordAlgorithm;
import org.insa.graphs.algorithm.shortestpath.AStarAlgorithm;
import org.insa.graphs.algorithm.shortestpath.ShortestPathData;
import org.insa.graphs.algorithm.shortestpath.ShortestPathSolution;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.RoadInformation;
import org.insa.graphs.model.RoadInformation.RoadType;
import org.insa.graphs.model.io.BinaryGraphReader;
import org.insa.graphs.model.io.GraphReader;
import org.junit.BeforeClass;
import org.junit.Test;

public class AstarTest {
		// Small graph use for tests
		private static Graph graph;

		// List of nodes
		private static Node[] nodes;

		// List of arcs in the graph, a2b is the arc from node A (0) to B (1).
		@SuppressWarnings("unused")
		private static Arc a2b, a2c, b2d, b2e, b2f, c2a, c2b, c2f, e2c, e2d, e2f, f2e;
		
		@BeforeClass
		public static void initAll() throws IOException {

			// Define roads
			RoadInformation RoadInfo = new RoadInformation(RoadType.UNCLASSIFIED, null, true, 1, null);

			// Create nodes
			nodes = new Node[6];
			for (int i = 0; i < nodes.length; ++i) {
				nodes[i] = new Node(i, null);
			}

			// Add arcs
			a2b = Node.linkNodes(nodes[0], nodes[1], 7, RoadInfo, null);
			a2c = Node.linkNodes(nodes[0], nodes[2], 8, RoadInfo, null);
			b2d = Node.linkNodes(nodes[1], nodes[3], 4, RoadInfo, null);
			b2e = Node.linkNodes(nodes[1], nodes[4], 1, RoadInfo, null);
			b2f = Node.linkNodes(nodes[1], nodes[5], 5, RoadInfo, null);
			c2a = Node.linkNodes(nodes[2], nodes[0], 7, RoadInfo, null);
			c2b = Node.linkNodes(nodes[2], nodes[1], 2, RoadInfo, null);
			c2f = Node.linkNodes(nodes[2], nodes[5], 2, RoadInfo, null);
			e2c = Node.linkNodes(nodes[4], nodes[2], 2, RoadInfo, null);
			e2d = Node.linkNodes(nodes[4], nodes[3], 2, RoadInfo, null);
			e2f = Node.linkNodes(nodes[4], nodes[5], 3, RoadInfo, null);
			f2e = Node.linkNodes(nodes[5], nodes[4], 3, RoadInfo, null);

			// Initialize the graph
			graph = new Graph("ID", "", Arrays.asList(nodes), null);

		}
		
		@SuppressWarnings("deprecation")
		public void testScenario(String mapName, int typeEvaluation, int origine, int destination) throws Exception {
			//public void testScenario(String mapName, int typeEvaluation, Node origine, Node destination) throws Exception {

			// Create a graph reader.
			GraphReader reader = new BinaryGraphReader(
					new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));

			// Read the graph.
			Graph graph = reader.read();
			System.out.println("Size graph :"+graph.size());

			if (typeEvaluation!=0 && typeEvaluation!=1) {
				System.out.println("Argument invalide");
			} else {
				if (origine<0 || destination<0 || origine>=(graph.size()-1) || destination>=(graph.size()-1)) { // On est hors du graphe. / Sommets inexistants
					System.out.println("ERREUR : Paramètres invalides ");
					
				} else {
					ArcInspector arcInspectorDijkstra;
					
					if (typeEvaluation == 0) { //Temps
						System.out.println("Mode : Temps");
						arcInspectorDijkstra = ArcInspectorFactory.getAllFilters().get(2);
					} else {
						System.out.println("Mode : Distance");
						arcInspectorDijkstra = ArcInspectorFactory.getAllFilters().get(0);
					}
					
					
					//System.out.println("Chemin de la carte : "+mapName);
					System.out.println("Origine : " + origine);
					System.out.println("Destination : " + destination);
					
					if(origine==destination) {
						System.out.println("Origine et Destination identiques");
						System.out.println("Cout solution: 0");
						
					} else {			
						ShortestPathData data = new ShortestPathData(graph, graph.get(origine),graph.get(destination), arcInspectorDijkstra);
			
						BellmanFordAlgorithm B = new BellmanFordAlgorithm(data);
						AStarAlgorithm D = new AStarAlgorithm(data);
						
						// Recuperation des solutions de Bellman et Dijkstra pour comparer 
						ShortestPathSolution solution = D.run();
						ShortestPathSolution expected = B.run();
		
						
						if (solution.getPath() == null) {
							assertEquals(expected.getPath(), solution.getPath());
							System.out.println("PAS DE SOLUTION");
							System.out.println("(infini) ");
						}
						// Un plus court chemin trouve 
						else {
							double costSolution;
							double costExpected;
							if(typeEvaluation == 0) { //Temps
								//Calcul du cout de la solution 
								costSolution = solution.getPath().getMinimumTravelTime();
								costExpected = expected.getPath().getMinimumTravelTime();
							} else {
								costSolution = solution.getPath().getLength();
								costExpected = expected.getPath().getLength();
							}
							assertEquals(costExpected, costSolution, 0.001);
							System.out.println("Cout solution: " + costSolution);
						}
					}
				}
			}
			System.out.println();
			System.out.println();
		}
		
		
	
		
		@Test
		public void testDoScenarioDistanceToulouse() throws Exception {
			
			String mapName = "/home/yduan/Bureau/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/toulouse.mapgr";
			
			AstarTest test = new  AstarTest();
			int origine;
			int destination;
			
			System.out.println("#####----- Test de validité sur une carte-----######");
			System.out.println("#####----- Carte : Toulouse -------------------------######");
			System.out.println("#####----- Mode : DISTANCE -------------------------------######");
			System.out.println();
			
			
			System.out.println("----- Cas d'un chemin nul ------");
			origine = 0 ;
			destination = 0;
			test.testScenario(mapName, 1,origine,destination);    
			
			System.out.println("----- Cas d'un chemin simple ------");
			origine = 10000;
			destination = 39000;
			test.testScenario(mapName, 1,origine,destination);  
			
			System.out.println("----- Cas d'un chemin inexistant ------");
			System.out.println("----- Cas d'un chemin origine inexistant ------");
			origine = -1 ;
			destination = 39000;
			test.testScenario(mapName, 1,origine,destination);  
			
			System.out.println("----- Cas d'un trajet long et pénible avec des enfants ------");
			System.out.println("----- Cas d'un chemin destination inexistant ------");
			origine =  36000;
			destination = 40000;
			test.testScenario(mapName, 1,origine,destination);  
		}
		
		@Test
		public void testDoScenarioTempsToulouse() throws Exception {
			String mapName = "/home/yduan/Bureau/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/toulouse.mapgr";
			
			AstarTest test = new  AstarTest();
			int origine;
			int destination;
			
			System.out.println("#####----- Test de validité sur une carte-----######");
			System.out.println("#####----- Carte : Toulouse -------------------------######");
			System.out.println("#####----- Mode : TEMPS -------------------------------######");
			System.out.println();
			
			System.out.println("----- Cas d'un chemin nul ------");
			origine = 0 ;
			destination = 0;
			test.testScenario(mapName, 0,origine,destination);    
			
			System.out.println("----- Cas d'un chemin simple ------");
			origine = 10000 ;
			destination = 39000;
			test.testScenario(mapName, 0,origine,destination);  
			
			System.out.println("----- Cas d'un chemin inexistant ------");
			System.out.println("----- Cas d'un chemin avec origine inexistant ------");
			origine = -1 ;
			destination = 39000;
			test.testScenario(mapName, 0,origine,destination);  
			
			System.out.println("----- Cas d'un trajet long et pénible avec des enfants ------");
			System.out.println("----- Cas d'un chemin avec destination inexistant ------");
			origine =  36000;
			destination = 40000;
			test.testScenario(mapName, 0,origine,destination); 
		}
}

