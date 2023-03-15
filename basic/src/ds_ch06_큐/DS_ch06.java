package ds_ch06_큐;

/*
풀이할 문제
    1. 06.1: 큐 구현: 연결 리스트 이용
    2. 원형 큐 구현
    3. 06.3: 다중 큐 구현
    4. 우선 순위 큐 구현
        4.1 정렬된 연결 리스트 이용
        4.2 무정렬 배열 이용
        4.3 재귀 형태로 삽입, 삭제 구현
    5. 다중 우선 큐 구현
        5.1 각 큐는 우선 순위가 다르다
        5.2 각 큐에 있는 원소는 큐 내에서 우선 순위가 다르다
        5.3 전체 원소의 우선 순위는 다음을 기준으로 한다
            1. 큐의 우선 순위
            2. 1이 같을 경우 큐 안에서의 우선순위
    6. 06.15: 두 개의 스택을 이용한 큐 구현
 */

interface Queue {
    boolean isEmpty();
    void enq(Object data);
    Object deq();
}

class LinkQueue implements Queue {

    Node front;
    Node rear;

    class Node {
        Object data;
        Node link;

        public Node(Object data) {
            this.data = data;
            this.link = null;
        }
    }

    @Override
    public boolean isEmpty() {
        return (front == null) && (rear == null);
    }

    @Override
    public void enq(Object data) {
        if (isEmpty()) {
            front = new Node(data);
            rear = front;
            return;
        }
        rear.link = new Node(data);
        rear = rear.link;
    }

    @Override
    public Object deq() {
        Object data = front.data;
        front = front.link;
        return data;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Node p = front;
        int i = 0;

        while (p != null) {
            sb.append(String.format("LQ[%d]: %s\n", i, p.data));
            p = p.link;
            i++;
        }

        return sb.toString();
    }

}

class CircularQueue implements Queue {

    Object[] data;
    int front, rear;

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void enq(Object data) {

    }

    @Override
    public Object deq() {
        return null;
    }
}

public class DS_ch06 {

    public static void main(String[] args) {

    }

}
