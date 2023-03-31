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
    - 수식 변환 및 계산: 전위, 중위, 후위
        재귀 표현
        비 재귀 표현
    - 좌우 반전 (07.17)
    - 일반 트리 변환, 순회 (단, 재귀적으로)
    - 포리스트 변환, 순회 (단, 재귀적으로)


 */

import java.util.Arrays;

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

    public List_Binary_Tree(int h) {
        data = new Object[(int) Math.pow(2, h + 1)];
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
            if ((data[p] != null) && (!mark[p])) {
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
            if ((data[p] != null) && (((List_Binary_Tree) t).data[p] != null) && (!mark[p])) {
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
        }

        if (is_invalid(loc)) {
            System.out.println("INVALID LOCATION");
            return;
        }
        data[loc] = data_in;
    }
    public void insert(Object data_in, int loc) {
        if (!is_empty() && is_invalid(loc)) {
            System.out.println("INVALID LOCATION");
            return;
        }
        data[loc] = data_in;
    }
    public boolean is_invalid(int loc) {
        return (loc >= data.length) || (loc <= 0) || (data[loc / 2] == null);
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
    }

    @Override
    public void rec_in_order() {

    }

    @Override
    public void rec_post_order() {

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
            if ((data[p] != null) && (!mark[p])) {
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

    }

    @Override
    public void post_order() {

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
        return null;
    }
}

public class DS_ch07 {

    static Binary_Tree str_to_bt(String s) {

        return null;
    }

    static void list_bt_test() {
        List_Binary_Tree bt = new List_Binary_Tree(3);

        bt.insert("Alpha", 1);
        bt.insert("Sig", 2);
        bt.insert("Nut", 3);
        bt.insert("Z", 5);
        bt.insert("Aki", 6);
        bt.insert("Juan", 7);
        bt.insert("Sq", 4);
        bt.insert("Wood", 9);

        /*
        System.out.println(bt.is_empty());

        bt.insert("Alpha");
        bt.insert("Beta");
        bt.insert("Gamma");
        bt.insert("Delta");
        bt.insert("Eps");
        */

        /*
        bt.insert("Alpha", 1);
        bt.insert("Sig", 2);
        bt.insert("Nut", 3);
        bt.insert("Z", 5);
        bt.insert("Aki", 6);
        bt.insert("Juan", 7);

        bt.pre_order();

        bt.insert("Wood", 9);

        bt.insert("Sq", 4);
        bt.insert("Wood", 9);

        bt.pre_order();
         */

        /*
        List_Binary_Tree cpy = (List_Binary_Tree) bt.copy();
        System.out.println("IS EQUAL bt, cpy? >>> " + bt.is_equal(cpy));
         */

        bt.delete("Z");
        bt.delete("Nut");
        bt.delete("Os");
        bt.pre_order();

    }

    public static void main(String[] a) {

        list_bt_test();
    }

}
