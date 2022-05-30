package org.insa.graphs.algorithm.shortestpath;

import java.util.*;

import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.algorithm.utils.*;
import org.insa.graphs.model.*;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {

	public int nbSommetsMarque;

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
        this.nbSommetsMarque = 0;
        
    }
    protected Label newLabel(Node node, ShortestPathData data) {
    	return new Label(node);
    }

    @Override
    protected ShortestPathSolution doRun() {
        final ShortestPathData data = getInputData();
        ShortestPathSolution solution = null;
        
        
       
        boolean fin = false;
        Graph graph = data.getGraph();
        int nbSommets = graph.size();
        Label tabLabels[] = new Label[nbSommets];
        BinaryHeap<Label> tas = new BinaryHeap<Label>();
        
        Arc[] ListeArc = new Arc[nbSommets];
        
        Label begin = newLabel(data.getOrigin(),data);
        tabLabels[begin.getNode().getId()] = begin;
        tas.insert(begin);
        begin.setInTas();
        begin.setCost(0);
        notifyOriginProcessed(data.getOrigin());
        
        while(!tas.isEmpty()&&!fin) {
        	Label current = tas.deleteMin();
        	Node currentNode = current.getNode();
        	notifyNodeMarked(currentNode);
        	current.setMarque();
     
        	if(data.getDestination()==current.getNode()) {
        		fin = true;
        	}
        	
        	Iterator<Arc> arc = currentNode.iterator();
        	while(arc.hasNext()) {
        		Arc arcIter = arc.next();
        		Node successors = arcIter.getDestination();
        		Label SuccessorsLabel = tabLabels[successors.getId()];
        		
        		if (SuccessorsLabel==null) {
        			notifyNodeReached(successors);
        			SuccessorsLabel = newLabel(successors,data);
        			tabLabels[SuccessorsLabel.getNode().getId()]=SuccessorsLabel;
        			this.nbSommetsMarque++;
        		}
     
        		if(!SuccessorsLabel.getmarque()) {
        			if (SuccessorsLabel.getTotalCost()>current.getTotalCost()+data.getCost(arcIter)) {
        				if(SuccessorsLabel.inTas()) {
        					tas.remove(SuccessorsLabel);
        				}
        				else {
        					SuccessorsLabel.setInTas();;
        				}
        				SuccessorsLabel.setCost(current.getTotalCost()+(float)data.getCost(arcIter));
        				SuccessorsLabel.setfather(current);
        				tas.insert(SuccessorsLabel);
        				ListeArc[arcIter.getDestination().getId()]= arcIter;
        			}
        		}
        	}
        	
        }
        if (ListeArc[data.getDestination().getId()]==null) {
        	solution = new ShortestPathSolution(data,Status.INFEASIBLE);
        }
        else {
        	notifyDestinationReached(data.getDestination());

			// Create the path from the array of predecessors...
			ArrayList<Arc> arcs = new ArrayList<>();
			Arc arc = ListeArc[data.getDestination().getId()];

			while (arc != null) {
				arcs.add(arc);
				arc = ListeArc[arc.getOrigin().getId()];
			}

			// Reverse the path...
			Collections.reverse(arcs);

			// Create the final solution.
			solution = new ShortestPathSolution(data, Status.OPTIMAL, new Path(data.getGraph(), arcs));
        }
        return solution;
    }


}
