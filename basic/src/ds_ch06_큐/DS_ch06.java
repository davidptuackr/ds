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

import java.util.Arrays;

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

        public Node() {

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

    class P_Node extends Node {
        int order;

        public P_Node(Object data) {
            super(data);
            this.order = (rear == null) ? 1 : rear.order + 1;
        }

        public P_Node(int order, Object data) {
            super(data);
            this.order = order;
        }

        public P_Node(int order, Object data, P_Node link) {
            super();
            this.order = order;
            this.data = data;
            this.link = link;
        }
    }

    @Override
    public boolean isEmpty() {
        return this.front == null && this.rear == null;
    }

    @Override
    public void enq(Object data) {
        if (isEmpty()) {
            front = new P_Node(data);
            rear = front;
            return;
        }
        rear.link = new P_Node(data);
        rear = (P_Node) rear.link;
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
            p = (P_Node) p.link;
        }
        q.link = new P_Node(order, data, p);
        while (rear.link != null) rear = (P_Node) rear.link;
    }

    @Override
    public Object deq() {
        Object data = front.data;
        front = (P_Node) front.link;
        if (front == null) rear = null;
        return data;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("QUEUE STATUS { ");

        P_Node p = front;

        while (p.link != null) {
            sb.append(String.format("(%s, %d), ", p.data, p.order));
            p = (P_Node) p.link;
        }
        sb.append(String.format("(%s, %d) }", p.data, p.order));

        return sb.toString();
    }
}

class List_PQ extends CircularQueue {

    /*
    무정렬 배열에서의 front, rear
        front: 삭제할 원소의 위치를 가리킨다 >>> 우선순위가 가장 높은 원소를 가리키는 숫자
        rear: 삽입할 위치를 가리킨다 >>> 우선순위에 상관 없이 빈 칸을 가리킨다

    무정렬 배열에서의 isEmpty가 true일 조건: (rear == front) && (data[front] == null) (CQ와 동일함)

    enq(data)
        1. rear 위치에 삽입
        2. 이 때 순위는 최후순위로 간주 >>> 빈 상태였다면 1, 아니면 orders.min+1
        3. front 갱신 불필요 >>> 어차피 위에서 front를 갱신할 정도의 우선순위가 아니란 것을 알았기 때문
        4. orders는 갱신
        5. rear 갱신: 다른 빈 칸 탐색. 만약 없다면 data.length로 설정

    enq(data, order)
        1. rear 위치에 삽입
        2. front 갱신: 새로 온 원소의 우선순위가 기존 front보다 높다면 변경, 아니면 pass
        3. orders 갱신
        4. rear 갱신

    deq
        front 위치의 원소 deq
        front 위치의 orders 제거 >>> 무한으로 설정
        만원 큐였다면 rear 갱신: data.length >>> front
        front 갱신: 다음으로 우선순위가 높은 원소로 지정

     */

    int[] orders;

    public List_PQ(int q_size) {
        super(q_size);
        orders = new int[q_size];
        Arrays.fill(orders, (int) Double.POSITIVE_INFINITY);
    }

    @Override
    public void enq(Object data_enq) {
        if (isEmpty()) {
            orders[rear] = 1;
        }
         //= (isEmpty()) ? 1 : Arrays.stream(orders).max().getAsInt() + 1;
        data[rear] = data_enq;

        int i;
        for (i = rear+1; (i != rear) && (data[i] != null); i = (i+1) % data.length) { }
        rear = (data[i] == null) ? i : data.length;
    }

    public void enq(Object data_enq, int order) {
        data[rear] = data_enq;
        orders[rear] = order;

        front = (orders[front] > order) ? orders[rear] : orders[front];

        int i = rear + 1;
        while ((i != rear) && (data[i] != null)) {
            i = (i + 1) % data.length;
        }
        rear = (data[i] == null) ? i : data.length;
    }

    @Override
    public Object deq() {
        Object data_deq = data[front];
        data[front] = null;
        orders[front] = (int) Double.POSITIVE_INFINITY;

        if (rear == data.length) rear = front;

        front = Arrays.stream(orders).min().getAsInt();

        return data_deq;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("QUEUE STATUS: { ");

        int i = 0;

        while ((i < data.length) && (data[i] != null)) {
            sb.append(String.format("(%s, %d), ", data[i], orders[i]));
            i++;
        }
        sb.delete(sb.length()-2, sb.length()-1);
        sb.append("}\n");
        sb.append(String.format("PRIOR: (%s, %d)\n", data[front], orders[front]));

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
        pq.enq("Scotch");

        System.out.println(pq);
    }

    static void list_pq_test() {
        List_PQ pq = new List_PQ(5);

        System.out.println(pq.isEmpty());

        pq.enq("Ro");
        pq.enq("Sigma");
        pq.enq("Tau", 26);
        pq.enq("Ipsilon", 23);

        System.out.println(pq);
    }

    public static void main(String[] args) {

        //lq_test();
        //cq_test();
        //mq_test();
        //link_pq_test();
        list_pq_test();
    }

}
