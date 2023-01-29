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

}

public class CircularLinkedList_test {

    public static void main(String[] args) {

        CircularLinkedList C1 = new CircularLinkedList();
        CircularLinkedList C2 = new CircularLinkedList();

        String[] C1_data = {
                "Mattox", "Zydeco", "Bengal", "Amberjack",
                "Colonial", "Crestwood", "Explore", "Triton"
        };
        String[] C2_data = {"Dominion", "Molecule", "International"};
        for (int i = 0; i < C1_data.length; i++) {
            C1.add_last_node(C1_data[i]);
        }
        for (int i = 0; i < C2_data.length; i++) {
            C2.add_last_node(C2_data[i]);
        }

        CircularLinkedList.concat(C1, C2).print();
    }
}
