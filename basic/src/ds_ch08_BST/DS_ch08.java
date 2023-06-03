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

class List_BST implements BST {

    private class Node {

        int key;
        Object data;

        public Node(int key, Object data) {
            this.key = key;
            this.data = data;
        }

    }

    private int h;
    private int cnt;
    private Node root;
    private Node[] tree;

    public List_BST(int max_h) {
        this.tree = new Node[(int) Math.pow(2, max_h + 1) - 1];
        this.root = tree[0];
        this.cnt = 0;
        this.h = 0;
    }

    /*
    배열 내 이동 방법
        부모 >>> 좌측 자식: (i+1)*2-1
        부모 >>> 좌측 자식: (i+1)*2
        좌측 자식 >>> 부모: (i-1)/2
        우측 자식 >>> 부모: (i)/2-1
     */

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

            ADL 삽입(키, 데이터) {
                if empty tree :
                    루트(==tree[0]) = (키, 데이터)
                    삽입 종료

                int i_cmp = 0 >>> 배열 내 비교할 원소의 인덱스

                while ((i_cmp < 현재 배열 길이) AND (tree[i_cmp] is not null)) :
                    if tree[i_cmp] > key : i_cmp = i_cmp*2 + 1
                    else if tree[i_cmp] < key : i_cmp = i_cmp * 2 + 2
                    else : // 키가 이미 있는 경우
                        삽입 불가 알림 후 종료

                if i_cmp > 현재 배열 길이 : // 트리 확장 후 삽입
                    트리 확장;

                tree[i_cmp]에 삽입
                원소 수 +1
            }

            ADL private 확장(확장 배열, n_iter: 중위 순회 시 활용할 인덱스 전달용 매개변수) {
                재귀 + 중위 순회 활용하여 (기존 배열)*2 크기 배열에 기존 위치에 맞게 원소 복사
            }
         */

        if (empty()) {
            tree[0] = new Node(key, data_in);
            cnt++;
            return;
        }

        int i_cmp = 0;

        while ((i_cmp < tree.length) && (tree[i_cmp] != null)) {
            if (tree[i_cmp].key > key) {        // 새 키가 현 위치의 키보다 작으면 현 위치의 왼쪽 자식과 비교
                i_cmp = (i_cmp + 1) * 2 - 1;
            }
            else if (tree[i_cmp].key < key) {   // 새 키가 현 위치의 키보다 크면 현 위치의 오른쪽 자식과 비교
                i_cmp = (i_cmp + 1) * 2;
            }
            else {                              // 이미 있는 키라면 종료
                System.out.printf("UNABLE TO INSERT (%d, %s). KEY %d IS ALREADY EXISTS\n", key, data_in, key);
                return;
            }
        }

        // 공간이 없다면 지금은 확장 (아니면 reshape)
        if (i_cmp > tree.length) {
            tree = exp_routine(new Node[tree.length * 2 + 1], 0);
        }
        tree[i_cmp] = new Node(key, data_in);
        cnt++;
    }
    private Node[] exp_routine(Node[] tree_exp, int i_iter) {

        int i_next = (i_iter + 1) * 2 - 1;
        if ((i_next < tree.length) && (tree[i_next] != null)) {
            tree_exp = exp_routine(tree_exp, i_next);
        }

        tree_exp[i_iter] = new Node(tree[i_iter].key, tree[i_iter].data);

        if ((i_next + 1 < tree.length) && (tree[i_next + 1] != null)) {
            tree_exp = exp_routine(tree_exp, i_next + 1);
        }

        return tree_exp;
    }

    @Override
    public void delete(int key) {
        /*
        순차 표현으로 구현한 이진 탐색 트리에서의 삭제

            과정
                - 빈 트리면 빈 트리임을 알리고 종료한다
                - 아니면 쭉 돌아다니면서 삭제 대상이 있는지 찾아본다
                - 찾아냈다면 (1. 삭제, 2. 빈 자리 보충, 3. 원소 수 감소, 4. 종료) 순으로 작업한다
                - 없다면 (1. 없다고 알림, 2. 종료) 순으로 작업한다

            ADL 삭제(키) {
                if empty tree :
                    빈 트리임을 알리고 종료

                int i_cmp = 0 >>> 배열 내 비교할 원소의 인덱스

                while ((i_cmp < 현재 배열 길이) AND (tree[i_cmp].key 가 찾는 키와 같지 않을 동안)) :
                    if tree[i_cmp] > key : i_cmp = i_cmp*2 + 1
                    else if tree[i_cmp] < key : i_cmp = i_cmp * 2 + 2
                    else : // 키가 이미 있는 경우
                        삽입 불가 알림 후 종료

                if i_cmp >= 현재 배열 길이 : // 찾는 값이 없을 때
                    찾는 값이 없다고 알린 후 종료

                (수정: 060223)
                else if 단말일 때(==자식이 없을 때):
                    tree[i_cmp] = null
                else : >>> 자식이 하나 있을 때
                    (우측 최소도 가능하지만 일단)
                    지울 노드의 좌측 최대 탐색
                    if 좌측 최대가 좌측 자식이 있다면 :
                        좌측 서브트리 끌어올리기

                원소 수 -1
            }

            ADL private 레벨 변경(int i: 끌어올릴 서브트리 루트) {
                큐 준비 >>> 레벨 순서로 보충해야 하기 때문
                큐에 서브트리 루트 인덱스 먼저 삽입
                i_sbtr = i; >>> tree에서 서브트리 원소들의 인덱스

                while (서브트리 노드 전부 위치를 바꿀 때까지) :
                    i_sbtr = deq

                    if (i_sbtr+1 * 2)-1 != null : >>> 좌측 자식이 있다면
                        enq (i_sbtr+1 * 2)-1

                    if (i_sbtr+1 * 2) != null : >>> 우측 자식이 있다면
                        enq (i_sbtr+1 * 2)

                    if (i_sbtr % 2 == == 1) : >>> 좌측이었다면
                        tree[(i_sbtr)/2]에 보충
                    else : >>> 우측이었다면
                        tree[(i_sbtr-1)/2+1]에 보충
            }
         */

        /* 아이디어 (060123)
        자식이 하나건 둘이건 어차피 밑에있는 것은 다 끌어 올려야 한다
        그럼 그냥 아래 있는 애들을 level order 순회하듯이 넣으면 되는 것 아닌가?
         */
        /*
        아이디어 2(060123)
        자식이 하나건 둘이건 그냥 '한 줄'만 끌어올려도 되지 않을까?
        끌어올리는건 왼쪽 슬로프 / 오른쪽 슬로프 로 하고, 중간에 슬로프가 끊기면 옆에 자식 노드로 이어서 하면 되고
         */
        /*
        생각난 문제 (053123)
            자식이 둘인 노드를 삭제하는 경우
            왼쪽 최대 / 오른쪽 최소가 자식이 있다면?
            그러니까
                좌측 최대: 왼쪽 자식이 있는 경우
                우측 최소: 우측 자식이 있는 경우
                >>> 이러면 그 자식들은 부모를 잃게 되는 격

            해결방안 1 >>> XXX
                1. 좌측 최대는 좌측 자식 / 우측 최소는 우측 자식이 있는지 검사한다
                2. 있다면 삭제할 노드 자리 보충이 끝난 뒤 올려준다

            해결방안 2 >>> XXX
                1. 아이디어 2에서 이야기한 것과 같이 슬로프를 끌어올린다
                    이 때 좌측 서브트리의 오른쪽 슬로프를 쓴다


        전체 해결방안
            삭제할 노드가
                자식이 하나면 그 자식+자식의 서브트리를 끌어올리고
                자식이 둘이면
                    좌측 최대 / 우측 최소를 삭제 위치로 가져온 다음
                    좌측 최대는 좌측 서브트리가 있다면 / 우측 최소는 우측 서브트리가 있다면
                    자식이 하나인 노드를 삭제할 때처럼 서브트리를 끌어올린다

        자식이 하나건 둘이건 한 쪽 서브트리를 끌어올리는 작업과 좌측 최대 / 우측 최소를 찾는 작업이 필요하다 !!!
         */

        if (empty()) {
            System.out.println("UNABLE TO DELETE. TREE IS EMPTY");
            return;
        }

        int i_cmp = 0;

        while ((i_cmp < tree.length) && (tree[i_cmp].key != key)) {
            if (tree[i_cmp].key > key) {        // 새 키가 현 위치의 키보다 작으면 현 위치의 왼쪽 자식과 비교
                i_cmp = (i_cmp + 1) * 2 - 1;
            }
            else if (tree[i_cmp].key < key) {   // 새 키가 현 위치의 키보다 크면 현 위치의 오른쪽 자식과 비교
                i_cmp = (i_cmp + 1) * 2;
            }
        }
        if (i_cmp >= tree.length) {
            System.out.printf("KEY %d NOT IN TREE \n", key);
        }
        else if (
                ((i_cmp * 2) - 1 >= tree.length)
                || (tree[(i_cmp + 1) * 2 - 1] == null && tree[(i_cmp + 1) * 2] == null)
        ) { // 단말일 경우
            tree[i_cmp] = null;
        }
        else { // 자식이 있을 경우
            int i_lmax = (i_cmp + 1) * 2 - 1;
            while (((i_lmax + 1) * 2 < tree.length) && (tree[(i_lmax + 1) * 2] != null)) {
                i_lmax = (i_lmax + 1) * 2;
            }

            if (tree[i_lmax] == null) { // 삭제할 노드에 좌측 서브트리가 없다면 우측 서브트리 끌어올리기
                lvl_routine(i_cmp, (i_cmp + 1) * 2);
            }
            else if (tree[(i_lmax + 1) * 2 - 1] != null) {
                // 좌측 최대에 좌측 서브트리가 있다면 좌측 최대 위치로 서브트리를 끌어올리고 삭제할 노드 위치에 좌측 최대 보충
                tree[i_cmp] = new Node(tree[i_lmax].key, tree[i_lmax].data);
                lvl_routine(i_lmax, (i_lmax + 1) * 2 - 1);
            }
            else {
                tree[i_cmp] = new Node(tree[i_lmax].key, tree[i_lmax].data);
                tree[i_lmax] = null;
            }

        }
        cnt--;
    }

    private void lvl_routine(int rploc_strt, int iter_strt) {

        Queue<Integer> q_iter = new LinkedList<>();
        Queue<Integer> q_rp = new LinkedList<>();
        int i_iter = iter_strt;
        int i_rploc = rploc_strt;

        q_iter.add(i_iter);
        q_rp.add(i_rploc);

        while (i_iter < tree.length && !q_iter.isEmpty() && !q_rp.isEmpty()) {
            i_iter = q_iter.poll();
            i_rploc = q_rp.poll();

            if (((i_iter+1)*2-1 < tree.length) && (tree[(i_iter+1)*2-1] != null)) {
                q_iter.add((i_iter+1)*2-1);
                q_rp.add((i_rploc+1)*2-1);

            }
            if (((i_iter+1)*2 < tree.length) && (tree[(i_iter+1)*2] != null)) {
                q_iter.add((i_iter+1)*2);
                q_rp.add((i_rploc+1)*2);
            }

            tree[i_rploc] = new Node(tree[i_iter].key, tree[i_iter].data);
            tree[i_iter] = null;
        }
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

        /*BST lb = new Link_BST();
        BST lb2 = new Link_BST();

        for (int i = 0; i < 10; i++) {
            int x = ran.nextInt(100);
            lb.insert(x, x*10);
        }
        for (int i = 0; i < 15; i++) {
            int x = ran.nextInt(100);
            lb2.insert(x, x);
        }*/

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

        BST lb = new List_BST(3);

        for (int i = 0; i < 12; i++) {
            int x = ran.nextInt(100);
            lb.insert(x, x);
        }

        /* 테스트 대상
            19 >>> 좌측이 있는데 하나만 있는 경우 O
            35 >>> 좌측 서브트리를 끌어올려야 함 O
            71 >>> 우측만 있는 경우 (즉 좌측이 null) (O)
            96 >>> 단말 (O)
         */
        lb.delete(9);
    }


}
