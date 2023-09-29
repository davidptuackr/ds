package ds_ch10_가중치그래프;

/*
가중치 그래프 순회 기본 구상
    -   그래프
        -   형태: 인접 공유 리스트
        -   멤버
            -   정점: 키, 첫 간선 포인터
            -   간선: 가중치, 키1, 키2 (무방향이면 삽입 시 반드시 키1 < 키2가 되도록 할 것), 키1 다음 간선 포인터, 키2 다음 간선 포인터

    -   신장 / 경로 탐색 알고리즘
        -   알고리즘만 있는 별도 클래스에 전부 넣을 것
        -   정적 메소드로 구현
        -   반환하는 값은 그래프로 할 것
        -   반환하는 그래프는 원 그래프의 정점 + 선택된 간선으로 구성할 것

    -   크루스칼
        -   과정
            1.  가중치가 가장 작은 간선 선택
            2.  사이클 생성 여부 검사
                -   간선 양 끝 정점 모두 신장 트리에 이미 있다면 무시하고 다음 간선 조사
                -   아니면 해당 간선과 간선으로 연결된 정점 추가. 이 때 없는 쪽만 추가할 것
            3.  간선 수가 n-1이 되면 종료
 */

import java.util.HashMap;

class Graph {

    class Edge {
        int weight;
        int v_left;
        int v_right;
        Edge l_next;
        Edge r_next;

        private Edge(int w, int l, int r) {
            weight = w;
            v_left = l;
            v_right = r;
        }
    }

    HashMap<Integer, Edge> vertex;
    Edge edges;

    public Graph() {
        vertex = new HashMap<>();
        edges = null;
    }

    public void add_vertex(int key) {
        if (vertex.get(key) != null) { // 넘긴 값을 키로 하는 정점이 이미 있는 경우 리턴
            return;
        }
        vertex.put(key, null);
    }

    public void add_edge(int weight, int k1, int k2) {
        // 연결하려는 정점 중 하나라도 없다면 이을 수 없으므로 작업 취소
        if (!vertex.containsKey(k1) || !vertex.containsKey(k2)) {
            return;
        }

        // (1, 3, 1 다음, 3 다음) 형태로 저장되도록 작은 쪽을 left로 지정
        int v_left = Math.min(k1, k2);
        int v_right = Math.max(k1, k2);

        Edge e_new = new Edge(weight, v_left, v_right); // 새로 추가할 간선
        Edge e_iter;                                    // 간선 목록 순회 포인터

        // 새 간선의 왼쪽 정점 검사
        // 새 간선의 왼쪽 정점에 간선이 있다면 왼쪽 정점의 마지막 간선이 무엇인지 파악
        e_iter = seek_last_edge(v_left);
        if (e_iter == null) { // 왼쪽 정점에 부속 간선이 없다면 null을 새 간선으로 대체
            vertex.replace(v_left, e_new);
        }
        else { // 왼쪽 정점에 간선이 있다면 마지막 간선 다음 간선으로 추가
            if (e_iter.v_left == e_new.v_left) { // 정점이 마지막 간선의 왼쪽에 표시됐다면 마지막 간선의 l_next로 추가
                e_iter.l_next = e_new;
            }
            else { // 아니면 마지막 간선의 r_next로 추가
                e_iter.r_next = e_new;
            }
        }

        // 새 간선의 오른쪽 검사
        // 새 간선의 오른쪽 정점에 간선이 없다면 null을 새 간선으로 대체
        e_iter = seek_last_edge(v_right);
        if (e_iter == null) {
            vertex.replace(v_right, e_new);
        }
        else { // 오른쪽 정점에 간선이 있다면 마지막 간선 다음 간선으로 추가
            if (e_iter.v_left == e_new.v_left) {
                e_iter.l_next = e_new;
            }
            else {
                e_iter.r_next = e_new;
            }
        }

        // 만든 간선을 정점의 새 멤버로 추가하는 부분 구현할 것 (091023)
    }
    private Edge seek_last_edge(int key) {
        Edge e_target = vertex.get(key);
        Edge e_iter = e_target;
        if (e_iter != null) {
            while (e_target != null) {
                e_iter = e_target;
                e_target = (key == e_target.v_right) ? e_target.r_next : e_target.l_next;
            }
        }
        return e_iter;
    }
}

class Traveler {

    static void Kruskal(Object g) {

    }

}



public class DS_ch10 {

    public static void main(String[] args) {

        Graph g = new Graph();

        g.add_vertex(1);
        g.add_vertex(3);
        g.add_vertex(4);
        g.add_vertex(7);
        g.add_vertex(10);
        g.add_vertex(6);

        g.add_edge(3, 1, 4);
        g.add_edge(5, 10, 1);
        g.add_edge(999, -1, 999);

    }

}
