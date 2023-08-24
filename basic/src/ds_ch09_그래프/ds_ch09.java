package ds_ch09_그래프;
/*

예제

그래프 종류별, 표현별로 구현
    방향, 무방향
    인접 행렬, 인접 리스트 (리스트 배열, 정수 배열), 인접 다중 리스트

그래프 연산
    1.
        정점, 간선 추가 및 삭제 (정점 삭제 시 부속 간선도 같이 삭제)
        순회: BFS, DFS
    2.
        연결 그래프 여부 판단
        강력/약한 연결 여부 판단 (방향 그래프)
        진부분그래프 확인
        노드별 인접 정보 출력 (방향 그래프의 경우엔 진출입 정보까지)
    3.
        신장 트리
        그래프 이분할
        최소 반지름, 지름을 갖는 신장 트리 생성
        단절점, 브리지 탐색
        오일러 사이클

    4.
        서로 다른 트리 노드 간 가장 가까운 공통 조상 노드 탐색
 */

/*

인터페이스
    기본 간선 (인접 리스트, 인접 다중 리스트에서 사용. 인접 행렬 표현에선 미사용)
        구성
            종점: 정점 이름
            다음 간선: 시점에 부속된 다음 간선
    확장 간선 (기본 간선을 상속받아 인접 다중 리스트에서 사용)
        추가
            시점: 시작 정점 이름
            종점의 다음 간선: 종점에 부속된 다음 간선

    정점 (인접 행렬 표현에선 미사용)
        이름
        부속 간선: 첫번째 부속 간선

    그래프
        정점들: Object
        *** 간선 정보는 확장 필드로 정의

        ***
            아니면 (정점, 간선)을 묶어 구성 요소라는 하나의 필드로 정의하고
            간선 안에 정점 정보를 포함시킬까

 */

import java.util.*;

class Edge {
    int end;
    Edge start_next;
}

class MLEdge {
    int start;
    int end;
    Edge start_next;
    Edge end_next;
}

class Vertex {
    int key;
    Edge edges;
}

interface Graph {

    // 1
    void add_vertex(int key);
    void add_edge(int start, int end);
    void delete_vertex(int key);
    void delete_edge(int start, int end);
    int[] bfs(int start);
    int[] dfs(int start);

    // 2
    /*
    연결 그래프 여부 판단
        강력/약한 연결 여부 판단 (방향 그래프)
        진부분그래프 확인
        노드별 인접 정보 출력 (방향 그래프의 경우엔 진출입 정보까지)

    boolean has_disconnection();
    void get_subgraph();
    void get_adj_summary();
     */
}

class Adj_matrix_Un_Digraph implements Graph {

    int[] vertex;
    int[] edges;
    protected final int n;

    public Adj_matrix_Un_Digraph(int n) {
        // 상삼각 행렬만 저장 + 대각은 어차피 자기 자신이므로 저장하지 않음
        // 행이 start, 열이 end 라고 간주할 것
        this.n = n;
        vertex = new int[n];
        edges = new int[n * (n-1) / 2];

    }

    protected int get_eloc(int start, int end) {
        return (start < end)
                ? (start * (n - 1)) - (start * (start + 1)) / 2 + end - 1
                : (end * (n - 1)) - (end * (end + 1)) / 2 + start - 1
        ;
    }

    @Override
    public void add_vertex(int key) {
        if (vertex[key] != 0) {
            System.out.printf("KEY %d ALREADY EXISTS", key);
            return;
        }

        System.out.printf("CREATE VERTEX V[%d]\n", key);
        vertex[key] = 1;
    }

    @Override
    public void add_edge(int start, int end) {
        if (start == end) {
            System.out.println("SELF CONNECTION IS NOT ALLOWED");
            return;
        }
        if (vertex[start] == 0 || vertex[end] == 0) {
            System.out.println("CONNECTING EMPTY VERTEX IS NOT ALLOWED");
            System.out.printf("V[%3d]: %3d\n", start, vertex[start]);
            System.out.printf("V[%3d]: %3d\n\n\n", end, vertex[end]);
            return;
        }

        // vertex.length*start/2 - start*(start-1)/2 + (end-1)
        // (s*(n-1))-(s*(s+1))/2+e-1

        System.out.printf("CREATE EDGE E(%d, %d)\n", start, end);

        edges[get_eloc(start, end)] = 1;
    }
    public void describe() {
        System.out.println("VERTEX: ");
        for (int i = 0; i < n; i++) {
            if (i % 5 == 0) System.out.println();
            System.out.printf("V[%d]: %d   ", i, vertex[i]);
        }

        System.out.println("\nEDGE:");
        System.out.print("   ");
        for (int s = 0; s < n; s++) {
            System.out.printf("%3d", s);
        }
        System.out.println();

        for (int s = 0; s < n; s++) {
            System.out.printf("%3d", s);
            for (int e = 0; e < n; e++) {
                if (s >= e) {
                    System.out.print("   ");
                }
                else {
                    System.out.printf("%3d", edges[(s*(n-1))-(s*(s+1))/2+e-1]);
                }
            }
            System.out.println();
        }
        System.out.println("\n\n");
    }

    @Override
    public void delete_vertex(int key) {
        if (key >= n) {
            System.out.println("INVALID KEY: " + key);
            return;
        }
        if (vertex[key] != 1) {
            System.out.println("YOU ARE TRYING TO REMOVE INVALID VERTEX");
            System.out.printf("V[%d]: %d\n", key, vertex[key]);
            return;
        }

        System.out.println("DELETE VERTEX V[" + key + "]");
        for (int e = 0; e < n; e++) {
            if (edges[get_eloc(key, e)] != 0) {
                delete_edge(key, e);
            }
        }
    }

    @Override
    public void delete_edge(int start, int end) {
        if (start >= n || start < 0 || end >= n || end < 0) {
            System.out.printf("INVALID EDGE E(%d, %d)\n", start, end);
            return;
        }

        int eloc = get_eloc(start, end);
        if (edges[eloc] != 1) {
            System.out.printf("EDGE E(%d, %d) DOES NOT EXISTS\n", start, end);
            return;
        }
        System.out.printf("DELETE EDGE E(%d, %d)\n", start, end);
        edges[eloc] = 0;
    }

    @Override
    public int[] bfs(int start) {
        boolean[] marks = new boolean[n];
        Queue<Integer> q = new LinkedList<>();
        int cnt = 0;
        for (int i = 0; i < n; i++) if (vertex[i] != 0) cnt++;
        int[] rs = new int[cnt];
        Arrays.fill(rs, -1);
        int loc;
        int i_iter = 0;

        q.add(start);
        while (!q.isEmpty()) {
            loc = q.poll();
            if (!marks[loc]) {
                marks[loc] = true;
                rs[i_iter++] = loc;
                for (int e = 0; e < n; e++) {
                    if (edges[get_eloc(loc, e)] != 0) {
                        q.add(e);
                    }
                }
            }
        }
        return rs;
    }

    @Override
    public int[] dfs(int start) {
        boolean[] marks = new boolean[n];
        Stack<Integer> s = new Stack<>();
        int cnt = 0;
        for (int i = 0; i < n; i++) if (vertex[i] != 0) cnt++;
        int[] rs = new int[cnt];
        Arrays.fill(rs, -1);
        int loc;
        int i_iter = 0;

        s.add(start);

        while (!s.isEmpty()) {
            loc = s.pop();
            if (!marks[loc]) {
                marks[loc] = true;
                rs[i_iter++] = loc;
                for (int e = 0; e < n; e++) {
                    if (edges[get_eloc(loc, e)] != 0) {
                        s.add(e);
                    }
                }
            }
        }
        return rs;
    }

    /*
    @Override
    public boolean has_disconnection() {
        // 일단 제일 단순하게 하자 (081123 1236)
        // 예상 시간복잡도: O(n^2)

        *//*
        방법 1. 순회 결과와 현재 노드가 일치하는지 확인

         *//*
        // >>> BFS, DFS return type: void >>> int[]

        int s = 0;
        while (vertex[s] == 0) s++;
        int[] rs = bfs(s);
        int i_iter = 0;

        while (i_iter < rs.length) {
            if (rs[i_iter++] < 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void get_subgraph() {
        *//*
        1. 있는 노드들 확인
        2. 순회 후 단절 상태 확인
        3. 순회로 확인한 최대연결 부분그래프 외 다른 노드들도 확인
        4. 각 최대연결 부분그래프에 대해 다음을 실시
            4-1. 노드 1개, 노드 2개+간선 1개로 이뤄진 부분그래프 확인
            4-2. (일단 생각 나는 것은) 노드-간선 조합을 만든 뒤 그 중 없는 것은 삭제
         *//*

        *//*
        정점 하나로만 이뤄진 서브그래프 >>> 정점 개수만 세고 끝
        (2 정점 + 1 간선) 형태의 서브그래프 >>> 간선 개수만 세고 끝 (어차피 양 끝에 정점 하나씩 있으니까)
        (n 정점 + m 간선) 형태의 서브그래프
            1. 정점 n개 선택
            2.
                각 정점의 부속 간선들만으로 이을 수 있는지 확인
                이 때
                    2 <= m <= n(n-1)/2 (무방향일 경우)
                    2 <= m <= n(n-1) (방향일 경우)
            3.
                각 n (3 <= n <= (최대 연결 서브그래프의 정점 수))에 대하여
                2를 m 범위만큼 반복

         *//*

        *//*
        최대 연결 서브그래프 확인 방법
            1. 아무데나 잡고 순회
            2. 순회 결과를 큐에 삽입
            3. 순회 결과에 없는 정점이 없을 때까지 1, 2 반복

         *//*

        Queue<int[]> q = new LinkedList<>();
        int cnt = 0;
        int i_iter = 0;

        for (int j : vertex) {
            cnt = (j != 0) ? cnt + 1 : cnt;
        }
        int[] locs = new int[cnt];

        for (int i = 0; i < vertex.length; i++) {
            if (vertex[i] != 0) {
                locs[i_iter++] = i;
            }
        }

        q = get_max_sub_connections(q, locs, 0);
        Queue<Vector<Integer>> candi = make_combi(q);
        
    }

    private void verifying_routine() {
        *//*
        1. 정점 1개짜리 조합 >>> 무조건 통과
        2. 정점 2개짜리 조합 >>> 간선 있으면 통과
        3. 정점 3개 이상 조합 >>> n-1 ~
         *//*
    }

    private Queue<Vector<Integer>> make_combi(Queue<int[]> q) {

        Queue<Vector<Integer>> candi = new LinkedList<>();
        Vector<Integer> tmp = new Vector<>();

        while (!q.isEmpty()) {
            int[] subset = q.poll();
            for (int i = 1; i < subset.length; i++) {
                make_combi_routine(candi, tmp, q, subset.length, 0, i);
            }
        }
        return candi;
    }

    private void make_combi_routine(
            Queue<Vector<Integer>> candi,
            Vector<Integer> tmp,
            Queue q,
            int n, int left, int k
    ) {
        if (k == 0) {
            candi.add(tmp);
            return;
        }
        for (int i = left; i <= n; ++i)
        {
            tmp.add(i);
            make_combi_routine(candi, tmp, q, i + 1, k - 1, k);
            tmp.remove(tmp.size() - 1);
        }
    }

    private Queue<int[]> get_max_sub_connections(Queue<int[]> q, int[] locs, int i_strt) {

        if (i_strt > locs.length) return q;

        int[] rs;
        int i_iter = 0;
        rs = bfs(locs[i_strt]);
        q.add(rs);

        for (int i = 0; i < locs.length; i++) {
            if (locs[i] == rs[i_iter]) {
                locs[i] = -1;
                i_iter++;
            }
        }
        while ((i_strt < locs.length) && (locs[i_strt] != -1)) i_strt++;

        return get_max_sub_connections(q, locs, i_strt);
    }

    @Override
    public void get_adj_summary() {

    }*/
}

class Adj_matrix_Digraph extends Adj_matrix_Un_Digraph implements Graph{

    public Adj_matrix_Digraph(int n) {
        super(n);
        this.edges = new int[n * n];
    }

    @Override
    protected int get_eloc(int start, int end) {
        return (n * start) + end;
    }

    @Override
    public void describe() {
        System.out.println("VERTEX: ");
        for (int i = 0; i < n; i++) {
            if (i % 5 == 0) System.out.println();
            System.out.printf("V[%d]: %d   ", i, vertex[i]);
        }

        System.out.println("\nEDGE:");
        System.out.print("   ");
        for (int s = 0; s < n; s++) {
            System.out.printf("%3d", s);
        }
        System.out.println();

        for (int s = 0; s < n; s++) {
            System.out.printf("%3d", s);
            for (int e = 0; e < n; e++) {
                System.out.printf("%3d", edges[n * s + e]);
            }
            System.out.println();
        }
        System.out.println("\n\n");
    }

    public void is_strongly_connected() {

    }
}

public class ds_ch09 {
    public static void main(String[] args) {

        /*Adj_matrix_Un_Digraph g1 = new Adj_matrix_Un_Digraph(10);
        g1.add_vertex(1);
        g1.add_vertex(3);
        g1.add_vertex(4);
        g1.add_vertex(7);
        g1.add_vertex(8);
        g1.add_vertex(9);
        g1.add_edge(1, 3);
        g1.add_edge(1, 4);
        g1.add_edge(1, 8);
        g1.add_edge(3, 7);
        g1.add_edge(3, 9);
        g1.add_edge(4, 8);
        g1.add_edge(7, 1);
        g1.add_edge(9, 4);

        for (int v : g1.bfs(3)) System.out.print(v);
        System.out.println("\nIS DISCONNECTED? : " + g1.has_disconnection());

        g1.delete_edge(9, 3);
        g1.delete_edge(4, 9);
        for (int v : g1.bfs(3)) System.out.print(v);
        System.out.println();
        for (int v : g1.dfs(9)) System.out.print(v);
        System.out.println("\nIS DISCONNECTED? : " + g1.has_disconnection());*/

    }




}
