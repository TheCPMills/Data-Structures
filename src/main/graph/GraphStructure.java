package main.graph;

import main.*;
import main.associative.*;

public interface GraphStructure<E> extends NodeBased<E> {
    public E addEdge(Node node, Node adjacentNode);

    public boolean adjacent(Node node);

    public Structure edges();

    public boolean isDirected();

    public boolean isWeighted();

    public Structure neighbors();

    public Structure path(Node start, Node destination);

    public E removeEdge(Node node, Node adjacentNode);

    public AssociativeStructure toMap();
    
    public Structure vertices();
}
