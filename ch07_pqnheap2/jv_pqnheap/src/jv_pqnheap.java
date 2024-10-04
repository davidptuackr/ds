import java.util.Arrays;
import java.util.Collections;
import java.util.PriorityQueue;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class jv_pqnheap {
    public static void main(String[] args) {
        Random r = new Random();
        // PQ는 기본 최소 힙 -> 루트가 최소
        PriorityQueue<Integer> minq = new PriorityQueue<>();
        for (int i = 0; i < 15; i++) {
            minq.add(r.nextInt(100));
        }
        System.out.println(minq);

        //
        PriorityQueue<Integer> maxq = new PriorityQueue<>(Collections.reverseOrder());
        for (int i = 0; i < 15; i++) {
            maxq.add(r.nextInt(100));
        }
        System.out.println(maxq);

        List<Integer> randomNumbers = Stream
                .generate(() -> new Random().nextInt(101)) // 0~100 사이의 숫자 생성
                .limit(15)                                // 스트림에서 10개의 숫자만 제한
                .toList();

        PriorityQueue<Integer> pq = new PriorityQueue<>(randomNumbers);
        System.out.println(randomNumbers);
        System.out.println(pq);

    }
}
