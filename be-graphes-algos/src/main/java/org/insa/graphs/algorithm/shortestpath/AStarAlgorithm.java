package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.algorithm.LabelStar;
import org.insa.graphs.model.*;

public class AStarAlgorithm extends DijkstraAlgorithm {

    public AStarAlgorithm(ShortestPathData data) {
        super(data);
    }

    protected Label newLabel(Node node, ShortestPathData data) {
		return new LabelStar(node, data);
	}
}
