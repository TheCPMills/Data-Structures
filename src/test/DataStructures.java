package test;

import main.*;
import main.linear.*;
import main.associative.*;
import main.tree.*;
import main.graph.*;
import main.util.*;

import org.junit.jupiter.api.*;

public class DataStructures {
    // linear structures
    Array<Integer> aI = new Array<Integer>(9);
    List<Integer> lI = new List<Integer>();
    Set<Integer> s = new Set<Integer>();
    Multiset<Integer> m = new Multiset<Integer>();
    Queue<Integer> qI = new Queue<Integer>();
    Stack<Integer> sI = new Stack<Integer>();
    LinkedList<Integer> lLI = new LinkedList<Integer>();
    DoubleEndedList<Integer> dEl = new DoubleEndedList<Integer>();
    CircularArray<Integer> cAI = new CircularArray<Integer>(9);
    CircularList<Integer> cLI = new CircularList<Integer>();

    // associative structures
    //HashMap<Integer> hM = new HashMap<Integer>();
    MultiHashMap<Integer> mHM = new MultiHashMap<Integer>();

    @Test
    public void test() {
        setUp();
        testLinearStructures();
        testAssociativeStructures();
        testTreeStructures();
        testGraphStructures();
    }

    private void setUp() {
        setUpLinear();
        setUpAssociative();
        setUpTree();
        setUpGraph();
    }

    private void setUpLinear() {
        // Array
        aI.add(1);
        aI.addAll(Arrays.asList(2, 3, 4, 5, 6, 7, 8));
        aI.add(9);

        // List
        lI.add(2);
        lI.add(0, 1);
        lI.addAll(Arrays.asList(6, 8));
        lI.add(3, 7);
        lI.addAll(2, Arrays.asList(3, 4, 5));
        lI.add(9);

        // Set
        s.add(2);
        s.add(0, 1);
        s.addAll(Arrays.asList(6, 1, 8));
        s.add(8);
        s.add(3, 7);
        s.addAll(2, Arrays.asList(3, 4, 7, 6, 5));
        s.add(5, 2);
        s.add(9);

        // Multiset
        m.add(2);
        m.add(0, 1);
        m.addAll(Arrays.asList(6, 8));
        m.add(3, 7);
        m.addAll(2, Arrays.asList(3, 4, 5));
        m.add(9);

        // Queue
        qI.offer(2);
        qI.add(0, 1);
        qI.offerAll(Arrays.asList(6, 8));
        qI.add(3, 7);
        qI.addAll(2, Arrays.asList(3, 4, 5));
        qI.offer(9);

        // Stack
        sI.push(8);
        sI.add(1, 9);
        sI.pushAll(Arrays.asList(3, 2));
        sI.add(2, 4);
        sI.addAll(3, Arrays.asList(7, 6, 5));
        sI.push(1);

        // LinkedList
        lLI.add(2);
        lLI.add(0, 1);
        lLI.addAll(Arrays.asList(6, 8));
        lLI.add(3, 7);
        lLI.addAll(2, Arrays.asList(3, 4, 5));
        lLI.add(9);

        // DoubleEndedList
        dEl.add(2);
        dEl.addFirst(1);
        dEl.addAll(Arrays.asList(6, 8));
        dEl.addLast(9);
        dEl.addAll(2, Arrays.asList(3, 4, 5));
        dEl.add(6, 7);

        // CircularArray
        cAI.add(1);
        cAI.addAll(Arrays.asList(2, 3, 4, 5, 6, 7, 8));
        cAI.add(9);

        // CircularList
        cLI.add(2);
        cLI.add(0, 1);
        cLI.addAll(Arrays.asList(6, 8));
        cLI.add(3, 7);
        cLI.addAll(2, Arrays.asList(3, 4, 5));
        cLI.add(9);
    }

    private void setUpAssociative() {

    }
    
    private void setUpTree() {

    }
    
    private void setUpGraph() {

    }

    @Test
    private void testLinearStructures() {
        String linearString = "[1, 2, 3, 4, 5, 6, 7, 8, 9]";

        Assertions.assertEquals(linearString, aI.toString());
        Assertions.assertEquals(linearString, lI.toString());
        Assertions.assertEquals(linearString, s.toString());
        Assertions.assertEquals(linearString, qI.toString());
        Assertions.assertEquals(linearString, sI.toString());
        Assertions.assertEquals(linearString, lLI.toString());
        Assertions.assertEquals(linearString, dEl.toString());
        Assertions.assertEquals(linearString, cAI.toString());
        Assertions.assertEquals(linearString, cLI.toString());
    }

    @Test
    private void testAssociativeStructures() {

    }

    @Test
    private void testTreeStructures() {

    }

    @Test
    private void testGraphStructures() {

    }
}