package main;

public interface NodeBased<E> extends Structure<E> {
    // only for trees that have an unknown amount of children
    // public void add(Node node, E element);

    public E get(Node node);

    public Node getNodeByIndex(int index);

    public Node lastNodeOf(E element);

    public Node nodeOf(E element);

    public default Node nodeOf(E element, int occurrence) {
        IndexBased<E> indexBased = toIndexBased();
        int index = indexBased.indexOf(element, occurrence);
        return getNodeByIndex(index);
    }

    public default Node[] nodesOf(E element) {
        IndexBased<E> indexBased = toIndexBased();
        int[] indices = indexBased.indicesOf(element);
        Node[] nodes = new Node[indices.length];
        for (int i = 0; i < indices.length; i++) {
            nodes[i] = getNodeByIndex(indices[i]);
        }
        return nodes;
    }

    public default int occurrences(E element) {
        return toIndexBased().occurrences(element);
    }

    public E remove(Node node);

    public E set(Node node, E element);

    public Structure<E> splice(Node fromNode, Node toNode);

    public boolean swap(Node element, Node swappedElement);

    public IndexBased<E> toIndexBased();
}