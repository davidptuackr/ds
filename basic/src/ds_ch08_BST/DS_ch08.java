package ds_ch08_BST;

/*
풀이할 문제

*** 전제: 키는 전부 정수로 할 것

이진 탐색 트리
    형태: 순차, 연결 표현 둘 다
    연산: 삽입, 삭제, 검색, 결합, 분할, 재구성(트리 높이가 log n이 되도록 구성 >>> 균형 탐색 트리로 만드는 작업)

주의사항
    이진탐색트리 분할의 정확한 의미는
        하나는 키 값이 전부 x보다 작은 이진탐색트리로
        다른 하나는 키 값이 전부 x보다 큰 이진탐색트리로
    만든다는 것이다

힙
    형태: 순차, 연결 표현
    연산: 삽입, 삭제, 검색, 결합, 분할, 재구성(이진 탐색 트리를 힙으로 구성)
    힙을 이용한 우선순위 큐
    힙을 이용한 정렬

승자, 패자트리

인터페이스 BST 구조
    연산
        공백 검사 () bool
        삽입 (키, 값) void
        삭제 (키) void
        검색 (키) returns 값
        결합A (bst 2) returns bst
        분할 (키) returns [ bst1, bst2 ]
        재구성 () returns BST

BST 외 이진탐색트리 추가사항: 없음

BST 외 힙 추가사항: 우선순위 큐(힙을 쓰는 별도 클래스로 제작), 정렬 (힙의 멤버(메소드)로 추가)



승자, 패자트리 요구사항
    2^h-1개의 정렬된 배열 (키 값은 전부 정수)
    초기화
        - 리프에 배열 원소 삽입
        - 승패 판정: 루트까지 나머지 노드에 원소 삽입

승자, 패자트리 연산
    통합: 각 배열 원소의 우열을 비교해 한 배열로 통합



추가 문제

- 이진 탐색 트리가 균형되도록 삽입하는 balanced_insert 
 */

import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

interface BST {
    boolean empty();
    void insert (int key, Object data_in);
    void delete (int key);
    Object seek (int key);
    BST concat (BST other);
    BST[] split (int key);
    BST reshape ();
    String describe();
}

class List_BST implements BST {

    private class Node {

        int key;
        Object data;

        public Node(int key, Object data) {
            this.key = key;
            this.data = data;
        }

    }

    int h;
    int cnt;
    private Node root;
    private Node[] tree;

    public List_BST(int max_h) {
        this.tree = new Node[(int) Math.pow(2, max_h)];
        this.root = tree[0];
        this.cnt = 0;
        this.h = 0;
    }

    @Override
    public boolean empty() {
        return this.tree[0] == null;
    }

    @Override
    public void insert(int key, Object data_in) {

        /*
        순차 표현으로 구현한 이진 탐색 트리에서의 삽입

            과정
                - 빈 트리면 루트에 삽입하고 끝낸다
                - 아니면 쭉 돌아다니면서 들어갈 수 있는 자리를 찾아본다
                - 자리를 찾아냈다면 (1. 삽입, 2. 원소 수 증가, 3. 종료) 순으로 작업한다
                - 자리가 없다면 (1. 배열 확장, 2. 삽입 재시도, 3. 원소 수 증가, 4. 종료) 순으로 작업한다
                - 이미 있는 원소는 (1. 삽입 불가를 알림, 2. 종료) 순으로 작업한다

            상세 (ADL)
                삽입(키, 데이터) {
                    if empty tree :
                        루트(==tree[0]) = (키, 데이터)
                        삽입 종료

                    자리찾기 과정
                        1. 루트랑 비교했을 때
                            키가 작다면 왼쪽 자식과 다시 비교
                            키가 크다면 오른쪽 자식과 다시 비교
                        2. 비교 대상이
                            null이 아니면 null이 아닐 때까지 이동 + 비교
                            null이면 해당 위치에 삽입
                        3. 더 이상 비교할 수가 없다면 == 자리가 없다면
                            배열 확장 후 삽입 재시도

                    자리를 잡고 나면 원소 수 +1
                    종료
                }
         */


        if (this.empty()) {
            tree[0] = new Node(key, data_in);
            cnt++;
            return;
        }

        int i_cmp = 0;

        while (i_cmp < tree.length) {
            if (tree[i_cmp].key == key) {
                System.out.printf("UNABLE TO INSERT (%d, %s). KEY %d IS ALREADY EXISTS\n", key, data_in, key);
                return;
            }

            if (tree[i_cmp] == null) {
                tree[i_cmp] = new Node(key, data_in);
                return;
            }

            i_cmp = (tree[i_cmp].key > key) ? (i_cmp + 1) * 2 - 1 : (i_cmp + 1) * 2;
        }

        int i_iter = 1;
        Node[] tree_exp = new Node[tree.length * 2];
        tree_exp = exp_routine(tree_exp, i_iter);

        tree = tree_exp;
        tree[i_cmp] = new Node(key, data_in);
    }
    private Node[] exp_routine(Node[] tree_exp, int i_iter) {

        if ((i_iter * 2 + 1 < tree.length) && (tree_exp[i_iter * 2 + 1] != null)) {
            tree_exp = exp_routine(tree_exp, i_iter * 2 + 1);
        }
        tree_exp[i_iter] = new Node(tree[i_iter].key, tree[i_iter].data);
        if ((i_iter * 2 + 1 < tree.length) && (tree_exp[(i_iter + 1) * 2] != null)) {
            tree_exp = exp_routine(tree_exp, i_iter * 2 + 1);
        }

        return tree_exp;
    }

    @Override
    public void delete(int key) {

    }

    @Override
    public Object seek(int key) {
        return null;
    }

    @Override
    public BST concat(BST other) {
        return null;
    }

    @Override
    public BST[] split(int key) {
        return new BST[0];
    }

    @Override
    public BST reshape() {
        return null;
    }

    @Override
    public String describe() {
        return null;
    }
}

class Link_BST implements BST {

    private class Node {
        int key;
        Object data;
        Node left;
        Node right;

        protected Node(int key, Object data) {
            this.key = key;
            this.data = data;
            this.left = null;
            this.right = null;
        }

        protected Node(int key, Object data, Node left, Node right) {
            this.key = key;
            this.data = data;
            this.left = left;
            this.right = right;
        }
    }

    Node root;
    int cnt, h;

    public Link_BST() {
        this.root = null;
        this.cnt = 0;
        this.h = 0;
    }
    private Link_BST(Node to_root) {
        this.root = to_root;
    }

    @Override
    public boolean empty() {
        return root == null;
    }

    @Override
    public void insert(int key, Object data_in) {
        if (empty()) {
            root = new Node(key, data_in);
            cnt++;
            return;
        }

        root = insert_routine(key, data_in, root);

        if (cnt == Math.pow(2, h+1)) {
            h++;
        }
    }
    private Node insert_routine(int key, Object data_in, Node p) {
        if (p == null) {
            cnt++;
            return new Node(key, data_in);
        }
        else if (p.key > key) {
            p.left = insert_routine(key, data_in, p.left);
        }
        else if (p.key < key) {
            p.right = insert_routine(key, data_in, p.right);
        }
        else {
            System.out.printf("KEY %d ALREADY EXIST IN TREE\n", key);
        }
        return p;
    }

    @Override
    public void delete(int key) {
    /*
    과정
        1. key를 찾는다
        2. 찾아내면 다음과 같이 행동한다
            CASE 1. 리프인 경우
                삭제하고 종료
            CASE 2. 한 쪽 자식만 있는 경우
                fast-forward 결합
            CASE 3. 양 쪽 자식 모두 있는 경우
                방법 1. 왼쪽 서브트리에서 가장 큰 원소를 가져와 대체
                방법 2. 오른쪽 서브트리에서 가장 작은 원소를 가져와 대체
                방법 ? 오른쪽을 하나씩 땡겨오기 >>> 너무 복잡해보임
     */
        root = del_routine(key, root);
        cnt--;
        if (cnt == Math.pow(2, h)-1) {
            h--;
        }
    }
    private Node del_routine(int key, Node p) {
        // 탐색 단계
        if (p == null) { // 없을 경우
            System.out.printf("KEY %d NOT IN TREE", key);
            return null;
        }
        else if (p.key > key) {
            p.left = del_routine(key, p.left);
            return p;
        }
        else if (p.key < key) {
            p.right = del_routine(key, p.right);
            return p;
        }

        // 여기까지 오면 찾은 것
        if ((p.left == null) && (p.right == null)) {
            return null;
        }
        else if ((p.left != null) && (p.right == null)) {
            return p.left;
        }
        else if ((p.left == null) && (p.right != null)) {
            return p.right;
        }
        else {
            Node rep = p.left;
            Node rep_p = rep;
            while (rep.right != null) {
                rep_p = rep;
                rep = rep.right;
            }
            if (rep_p.equals(rep)) {
                rep.right = p.right;
            }
            else {
                rep_p.right = rep.left;
                rep.left = p.left;
                rep.right = p.right;
            }
            return rep;
        }
    }

    @Override
    public Object seek(int key) {
        Node found = seek_routine(key,root);
        if (found != null) {
            System.out.format("KEY %d IN TREE: (key: %d, data: %s)\n", key, key, found.data);
            return found.data;
        }
        else {
            System.out.format("KEY %d NOT IN TREE\n", key);
            return null;
        }
    }
    private Node seek_routine(int key, Node p) {
        if ((p.key != key) && (p.left == null) && (p.right == null)) {
            return null;
        }

        if (p.key == key) {
            return p;
        }
        else if (p.key > key) {
            return seek_routine(key, p.left);
        }
        else {
            return seek_routine(key, p.right);
        }
    }

    @Override
    public BST concat(BST other) {
        /*
        서로 다른 이진 탐색 트리의 이항 결합
        교재에선 한쪽 트리의 모든 키가 다른 트리의 모든 키보다 작다고 했지만
        여기서는 그렇지 않은 경우를 상정하고 작업한다.
        
        아이디어
            1. 아예 양쪽 트리 노드를 큐에 전부 넣은 다음 하나씩 꺼내가면서 재구성
                >>> 이 경우 양쪽의 원래 형체는 알아볼 수 없게 된다

        과정
            1. other를 큐에 삽입
            2. 하나씩 deq
            3. deq한 것을 this에 삽입
         */
        // 이것부터 시작 (완료: 051123 14)
        // >>> Link_BST = new ... <<<

        Link_BST conc = new Link_BST();
        Queue<Node> q = new LinkedList<>();
        Node polled;
        q.add(this.root);

        while (!q.isEmpty()) {
            polled = q.poll();
            conc.insert(polled.key, polled.data);
            if (polled.left != null) {
                q.add(polled.left);
            }
            if (polled.right != null) {
                q.add(polled.right);
            }
        }

        q.add(((Link_BST) other).root);

        while (!q.isEmpty()) {
            polled = q.poll();
            conc.insert(polled.key, polled.data);
            if (polled.left != null) {
                q.add(polled.left);
            }
            if (polled.right != null) {
                q.add(polled.right);
            }
        }

        return conc;
    }

    @Override
    public BST[] split(int key) {

        Node found = seek_routine(key, root);
        if (found == null) {
            System.out.printf("UNABLE TO SPLIT TREE: KEY %d NOT IN TREE", key);
            return null;
        }

        Link_BST lower = new Link_BST(found.left);
        Link_BST higher = new Link_BST(found.right);
        Queue<Node> lq = new LinkedList<>();
        Queue<Node> hq = new LinkedList<>();
        Node polled;

        if (this.root.key < found.key) {
            lq.add(this.root);
        }
        else {
            hq.add(this.root);
        }

        while (!lq.isEmpty() || !hq.isEmpty()) {
            if (lq.peek() != null) {
                polled = lq.poll();
                if (polled.key == found.key) {
                    continue;
                }
                lower.insert(polled.key, polled.data);
                if (polled.left != null) {
                    if (polled.left.key < found.key) {
                        lq.add(polled.left);
                    }
                    else {
                        hq.add(polled.left);
                    }
                }
                if (polled.right != null) {
                    if (polled.right.key < found.key) {
                        lq.add(polled.right);
                    }
                    else {
                        hq.add(polled.right);
                    }
                }
            }

            if (hq.peek() != null) {
                polled = hq.poll();
                if (polled.key == found.key) {
                    continue;
                }
                higher.insert(polled.key, polled.data);
                if (polled.left != null) {
                    if (polled.left.key < found.key) {
                        lq.add(polled.left);
                    }
                    else {
                        hq.add(polled.left);
                    }
                }
                if (polled.right != null) {
                    if (polled.right.key < found.key) {
                        lq.add(polled.right);
                    }
                    else {
                        hq.add(polled.right);
                    }
                }
            }

        }


        /*
        트리를 순회하면서
            key의 왼쪽 서브트리는 lower
            key의 오른쪽 서브트리는 higher

            p.key가 기준보다 크면 hq에 삽입
            p.key가 기준보다 작다면 lq에 삽입

            p의 왼쪽 자식이 key보다 작다면 왼쪽 서브트리 전부 삽입
            p의 오른쪽 자식은 검사해봐야 암

            lq에 있는 노드는 전부 lower에 삽입
            hq에 있는 노드는 전부 higher에 삽입
         */

        return new Link_BST[] {lower, higher};
    }

    @Override
    public BST reshape() {

        /*
        수정 (052823)

        중위순회를 이용해 재형성

        과정
            1. 트리 내의 노드 Node 배열 q에 다 빼낸다
            2. 꺼낸 노드 수+1 과 길이가 같은 정수 배열 ords를 생성한다
            3. q를 키 값 순으로 오름차순 정렬한다
            4. ords의 값을 완전 이진 트리 중위 순회 시 방문 순서로 한다
                ex.
                    ords[1]: 2^h ( < cnt)
                    ords[2]: 2^(h-1)
                    ords[3]: 2^h +1
                    ...
                    ords[LENGTH]: cnt
            5. 재형성 결과를 저장할 트리 rsh에 q[ord[1]], q[ord[2]] 순으로 집어넣는다
         */

        Queue<Node> tq = new LinkedList<>();
        Node[] q = new Node[cnt+1];
        int[] ords = new int[cnt+1];
        int qi = 1;
        tq.add(this.root);

        // 1. 트리 내의 노드 Node 배열 q에 다 빼낸다

        while (!tq.isEmpty()) {

            q[qi] = tq.poll();

            if (q[qi].left != null) { tq.add(q[qi].left); }
            if (q[qi].right != null) { tq.add(q[qi].right); }

            qi++;
        }

        // 2. ords 값을 완전 이진 트리 중위 순회 시 방문 순서로 한다

        int i_iter = 1;
        ords = ord_routine(i_iter, ords);

        // 3. q를 키 값 순으로 오름차순 정렬한다

        for (int i = 1; i < q.length; i++) {
            for (int j = i; j < q.length; j++) {
                if (q[i].key > q[j].key) {
                    Node tn = q[i];
                    q[i] = q[j];
                    q[j] = tn;
                }
            }
        }

        // 4. 재형성 결과를 저장할 트리 rsh에 q[ord[1]], q[ord[2]] 순으로 집어넣는다

        BST rsh = new Link_BST();
        for (int i = 1; i < cnt+1; i++) {
            for (int j = 1; j < cnt+1; j++) {
                if (ords[j] == i) {
                    rsh.insert(q[j].key, q[j].data);
                    break;
                }
            }

        }

        return rsh;
    }

    private static int i_ord_ = 1;
    private int[] ord_routine(int i_iter, int[] ords) {

        if (i_iter == 0) {
            return ords;
        }

        if (i_iter * 2 <= cnt) {
            ords = ord_routine(i_iter * 2, ords);
        }

        ords[i_ord_] = i_iter;
        i_ord_ += 1;

        if (i_iter * 2 + 1 <= cnt) {
            ords = ord_routine(i_iter * 2 + 1, ords);
        }

        return ords;
    }

    public String describe() {
        /*
        설정
            q: 레벨 순서로 뽑아내기 위한 큐. 초기에 루트 삽입
            is_lv_first: true일 경우 peek 한 노드가 해당 레벨의 첫 원소라는 의미. 레벨 정보 추가 목적.
            lv_first: 레벨 첫 원소의 키를 저장
         */

        StringBuilder sb = new StringBuilder();
        Queue<Node> q = new LinkedList<>();
        q.add(this.root);
        boolean is_lv_first = true;
        int lv_first = this.root.key;
        int lv = 1;
        Node peeked;

        while (!q.isEmpty()) {
            if (lv_first == q.peek().key) {
                sb.append(String.format("\nLEVEL %d: %s ", lv++, q.peek().data));
                is_lv_first = false;
            }
            else {
                sb.append(q.peek().data);
                sb.append(" ");
            }
            peeked = q.poll();

            if (peeked.left != null) {
                q.add(peeked.left);
                if (!is_lv_first) {
                    is_lv_first = true;
                    lv_first = peeked.left.key;
                }
            }
            if (peeked.right != null) {
                q.add(peeked.right);
                if (!is_lv_first) {
                    is_lv_first = true;
                    lv_first = peeked.right.key;
                }
            }
        }

        return sb.toString();
    }
}

public class DS_ch08 {

    public static void main(String[] args) {

        Random ran = new Random(273);

        BST lb = new Link_BST();
        BST lb2 = new Link_BST();

        for (int i = 0; i < 10; i++) {
            int x = ran.nextInt(100);
            lb.insert(x, x*10);
        }
        for (int i = 0; i < 15; i++) {
            int x = ran.nextInt(100);
            lb2.insert(x, x);
        }

        /*
        System.out.println(lb.describe());
        System.out.println(lb2.describe());
        */

        /*
        lb.delete(92);
        lb.delete(19);
        */

        /*
        System.out.println(lb.seek(13));
         */
/*
        BST conc = lb.concat(lb2);
        System.out.println(conc.describe());

        BST[] spl = conc.split(38);
        System.out.println(spl[0].describe());
        System.out.println(spl[1].describe());*/

        System.out.println(lb2.reshape().describe());

    }


}
