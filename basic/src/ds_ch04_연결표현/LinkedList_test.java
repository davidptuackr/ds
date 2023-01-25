package ds_ch04_연결표현;

class Node {
    String data;
    Node link;

    public Node() {
        data = null;
        link = null;
    }

    public Node(String val) {
        data = val;
        link = null;
    }

    public Node(String val, Node p) {
        data = val;
        link = p;
    }
}

class LinkedList implements Cloneable {

    private Node head;

    public void add_last_node(String x) {
        Node p;

        if (head == null) {
            head = new Node(x);
            return;
        }

        p = head;
        while (p.link != null) {
            p = p.link;
        }
        p.link = new Node(x);
    }

    public void reverse() {
        Node p, q, r;

        p = head;
        q = p.link;
        r = p;
        r.link = null;

        while (q != null) {
            p = q;
            q = q.link;
            p.link = r;
            r = p;
        }
        head = r;
    }

    public void delete_last_node() {
        Node p = head;
        Node q = p;

        if (p == null) {
            System.out.println("EMPTY LIST");
            return;
        } else {
            while (p.link != null) {
                q = p;
                p = p.link;
            }
            if (p.equals(head)) head = null;
            else q.link = null;
        }
    }

    public void print() {
        Node p = head;
        int i = 0;

        if (head == null) System.out.print("EMPTY LIST");
        else {
            while (p != null) {
                System.out.format("L[%d]: %s\n", i++, p.data);
                p = p.link;
            }
        }
        System.out.format("List has %d elements\n\n", length());
    }

    @Override
    public LinkedList clone() {
        try {
            return (LinkedList) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    /*
    04.1 단순 연결 리스트의 길이를 계산하는 함수 length() 만들기
    풀이
        1. Node p가 리스트 첫 원소(head)를 가리키도록 한다.
        2. length = 0으로 한다.
        3. length++를 다음 조건을 모두 만족할 때까지 반복한다
            3.1 head != null >>> 빈 리스트일경우
            3.2 p != null >>> 직전 p가 리스트의 마지막 원소일 경우

    시간복잡도: big-O(리스트의 원소 수) >>> 리스트 원소 수만큼 반복하기 때문
     */
    public int length() {
        int length = 0;
        Node p = head;
        while ((head != null) && (p != null)) {
            p = p.link;
            length++;
        }
        return length;
    }

    /*
    04.2 두 개의 연결 리스트 X, Y를 Z=(X1, Y1, X2, Y2, ...) 형태로 합치기
    조건
        1. X가 더 길면 합치고 남은 X 원소는 Z 뒤에 붙인다
        2. Y가 더 길면 합치고 남은 Y 원소는 Z 뒤에 붙인다
        3. 끝났을 때 X, Y는 공백 리스트여야 한다
        4. 도중에 추가 노드를 쓸 수 없다 >>> 노드 생성 + 노드 값 복사 불가
    풀이
        1. 노드 포인터 2개를 준비한다. 각각의 역할은 다음과 같다.
            p: flag==F >>> point X, else >>> point Y, 초기엔 X.head;
            zp: Z 포인터,
        2. 다음을 준비한다
            boolean 변수 flag: 초기값은 true다
            Z.head = X.head
            X.head = X.head.link
        3. 다음 작업을 한 쪽이 null이 될 때까지 반복한다
            3.1 flag==F일 경우
                1. p = X.head
                2. X.head = X.head.link
            3.2 flag==T일 경우
                1. p = Y.head
                2. Y.head = Y.head.link
            3.3 zp.link = p, zp = zp.link
            3.4. flag = !flag
        4. flag에 따라 다음을 결정한다
            4.1 flag==F >>> zp.link = Y.head, Y.head = null
            4.2 flag==T >>> zp.link = X.head, X.head = null
        5. return Z
     */
    public static LinkedList concat(LinkedList X, LinkedList Y) {
        LinkedList Z = new LinkedList();
        Node p, zp;
        boolean flag;

        // 한 쪽이 공백 리스트면 그냥 상대 리스트 반환
        if (X.head == null) return Y;
        if (Y.head == null) return X;

        // 둘 다 공백이 아니면 정상 진행: X가 먼저 와야 되니 초기엔 다음과 같이 설정
        Z.head = X.head;
        X.head = X.head.link;
        p = X.head;
        zp = Z.head;
        flag = true;

        while ((X.head != null) && ((Y.head != null))) {
            if (flag) {
                p = Y.head;
                Y.head = Y.head.link;
            }
            else {
                p = X.head;
                X.head = X.head.link;
            }
            zp.link = p;
            zp = zp.link;
            flag = !flag;
        }
        if (flag) {
            zp.link = Y.head;
            Y.head = null;
        }
        else {
            zp.link = X.head;
            X.head = null;
        }
        return Z;
    }
}

public class LinkedList_test {

    public static void main(String[] args) {

        LinkedList L1 = new LinkedList();
        LinkedList L2 = new LinkedList();
        LinkedList OL = new LinkedList();
        LinkedList ZL = new LinkedList();

        L1.add_last_node("Zydeco");
        L1.add_last_node("Amberjack");
        L1.add_last_node("Explore");
        L1.add_last_node("Colonial");
        L1.add_last_node("Crestwood");

        L2.add_last_node("Bengal");
        L2.add_last_node("Mattox");
        L2.add_last_node("Triton");

        OL.add_last_node("Arrow");

        /*
        L.reverse();
        L.print();

        L.delete_last_node();
        L.print();

        L2.delete_last_node();
        L2.print();
        */

        // LinkedList.concat(L1, OL).print();
        // LinkedList.concat(OL, ZL).print();
        // LinkedList.concat(ZL, OL).print();
        LinkedList.concat(L1, ZL).print();
    }
}
