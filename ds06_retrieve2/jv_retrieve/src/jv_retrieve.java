import com.sun.source.doctree.EscapeTree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class jv_retrieve {
    public static void main(String[] args) {
        int[] a1 = {1, 2, 3, 4, 5};
        int index = Arrays.binarySearch(a1, 4);
        System.out.println(index);

        int[] a2 = {5, 4, 3, 2, 1};
        index = Arrays.binarySearch(a2, 4);
        System.out.println(index);

        int[] a3 = {1, 1, 1, 2, 2};
        index = Arrays.binarySearch(a3, 2);
        System.out.println(index);

        int[] a4 = java.util.stream.IntStream.range(1, 10).toArray();
        System.out.println(Arrays.toString(a4));

        IntStream r = java.util.stream.IntStream.rangeClosed(1, 10);

        // ArrayList<Integer> l1 = r.collect(Collectors.toList());
        ArrayList<Integer> l1 = r
                .boxed()
                .collect(Collectors.toCollection(ArrayList::new));
        System.out.println(l1.toString());

        IntStream r2 = java.util.stream.IntStream.rangeClosed(1, 10);
        ArrayList<Integer> l2 = new ArrayList<>();
        r2.forEach(l2::add);
        System.out.println(l2.toString());

        System.out.println(Collections.binarySearch(l2, 9));

    }
}
