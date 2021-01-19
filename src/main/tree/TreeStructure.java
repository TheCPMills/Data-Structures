package main.tree;

import main.*;
import main.linear.*;

public interface TreeStructure<E> extends Structure {
    public TreeStructure<E> root();

    public TreeStructure<E> parents();

    public LinearStructure<TreeStructure<E>> children();

    public boolean isLeaf();

    public TreeStructure<E> subtree();

    public LinearStructure<E> toList();
}