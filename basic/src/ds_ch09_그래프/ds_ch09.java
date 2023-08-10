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

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

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
    void bfs(int start);
    void dfs(int start);
}

class Adj_matrix_Un_Digraph implements Graph {

    int[] vertex;
    int[] edges;
    private final int n;

    public Adj_matrix_Un_Digraph(int n) {
        // 상삼각 행렬만 저장 + 대각은 어차피 자기 자신이므로 저장하지 않음
        // 행이 start, 열이 end 라고 간주할 것
        this.n = n;
        vertex = new int[n];
        edges = new int[n * (n-1) / 2];

    }

    private int get_eloc(int start, int end) {
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

        int eloc = (start < end)
                ? (start*(n-1))-(start*(start+1))/2+end-1
                : (end*(n-1))-(end*(end+1))/2+start-1
        ;

        edges[eloc] = 1;
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

        int eloc;
        System.out.println("DELETE VERTEX V[" + key + "]");
        for (int e = 0; e < n; e++) {
            eloc = (key < e)
                    ? (key*(n-1))-(key*(key+1))/2+e-1
                    : (e*(n-1))-(e*(e+1))/2+key-1
            ;

            if (edges[eloc] != 0) {
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

        int eloc = (start < end)
                ? (start*(n-1))-(start*(start+1))/2+end-1
                : (end*(n-1))-(end*(end+1))/2+start-1
        ;
        if (edges[eloc] != 1) {
            System.out.printf("EDGE E(%d, %d) DOES NOT EXISTS\n", start, end);
            return;
        }

        System.out.printf("DELETE EDGE E(%d, %d)\n", start, end);
        edges[eloc] = 0;
    }

    @Override
    public void bfs(int start) {
        boolean[] marks = new boolean[n];
        Queue<Integer> q = new LinkedList<>();
        int loc, eloc;

        q.add(start);

        while (!q.isEmpty()) {
            loc = q.poll();
            if (!marks[loc]) {
                marks[loc] = true;
                System.out.print(loc + "   ");

                for (int e = 0; e < n; e++) {
                    eloc = (loc < e)
                            ? (loc * (n - 1)) - (loc * (loc + 1)) / 2 + e - 1
                            : (e * (n - 1)) - (e * (e + 1)) / 2 + loc - 1
                    ;
                    if (edges[eloc] != 0) {
                        q.add(e);
                    }
                }
            }
        }
    }

    @Override
    public void dfs(int start) {
        boolean[] marks = new boolean[n];
        Stack<Integer> s = new Stack<>();
        int loc, eloc;

        s.add(start);

        while (!s.isEmpty()) {
            loc = s.pop();
            if (!marks[loc]) {
                marks[loc] = true;
                System.out.print(loc + "   ");

                for (int e = 0; e < n; e++) {
                    eloc = (loc < e)
                            ? (loc * (n - 1)) - (loc * (loc + 1)) / 2 + e - 1
                            : (e * (n - 1)) - (e * (e + 1)) / 2 + loc - 1
                    ;
                    if (edges[eloc] != 0) {
                        s.add(e);
                    }
                }
            }
        }
    }
}

class Adj_matrix_Digraph extends Adj_matrix_Un_Digraph implements Graph{

    public Adj_matrix_Digraph(int n) {
        super(n);
        this.edges = new int[n * n];
    }


}

public class ds_ch09 {
    public static void main(String[] args) {

        Adj_matrix_Un_Digraph g1 = new Adj_matrix_Un_Digraph(10);
        g1.add_vertex(1);
        g1.add_vertex(2);
        g1.add_vertex(5);
        g1.add_vertex(6);
        g1.add_vertex(7);
        g1.add_vertex(9);

        g1.add_edge(1, 2);
        g1.add_edge(1, 5);
        g1.add_edge(1, 9);
        g1.add_edge(2, 7);
        g1.add_edge(2, 6);
        g1.add_edge(5, 6);
        g1.add_edge(6, 7);

        //g1.bfs(7);
        //g1.dfs(1);
        //g1.dfs(9);
    }
}
