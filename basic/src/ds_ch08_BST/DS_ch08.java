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

abstract class List_BST implements BST {

    @Override
    public boolean empty() {
        return false;
    }

    @Override
    public void insert(int key, Object data_in) {

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

    public Link_BST() {
        this.root = null;
    }

    @Override
    public boolean empty() {
        return root == null;
    }

    @Override
    public void insert(int key, Object data_in) {
        if (empty()) {
            root = new Node(key, data_in);
            return;
        }

        root = insert_routine(key, data_in, root);
    }
    private Node insert_routine(int key, Object data_in, Node p) {
        if (p == null) {
            return new Node(key, data_in);
        }
        else if (p.key > key) {
            p.left = insert_routine(key, data_in, p.left);
        }
        else if (p.key < key) {
            p.right = insert_routine(key, data_in, p.right);
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
        Node found = seek_routine(key, 1, root) ;
        return found == null ? null : found.data;
    }
    private Node seek_routine(int key, int lv, Node p) {
        if ((p.key != key) && (p.left == null) && (p.right == null)) {
            System.out.format("KEY %d NOT IN TREE\n", key);
            return null;
        }

        if (p.key == key) {
            System.out.format("KEY %d IN LEVEL %d: (key: %d, data: %s)\n", key, lv, key, p.data);
            return p;
        }
        else if (p.key > key) {
            return seek_routine(key, lv+1, p.left);
        }
        else {
            return seek_routine(key, lv+1, p.right);
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
         */
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

    public String describe() {
        StringBuilder sb = new StringBuilder();
        Queue<Node> q = new LinkedList<>();
        q.add(this.root);
        int cnt = 1;
        int lv = 1;
        Node polled;

        /*while (!q.isEmpty()) {
            if ((cnt) == Math.pow(2, lv-1)) {
                sb.append(String.format("\nLEVEL %d: ", lv++));
            }
            else {
                sb.append("\t, ");
            }
            cnt++;
            if (q.peek() != null) {
                polled = q.poll();
            }
            else {
                q.remove();
                continue;
            }
            sb.append(polled.data);
            q.add(polled.left);
            q.add(polled.right);
        }*/

        while (!q.isEmpty()) {
            polled = q.poll();
            if (q.isEmpty() || (q.peek().key < polled.key)) {
                sb.append(String.format("\nLEVEL %d: %s ", lv++, polled.data));
            }
            else {
                sb.append(polled.data);
                sb.append(" ");
            }
            if (polled.left != null) {
                q.add(polled.left);
            }
            if (polled.right != null) {
                q.add(polled.right);
            }

        }
        return sb.toString();
    }
}

public class DS_ch08 {

    public static void main(String[] args) {

        Random ran = new Random(273);

        BST lb = new Link_BST();
        for (int i = 0; i < 10; i++) {
            int x = ran.nextInt(100);
            lb.insert(x, x*10);
        }
        System.out.println(lb.describe());
        /*lb.delete(92);
        lb.delete(19);
        System.out.println(lb.describe());*/
        System.out.println(lb.seek(13));


    }


}
