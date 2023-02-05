package ds_ch04_연결표현;

class PolyNode {

    int coef;
    int exp;
    PolyNode link;

    public PolyNode() {
        this.coef = 0;
        this.exp = 0;
        link = null;
    }

    public PolyNode(int coef, int exp) {
        this.coef = coef;
        this.exp = exp;
        link = null;
    }

    public PolyNode(int coef, int exp, PolyNode p) {
        this.coef = coef;
        this.exp = exp;
        link = p;
    }
}

class CircularPoly {

    PolyNode head;

    public void add_last_node(int coef, int exp) {
        PolyNode p;

        if (head == null) {
            head = new PolyNode(coef, exp);
            head.link = head;
            return;
        }

        p = head;
        while (!p.link.equals(head)) {
            p = p.link;
        }
        p.link = new PolyNode(coef, exp);
        p.link.link = head;
    }

    public void print() {
        PolyNode p = head;

        if (head == null) System.out.print("EMPTY POLY");
        else {
            while (!p.link.equals(head)) {
                System.out.format("%dX^%d + ", p.coef, p.exp);
                p = p.link;
            }
            System.out.format("%dX^%d\n", p.coef, p.exp); // 마지막 직전에 멈추기 때문에 한 번 더 출력
        }
    }

    /*
    1. 길이를 구하는 메소드 length()
     */
    public int length() {
        int length = 0;
        PolyNode p = head;
        while ((head != null) && (!p.link.equals(head))) {
            p = p.link;
            length++;
        }
        return length + 1; // 마지막 원소는 반영이 안된 것을 감안;
    }

    /*
    04.7 원형 연결 리스트로 표현된 다항식의 시간복잡도 증명
    다항식 A(x), B(x)에 대하여 A와 B의 항 수가 각각 m, n일 때
    C = A * B가 big-O(m^2n) || big-O(mn^2)에 수행될 수 있음을 증명하라
    만약 A, B가 조밀하다면(?) big-O(mn)에 수행됨을 증명하라
        >>> 차수가 겹치지 않고, 순서대로라면 이라고 일단 해석했다

    다항식 곱셈 과정
        1. 일단 전개해서 C의 노드로 추가한다
        2. 노드를 차수 순으로 내림차순 정렬한다
        3. 현재 노드의 차수와 바로 앞 노드의 차수가 같으면 서로 결합한다
     */

    static CircularPoly polymult(CircularPoly A, CircularPoly B) {
        CircularPoly C = new CircularPoly();
        PolyNode ap, bp, cp;
        ap = A.head;
        bp = B.head;


        /*
        증명
            1. A, B 순회에 m * n
            2. 차수가 같은 노드 찾는데 m OR n
            3. 조밀할 경우 찾을 필요 없으므로 2는 무효 >>> 총 시간복잡도 big-o(mn)
         */

        for (int i = 0; i < A.length(); i++) {
            for (int j = 0; j < B.length(); j++) {
                int coef = ap.coef * bp.coef;
                int exp = ap.exp + bp.exp;
                cp = C.head;

                if (cp == null) {
                    C.add_last_node(coef, exp);
                    cp = C.head;
                    bp = bp.link;
                    continue;
                }

                while (!cp.link.equals(C.head) && cp.exp != exp) {
                    cp = cp.link;
                }
                if (cp.exp == exp) {
                    cp.coef += coef;
                }
                if (cp.link.equals(C.head) && cp.exp != exp) {
                    C.add_last_node(coef, exp);
                }
                bp = bp.link;
            }
            ap = ap.link;
        }

        return C;
    }
}

public class CircularPoly_test {

    public static void main(String[] args) {

        CircularPoly A = new CircularPoly();
        CircularPoly B = new CircularPoly();

        for (int i = 4; i > 1; i--) {
            A.add_last_node((i*2), (i));
            B.add_last_node((i*3), (i+1));
        }

        A.print();
        B.print();

        CircularPoly C = CircularPoly.polymult(A, B);
        C.print();

    }

}
