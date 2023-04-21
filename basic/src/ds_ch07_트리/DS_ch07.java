package ds_ch07_트리;

/*
풀이할 문제

    - 07.5, 07.6, 07.7, 07.8, 07.16
    - 이진 트리 및 그 연산 구현
        순차 표현
        연결 표현
        연산: 복사, 공백 검사, 동일 여부 검사, 삽입, 삭제
    - 문자열로 표현한 트리를 변환
    - 순회: 전위, 중위, 후위, 레벨 순서
        재귀 표현
        비 재귀 표현
    - 수식 변환 및 계산: 전위, 중위, 후위 (DEP)
        재귀 표현
        비 재귀 표현
    - 좌우 반전 (07.17)
    - 일반 트리 변환, 순회 (단, 재귀적으로)
    - 포리스트 변환, 순회 (단, 재귀적으로)
 */

/*
    리팩토링 계획 (042123)
        1. 비슷한 코드 반복 최소화
            ex. 전위 순회로 목표값을 찾는 메소드는 클래스에 정의된 전위 순회 함수를 사용할 수 있도록 수정

        2. 인터페이스 수정
        3. private method, inner class, member 필요성 재검토
        4. 리팩토링한 결과는 접두사 'adv_' 붙일 것
 */

import java.util.Arrays;
import java.util.Stack;

interface Binary_Tree {

    Binary_Tree copy();
    boolean is_empty();
    boolean is_equal(Binary_Tree t);
    void insert(Object data_in);
    void delete(Object data_del);

    void rec_pre_order();
    void rec_in_order();
    void rec_post_order();
    void pre_order();
    void in_order();
    void post_order();

    void rec_cal_pre_order();
    void rec_cal_in_order();
    void rec_cal_post_order();
    void cal_pre_order();
    void cal_in_order();
    void cal_post_order();

    Binary_Tree inverse();
}

class List_Binary_Tree implements Binary_Tree {

    Object[] data;
    int h;

    public List_Binary_Tree(int h) {
        data = new Object[(int) Math.pow(2, h + 1)];
        this.h = h;
    }

    @Override
    public Binary_Tree copy() {

        // 과정은 전위 순회를 약간 변형한 모습

        if (is_empty()) {
            System.out.println("EMPTY TREE");
            return null;
        }

        List_Binary_Tree cpy = new List_Binary_Tree((int) (Math.log(data.length) / Math.log(2)));
        boolean[] mark = new boolean[data.length];
        int p = 1;

        while (p != 0) {
            if (!mark[p]) {
                cpy.data[p] = data[p];
                mark[p] = true;
            }
            if ((p*2 < data.length) && (data[p*2] != null) && (!mark[p*2])) {
                p *= 2;
            }
            else if ((p*2+1 < data.length) && (data[p*2+1] != null) && (!mark[p*2+1])) {
                p = p * 2 + 1;
            }
            else {
                p /= 2;
            }
        }

        System.out.println("복제 대상");
        this.pre_order();

        System.out.println("복제 결과");
        cpy.pre_order();

        return cpy;
    }

    @Override
    public boolean is_empty() {
        return data[1] == null;
    }

    @Override
    public boolean is_equal(Binary_Tree t) {

        // 이것도 전위 순회 변형
        if (this.is_empty() || t.is_empty()) {
            System.out.println("EMPTY TREE");
            return false;
        }
        boolean[] mark = new boolean[data.length];
        int p = 1;

        while (p != 0) {
            if (!mark[p]) {
                if (((List_Binary_Tree) t).data[p] != data[p]) {
                    return false;
                }
                mark[p] = true;
            }
            if (
                    (p*2 < data.length) &&
                    (data[p*2] != null) &&
                    (((List_Binary_Tree) t).data[p*2] != null) &&
                    (!mark[p*2])
            ) {
                p *= 2;
            }
            else if (
                    (p*2+1 < data.length) &&
                    (data[p*2+1] != null) &&
                    (((List_Binary_Tree) t).data[p*2+1] != null) &&
                    (!mark[p*2+1])
            ) {
                p = p * 2 + 1;
            }
            else {
                p /= 2;
            }
        }

        return true;
    }

    @Override
    public void insert(Object data_in) {
        int loc = 1;
        while (data[loc] != null) {
            loc++;
            if (loc > data.length) {
                System.out.println("TREE IS FULL");
                return;
            }
        }
        data[loc] = data_in;
    }
    public void insert(Object data_in, int loc) {
        if ((!is_empty()) && (
                (loc >= data.length) ||
                (loc <= 0) ||
                (data[loc / 2] == null)
        )) System.out.println("INVALID LOCATION");
        else data[loc] = data_in;
    }
    @Override
    public void delete(Object data_del) {
        /*
        CASE 1. 단말 노드의 경우
            - 그냥 부모로부터의 연결만 끊어내면 된다
        CASE 2. 서브트리의 차수가 1인 경우
            - 자식을 가져다 채워넣으면 된다
        CASE 3. 차수가 2 이상인 경우
            - 단말 하나 가져와서 채우면 되나?

        결론
            1. 단말 노드는 부모로부터의 연결만 끊으면 된다
            2. 그 외엔 리프 하나 가져와서 채워넣는 것으로 하자. 어차피 꼭 완전 이진 트리일 필요는 없다.
            3. 채워 넣을 리프는 전위 순회했을 때 제일 먼저 만나는 것을 쓰자

        과정
            1. 트리를 전위 순회하면서 삭제 대상을 찾는다
            2. 찾아낼 경우 다음과 같이 처리한다
                2.1 위치가 리프인 경우
                    - 지운다
                2.2 위치가 리프가 아닐 경우
                    - 전위 순회로 제일 끝에 있는 리프 노드 하나를 가져와서 덮어쓴다
                    - 방금 썼던 리프 노드는 없앤다
         */

        if (is_empty()) {
            System.out.println("EMPTY TREE");
            return;
        }

        boolean[] mark = new boolean[data.length];
        int p = 1, q = 1;

        while ((p != 0)) {
            // 어차피 is_empty로 검사했는데 여기 if에서 비었는지 검사할 필요가 있나?
            if (data[p].equals(data_del) && (!mark[p])) { // 일치하는 곳 찾아내면
                if ((p*2 > data.length) || (data[p*2] == null && data[p*2+1] == null)) {
                    data[p] = null;
                    break;
                }
                Arrays.fill(mark, false); // 찾아냈으니 초기화해도 괜찮겠지?

                while ((q != 0) && (q*2 < data.length)) {
                    mark[q] = true;
                    if ((data[q*2] != null) && (!mark[q*2])) {
                        q *= 2;
                    }
                    else if ((data[q*2+1] != null) && (!mark[q*2+1])) {
                        q = q * 2 + 1;
                    }
                    else {
                        q /= 2;
                    }
                }
                data[p] = data[q];
                data[q] = null;
                break;
            }
            else {
                mark[p] = true;
            }

            if ((p*2 < data.length) && (data[p*2] != null) && (!mark[p*2])) {
                p *= 2;
            }
            else if ((p*2+1 < data.length) && (data[p*2+1] != null) && (!mark[p*2+1])) {
                p = p * 2 + 1;
            }
            else {
                p /= 2;
            }
        }

        if (p != 0) { System.out.println("REMOVE " + data_del); }
        else { System.out.println("COULD NOT FIND " + data_del); }
    }

    @Override
    public void rec_pre_order() {
        rec_pre_order(1);
        System.out.println();
    }
    @Override
    public void rec_in_order() {
        rec_in_order(1);
        System.out.println();
    }
    @Override
    public void rec_post_order() {
        rec_post_order(1);
        System.out.println();
    }

    private void rec_pre_order(int cursor) {
        if ((cursor == 0) || (cursor >= data.length)) {
            return;
        }

        if (data[cursor] == null) {
            return;
        }

        System.out.print(data[cursor] + " ");

        rec_pre_order(cursor * 2);
        rec_pre_order(cursor * 2 + 1);
    }
    private void rec_in_order(int cursor) {
        if ((cursor == 0) || (cursor >= data.length)){
            return;
        }

        if(data[cursor] == null) {
            return;
        }

        rec_in_order(cursor * 2);
        System.out.print(data[cursor] + " ");
        rec_in_order(cursor * 2 + 1);
    }
    private void rec_post_order(int cursor) {
        if ((cursor == 0) || (cursor >= data.length)) {
            return;
        }

        if (data[cursor] == null) {
            return;
        }

        rec_post_order(cursor * 2);
        rec_post_order(cursor * 2 + 1);
        System.out.print(data[cursor] + " ");
    }

    @Override
    public void pre_order() {
        /*
        과정
            1. 루트 방문
            2. 다음 조건을 만족할 시 좌측 서브트리 방문. 이 때 방문한 노드는 별도 표시
                2.1 조건 1. 좌측 서브트리가 있는가?
                2.2 조건 2. 트리 범위를 벗어나지 않는가?
            3. 다음 조건을 만족할 시 우측 서브트리 방문. 이 때 방문한 노드는 별도 표시
                3.1 조건 1. 우측 서브트리가 있는가>
                3.2 조건 2. 트리 범위를 벗어나지 않는가?
            4. 종료 조건: p/2 == 0일 때
         */

        if (is_empty()) {
            System.out.println("EMPTY TREE");
            return;
        }

        boolean[] mark = new boolean[data.length];
        int p = 1;

        while (p != 0) {
            if (!mark[p]) {
                System.out.print(data[p] + " ");
                mark[p] = true;
            }
            if ((p*2 < data.length) && (data[p*2] != null) && (!mark[p*2])) {
                p *= 2;
            }
            else if ((p*2+1 < data.length) && (data[p*2+1] != null) && (!mark[p*2+1])) {
                p = p * 2 + 1;
            }
            else {
                p /= 2;
            }
        }
        System.out.println();
    }
    @Override
    public void in_order() {
        if (is_empty()) {
            System.out.println("EMPTY TREE");
            return;
        }
        boolean[] mark = new boolean[data.length];
        int p = 1;
        /*
        루트 방문 조건
            p*2 > data.length일 때
            왼쪽을 다 돌았을 때
            좌측 서브트리가 없을 때
        왼쪽 이동 조건: 왼쪽이 null이 아닐 것 + 현재 위치가 리프가 아닐 것 + 방문하지 않았을 것
        오른쪽 이동 조건: 루트를 방문했을 것

         */
        while (p != 0) {
            if ((p*2 >= data.length) || (mark[p*2] && !mark[p*2+1]) || (!mark[p*2+1] && (data[p*2] == null))) {
                System.out.print(data[p] + " ");
                mark[p] = true;
            }

            if ((p*2 < data.length) && (data[p*2] != null) && (!mark[p*2])) {
                p *= 2;
            }
            else if ((p*2+1 < data.length) && (data[p*2+1] != null) && (!mark[p*2+1])) {
                p = p * 2 + 1;
            }
            else {
                p /= 2;
            }
        }
        System.out.println();
    }
    @Override
    public void post_order() {
        if (is_empty()) {
            System.out.println("EMPTY TREE");
            return;
        }
        boolean[] mark = new boolean[data.length];
        int p = 1;
        /*
        루트 방문 조건
            p*2 > data.length거나 양쪽 서브트리 모두 null일 때 >>> 리프
            양쪽 다 돌았을 때 >>> mark[p*2], [p*2+1] >>> true
            한 쪽만 있는 경우: 한 쪽을 다 방문했을 때
        좌/우 이동 조건: 방문하지 않았을 때

         */
        while (p != 0) {
            if (p*2 >= data.length) {
                System.out.print(data[p] + " ");
                mark[p] = true;
            }
            else if ( (p*2 < data.length) && (data[p*2] == null) && (data[p*2+1] == null) ) {
                System.out.print(data[p] + " ");
                mark[p] = true;
            }
            else if (mark[p*2] && mark[p*2+1]) {
                System.out.print(data[p] + " ");
                mark[p] = true;
            }
            else if (((mark[p*2]) && (data[p*2+1] == null)) || ((data[p*2] == null) && (mark[p*2+1]))) {
                System.out.print(data[p] + " ");
                mark[p] = true;
            }

            if ((p*2 < data.length) && (data[p*2] != null) && (!mark[p*2])) {
                p *= 2;
            }
            else if ((p*2+1 < data.length) && (data[p*2+1] != null) && (!mark[p*2+1])) {
                p = p * 2 + 1;
            }
            else {
                p /= 2;
            }
        }
        System.out.println();
    }

    /*
        1. 수식 변환
        2. 입력받은 수식을 전위 / 중위 / 후위 표기로 변환 >>> 스택 활용
        3. 변환한 수식을 슨회하며 계산

        내가 뭘 하려는거지
        rec_cal-: 수식을 재귀를 이용한 트리 전위/중위/후위 순회로 계산할 수 있게 변형한 다음 계산 결과 산출
        cal-: 수식을 트리 전위/중위/후위 순회로 계산할 수 있게 변형한 다음 계산 결과 산출
        str_to_bt: 중위식을 전위/중위/후위 순회로 계산할 수 있는 형태의 트리로 변환
         */

    @Override
    public void rec_cal_pre_order() {

    }

    @Override
    public void rec_cal_in_order() {

    }

    @Override
    public void rec_cal_post_order() {

    }

    @Override
    public void cal_pre_order() {

    }

    @Override
    public void cal_in_order() {

    }

    @Override
    public void cal_post_order() {

    }

    @Override
    public Binary_Tree inverse() {
        List_Binary_Tree inv = new List_Binary_Tree(this.h);

        for (int i = 1; i < (Math.pow(2, this.h+1)); i*=2) {
            for (int j = 0; j < i; j++) {
                inv.data[i*2-j-1] = data[i+j];
            }
        }

        return inv;
    }

    static List_Binary_Tree expr_to_bt(String expr, int expr_type) {

        String fx;

        if (expr_type == 0) { fx = expr_to_prfx(expr); }
        else { fx = expr_to_pofx(expr); }

        List_Binary_Tree fx_bt = new List_Binary_Tree((int) Math.round(Math.log(fx.length() / Math.log(2))));

        int i;

        for (
                i = 0;
                fx.charAt(i) == '+' || fx.charAt(i) == '-' || fx.charAt(i) == '*' || fx.charAt(i) == '/';
                i++
            ) {
            fx_bt.insert(fx.charAt(i));
        }

        int start = 1;

        while (fx_bt.data[2*start] != null || fx_bt.data[2*start+1] != null || start % 2 != 0) {
            start++;
        }

        start *= 2;

        for ( ; i < fx.length(); i++) {
            fx_bt.insert(fx.charAt(i), start);
            if (fx_bt.data[(start+1) / 2] == null) {
                start /= 2;
                if (fx_bt.data[start] != null) {
                    start += 1;
                }
            }
            else start += 1;
        }

        return fx_bt;
    }

    private static String expr_to_prfx(String expr) {
        StringBuilder sb = new StringBuilder();
        Stack<Character> ops = new Stack<>();
        Stack<Character> prfx = new Stack<>();

        for (int i = expr.length()-1; i >= 0; i--) {
            char token = expr.charAt(i);

            switch (token) {
                case '*': case '/': case '(':
                    ops.push(token);
                    break;
                case '+': case '-':
                    if (ops.isEmpty()) {
                        ops.push(token);
                    }
                    else {
                        while (!ops.isEmpty() && (get_ps(ops.peek()) <= get_ps(token))) {
                            prfx.push(ops.pop());
                        }
                        ops.push(token);
                    }
                    break;
                case ')':
                    while (!ops.peek().equals('(')) {
                        prfx.push(ops.pop());
                    }
                    ops.pop();
                default:
                    prfx.push(token);
                    break;
            }
        }

        while (!ops.isEmpty()) {
            prfx.push(ops.pop());
        }
        while (!prfx.empty()) {
            sb.append(prfx.pop());
        }

        return sb.toString();
    }
    private static String expr_to_pofx(String expr) {
        StringBuilder sb = new StringBuilder();
        Stack<Character> ops = new Stack<Character>();

        for (int i = 0; i < expr.length(); i++) {
            char token = expr.charAt(i);

            switch (token) {
                case '*': case '/': case '(':
                    ops.push(token);
                    break;
                case '+': case '-':
                    if (ops.isEmpty()) {
                        ops.push(token);
                    }
                    else {
                        while (!ops.isEmpty() && (get_ps(ops.peek()) <= get_ps(token))) {
                            sb.append(ops.pop());
                        }
                        ops.push(token);
                    }
                    break;
                case ')':
                    while (!ops.peek().equals('(')) {
                        sb.append(ops.pop());
                    }
                    ops.pop();
                default:
                    sb.append(token);
                    break;
            }
        }

        while (!ops.isEmpty()) {
            sb.append(ops.pop());
        }

        return sb.toString();
    }
    private static int get_ps(char op) {

        int ps = 0;

        switch (op) {
            case '(': break;
            case '+': case '-': ps = 1;
            case '*': case '/': ps = 2;
        }

        return ps;
    }

    /*
    List_Binary_Tree 리팩토링 계획 (042123)

        1. 전위 순회를 사용하는 메소드는 클래스 내의 메소드를 사용할 수 있도록 개선
            >>> 코드 반복 최소회
            대상
                copy, is_equal, 모든 순회 메소드
        2. 멤버 필요성 재검토
            대상: 전체

     */

    private int ord;

    public int[] adv_pre_order() {
        int [] locs = new int[data.length-1];
        this.ord = 1;
        adv_pre_order(locs, 1, 1);
        return locs;
    }

    private void adv_pre_order(int[] locs, int loc, int ord) {
        locs[ord-1] = loc;
        this.ord += 1;
        if (loc*2 >= 16) {
            return;
        }
        adv_pre_order(locs, loc * 2, this.ord);
        adv_pre_order(locs, loc * 2 + 1, this.ord);
    }

    public Binary_Tree adv_copy() {
        if (is_empty()) return null;

        List_Binary_Tree cpy = new List_Binary_Tree(h);
        if (this.data.length - 1 >= 0) System.arraycopy(data, 1, cpy.data, 1, this.data.length - 1);

        return cpy;
    }

    public boolean adv_is_equal(Binary_Tree t) {

        int[] po_this = adv_pre_order();
        int[] po_t = ((List_Binary_Tree) t).adv_pre_order();

        for (int i = 0; i < po_this.length; i++) {
            if (this.data[po_this[i]] != ((List_Binary_Tree) t).data[po_t[i]]) return false;
        }

        return true;
    }

}

class Link_BT implements Binary_Tree {

    private class Node {
        Object data;
        Node left;
        Node right;

        public Node(Object data) {
            this.data = data;
            left = null;
            right = null;
        }
    }
    private class Del_info {
        Node tp;
        Node t;
        boolean is_left;

        public Del_info(Node tp, Node t, boolean is_left) {
            this.tp = tp;
            this.t = t;
            this.is_left = is_left;
        }
    }
    private class Filler {
        Node[] q;
        Node fp;
        int front;
        int rear;

        private Filler() {
            q = new Node[(int) Math.pow(2, h)];
            front = 0;
            rear = 0;
        }

        private void enq(Node data_in) {
            q[rear] = data_in;
            rear = (rear+1) % q.length;
        }

        private void deq() {
            q[front] = null;
            front = (front+1) % q.length;
            fp = q[front];
        }
    }

    int h;
    Node root;
    private int stat;
    private Filler filler;

    public Link_BT(int h) {
        this.h = h;
        root = null;
        filler = new Filler();
    }

    public void adv_insert(Object data_in) {
        if (this.is_empty()) {
            root = new Node(data_in);
            filler.enq(this.root);
            filler.fp = filler.q[filler.front];
            return;
        }

        Node p = new Node(data_in);
        filler.enq(p);
        if (filler.fp.left == null) {
            filler.fp.left = p;
        }
        else if (filler.fp.right == null) {
            filler.fp.right = p;
            filler.deq();
        }
    }

    @Override
    public Binary_Tree copy() {
        /*
        전위 탐색 응용하여 복사
         */
        Link_BT cpy = new Link_BT(this.h);
        cpy.root = new Node(this.root.data);
        cpy.copy(this.root, cpy.root);

        return cpy;
    }
    private void copy(Node tp, Node cp) {
        if ((tp.left == null) && (tp.right == null)) {
            return;
        }
        if (tp.left != null) {
            cp.left = new Node(tp.left.data);
            this.copy(tp.left, cp.left);
        }
        else {
            return;
        }
        if (tp.right != null) {
            cp.right = new Node(tp.right.data);
            this.copy(tp.right, cp.right);
        }
    }

    @Override
    public boolean is_empty() {
        return root == null;
    }

    @Override
    public boolean is_equal(Binary_Tree t) {

        stat = 0;

        if (t.is_empty() || this.is_empty()) {
            return false;
        }

        is_equal(this.root, ((Link_BT) t).root);

        return stat == 0;
    }
    private void is_equal(Node p, Node tp) {
        if ((!p.data.equals(tp.data)) || (stat == 1)) {
            stat = 1;
            return;
        }
        if (
            ((p.left == null) && (p.right == null)) &&
            ((tp.left == null) && (tp.right == null))
        ) {
            return;
        }

        is_equal(p.left, tp.left);
        is_equal(p.right, tp.right);
    }


    @Override
    public void insert(Object data_in) {
        if (this.is_empty()) {
            root = new Node(data_in);
            return;
        }
        stat = 0;

        insert(data_in, root, 0);
    }
    private void insert(Object data_in, Node p, int loc) {
        /*
         이 insert로는 완전 이진 트리를 만들 수 없다 >>> 좌편향된 트리 형성
         완전 이진 트리로 만들려면 순차 표현으로 구현하는게 편하다 >>> 인덱스만 증감시키면 되니까
         굳이 연결 표현으로 완전 이진 트리를 만들려면 뭔가 기준이 필요해 보인다
            >>> 키 값을 갖거나, 전체 트리 정보를 저장해두거나
         즉, 연결 표현 이진 트리는 삽입, 삭제가 쉽다는 말은 이 기준을 이용했기 때문일 것 >>> 이진 탐색 트리
        */

        if (loc >= this.h) {
            return;
        }
        if (stat == 1) return;

        if (p.left == null) {
            p.left = new Node(data_in);
            stat = 1;
        }
        else if (p.right == null) {
            p.right = new Node(data_in);
            stat = 1;
        }
        else {
            insert(data_in, p.left, loc+1);
            insert(data_in, p.right, loc+1);
        }

    }

    @Override
    public void delete(Object data_del) {
        if (is_empty()) {
            System.out.println("EMPTY TREE");
            return;
        }
    /*
    일치하지 않을 때
        case 1. 리프인 경우
            back
        case 2. 그 외의 경우
            trace
    일치할 때
        case 1. 리프인 경우
            해당 노드 + 부모의 left / right null
        case 2. 리프가 아닌 경우
            맨 끝에서 하나 가져오기
                해야되는 작업
                1. 맨 끝의 부모 노드 null
                2. 가져온 노드의 left, right 재설정
                3. 지운 노드의 부모 노드 링크도 다시 설정
    필요한 것
        지울 노드 정보
        지울 노드의 부모 노드 정보
        맨 끝 노드
        맨 끝 노드의 부모

    delete 본체는 non-recursive, 목표 찾기는 recursive 하게?
     */

        Del_info del_info  = seek(data_del, root, root);
        if (del_info == null) {
            System.out.printf("%s NOT IN", data_del);
            return;
        }

        Node t = del_info.t;
        Node tp = del_info.tp;

        if ((t.left == null) && (t.right == null)) {
            if (del_info.is_left) {
                tp.left = null;
            }
            else {
                tp.right = null;
            }
        }
        else {
            Node l = root, lp = root;
            boolean is_left = false;

            while ((l.left != null) || (l.right != null)) { // 어쩌다보니 완전 상태 유지하면서 지우도록 만들었네?
                lp = l;
                if ((l.left != null) && (l.right == null)) { // 갈 곳이 왼쪽밖에 없을 때만 왼쪽으로 이동
                    l = l.left;
                    is_left = true;
                } else { // 아니면 오른쪽으로 먼저 이동
                    l = l.right;
                    is_left = false;
                }
            }

            l.left = t.left;
            l.right = t.right;

            if (del_info.is_left) {
                tp.left = l;
            }
            else {
                tp.right = l;
            }

            if (is_left) {
                lp.left = null;
            }
            else {
                lp.right = null;
            }

        }

    }
    private Del_info seek(Object data_target, Node tp, Node t) {
        /*
        전위 탐사로 대상 노드 색출
        자식이 목표 데이터를 갖고 있으면 return [ p, c, is_left ]
         */

        if (t.data.equals(data_target)) { // 값을 찾아내면
            stat = 1;
            if (tp.left.data.equals(t.data)) { // 찾는 값이 부모의 왼쪽에 있다면
                return new Del_info(tp, t, true);
            }
            else { // 오른쪽에 있다면
                return new Del_info(tp, t, false);
            }
        }
        if ((t.left == null) && (t.right == null)) return null;

        Del_info del_info = seek(data_target, t, t.left);
        if (stat == 1) return del_info;

        del_info = seek(data_target, t, t.right);
        if (stat == 1) return del_info;

        // 한바퀴 돌았는데 못찾았으면
        return null;
    }

    @Override
    public void rec_pre_order() {
        rec_pre_order(root);
        System.out.println();
    }
    private void rec_pre_order(Node p) {
        System.out.print(p.data + " ");
        if (p.left != null) {
            rec_pre_order(p.left);
        }
        if (p.right != null) {
            rec_pre_order(p.right);
        }
    }

    @Override
    public void rec_in_order() {

    }

    @Override
    public void rec_post_order() {

    }

    private class Trace_info {
        Node n;
        boolean lv;
        boolean rv;

        Trace_info(Node data, boolean lv, boolean rv) {
            this.n = data;
            this.lv = lv;
            this.rv = rv;
        }
    }

    @Override
    public void pre_order() {
        /*
        스택을 이용해보자 (O)
         */
        Stack<Trace_info> tracer = new Stack<>();
        tracer.push(new Trace_info(this.root, false, false));

        while (!tracer.empty()) {
            Trace_info p = tracer.peek();
            if (!(p.lv || p.rv)) {
                System.out.print(p.n.data + " ");
            }

            if (!p.lv) {
                p.lv = true;
                if (p.n.left != null) {
                    tracer.push(new Trace_info(p.n.left, false, false));
                }
            }
            else if (!p.rv) {
                p.rv = true;
                if (p.n.right != null) {
                    tracer.push(new Trace_info(p.n.right, false, false));
                }
            }
            else {
                tracer.pop();
            }

            if (p.n.left == null && p.n.right == null) {
                tracer.pop();
            }
        }
        System.out.println();
    }

    @Override
    public void in_order() {
        Stack<Trace_info> tracer = new Stack<>();
        tracer.push(new Trace_info(this.root, false, false));

        while (!tracer.empty()) {
            Trace_info p = tracer.peek();

            if (!p.lv) {
                p.lv = true;
                if (p.n.left != null) {
                    tracer.push(new Trace_info(p.n.left, false, false));
                }
            }
            else if (!p.rv) {
                System.out.print(p.n.data + " ");
                p.rv = true;
                if (p.n.right != null) {
                    tracer.push(new Trace_info(p.n.right, false, false));
                }
            }
            else {
                tracer.pop();
            }

            if (p.n.left == null && p.n.right == null) {
                System.out.print(p.n.data + " ");
                tracer.pop();
            }
        }
        System.out.println();
    }

    @Override
    public void post_order() {
        Stack<Trace_info> tracer = new Stack<>();
        tracer.push(new Trace_info(this.root, false, false));

        while (!tracer.empty()) {
            Trace_info p = tracer.peek();

            if (!p.lv) {
                p.lv = true;
                if (p.n.left != null) {
                    tracer.push(new Trace_info(p.n.left, false, false));
                }
            }
            else if (!p.rv) {
                p.rv = true;
                if (p.n.right != null) {
                    tracer.push(new Trace_info(p.n.right, false, false));
                }
            }
            else {
                System.out.print(p.n.data + " ");
                tracer.pop();
            }

            if (p.n.left == null && p.n.right == null) {
                System.out.print(p.n.data + " ");
                tracer.pop();
            }
        }
        System.out.println();
    }

    @Override
    public void rec_cal_pre_order() {

    }

    @Override
    public void rec_cal_in_order() {

    }

    @Override
    public void rec_cal_post_order() {

    }

    @Override
    public void cal_pre_order() {

    }

    @Override
    public void cal_in_order() {

    }

    @Override
    public void cal_post_order() {

    }

    @Override
    public Binary_Tree inverse() {

        Link_BT inv = new Link_BT(h);
        inv.root = new Node(this.root.data);

        inv.inv_routine(this.root, inv.root);

        return inv;
    }

    private void inv_routine(Node p, Node ip) {

        if ((p.left == null) && (p.right == null)) {
            return;
        }

        ip.left = new Node(p.right);
        ip.right = new Node(p.left);

        if ((p.left != null)) {
            inv_routine(p.left, ip.right);
        }
        if (p.right != null) {
            inv_routine(p.right, ip.left);
        }
    }
}

public class DS_ch07 {

    static void list_bt_test(List_Binary_Tree bt) {

        System.out.println("IS EMPTY TEST: EMPTY? >>> " + bt.is_empty());
        System.out.println("NON RECURSIVE PRE ORDER TEST");
        bt.pre_order();

        System.out.println("INSERTION TEST");
        bt.insert("Wood", 9);
        bt.insert("Sq", 4);
        bt.insert("Wood", 9);
        System.out.println("AFTER INSERTION");
        bt.pre_order();

        System.out.println("CPY TEST");
        List_Binary_Tree cpy = (List_Binary_Tree) bt.copy();
        List_Binary_Tree bt2 = new List_Binary_Tree(4);
        bt2.insert("Alpha", 1);
        bt2.insert("Sig", 2);
        bt2.insert("Nut", 3);
        bt2.insert("Quan", 5);
        System.out.println("IS EQUAL bt, cpy? >>> " + bt.is_equal(cpy));
        System.out.println("IS EQUAL bt, bt2? >>> " + bt.is_equal(bt2));

        System.out.println("DELETION TEST");
        bt.delete("Z");
        bt.delete("Nut");
        bt.delete("Os");
        System.out.println("AFTER DELETE");
        bt.pre_order();

        System.out.println("RECURSIVE PRE ORDER TEST");
        bt.rec_pre_order();

        bt.in_order();
    }

    public static void main(String[] a) {

        List_Binary_Tree lt = new List_Binary_Tree(3);

        for (int i = 1; i < 16; i++) {
            lt.insert(i);
        }

        int[] po = lt.adv_pre_order();

        for (int j : po) {
            System.out.print(j + " ");
        }
        System.out.println();

        for (int j :
                ((List_Binary_Tree) lt.adv_copy()).adv_pre_order()) {
            System.out.print(j + " ");
        }

        System.out.println("\nEQUAL?: " + lt.is_equal(lt.adv_copy()));

        /*List_Binary_Tree bt1 = new List_Binary_Tree(3);
        bt1.insert("Alpha", 1);
        bt1.insert("Sig", 2);
        bt1.insert("Nut", 3);
        bt1.insert("Z", 5);
        bt1.insert("Aki", 6);
        bt1.insert("Juan", 7);
        bt1.insert("Sq", 4);
        bt1.insert("Wood", 9);
        list_bt_test(bt1);

        List_Binary_Tree bt2 = new List_Binary_Tree(3);
        bt2.insert("A");
        bt2.insert("B");
        bt2.insert("C");
        bt2.insert("D");
        bt2.insert("E", 6);
        bt2.insert("F", 7);
        bt2.insert("G", 8);
        bt2.insert("H", 15);
        bt2.in_order();
        bt2.post_order();

        bt2.rec_in_order();
        bt2.rec_post_order();

        List_Binary_Tree fx_bt = List_Binary_Tree.expr_to_bt("A*B+C", 0);
        fx_bt.in_order();

        List_Binary_Tree inv = (List_Binary_Tree) bt2.inverse();
        inv.pre_order();

        Link_BT bt = new Link_BT(3);

        for (int i = 0; i < 15; i++) {
            bt.adv_insert(i);
        }
        bt.rec_pre_order();

        Link_BT cpy = (Link_BT) bt.copy();
        cpy.rec_pre_order();

        System.out.println("IS EQUAL? : " + bt.is_equal(cpy));
        bt.inverse().rec_pre_order();

        bt.pre_order();
        bt.in_order();
        bt.post_order();
        
         */

    }

}
