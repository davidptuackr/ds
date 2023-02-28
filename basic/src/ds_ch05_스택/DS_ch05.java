package ds_ch05_스택;

import java.util.Random;
import java.util.Scanner;

interface Stack {

    boolean isEmpty();
    void push(Object data);
    Object pop();
    Object peek();
    void delete();

}

/*
    05.1 스택을 순차 / 연결 표현으로 각각 만들어보기
 */

class Seq_Stack implements Stack_test {

    Object[] data;
    int top = -1;

    public Seq_Stack(int n) {
        data = new Object[n];
    }

    @Override
    public void push(Object x) {
        if (top >= data.length) {
            System.out.println("UNABLE TO PUSH. RUN \'STACKFULL\'");
            stackFull();
        }
        data[++top] = x;
    }

    @Override
    public boolean isEmpty() {
        return top < 0;
    }

    @Override
    public Object pop() {
        if (isEmpty()) return null;
        return data[top--];
    }

    @Override
    public void delete() {
        top--;
    }

    @Override
    public Object peek() {
        return data[top];
    }

    /*
        05.2 stackFull() 작성
        동작: 스택이 꽉 찰 경우 현재 배열 크기의 두 배만큼 새 공간을 만든다. 이 때 원소들은 기존과 위치가 같아야 한다
     */
    private void stackFull() {
        Object[] doubled = new Object[data.length * 2];

        for (int i = 0; i < data.length; i++) {
            data[i] = doubled[i];
        }

        data = doubled;
    }
}

class Link_Stack implements Stack_test {

    class Node {

        Object data;
        Node link;

        public Node(Object x) {
            data = x;
            link = null;
        }

        public Node(Object x, Node next) {
            data = x;
            link = next;
        }

    }

    Node top;

    public Link_Stack() {
        top = null;
    }

    @Override
    public void push(Object x) {
        top = new Node(x, top);
    }

    @Override
    public boolean isEmpty() {
        return top == null;
    }

    @Override
    public Object pop() {
        if (isEmpty()) {
            System.out.println("UNABLE TO POP. STACK IS EMPTY");
            return null;
        }
        Object popped = top.data;
        top = top.link;
        return popped;
    }

    @Override
    public void delete() {
        if (isEmpty()) return;
        else top = top.link;
    }

    @Override
    public Object peek() {
        return top.data;
    }

    public void print() {
        Node p = top;
        while (p != null) {
            System.out.format(" %s ", p.data);
            p = p.link;
        }
    }
}

/*
    05.4
        배열 S[m]에 두 개의 스택을 표현하려고 한다.
        스택 i (i = 0 OR 1)을 위한 push, pop을 작성한다.
        이 때, S[m] 안에는 최대 m-1개의 원소가 들어갈 수 있다
        >>> 경계를 만들어 명확하게 구분할 수 있도록
 */
class LR_Stack {

    private Object[] S;
    private int l_top;
    private int r_top;

    public LR_Stack(int m) {
        S = new Object[m];
        l_top = -1;
        r_top = S.length;
    }

    void push(Object x, int i) {
        if ((l_top + 1) == (r_top - 1)) {
            System.out.println("UNABLE TO PUSH. STACK IS FULL");
            return;
        }

        switch (i) {
            case 0:
                S[++l_top] = x;
                break;
            case 1:
                S[--r_top] = x;
                break;
        }
    }

    Object pop(int i) {
        String target = (i == 0) ? "LEFT" : "RIGHT";
        Object popped = null;

        switch (i) {
            case 0:
                if (l_top < 0) System.out.format("UNABLE TO POP. %s STACK IS EMPTY", target);
                else popped = S[l_top--];
                break;
            case 1:
                if (r_top >= S.length) System.out.format("UNABLE TO POP. %s STACK IS EMPTY", target);
                else popped = S[r_top++];
                break;
        }
        return popped;
    }

    public String toString() {

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < l_top+1; i++) {
            sb.append(String.format("LEFT[%d]: %s\n", i, S[i].toString()));
        }
        sb.append("\n");
        for (int i = S.length-1; i > r_top-1; i--) {
            sb.append(String.format("RIGHT[%d]: %s\n", S.length - i - 1, S[i].toString()));
        }
        return sb.toString();
    }
}

/*
    05.5 
        다음 조건에 맞춰 스택을 만든다
            1. 담을 수 있는 자료형은 char, int, float 이다
            2. 이중 연결 형태로 만든다 
 */
class DLN_Stack implements Stack {

    class DL_Node {

        DL_Node left;
        float data;
        DL_Node right;

        public DL_Node(DL_Node l_next, float x) {
            left = l_next;
            data = x;
            right = null;
        }

        public DL_Node(float x, DL_Node r_next) {
            left = null;
            data = x;
            right = r_next;
        }

        public DL_Node(DL_Node l_next, float x, DL_Node r_next) {
            left = l_next;
            data = x;
            right = r_next;
        }

    }

    DL_Node top;

    @Override
    public boolean isEmpty() {
        return top == null;
    }

    @Override
    public void push(Object data) {
        DL_Node p = new DL_Node(top, (float) data, null);
        top.right = p;
        top = top.right;
    }

    @Override
    public Object pop() {
        float popped = top.data;
        top = top.left;
        top.right = null;
        return popped;
    }

    @Override
    public Object peek() {
        return top.data;
    }

    @Override
    public void delete() {
        top = top.left;
        top.right = null;
    }

}

/*
    05.6 좀 더 일반적인 후위 표기식 변환
        구현 1. 음수 처리
        구현 2. 논리 연산 처리

    처리 방안
        음수, 빼기 연산은 앞에 -1을 곱한 것으로 간주한다
            ex.
                A - B >>> A + (-1 * B)
                A * -B >>> A * -1 * B
        논리 연산
            not: 발견 즉시 push, 여는 괄호까지 나오고 맨 마지막에 pop
            and, or, cmp
                괄호가 없을 땐: 발견 즉시 push, 피연산자 만나면 그 때 출력
                괄호가 있을 땐: 닫는 괄호가 나올 때 출력

        피연산자: 무조건 출력
        +
            1. top == *, /인 경우: top이 * OR /이 아닐 때까지 전부 pop 한 후 push
            2. top == +, (인 경우: 그냥 push
            3. isEmpty == true: 2와 같음
        -
            1. 음수를 나타낼 때 쓸 경우: -1을 곱한 것으로 간주한다 (ex. -X+Y >>> (-1*X)+Y >>> -1X*Y+
                1.1 push - // push *
                1.2 print -1
                1.3 다음 토큰으로
                1.4 print X
                1.5 pop - as *
            2. 빼기 연산을 나타낼 때 쓸 경우: -1을 곱하고 더하기로 바꾼다 (ex. A-B >>> A+(-1*B) >>> A-1B*+)
                2.1 push - // push *
                2.2 print -1
                2.3 다음 토큰으로
                2.4 print B
                1.5 pop - as *+
            3. 괄호 앞에 있을 경우: (ex. A-(B+C) >>> A+(-1)*(B+C) >>> A-1BC+*+)
            >>> 1은 i=0이거나 charat(i-1)이 연산자인 경우 // 2, 3은 charat(i-1) 피연산자인 경우
        *, /
            1. push: 발견 즉시
            2. pop: 토큰이 +, -일 경우 && 수식이 끝났을 경우
        (
            1. push: 발견 즉시
            2. delete: ) 발견 즉시 + ( 위에 있던 모든 연산자들을 pop 한 후
        )
            스택에 절대 들어가지 않음
 */

/*
05.8 전위 표기식
    규칙
        첫 연산자는 일단 내버려둔다. 일단 스택 안에 저장한다고 하자
        /, *가 top이면 어떤 연산자도 스택에 push할 수 있다
        3. +, -가 top이면 그 뒤론 +, -, 괄호쌍만 들어올 수 있다
        4. 여는 괄호가 들어오면 3은 무효화된다
        5. 닫힌 괄호가 들어오면 4는 무효화된다

    연산 우선순위가 높을 수록 전위 표기식에서 나중에 나오고, 낮을 수록 앞에 나온다
    여는 괄호가 나오면 연산 우선순위를 높인다
    닫힌 괄호가 나오면 연산 우선순위를 낮춘다

    구성 요소를 넣는 것은 앞뒤에서 동시에 할 수 있지만
    출력하는 것은 맨 앞에서부터 하도록?

    아니면
    연산자를 넣는 것을 앞뒤에서 하고, 피연산자는 순서대로 넣어둔 뒤
    우선순위가 바뀔 때마다 출력을 다르게 하도록?

    위에꺼 다 헛짓거리다
    주어진 식을 뒤에서부터 하면 된다
    자세히 말하자면
        연산자
            1. 스택에 출입하는 동작은 후위 표기와 같다
            2. 차이라면 후위 표기의 (, ) 역할이 전위 표기에선 서로 반대라는 점이다
            3. pop할 때 후위 표기처럼 그대로 출력하는 것이 아닌, 전위 표기식을 저장한 스택에 push한다
        피연산자
            1. 피연산자를 만나면 전체 전위 표기식을 저장할 스택에 넣어둔다
 */

/*
05.17 후위 표기식 계산 (단, 연산자는 +-*\/만 사용한다)

    과정
        1. 사용자로부터 중위 표기식을 입력받는다
        2. 입력받은 중위 표기식을 후위 표기식으로 바꾼다
        3. 후위 표기식을 점검하면서 계산을 진행한다. 방법은 다음과 같다.
            3.1 피연산자를 만나면 사용자로부터 숫자 입력을 받는다
            3.2 입력받은 숫자를 스택에 저장한다
            3.3 연산자를 만나면 스택에서 숫자 두 개를 pop해서 계산한다 (비정상적으로 변환되면 오류 발생 >>> 꺼낼 숫자가 없는 경우)
            3.4 중간 계산 결과는 다시 스택에 push한다
            3.5 계산 마지막에 최종 결과를 pop한다
 */

/*
05.18 n-queens 문제

규칙: n*n 크기의 체스판 위에 n개의 퀸을 서로의 상, 하, 좌, 우, 대각선 방향으로 겹치지 않게 배치한다

과정
    0. 시작은 좌측 최상단부터 한다
    1. 다음 방향으로 갈 수 있는지 검사한다.
        1.1 (좌측 + 1, 상단 + 2) >>> (-1, -2)
        1.2 (좌측 + 1, 하단 + 2) >>> (-1, +2)
        1.3 (좌측 + 2, 상단 + 1) >>> (-2, -1)
        1.4 (좌측 + 2, 하단 + 1) >>> (-2, -1)
        1.5 (우측 + 1, 상단 + 2) >>> (+1, -2)
        1.6 (우측 + 1, 하단 + 2) >>> (+1, +2)
        1.7 (우측 + 2, 상단 + 1) >>> (+1, -2)
        1.8 (우측 + 2, 하단 + 1) >>> (+1, +2)
    2. 이동할 수 있으면 이동하고, 없으면 다른 방향을 검사한다
    3. 다 검사해도 이동할 수 있는 방향이 없으면 한 수 물린다
    4. 한 수 물렸을 때 방향이 나오면 계속 시도해보고, 없으면 나올 때까지 물린다
    5. 말을 전부 놓을 때까지 이를 반복한다

종료 조건: 말을 다 놓는 것
반복 작업
    - 검사
    - 이동
    - 복귀

방향은 static 배열로 설정
말 개수는 전역 변수로 설정
n*n 말판 준비
    말이 있는 위치는 1, 없는 위치는 0으로 표시
    이동할 때 1로, 물릴 때 0으로 변경

n-queens 구조
    n-queens 실행
        ...
        초기 설정들
        ...
        말을 다 놓을 때까지
            탐색(다음 이동할 위치)
        결과 표시
    n-queens 종료

탐색 함수 구조
    방향이 나올 때까지
        위치로 갈 수 있으면
            이동 + 이동하는 위치 표시 + 검사(이동한 위치를 기준으로)
        아니면
            검사(다른 위치)
    말 개수를 확인했을 때
        남았으면 (위의 반복이 끝났다는 것은 방향이 없거나 말을 다 놓은 것 중 하나)
            이동했던 위치의 표시 지우기 + 복귀
        아니면
            종료

말 개수를 확인했을 때
    말이 남았다면
        방향이 있다면
            이동 + 이동 위치 표시 + 검사(이동한 위치를 기준으로)
        아니면
            검사(현재 위치를 기준으로)
     아니면
        종료

 */

/*
고민중인 문제

022823 1614
종료는 어떻게 할 것인가
각 함수 스택의 q_left는 다를텐데 q_left==0일 때 종료한다는 것은 맨 마지막만 가능하지 않겠는가
qs를 static 변수로 설정해볼까

 */

public class DS_ch05 {

    static int qs;
    static boolean[][] board;
    static int[][] dirs = { // 시계 방향으로 조사
            { 1, -2}, // 북동
            { 2, -1}, // 동북
            { 2,  1}, // 동남
            { 1,  2}, // 남동
            {-1,  2}, // 남서
            {-2,  1}, // 서남
            {-2, -1}, // 서북
            {-1, -2}  // 북서
    };

    static void n_queens() {

        Scanner sc = new Scanner(System.in);
        qs = sc.nextInt();
        board = new boolean[qs][qs];

        Random rand = new Random();
        int rand_x_start;
        int rand_y_start;

        /*rand_x_start = rand.nextInt(qs);
        rand_y_start = rand.nextInt(qs);*/

        rand_x_start = 0;
        rand_y_start = 0;

        /*
        1.1 (좌측 + 1, 상단 + 2) >>> (-1, -2)
        1.2 (좌측 + 1, 하단 + 2) >>> (-1, +2)
        1.3 (좌측 + 2, 상단 + 1) >>> (-2, -1)
        1.4 (좌측 + 2, 하단 + 1) >>> (-2, -1)
        1.5 (우측 + 1, 상단 + 2) >>> (+1, -2)
        1.6 (우측 + 1, 하단 + 2) >>> (+1, +2)
        1.7 (우측 + 2, 상단 + 1) >>> (+2, -1)
        1.8 (우측 + 2, 하단 + 1) >>> (+2, +1)
         */

        System.out.println("RANDOM START");
        System.out.format("LOCATION: (%d, %d)\n", rand_x_start, rand_y_start);
        board[rand_y_start][rand_x_start] = true;
        qs--;
        search(rand_x_start, rand_y_start, qs);

        if (!board[rand_y_start][rand_x_start]) {
            System.out.println("FAIL");
        }

        for (int i = 0; i < board[0].length; i++) {
            for (int j = 0; j < board.length; j++) {
                System.out.format("%5c", ((board[i][j]) ? 'Q' : '*'));
            }
            System.out.println();
        }

    }

    static void search(int current_xloc, int current_yloc, int qs_left) {

        int dir_idx = 0;
        int[] dir;
        int x_next, y_next;

        while (dir_idx < 8) {

            // 마지막에 끝나면 바로 다음 코드로 인해 종료
            if (qs == 0) {
                System.out.format("X: %d, Y: %d\n", current_xloc, current_yloc);
                return;
            }

            dir = dirs[dir_idx];
            x_next = current_xloc + dir[0];
            y_next = current_yloc + dir[1];

            if (is_available(board, x_next, y_next)) {
                board[y_next][x_next] = true;
                qs--;
                search(x_next, y_next, qs);
            }

            // 검사한 방향으론 불가능하거나, 앞서 갔던 곳으론 더이상 진행할 수 없을 경우 다른 방향을 검사한다
            dir_idx++;
        }

        // 위 루프를 빠져나왔다는 것은 다음에 갈 곳이 없다는 것 >>> 지금 있는 곳부터 지워나가기 + 남은 말 수 증가
        board[current_yloc][current_xloc] = false;
        qs++;

    }

    static boolean is_available(boolean[][] board, int x_next, int y_next) {

        int x = x_next;
        int y = y_next;
        int n = board.length;
        
        // 배열 바깥을 벗어나지 않는지 먼저 검사
        if (x < 0 || y < 0) return false;
        if (x >= n || y >= n) return false;

        // 해당 지점의 상하로 말이 있는지 검사
        for (int i = 0; i < n; i++) {
            if (board[y][i]) return false;
        }
        // 해당 지점의 좌우로 말이 있는지 검사
        for (int i = 0; i < n; i++) {
            if (board[i][x]) return false;
        }
        // 해당 지점의 대각선 방향으로 말이 있는지 검사
        if (x > y) {
            for (int i = 0; i < y+n-x; i++) {
                if (board[i][x-y+i]) return false;
                if ((x+y) > n) {
                    if (board[x+y-n+i][n-i-1]) return false;
                }
                else {
                    if (board[i][x+y-n+i]) return false;
                }
            }
        }
        else {
            for (int i = 0; i < y+n-x; i++) {
                if (board[i][y-x+i]) return false;
                if ((x+y) > n) {
                    if (board[x+y-n+i][n-i-1]) return false;
                }
                else {
                    if (board[i][x+y-n+i]) return false;
                }
            }
        }

        return true;
    }

    static int get_ps(char op) {
        
        int ps = 0;
        
        switch (op) {
            case '(': ps = 0;
            case '+': case '-': ps = 1;
            case '*': case '/': ps = 2;
        }
        
        return ps;
    }

    /*static void general_infix_to_postfix(String infix) {

        Link_Stack ops = new Link_Stack();

        for (int i = 0; i < infix.length(); i++) {
            char token = infix.charAt(i);

            switch (token) {
                case '*': case '/': case '(': case '^': case '|': case '!':
                    ops.push(token);
                    break;
                case '+':
                    if (ops.isEmpty()) {
                        ops.push(token);
                    }
                    else {
                        while (!ops.isEmpty() && (ops.peek().equals('*') || ops.peek().equals('/'))) {
                            System.out.print(ops.pop());
                        }
                        ops.push(token);
                    }
                    break;
                case '-':
                    ops.push('*');
                    System.out.print("-1");
                    if (
                        (i != 0) &&
                        (
                            (infix.charAt(i-1) != '+') &&
                            (infix.charAt(i-1) != '*') &&
                            (infix.charAt(i-1) != '/')
                        )
                    ) {
                        ops.delete();
                        ops.push('+');
                        ops.push('*');
                    }
                    break;
                case ')':
                    while (!ops.peek().equals('(')) {
                        System.out.print(ops.pop());
                    }
                    ops.delete();
                    if (ops.isEmpty()) break;
                    if (ops.peek().equals('!')) {
                        System.out.print(ops.pop());
                    }
                    break;
                default:
                    System.out.print(token);
                    if (!ops.isEmpty() && ((ops.peek().equals('^')) || (ops.peek().equals('|')))) {
                        System.out.print(ops.pop());
                    }
                    break;
            }
        }
        while (!ops.isEmpty()) {
            System.out.print(ops.pop());
        }
        System.out.println();
    }*/

    static String general_infix_to_postfix(String infix) {

        Link_Stack ops = new Link_Stack();

        StringBuilder postfix = new StringBuilder();

        for (int i = 0; i < infix.length(); i++) {
            char token = infix.charAt(i);

            switch (token) {
                case '*': case '/': case '(':
                    ops.push(token);
                    break;
                case '+': case '-':
                    if (ops.isEmpty()) {
                        ops.push(token);
                    }
                    else {
                        while (!ops.isEmpty() && (get_ps((char) ops.peek()) <= get_ps(token))) {
                            postfix.append(ops.pop());
                        }
                        ops.push(token);
                    }
                    break;
                case ')':
                    while (!ops.peek().equals('(')) {
                        postfix.append(ops.pop());
                    }
                    ops.delete();
                default:
                    postfix.append(token);
                    break;
            }
        }

        while (!ops.isEmpty()) {
            postfix.append(ops.pop());
        }

        return postfix.toString();
    }

    static void infix_to_prefix(String infix) {

        Link_Stack ops = new Link_Stack();
        Link_Stack prefix = new Link_Stack();

        for (int i = infix.length()-1; i >= 0; i--) {

            char token = infix.charAt(i);

            switch (token) {
                case '*': case '/': case ')':
                case '^': case '|': case '!':
                case '>': case '<':
                    ops.push(token);
                    break;
                case '+':
                    if (ops.isEmpty()) {
                        ops.push(token);
                    }
                    else {
                        while (!ops.isEmpty() && (ops.peek().equals('*') || ops.peek().equals('/'))) {
                            prefix.push(ops.pop());
                        }
                        ops.push(token);
                    }
                    break;
                case '-':
                    prefix.push("-1");
                    prefix.push('*');
                    if (
                        (i != 0) && (
                        (infix.charAt(i-1) != '+') &&
                        (infix.charAt(i-1) != '*') &&
                        (infix.charAt(i-1) != '/')
                        )
                    ) {
                        if (!ops.isEmpty() && (ops.peek().equals('*') || ops.peek().equals('/'))) {
                            prefix.push(ops.pop());
                        }
                        ops.push('+');
                    }
                    break;
                case '(':
                    while (!ops.peek().equals(')')) {
                        prefix.push(ops.pop());
                    }
                    ops.delete();
                    if (!ops.isEmpty() && ops.peek().equals('!')) {
                        prefix.push(ops.pop());
                    }
                    break;
                default:
                    prefix.push(token);
                    if (
                        (!ops.isEmpty()) && (
                        (ops.peek().equals('^')) ||
                        (ops.peek().equals('|')) ||
                        (ops.peek().equals('>')) ||
                        (ops.peek().equals('<'))
                        )
                    ) {
                        prefix.push(ops.pop());
                    }
                    break;
            }
        }
        while (!ops.isEmpty()) prefix.push(ops.pop());
        while (!prefix.isEmpty()) System.out.print(prefix.pop());
    }

    static void calc_infix_using_postfix(String infix) {

        String postfix = general_infix_to_postfix(infix);
        Link_Stack ops = new Link_Stack();
        Scanner sc = new Scanner(System.in);

        for (int i = 0; i < postfix.length(); i++) {
            char token = postfix.charAt(i);
            if (token != '+' && token != '-' && token != '*' && token != '/') {
                ops.push(sc.nextInt());
            }
            else {
                int later = (int) ops.pop();
                int earlier = (int) ops.pop();

                switch (token) {
                    case '+':
                        ops.push(earlier + later);
                        break;
                    case '-':
                        ops.push(earlier - later);
                        break;
                    case '*':
                        ops.push(earlier * later);
                        break;
                    case '/':
                        ops.push(earlier / later);
                        break;
                }
            }
        }
        System.out.format("%s >>> %s >>> %d\n", infix, postfix, (int) ops.pop());
    }

    public static void main(String[] args) {

        n_queens();

    }

}
