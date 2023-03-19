package ds_ch06_큐;

/*
풀이할 문제
    1. 06.1: 큐 구현: 연결 리스트 이용
    2. 원형 큐 구현
        조건: 큐 공간을 남김없이 써야 한다 + (front: 삭제"할" 것, rear: 넣을 위치)을 가리킨다
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
        if (front == null) rear = null;
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

    public CircularQueue(int q_size) {
        this.front = 0;
        this.rear = 0;
        this.data = new Object[q_size];
    }

    @Override
    public boolean isEmpty() {
        return (rear == front) && (data[front] == null);
    }

    @Override
    public void enq(Object data_enq) {
        if (is_full()) {
            System.out.println("QUEUE FULL");
            return;
        }
        data[rear] = data_enq;
        rear = (rear+1) % data.length;
    }

    @Override
    public Object deq() {
        Object data_deq = data[front];
        data[front] = null;
        front = (front+1) % data.length;
        return data_deq;
    }

    public boolean is_full() {
        return (rear == front) && (data[rear] != null);
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        for (int i = 0; ((front + i) % data.length) != rear; i++) {
            int loc = (front+i) % data.length;
            sb.append(String.format("CQ[%d]: %s\n", i, data[loc]));
        }
        
        return sb.toString();
    }
}

public class DS_ch06 {

    static void lq_test() {
        LinkQueue lq = new LinkQueue();

        System.out.println("IS_EMPTY TEST");
        lq.enq("QQQ");
        lq.deq();
        System.out.format("IS EMPTY? >>> %b\n", lq.isEmpty());

        lq.enq("Zydeco");
        lq.enq("Ensemble");
        lq.enq("Gradient");
        lq.enq("Spodumene");

        System.out.format("DEQ: %s\n", lq.deq());

        System.out.println(lq + "IN QUEUE");
    }
    
    static void cq_test() {
        CircularQueue cq = new CircularQueue(3);

        System.out.println("IS_EMPTY TEST");
        cq.enq("QQQ");
        cq.deq();
        System.out.format("IS EMPTY? >>> %b\n", cq.isEmpty());

        cq.enq("Zydeco");
        cq.enq("Ensemble");
        cq.enq("Gradient");
        cq.enq("Spodumene");

        System.out.format("DEQ: %s\n", cq.deq());

        System.out.println(cq + "IN QUEUE");
    }

    public static void main(String[] args) {

        //lq_test();
        cq_test();

    }

}
