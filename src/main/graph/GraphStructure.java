package main.graph;

import main.*;
import main.associative.*;

public interface GraphStructure<E> extends NodeBased<E> {
    public E addEdge(Node node, Node adjacentNode);

    public boolean adjacent(Node node);

    public Structure<E> edges();

    public boolean isDirected();

    public boolean isWeighted();

    public Structure<E> neighbors();

    public Structure<E> path(Node start, Node destination);

    public E removeEdge(Node node, Node adjacentNode);

    public HashMap<E, Structure<E>> toMap();
    
    public Structure<E> vertices();
}
