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

    public CircularQueue() {

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
        System.out.format("EN_QUEUE %s\n", data_enq.toString());
    }

    @Override
    public Object deq() {
        if (isEmpty()) {
            System.out.println("EMPTY QUEUE");
            return null;
        }

        Object data_deq = data[front];
        data[front] = null;
        front = (front+1) % data.length;
        System.out.format("DE_QUEUE %s\n", data_deq.toString());
        return data_deq;
    }

    public boolean is_full() {
        return (rear == front) && (data[rear] != null);
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{ ");

        int i = 0;

        do {
            int loc = (front+i) % data.length;
            sb.append(String.format("%s", data[loc]));
            i++;
            if (((front + i) % data.length) != rear) sb.append(", ");
            else sb.append(" }\n");
        } while (((front + i) % data.length) != rear);

        return sb.toString();
    }
}

class MultiQueue implements Queue{

    CircularQueue[] qs;

    public MultiQueue(int n_qs, int q_size) {
        qs = new CircularQueue[n_qs];
        for (int i = 0; i < n_qs; i++) {
            qs[i] = new CircularQueue(q_size);
        }
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < qs.length; i++) {
            if (!is_q_n_empty(i)) return false;
        }
        return true;
    }

    public boolean is_q_n_empty(int q_number) {
        return qs[q_number].isEmpty();
    }

    @Override
    public void enq(Object data) {
        qs[0].enq(data);
    }

    public void enq(Object data, int q_number) {
        qs[q_number].enq(data);
    }

    @Override
    public Object deq() {
        return qs[0].deq();
    }

    public Object deq(int q_number) {
        return qs[q_number].deq();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("QUEUE STATUS\n");
        for (int i = 0; i < qs.length; i++) {
            sb.append(String.format("\tQUEUE[%d]: %s", i, qs[i]));
        }
        return sb.toString();
    }
}

class Link_PQ extends LinkQueue {

    P_Node front;
    P_Node rear;

    class P_Node {
        int order;
        Object data;
        P_Node link;

        public P_Node(Object data) {
            this.order = rear.order + 1;
            this.data = data;
            this.link = null;
        }

        public P_Node(int order, Object data) {
            this.order = order;
            this.data = data;
            this.link = null;
        }

        public P_Node(int order, Object data, P_Node link) {
            this.order = order;
            this.data = data;
            this.link = link;
        }
    }

    @Override
    public void enq(Object data) {
        if (isEmpty()) {
            front = new P_Node(data);
            rear = front;
            return;
        }
        rear.link = new P_Node(data);
        rear = rear.link;
    }

    public void enq(Object data, int order) {
        if (isEmpty()) {
            front = new P_Node(data);
            rear = front;
            return;
        }
        P_Node p = front;
        P_Node q = front;
        while ((p != null) && (p.order < order)) {
            q = p;
            p = p.link;
        }
        q.link = new P_Node(order, data, p);
    }

    @Override
    public Object deq() {
        Object data = front.data;
        front = front.link;
        if (front == null) rear = null;
        return data;
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

    static void mq_test() {
        MultiQueue mq = new MultiQueue(4, 3);

        System.out.println("ISEMPTY TEST");
        mq.enq("Alpha");
        mq.enq("Beta", 0);
        mq.enq("Gamma");
        mq.enq("Omega", 0);

        System.out.println(mq.qs[0]);

        System.out.println("DEQUEUE TEST");
        mq.deq(); mq.deq(0); mq.deq(1);

        System.out.println(mq.qs[0]);

        System.out.println("ENQUEUE TEST");
        mq.enq("Classification", 2);
        mq.enq("Neighbor", 2);
        mq.enq("Boost", 3);
        mq.enq("Forest", 3);
        mq.enq("Gradient", 1);
        mq.enq("Regression", 1);

        System.out.println(mq);
    }

    static void link_pq_test() {
        Link_PQ pq = new Link_PQ();

        System.out.println("IS EMPTY? >>> " + pq.isEmpty());

        System.out.println("ENQ TEST");
        pq.enq("Red");
        pq.enq("White", 44);
        pq.enq("Rose", 27);


    }

    public static void main(String[] args) {

        //lq_test();
        //cq_test();
        //mq_test();
        link_pq_test();
    }

}
