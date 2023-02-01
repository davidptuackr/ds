package ds_ch04_연결표현;

class CircularLinkedList {

    /*
    04.6 원형 연결 리스트를 이용하여 04.1 ~ 04.5번 풀기
     */

    Node head;

    public void add_last_node(String x) {
        Node p;

        if (head == null) {
            head = new Node(x);
            head.link = head;
            return;
        }

        p = head;
        while (!p.link.equals(head)) {
            p = p.link;
        }
        p.link = new Node(x);
        p.link.link = head;
    }

    public void print() {
        Node p = head;
        int i = 0;

        if (head == null) System.out.print("EMPTY LIST");
        else {
            while (!p.link.equals(head)) {
                System.out.format("L[%d]: %s\n", i++, p.data);
                p = p.link;
            }
            System.out.format("L[%d]: %s\n", i++, p.data); // 마지막 직전에 멈추기 때문에 한 번 더 출력
        }
        System.out.format("List has %d elements\n\n", length());
    }

    /*
    1. 길이를 구하는 메소드 length()
     */
    public int length() {
        int length = 0;
        Node p = head;
        while ((head != null) && (!p.link.equals(head))) {
            p = p.link;
            length++;
        }
        return length + 1; // 마지막 원소는 반영이 안된 것을 감안;
    }

    /*
    2. 번갈아가며 결합하기
     */
    public static CircularLinkedList concat(CircularLinkedList X, CircularLinkedList Y) {
        CircularLinkedList Z = new CircularLinkedList();
        Node p, xp, yp, zp;
        boolean flag;

        // 한 쪽이 공백 리스트면 그냥 상대 리스트 반환
        if (X.head == null) return Y;
        if (Y.head == null) return X;

        // 둘 다 공백이 아니면 정상 진행: X가 먼저 와야 되니 초기엔 다음과 같이 설정
        Z.head = X.head;
        xp = X.head.link;
        Z.head.link = Y.head;
        yp = Y.head.link;
        zp = Z.head.link;
        flag = false;

        while ((!xp.equals(X.head)) && (!yp.equals(Y.head))) {
            if (flag) {
                p = yp;
                yp = yp.link;
            }
            else {
                p = xp;
                xp = xp.link;
            }
            zp.link = p;
            zp = zp.link;
            flag = !flag;
        }
        if (flag) {
            zp.link = yp;
            while (!zp.link.equals(Y.head)) {
                zp = zp.link;
            }
        }
        else {
            zp.link = xp;
            while (!zp.link.equals(X.head)) {
                zp = zp.link;
            }
        }
        zp.link = Z.head;
        return Z;
    }

    /*
    3. 순서를 고려하여 결합하기
     */
    public static CircularLinkedList ordered_concat(CircularLinkedList X, CircularLinkedList Y) {
        CircularLinkedList Z = new CircularLinkedList();
        Node p, xp, yp, zp;
        int cnt_x = 0, cnt_y = 0;
        int len_x = X.length(), len_y = Y.length();

        // 한 쪽이 공백 리스트면 그냥 상대 리스트 반환
        if (X.head == null) return Y;
        if (Y.head == null) return X;

        xp = X.head;
        yp = Y.head;

        // 초기 설정: Z가 어느 하나는 가리키도록 설정
        if (xp.data.compareTo(yp.data) <= 0) {
            Z.head = xp;
            xp = xp.link;
            cnt_x++;
        }
        else {
            Z.head = yp;
            yp = yp.link;
            cnt_y++;
        }
        zp = Z.head;

        while ((cnt_x < len_x) && (cnt_y < len_y)) {
            if (xp.data.compareTo(yp.data) <= 0) {
                zp.link = xp;
                xp = xp.link;
                cnt_x++;
            }
            else {
                zp.link = yp;
                yp = yp.link;
                cnt_y++;
            }
            zp = zp.link;
        }

        if (cnt_x == len_x) {
            zp.link = yp;
            while (cnt_y < len_y) {
                zp = zp.link;
                cnt_y++;
            }
        }
        else {
            zp.link = xp;
            while (cnt_x < len_x) {
                zp = zp.link;
                cnt_x++;
            }
        }
        zp.link = Z.head;

        return Z;
    }

    /*
    4. 원형 연결 리스트에서의 제한적인 양방향 순회

    ***
        입력은 [ CircularLinkedList X, Node L, Node R ], int step, bool right 로 한다
        출력은 없다.
            단, 함수 실행 중 L, R은 변경된다

    1. 다음을 준비한다
        R: 초기엔 헤드를 가리키는 포인터. 이후엔 우측 노드들의 헤드 역할
        L: 초기엔 헤드를 가리키는 포인터. 이후엔 좌측 노드들의 헤드 역할
        p: 보조 포인터
        n: 카운터
    2. 각 경우에 대하여 다음과 같이 동작한다
        2.1 우측 이동
            R = R.link
            p.link = L
            L = p
            p = R
            n--
        2.2 좌측 이동
            p = L
            L = L.link
            p.link = R
            R = p
            n--
    3. 각자의 노드를 출력한다
     */
    public static void retrieve(Object[] ret_set, int step, boolean right) {

        CircularLinkedList X = (CircularLinkedList) ret_set[0];
        Node L = (Node) ret_set[1];
        Node R = (Node) ret_set[2];

        if (R.equals(X.head) && R.link.equals(X.head.link)) {
            L = X.head;
        }

        int n = step;
        Node p;

        if (right) {
            p = R;
            while ((n != 0) && (!R.link.equals(R))) {
                R = R.link;
                p.link = L;
                L = p;
                p = R;
                n--;
            }
        }
        else {
            p = L;
            while ((n != 0) && (!L.link.equals(L))) {
                p = L;
                L = L.link;
                p.link = R;
                R = p;
                n--;
            }
        }

        p = L;
        int i = 0;

        while (!p.link.equals(p)) {
            System.out.format("L[%d]: %s\n", i++, p.data);
            p = p.link;
        }
        System.out.format("L[%d]: %s\n\n", i++, p.data); // 마지막 하나 남은 것

        p = R;
        i = 0;

        while (!p.link.equals(p)) {
            System.out.format("R[%d]: %s\n", i++, p.data);
            p = p.link;
        }
        System.out.format("R[%d]: %s\n\n", i++, p.data); // 마지막 하나 남은 것

        ret_set[1] = L;
        ret_set[2] = R;

    }

}

public class CircularLinkedList_test {

    public static void main(String[] args) {

        CircularLinkedList C1 = new CircularLinkedList();
        CircularLinkedList C2 = new CircularLinkedList();
        CircularLinkedList OC = new CircularLinkedList();
        CircularLinkedList ZC = new CircularLinkedList();

        String[] C1_data = {
                "Amberjack", "Bengal", "Colonial", "Crestwood",
                "Explore", "Mattox", "Triton", "Zydeco"
        };
        String[] C2_data = {"Dominion", "International", "Molecule"};

        for (int i = 0; i < C1_data.length; i++) {
            C1.add_last_node(C1_data[i]);
        }
        for (int i = 0; i < C2_data.length; i++) {
            C2.add_last_node(C2_data[i]);
        }
        OC.add_last_node("Colombia");

        //CircularLinkedList.concat(C1, C2).print();
        //CircularLinkedList.concat(ZC, C2).print();

        //CircularLinkedList.ordered_concat(C1, C2).print();

        Node L = null;
        Node R = C1.head;

        Object[] ret_set = { C1, L, R } ;

        CircularLinkedList.retrieve(ret_set, 3, true);
        CircularLinkedList.retrieve(ret_set, 4, true);
        CircularLinkedList.retrieve(ret_set, 4, false);

    }
}
