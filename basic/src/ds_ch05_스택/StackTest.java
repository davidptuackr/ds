package ds_ch05_스택;

interface Stack {

    // abstract void create() >>> 빈 스택 생성, 생성자로 대체

    void push(Object x);
    boolean isEmpty();
    Object pop();
    void delete();
    Object peek();
}

class Seq_Stack implements Stack {

    Object[] data;
    int top = -1;

    public Seq_Stack(int n) {
        data = new Object[n];
    }

    @Override
    public void push(Object x) {
        if (top >= data.length) {
            System.out.println("UNABLE TO PUSH. STACK IS FULL");
            return;
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
}

class M_Seq_Stack implements Stack {

    Object data[];
    int[] tops;
    int[] bases;
    int total_length;
    int n_div;

    public M_Seq_Stack(int m, int n) {
        total_length = m;
        n_div = n;

        data = new Object[m];
        tops = new int[m / n];
        for (int i = 0; i < tops.length; i++) {
            tops[i] = i * (m / n) - 1;
            bases[i] = i * (m / n);
        }
    }

    @Override
    public void push(Object x) {

    }
    public void push(Object x, int i) {
        if (tops[i] == bases[i+1]) {
            System.out.format("UNABLE TO PUSH. STACK[%d] IS FULL", i);
        }
        data[++tops[i]] = x;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
    public boolean isEmpty(int i) {
        return bases[i] == tops[i];
    }

    @Override
    public Object pop() {
        return null;
    }
    public Object pop(int i) {
        return data[tops[i]--];
    }

    @Override
    public void delete() {

    }
    public void delete(int i) {
        tops[i]--;
    }

    @Override
    public Object peek() {
        return null;
    }
    public Object peek(int i) {
        return data[tops[i]];
    }
}

class Link_Stack implements Stack {

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
    미로 문제

    1. 초기화
        미로: m * n 크기의 미로는 m+2 * n+2 크기의 배열로 표현한다
            >>> 테두리의 벽을 표현하기 위해
            갈 수 있는 경로는 0, 벽은 1로 표현한다
        경로 내역: 스택
        시작: ( 1, 1 )
    2. 진행 방법
        1. 방향을 조사한다. 이 때 동쪽부터 시작한다
        2. 조사했을 때 해당 방향으로 갈 수 없으면 다음 방향을 조사하고, 갈 수 있으면 이동한다. 이 때 
            2.1 (현재 X좌표, 현재 Y좌표, 이동 방향)을 스택에 기록한다
            2.2 (현재 X좌표, 현재 Y좌표)를 1로 설정한다
            2.3 현재 위치 정보를 갱신한다
        3. 1, 2를 막다른 곳에 다다르거나 목적지에 도착할 때까지 반복한다
        4. 막다른 곳에 다다를 경우
            4.1 pop + 현재 위치를 그 다음 top의 내용으로 설정
            4.2 top에 기록된 내역 외에 다른 방향을 조사한다 >>> 해당 방향이 0인 곳을 찾는다
            4.3 갈 수 있는 방향이 있으면 1, 2, 3을 똑같이 반복한다
            4.4 없다면 갈 수 있는 방향이 나올 때까지 4.1부터 다시 한다
    3. 경로 출력
        1. m+2 * n+2만큼 ■을 출력한다
        2. 스택에 있던 경로들은 □로 출력한다
            
 */

public class StackTest {
    
    static void infix_to_postfix(String infix) {

        /*
        수식 구성요소 별 행동
            피연산자: 무조건 출력
            +, -
                1. top == *, /인 경우: top이 * OR /이 아닐 때까지 전부 pop 한 후 push
                2. top == +, -, (인 경우: 그냥 push
                3. isEmpty == true: 2와 같음
            *, /
                1. push: 발견 즉시
                2. pop: 토큰이 +, -일 경우 && 수식이 끝났을 경우
            (
                1. push: 발견 즉시
                2. delete: ) 발견 즉시 + ( 위에 있던 모든 연산자들을 pop 한 후
            )
                스택에 절대 들어가지 않음
         */

        Link_Stack ops = new Link_Stack();

        for (int i = 0; i < infix.length(); i++) {
            char token = infix.charAt(i);

            if (token == '+' || token == '-') {
                if (ops.isEmpty()) {
                    ops.push(token);
                }
                else {
                    while (ops.peek().equals('*') || ops.peek().equals('/')) {
                        System.out.print(ops.pop());
                    }
                    ops.push(token);
                }
            }
            else if (token == '*' || token == '/' || token == '(') {
                ops.push(token);
            }
            else if (token == ')') {
                while (!ops.peek().equals('(')) {
                    System.out.print(ops.pop());
                }
                ops.delete();
            }
            else {
                System.out.print(token);
            }
        }
        while (!ops.isEmpty()) {
            System.out.print(ops.pop());
        }

    }

    public static void main(String[] args) {

        // infix_to_postfix("(A+B*C/(X*(Y-Z)))+V");


    }
    
}
