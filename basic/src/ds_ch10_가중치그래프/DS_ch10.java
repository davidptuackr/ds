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

import java.util.*;
import java.util.stream.Collectors;

class Edge {
    int weight;
    int v_left;
    int v_right;
    Edge l_next;
    Edge r_next;

    Edge(int l, int r, int w) {
        v_left = l;
        v_right = r;
        weight = w;
    }
}

class Graph {

    HashMap<Integer, Edge> vertex;

    public Graph() {
        vertex = new HashMap<>();
    }

    public void add_vertex(int key) {
        if (vertex.get(key) != null) { // 넘긴 값을 키로 하는 정점이 이미 있는 경우 리턴
            return;
        }
        vertex.put(key, null);
    }

    public void add_edge(int k1, int k2, int weight) {
        // 연결하려는 정점 중 하나라도 없다면 이을 수 없으므로 작업 취소
        if (!vertex.containsKey(k1) || !vertex.containsKey(k2)) {
            return;
        }

        // (1, 3, 1 다음, 3 다음) 형태로 저장되도록 작은 쪽을 left로 지정
        int v_left = Math.min(k1, k2);
        int v_right = Math.max(k1, k2);

        Edge e = new Edge(v_left, v_right, weight); // 새로 추가할 간선
        Edge e_iter = seek_last_edge(e.v_left); // 간선 목록 순회 포인터

        // 새 간선의 왼쪽 정점 검사
        // 새 간선의 왼쪽 정점에 간선이 있다면 왼쪽 정점의 마지막 간선이 무엇인지 파악
        if (e_iter == null) { // 왼쪽 정점에 부속 간선이 없다면 null을 새 간선으로 대체
            vertex.replace(e.v_left, e);
        }
        else { // 왼쪽 정점에 간선이 있다면 마지막 간선 다음 간선으로 추가
            if (e_iter.v_left == e.v_left) { // 정점이 마지막 간선의 왼쪽에 표시됐다면 마지막 간선의 l_next로 추가
                e_iter.l_next = e;
            }
            else { // 아니면 마지막 간선의 r_next로 추가
                e_iter.r_next = e;
            }
        }

        // 새 간선의 오른쪽 검사
        // 새 간선의 오른쪽 정점에 간선이 없다면 null을 새 간선으로 대체
        e_iter = seek_last_edge(e.v_right);
        if (e_iter == null) {
            vertex.replace(e.v_right, e);
        }
        else { // 오른쪽 정점에 간선이 있다면 마지막 간선 다음 간선으로 추가
            if (e_iter.v_left == e.v_right) {
                e_iter.l_next = e;
            }
            else {
                e_iter.r_next = e;
            }
        }
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

    public static Graph Kruskal(Graph g) {

        /*
        쿠르스칼 복습
            과정 : 그래프 내에서 비용이 가장 작은 간선을 고르는 것으로 시작해 그 다음 작은 간선을 선택하는 식으로 확장하는 방식
            사이클 검사 : 간선을 추가할 때 간선으로 연결한 두 정점이 모두 신장 트리에 있으면 추가하지 않고 다른 간선을 선택한다
         */

        Graph res = new Graph();
        int n_edges = 0;
        Iterator<Edge> edges = get_sorted_edge_list(g).iterator();    // 가중치 순으로 오름차순 정렬된 간선 목록 만들기
        Set<Integer> keys = g.vertex.keySet();

        Edge e_iter;
        int v_left, v_right;

        while ((n_edges != g.vertex.size() - 1) && (edges.hasNext())) {
            e_iter = edges.next();
            v_left = e_iter.v_left;
            v_right = e_iter.v_right;
            if (res.vertex.containsKey(v_left) && res.vertex.containsKey(v_right)) {
                continue;
            }
            if (!res.vertex.containsKey(v_left)) {
                res.add_vertex(v_left);
            }
            if (!res.vertex.containsKey(v_right)) {
                res.add_vertex(v_right);
            }
            res.add_edge(v_left, v_right, e_iter.weight);

            n_edges++;
        }
        if (n_edges != g.vertex.size() - 1) {
            System.out.println("FAIL TO CREATE SPANNING TREE");
            return null;
        }

        return res;
    }

    private static ArrayList<Edge> get_sorted_edge_list(Graph g) {
        Iterator<Integer> keys = g.vertex.keySet().iterator();
        Set<Edge> edge_set = new HashSet<>();

        while (keys.hasNext()) {
            int key = keys.next();
            Edge e_iter = g.vertex.get(key);
            while (e_iter != null) {
                edge_set.add(e_iter);
                e_iter = (e_iter.v_left == key) ? e_iter.l_next : e_iter.r_next;
            }
        }
        ArrayList<Edge> edge_list = new ArrayList<>(edge_set);
        edge_list.sort(Comparator.comparingInt(o -> o.weight));

        return edge_list;
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

        g.add_edge(4, 1, 3);
        g.add_edge(1, 10, 5);
        g.add_edge(7, 4, 7);
        g.add_edge(7, 1, 1);
        g.add_edge(6, 3, 9);
        g.add_edge(3, 1, 2);
        g.add_edge(3, 4, 4);
        g.add_edge(10, 4, 8);
        g.add_edge(7, 6, 17);

        // 사이클 테스트 + 같읕 가중치의 경우 테스트
        g.add_edge(10, 6, 7);
        g.add_edge(10, 3, 5);

        Graph kruskal_res = Traveler.Kruskal(g);
    }

}
