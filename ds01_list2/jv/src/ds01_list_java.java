import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Vector;

public class ds01_list_java {
    public static void main(String[] args) {
        ArrayList<Integer> al = new ArrayList<>();
        LinkedList<Integer> ll = new LinkedList<>();
        Vector<Integer> vec = new Vector<>();

        al.add(10);
        al.add(20);
        al.add(30);
        System.out.println(al);

        al.set(1, 999);
        al.addLast(0);
        al.addFirst(0);

        System.out.println(al);

        ll.add(100);
        ll.add(101);
        ll.add(102);
        ll.addLast(777);
        System.out.println(ll);
    }
}
