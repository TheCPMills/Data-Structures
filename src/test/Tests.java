package test;

import main.*;
import main.linear.*;
import main.associative.*;
import main.tree.*;
import main.graph.*;
import main.util.*;
import org.junit.jupiter.api.*;

public class Tests {
    // linear structures
    Array<Integer> aI = new Array<>(9);
    List<Integer> lI = new List<>();
    Set<Integer> s = new Set<>();
    Multiset<Integer> m = new Multiset<>();
    Queue<Integer> qI = new Queue<>();
    Stack<Integer> sI = new Stack<>();
    LinkedList<Integer> lLI = new LinkedList<>();
    DoubleEndedList<Integer> dEl = new DoubleEndedList<>();
    CircularArray<Integer> cAI = new CircularArray<>(9);
    CircularList<Integer> cLI = new CircularList<>();

    // associative structures
    HashMap<String, Integer> hM = new HashMap<>();
    // MultiHashMap<String, Integer> mHM = new MultiHashMap<>();

    @Test
    public void test() {
        testLinearStructures();
        // testAssociativeStructures();
        // testTreeStructures();
        // testGraphStructures();
    }

    private void testLinearStructures() {
    }
        
    @Test
    private void testList() {
        // test manipulation functions
        lI.addAll(Arrays.asList(28, 1, 10, 3, 32, 21, 30, 12, 23, 27)); // {28, 1, 10, 3, 32, 21, 30, 12, 23, 27}
        Assertions.assertEquals(Arrays.asList(28, 1, 10, 3, 32, 21, 30, 12, 23, 27), lI);

        lI.addIf(x -> x > 0, Arrays.asList(-2, 15, -4, -1, 31, 11, 20, 8, -3, 9, -5)); // {28, 1, 10, 3, 32, 21, 30, 12, 23, 27, 15, 31, 11, 20, 8, 9}
        Assertions.assertEquals(Arrays.asList(28, 1, 10, 3, 32, 21, 30, 12, 23, 27, 15, 31, 11, 20, 8, 9), lI);

        lI.add(26); // {28, 1, 10, 3, 32, 21, 30, 12, 23, 27, 15, 31, 11, 20, 8, 9, 26}
        Assertions.assertEquals(Arrays.asList(28, 1, 10, 3, 32, 21, 30, 12, 23, 27, 15, 31, 11, 20, 8, 9, 26), lI);

        lI.addAll(9, Arrays.asList(25, 13, 4, 22, 5, 29, 6, 24, 7)); // {28, 1, 10, 3, 32, 21, 30, 12, 23, 27, 25, 13, 4, 22, 5, 29, 6, 24, 7, 15, 31, 11, 20, 8, 9, 26}
        Assertions.assertEquals(Arrays.asList(28, 1, 10, 3, 32, 21, 30, 12, 23, 27, 25, 13, 4, 22, 5, 29, 6, 24, 7, 15, 31, 11, 20, 8, 9, 26), lI);
        
        lI.add(14, 14); // {28, 1, 10, 3, 32, 21, 30, 12, 23, 27, 25, 13, 4, 22, 5, 14, 29, 6, 24, 7, 15, 31, 11, 20, 8, 9, 26}
        Assertions.assertEquals(Arrays.asList(28, 1, 10, 3, 32, 21, 30, 12, 23, 27, 25, 13, 4, 22, 5, 14, 29, 6, 24, 7, 15, 31, 11, 20, 8, 9, 26), lI);
        
        lI.set(2, 2); // {28, 1, 2, 3, 32, 21, 30, 12, 23, 27, 25, 13, 4, 22, 5, 14, 29, 6, 24, 7, 15, 31, 11, 20, 8, 9, 26}
        Assertions.assertEquals(Arrays.asList(28, 1, 2, 3, 32, 21, 30, 12, 23, 27, 25, 13, 4, 22, 5, 14, 29, 6, 24, 7, 15, 31, 11, 20, 8, 9, 26), lI);
        
        lI.replaceAll(Arrays.asList(30, 31, 32), 0); // {28, 1, 2, 3, 0, 21, 0, 12, 23, 27, 25, 13, 4, 22, 5, 14, 29, 6, 24, 7, 15, 0, 11, 20, 8, 9, 26}
        Assertions.assertEquals(Arrays.asList(28, 1, 2, 3, 0, 21, 0, 12, 23, 27, 25, 13, 4, 22, 5, 14, 29, 6, 24, 7, 15, 0, 11, 20, 8, 9, 26), lI);
        
        lI.replaceIf(x -> x > 25, 0); // {0, 1, 2, 3, 0, 21, 0, 12, 23, 0, 25, 13, 4, 22, 5, 14, 0, 6, 24, 7, 15, 0, 11, 20, 8, 9, 0}
        Assertions.assertEquals(Arrays.asList(0, 1, 2, 3, 0, 21, 0, 12, 23, 0, 25, 13, 4, 22, 5, 14, 0, 6, 24, 7, 15, 0, 11, 20, 8, 9, 0), lI);
        
        lI.remove((Integer) 25); // {0, 1, 2, 3, 0, 21, 0, 12, 23, 0, 13, 4, 22, 5, 14, 0, 6, 24, 7, 15, 0, 11, 20, 8, 9, 0}
        Assertions.assertEquals(Arrays.asList(0, 1, 2, 3, 0, 21, 0, 12, 23, 0, 13, 4, 22, 5, 14, 0, 6, 24, 7, 15, 0, 11, 20, 8, 9, 0), lI);
        
        lI.remove(4); // {0, 1, 2, 3, 0, 21, 0, 12, 23, 0, 13, 4, 22, 5, 14, 0, 6, 24, 7, 15, 0, 11, 20, 8, 9, 0}
        Assertions.assertEquals(Arrays.asList(0, 1, 2, 3, 0, 21, 0, 12, 23, 0, 13, 4, 22, 5, 14, 0, 6, 24, 7, 15, 0, 11, 20, 8, 9, 0), lI);
        
        lI.removeAll(Arrays.asList(11, 12, 13, 14)); // {0, 1, 2, 3, 0, 21, 0, 23, 0, 4, 22, 5, 0, 6, 24, 7, 15, 0, 20, 8, 9, 0}
        Assertions.assertEquals(Arrays.asList(0, 1, 2, 3, 0, 21, 0, 23, 0, 4, 22, 5, 0, 6, 24, 7, 15, 0, 20, 8, 9, 0), lI);
        
        lI.removeIf(x -> x == 0); // {1, 2, 3, 21, 23, 4, 22, 5, 6, 24, 7, 15, 20, 8, 9}
        Assertions.assertEquals(Arrays.asList(1, 2, 3, 21, 23, 4, 22, 5, 6, 24, 7, 15, 20, 8, 9), lI);

        lI.retainAll(Arrays.asList(1, 2, 3, 23, 4, 22, 5, 6, 24, 7, 8, 9)); // {1, 2, 3, 23, 4, 22, 5, 6, 24, 7, 8, 9}
        Assertions.assertEquals(Arrays.asList(1, 2, 3, 23, 4, 22, 5, 6, 24, 7, 8, 9), lI);
        
        lI.retainIf(x -> x < 10); // {1, 2, 3, 4, 5, 6, 7, 8, 9}
        Assertions.assertEquals(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9), lI);
    }
}