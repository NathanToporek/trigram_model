package tests;

import java.util.LinkedList;

/**
 * Created by nate on 3/9/17.
 */
public class Testing {

    public static void main(String ... args) {
        LinkedList<Integer> list = new LinkedList<Integer>();

        for (int i = 0; i < 100; i++) {
            list.add(i);
        }

        System.out.printf("FIRST ELEMENT OF LIST: %d\nLAST ELEMENT OF LIST: %d\n\n",
                          list.getFirst(), list.getLast());

    }
}
