package main;

public interface NodeBased<E> extends Structure {
    // only for trees that have an unknown amount of children
    // public void add(Node node, E element);

    public E get(Node node);

    public Node getNodeByIndex(int index);

    public Node lastNodeOf(Object o);

    public Node nodeOf(Object o);

    public default Node nodeOf(Object o, int occurrence) {
        IndexBased<E> indexBased = toIndexBased();
        int index = indexBased.indexOf(o, occurrence);
        return getNodeByIndex(index);
    }

    public default Node[] nodesOf(Object o) {
        IndexBased<E> indexBased = toIndexBased();
        int[] indices = indexBased.indicesOf(o);
        Node[] nodes = new Node[indices.length];
        for (int i = 0; i < indices.length; i++) {
            nodes[i] = getNodeByIndex(indices[i]);
        }
        return nodes;
    }

    public default int occurrences(Object o) {
        return toIndexBased().occurrences(o);
    }

    public E remove(Node node);

    public E set(Node node, E element);

    public Structure splice(Node fromNode, Node toNode);

    public boolean swap(Node element, Node swappedElement);

    public IndexBased<E> toIndexBased();
}