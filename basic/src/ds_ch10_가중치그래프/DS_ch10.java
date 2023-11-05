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

    public Edge next(int v_start) {
        return v_left == v_start ? l_next : r_next;
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
        // 연결하는 위치와 가중치가 동일하다면 똑같은걸 또 놓는 것이므로 취소
        Edge e_iter = this.vertex.get(k1);

        while (e_iter != null) {
            if (e_iter.v_left == k1 && e_iter.v_right == k2 && e_iter.weight == weight) {
                return;
            }
            e_iter = e_iter.next(k1);
        }

        // (1, 3, 1 다음, 3 다음) 형태로 저장되도록 작은 쪽을 left로 지정
        int v_left = Math.min(k1, k2);
        int v_right = Math.max(k1, k2);

        Edge e = new Edge(v_left, v_right, weight); // 새로 추가할 간선
        e_iter = seek_last_edge(e.v_left); // 간선 목록 순회 포인터

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

    public static Graph prim(Graph g, int start) {

        /*
        프림 복습
            과정
                -   시작은 정점 중 아무거나 하나 골라서 한다
                -   정점에 부속한 가장 저비용인 간선 e를 고른다
                -   현재 신장 트리 내에 있는 정점 중 e 끝의 정점과 이어진 간선 준 최저비용의 간선 f를 고른다
                -   e, f 중 비용이 더 적은 간선과 그 끝의 정점 w를 추가한다
                -   w를 대상으로 지금까지 했던 작업을 똑같이 한다
                -   위 작업은 간선 수가 (정점 개수 - 1) 만큼 될 때까지 반복한다
            사이클 방지 대책
                -   정점 추가 시 신장 트리에 이미 있는 정점이라면 제외한다
                -   그 다음으로 비용이 적은 간선을 고려한다
         */
        
        /*
        수정할 것: 최저 비용의 간선이 사이클을 만든다면 그 다음으로 비용이 적으면서 사이클을 만들지 않는 간선을 찾아내도록 할 것
         */
        Graph res = new Graph();
        int n_edges = 0;
        Edge e_iter, e_min;
        int v_end;

        res.add_vertex(start);

        while (n_edges != g.vertex.size() - 1) {

            Set<Integer> res_keys = res.vertex.keySet();
            e_min = null;

            for (Integer key_iter : res_keys) {
                e_iter = g.vertex.get(key_iter);
                while (e_iter != null) {
                    v_end = (key_iter == e_iter.v_left) ? e_iter.v_right : e_iter.v_left;
                    if (!res.vertex.containsKey(v_end)) {
                        if (e_min == null || e_min.weight >= e_iter.weight) {
                            e_min = e_iter;
                        }
                    }
                    e_iter = e_iter.next(key_iter);
                }
            }
            assert e_min != null;
            if (!res.vertex.containsKey(e_min.v_left)) {
                res.add_vertex(e_min.v_left);
            }
            if (!res.vertex.containsKey(e_min.v_right)) {
                res.add_vertex(e_min.v_right);
            }
            res.add_edge(e_min.v_left, e_min.v_right, e_min.weight);

            n_edges++;
        }

        return res;
    }

    /*public static Graph solin(Graph g, int n_start) {

        *//*
        솔린 복습

        과정
            -   처음엔 >>> 정점 수 만큼의 트리 (n개) <<< 로 시작한다
            -   각 트리별로 소속된 정점들 내에서 가장 가중치가 작으면서 사이클을 만들지 않는 간선을 선택한다
            -   후술한 주의사항을 준수하면서 단 하나의 신장 트리를 완성시킨다

        주의사항
            -   트리들이 가중치가 같으면서 서로 다른 간선을 골랐을 경우
                >   한 쪽 트리만 간선을 선택하도록 한다

            -   트리들이 가중치가 같으면서 서로 같은 간선을 골랐을 경우
                >   해당 간선을 선택하고 트리를 합친다
         *//*

        Graph res = new Graph();
        Graph[] forest = new Graph[n_start];
        Queue<Edge> edges = new LinkedList<>();
        Iterator<Integer> it = g.vertex.keySet().iterator();
        int n_edges = 0;
        int v_end;
        Edge e_iter, e_min;
        Graph tree_left = null, tree_right = null;

        for (int i = 0; i < n_start && it.hasNext(); i++) {
            forest[i] = new Graph();
            forest[i].add_vertex(it.next());
        }

        while (n_edges < g.vertex.size() - 1) {

            *//*for (int i : res.vertex.keySet()) {
                e_iter = g.vertex.get(i);
                while (e_iter != null) {
                    v_end = (i == e_iter.v_left) ? e_iter.v_right : e_iter.v_left;
                    if (!res.vertex.containsKey(v_end)) {
                        if (e_min == null || e_min.weight >= e_iter.weight) {
                            e_min = e_iter;
                        }
                    }
                    e_iter = e_iter.next(i);
                }
                edges.add(e_iter);
            }*//*
            for (Graph tree : forest) {
                e_min = null;
                for (Integer key_iter : tree.vertex.keySet()) {
                    e_iter = g.vertex.get(key_iter);
                    while (e_iter != null) {
                        v_end = (key_iter == e_iter.v_left) ? e_iter.v_right : e_iter.v_left;
                        if (!tree.vertex.containsKey(v_end)) {
                            if (e_min == null || e_min.weight >= e_iter.weight) {
                                e_min = e_iter;
                            }
                        }
                        e_iter = e_iter.next(key_iter);
                    }
                }
                if (!edges.contains(e_min)) {
                    edges.add(e_min);
                }
            }

            while (!edges.isEmpty()) {
                e_iter = edges.poll();
                for (Graph tree : forest) {
                    if (tree.vertex.containsKey(e_iter.v_left)) {
                        tree_left = tree;
                    }
                    else if (tree.vertex.containsKey(e_iter.v_right)) {
                        tree_right = tree;
                    }
                }
                if (tree_left != null && tree_right != null) {
                    if (!tree_left.vertex.containsValue(e_iter) || !tree_right.vertex.containsValue(e_iter)) {
                        // merge tree
                        // START FROM HERE (100823 1604)
                    }
                }
            }

            n_edges++;
        }

        return res;
    }*/

    public static Graph solin(Graph g) {

        /*
        솔린 복습

        과정
            -   처음엔 >>> 정점 수 만큼의 트리 (n개) <<< 로 시작한다
            -   각 트리별로 소속된 정점들 내에서 가장 가중치가 작으면서 사이클을 만들지 않는 간선을 선택한다
            -   후술한 주의사항을 준수하면서 단 하나의 신장 트리를 완성시킨다

        주의사항
            -   트리들이 가중치가 같으면서 서로 다른 간선을 골랐을 경우
                >   한 쪽 트리만 간선을 선택하도록 한다

            -   트리들이 가중치가 같으면서 서로 같은 간선을 골랐을 경우
                >   해당 간선을 선택하고 트리를 합친다
         */

        /*
        시도해볼 방법 (110323)

            1.  다음을 준비한다
                Graph res : 결과물
                Graph List forest
                    간선을 선택할 수 있는 트리들을 나타냄.
                    처음엔 하나의 정점으로만 이뤄졌다 시간이 갈수록 합쳐지면서 리스트 길이는 줄고 요소들의 몸칩은 커진다
                List edge_picked
                    각 트리에서 선택한 간선
                    edge_picked[i] 는 forest[i] 가 선택한 간선
                Graph tree_left, tree_right
                    트리 병합에 사용할 임시 변수
                    서로 동일한 간선을 고른 트리들로
                    tree_left : v_left 만 포함한 트리, tree_right : v_right 만 포함한 트리로 한다

         */

        Graph res = new Graph();
        Vector<Graph> forest = new Vector<>();
        Vector<Edge> edge_picked = new Vector<>();
        int n_edges = 0;
        int v_end, i;
        Edge e_iter, e_min;
        Graph tree_left, tree_right;

        for (Integer v : g.vertex.keySet()) {
            Graph t = new Graph();
            t.add_vertex(v);
            forest.add(t);
            edge_picked.add(null);
        }

        while (n_edges < g.vertex.size() - 1) {
            tree_left = null;
            tree_right = null;
            i = 0;

            for (Graph tree : forest) { // 각 트리에 대해 조사
                e_min = null;
                for (Integer key_iter : tree.vertex.keySet()) { // 각 트리 내 정점에 대해 조사
                    e_iter = g.vertex.get(key_iter);
                    while (e_iter != null) {
                        v_end = (key_iter == e_iter.v_left) ? e_iter.v_right : e_iter.v_left;
                        if (!tree.vertex.containsKey(v_end)) { // v_end가 트리에 없고 (즉, 이 간선을 넣어도 사이클이 안생기고)
                            if (e_min == null || e_min.weight >= e_iter.weight) { // 최소 간선이 정해지지 않았거나 더 작은 간선일 때
                                e_min = e_iter; // 최소 간선 갱신
                            }
                        }
                        e_iter = e_iter.next(key_iter);
                    }
                }
                if (!edge_picked.contains(e_min)) {
                    edge_picked.setElementAt(e_min, i);
                }
                i++;
            }
            i = 0;

            /*
            ConcurrentModificationException
                컬렉션 객체를 수정할 때 발생
                컬렉션을 순회하는 Iterator의 modCount와 컬렉션의 modCount가 다를 경우 발생
                즉, 원본을 이용해 순회 객체를 만들었는데 중간에 원본에 수정이 가해지면 아까 만든 순회 객체는 어떻할지 모르게 되는 것
             */

            Iterator<Edge> eit = edge_picked.iterator();

            while (eit.hasNext()) {
                Edge e = (Edge) eit.next();
                e_iter = e;
                if (e != null) {
                    for (Graph tree : forest) {
                        if (tree.vertex.containsKey(e_iter.v_left)) {
                            tree_left = tree;
                        } else if (tree.vertex.containsKey(e_iter.v_right)) {
                            tree_right = tree;
                        }
                    }
                    assert tree_left != null;
                    assert tree_right != null;
                    if (!tree_left.vertex.containsValue(e_iter) || !tree_right.vertex.containsValue(e_iter)) {
                        for (Integer key_iter : tree_left.vertex.keySet()) {
                            tree_right.add_vertex(key_iter);
                        }
                        for (Integer key_iter : tree_left.vertex.keySet()) {
                            e_iter = tree_left.vertex.get(key_iter);
                            while (e_iter != null) {
                                tree_right.add_edge(e_iter.v_left, e_iter.v_right, e_iter.weight);
                                e_iter = e_iter.next(key_iter);
                            }
                        }
                        tree_right.add_edge(e.v_left, e.v_right, e.weight);
                        if (tree_left.vertex.size() > tree_right.vertex.size()) {
                            forest.remove(tree_right);
                        }
                        else {
                            forest.remove(tree_left);
                        }
                        eit.remove();
                    }
                    n_edges++;
                }
                else {
                    i++;
                }
            }
            edge_picked.removeAllElements();
        }

        res = forest.get(0);

        return res;
    }

    private static boolean is_member(Graph g, Edge e) {
        return g.vertex.containsKey(e.v_left) && g.vertex.containsKey(e.v_right);
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

        //Graph kruskal_res = Traveler.Kruskal(g);
        //Graph prim_res = Traveler.prim(g, 4);
        Graph solin_res = Traveler.solin(g);
    }

}