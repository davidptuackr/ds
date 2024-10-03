import java.util.Queue;
import java.util.LinkedList;
import java.util.ArrayDeque;

public class queue_jv {
    public static void main(String[] args) {
        Queue<Integer> link_q = new LinkedList<>();
        Queue<Integer> array_q = new ArrayDeque<>();

        link_q.add(100);
        link_q.add(999);
        link_q.add(545);
        link_q.add(12);
        link_q.add(444);

        System.out.println(link_q);

        array_q.add(273);
        array_q.add(555);
        array_q.add(911);
        array_q.add(123);
        array_q.add(322);

        System.out.println(array_q);

        int rm;
        rm = link_q.remove();
        System.out.printf("REMOVED FROM LINK QUEUE -> %d\n", rm);
        System.out.printf("LQ AFTER RM : %s\n", link_q);

        rm = array_q.remove();
        System.out.printf("REMOVED FROM ARRAY QUEUE -> %d\n", rm);
        System.out.printf("AQ AFTER RM : %s\n", array_q);

    }
}
