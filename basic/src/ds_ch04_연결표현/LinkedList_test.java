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

    시간복잡도: big-O(min(X.length(), Y.length))
        1. 짧은 쪽에 의해 결정된다
        2. 최악의 경우는 둘의 길이가 같은 경우이다
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

    /*
    04.3 오름차순 concat
    조건
        1. X, Y는 오름차순 정렬된 상태다
        2. X, Y를 Z에 합병하되, 합병 결과도 오름차순 정렬되어 있어야 한다
        3. 나머지 조건은 04.2와 같다
    풀이
        1. 다음을 준비한다
            zp: Z 포인터,
            Z.head = 더 큰 쪽의 헤드
            더 큰 쪽의 헤드를 한 단계 이동
        2. 다음 작업을 한 쪽이 끝날 때까지 반복한다
            2.1 X.data >= Y.data일 경우
                zp.link = Y
                Y.head = Y.head.link
                zp = zp.link
            2.2 X.data < Y.data일 경우
                zp.link = X
                X.head = X.head.link
                zp = zp.link
        3. 다음 조건에 맞춰 작업을 실시한다 (둘 다 길이가 같아도 어느 한 쪽은 마지막 딱 하나 남았을 것)
            3.1 X.head is null일 경우 (즉, X는 끝났을 경우)
                zp.link = Y.head
                Y.head는 null을 가리키도록 설정
            3.1 Y.head is null일 경우 (즉, Y는 끝났을 경우)
                zp.link = X.head
                X.head는 null을 가리키도록 설정
        4. return Z
    시간복잡도: 04.2와 같음
     */
    public static LinkedList ordered_concat(LinkedList X, LinkedList Y) {

        if (X.head == null) return Y;
        if (Y.head == null) return X;

        LinkedList Z = new LinkedList();
        Node zp;

        /*
        String S.compareTo(String T)
            S가 T보다 사전 순서상 앞에 있을 경우: -1
            S와 T가 같을 경우: 0
            S가 T보다 사전 순서상 뒤에 있을 경우: 1
         */
        if (X.head.data.compareTo(Y.head.data) <= 0) {
            Z.head = X.head;
            X.head = X.head.link;
        }
        else {
            Z.head = Y.head;
            Y.head = Y.head.link;
        }

        zp = Z.head;

        while ((X.head != null) && (Y.head != null)) {
            if (X.head.data.compareTo(Y.head.data) <= 0) {
                zp.link = X.head;
                X.head = X.head.link;
            }
            else {
                zp.link = Y.head;
                Y.head = Y.head.link;
            }
            zp = zp.link;
        }

        if (X.head == null) {
            zp.link = Y.head;
            Y.head = null;
        }
        else {
            zp.link = X.head;
            X.head = null;
        }

        return Z;
    }

    /*
    4.4 제한적이지만 양방향 순회가 가능한 단순 연결 리스트
    원리
        1. 두 개의 포인터 L, R을 준비한다
        2. 특정 위치를 탐색한다. 탐색 결과 R은 찾던 위치, L은 R의 직전 위치를 가리킨다
        3. 순회 도중 R이 지나쳐온 노드는 링크를 반대 방향으로 한다
        4. 만약 여기서 n만큼 떨어진 위치로 이동하고자 한다면
            4.1 R의 오른쪽으로 이동할 경우
                R이 지나간 곳은 링크를 반대 방향으로 설정한다
                n > 우측에 남아있는 원소일 경우 R은 null, L은 마지막 원소를 가리킨다
            4.2 R의 왼쪽으로 이동할 경우
                R이 지나간 곳은 링크 원상복귀
                n > 좌측에 남아있는 원소일 경우 L은 null, R은 첫 원소를 가리킨다
    풀이
        1. 초기 설정: L=null, R=head, n, p = R
        2. 특정 위치로 이동
            2.1 오른쪽 이동일 때
                2.1.1. L==null일 경우 다음 작업 먼저 하고 시작
                    1. L = R
                    2. R = R.link
                    3. L.link = null
                    4. n--
                2.1.2. 다음 작업을 n == 0이거나(찾아내거나) R == null이 될 때까지(끝에 도달하거나) 반복
                    1. R = R.link
                    2. p.link = L
                    3. L = p
                    4. p = R
                    5. n--
            2.2 왼쪽 이동일 때
                2.2.1 R==null일 경우 다음 작업 먼저 하고 시작
                    1. R = L
                    2. L = L.link
                    3. R.link = null
                    4. p = L
                    4. n--
                2.2.2 다음 작업을 n == 0이거나(찾아내거나) L == null이 될 때까지(끝에 도달하거나) 반복
                    1. L = L.link
                    2. p.link = R
                    3. R = p
                    4. p = L
                    5. n--
        3. print(L), print(R)
     */
    public static void retrieve(LinkedList L, LinkedList R, int step, boolean right) {
        // right==T >>> 우측, right==F >>> 좌측 순회
        if ((L == null) && (R == null)) {
            System.out.print("AT LEAST ONE LIST MUST NOT EMPTY");
            return;
        }

        int n = step;
        Node p;

        if (right) {
            p = R.head;
            if(L == null) {
                L.head = R.head;
                R.head = R.head.link;
                L.head.link = null;
                p = R.head;
                n--;
            }
            while ((n != 0) && (R != null)) {
                R.head = R.head.link;
                p.link = L.head;
                L.head = p;
                p = R.head;
                n--;
            }
        }
        else {
            p = L.head;
            if(L == null) {
                R.head = L.head;
                L.head = L.head.link;
                R.head.link = null;
                n--;
            }
            while ((n != 0) && (L != null)) {
                L.head = L.head.link;
                p.link = R.head;
                R.head = p;
                p = L.head;
                n--;
            }
        }

        L.print();
        R.print();
    }
}

public class LinkedList_test {

    public static void main(String[] args) {

        LinkedList L1 = new LinkedList();
        LinkedList L2 = new LinkedList();
        LinkedList OL = new LinkedList();
        LinkedList ZL = new LinkedList();

        L1.add_last_node("Amberjack");
        L1.add_last_node("Colonial");
        L1.add_last_node("Crestwood");
        L1.add_last_node("Mattox");
        L1.add_last_node("Zydeco");
        L1.add_last_node("Bengal");
        L1.add_last_node("Explore");
        L1.add_last_node("Triton");

        L2.add_last_node("Dominion");
        L2.add_last_node("Molecule");
        L2.add_last_node("International");

        OL.add_last_node("United");

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
        // LinkedList.concat(L1, L2).print();

        /*
        LinkedList.ordered_concat(L1, L2).print();
        LinkedList.ordered_concat(OL, L2).print();
         */

        LinkedList.retrieve(L1, L2, 3, false);
    }
}
