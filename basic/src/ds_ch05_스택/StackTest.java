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

class Maze_Path {

    class Node {

        int xloc;
        int yloc;
        Node link;

        public Node(int x, int y, Node next) {
            xloc = x;
            yloc = y;
            link = next;
        }

    }

    Node top;

    public Maze_Path() {
        top = null;
    }

    public void push(int x, int y) {
        top = new Node(x, y, top);
    }

    public boolean isEmpty() {
        return top == null;
    }

    public int[] pop() {
        if (isEmpty()) {
            System.out.println("UNABLE TO POP. STACK IS EMPTY");
            return null;
        }
        int[] popped = {top.xloc, top.yloc};
        top = top.link;
        return popped;
    }

    public void delete() {
        if (isEmpty()) return;
        else top = top.link;
    }

    public int[] peek() {
        return new int[] {top.xloc, top.yloc};
    }

    public void print() {
        Node p = top;
        while (p != null) {
            //System.out.format(" %s ", p.data);
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
        1. 방향을 조사한다. 이 때
            1.1 동쪽부터 조사한다
            1.2 막다른 곳인지 확인한다
            1.3 직전에 건너왔던 곳은 제외한다
        2. 조사했을 때 해당 방향으로 갈 수 없으면 다음 방향을 조사하고, 갈 수 있으면 이동한다. 이 때 
            2.1 (다음 X좌표, 다음 Y좌표)를 스택에 기록한다
            2.2 현재 위치 정보를 갱신한다
        3. 1, 2를 막다른 곳에 다다르거나 목적지에 도착할 때까지 반복한다
        4. 막다른 곳에 다다를 경우
            4.1 pop + 현재 위치를 그 다음 top의 내용으로 설정
            4.2 4.1에서 pop했던 좌표를 1로 설정한다 >>> 아예 벽 취급해서 못가도록 설정
            4.2 top에 기록된 내역 외에 다른 방향을 조사한다 >>> 해당 방향이 0인 곳을 찾는다
            4.3 갈 수 있는 방향이 있으면 1, 2, 3을 똑같이 반복한다
            4.4 없다면 갈 수 있는 방향이 나올 때까지 4.1부터 다시 한다
    3. 경로 출력
        1. m+2 * n+2만큼 ■을 출력한다
        2. 스택에 있던 경로들은 □로 출력한다

    이동목록: 동, 서, 남, 북

    while (스택이 비어있지 않을 때까지) {
        현재 x좌표 = 스택 top
        현재 y좌표 = 스택 top
        방향지시 = 0 (0 ~ 3)

        while (방향지시 < 4) {
            if (직전 위치는 제외하고 방향지시 한 곳으로 갈 수 있으면) {
                다음 위치 스택에 기록
                위치 정보 갱신
                break
            }
            else {
                방향지시 + 1
            }
        }
        if (방향지시 >= 4) {
            롤백
            >>> {
            방향지시 = 0
            while (top 위치에 가능한 방향이 있거나 스택에 원소가 있는 동안) {
                1. pop 한다. 이 때 나오면서 pop한 위치를 1로 바꾼다
                2. pop한 다음에 나온 top을 조사한다
                3. top에 경로가 있으면 즉시 break
                4. top에 경로가 없으면 추가 조사 (방향지시 + 1)
                5. 추가 조사해도 안나오면 1로 (방향지시 다시 0으로)
            } }
            if (스택이 비었으면) 경로 없음
            else 계속 진행
        }
    }
    결과 print()
 */

public class StackTest {
    
    static int[][] init_maze(int m, int n) {
        
        int[][] map = new int[12][12];


        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                map[i][j] = 1;
            }
        }

        for (int i = 1; i < 6; i++) {
            map[1][i] = 0;
            map[i][5] = 0;
            map[5+i][5] = 0;
            map[10][5+i] = 0;
            map[5][5+i] = 0;
        }

        return map;
        
    }
    
    static void seek(int[][] map, Maze_Path path) {

        /*int xloc = ((int[]) path.top.data)[0];
        int yloc = ((int[]) path.top.data)[1];
        int vec = 0;
        int next_x, next_y;

        int[][] dir = {
                {0, 1}, // E
                {0, -1}, // W
                {1, 0}, // S
                {-1, 0}  // N
        };

        while (vec < 4) {
            next_x = xloc + dir[vec][1];
            next_y = yloc + dir[vec][0];

            if (map[next_x][next_x] == 0) {
                path.push(new int[]{ next_x, next_y });
                break;
            }
            else vec++;
        }

        if (vec >=  4) {
            while (!path.isEmpty()) {
                int popped_x = ((int[]) path.top.data)[0];
                int popped_y = ((int[]) path.top.data)[1];
            }
        }*/

        int[][] dir = {
                {0, 1}, // E
                {0, -1}, // W
                {1, 0}, // S
                {-1, 0}  // N
        };

        int[][] mark = new int[map.length][map[0].length];

        path.push(1, 1);

        while (
            !path.isEmpty() &&
            (path.top.xloc != (map.length-2)) &&
            (path.top.yloc != (map[0].length-2))
        ) {
            int xloc = path.top.xloc;
            int yloc = path.top.yloc;
            int v = 0;
            int x_next;
            int y_next;

            while (v < 4) {
                x_next = xloc + dir[v][1];
                y_next = yloc + dir[v][0];
                if ((map[y_next][x_next] == 0) && (mark[y_next][x_next] == 0)) {
                    mark[y_next][x_next] = 1;
                    path.push(x_next, y_next);
                    break;
                }
                else v++;
            }
            if (v >= 4) {
                v = 0;
                int x_before;
                int y_before;
                x_next = xloc + dir[v][1];
                y_next = yloc + dir[v][0];

                while (!path.isEmpty() && (map[y_next][x_next] != 0)) {
                    v = 0;
                    int[] popped = path.pop();
                    map[popped[1]][popped[0]] = 1;
                    x_before = path.top.xloc;
                    y_before = path.top.yloc;
                    while (v < 4) {
                        x_next = x_before + dir[v][1]; //
                        y_next = y_before + dir[v][0];
                        if ((map[y_next][x_next] == 0) && (mark[y_next][x_next] == 0)) {
                            mark[y_next][x_next] = 1;
                            path.push(x_next, y_next);
                            break;
                        }
                        else v++;
                    }
                }
            }
        }
        
        if (path.isEmpty()) System.out.println("THERE IS NO WAY OUT");
        else print(map);
    }
    
    static void print(int[][] map) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] == 0) System.out.format("   ", map[i][j]);
                else System.out.format(" %d ", map[i][j]);
            }
            System.out.println();
        }
    }
    
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

        int[][] map = init_maze(10, 10);
        Maze_Path path = new Maze_Path();

        seek(map, path);

    }
    
}
