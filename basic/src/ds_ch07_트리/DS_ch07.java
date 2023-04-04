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
        }
        if (loc >= data.length) System.out.println("INVALID LOCATION");
        else data[loc] = data_in;
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
    }

    @Override
    public void rec_in_order() {
        
    }

    @Override
    public void rec_post_order() {

    }

    private void rec_pre_order(int cursor) {
        if (cursor == 0) {
            System.out.println();
            return;
        }

        if ((cursor > data.length) || (data[cursor] == null)) {
            return;
        }

        System.out.print(data[cursor] + " ");

        rec_pre_order(cursor * 2);
        rec_pre_order(cursor * 2 + 1);
    }

    public void rec_in_order(int cursor) {

    }

    public void rec_post_order(int cursor) {

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

        /*List_Binary_Tree bt1 = new List_Binary_Tree(3);
        bt1.insert("Alpha", 1);
        bt1.insert("Sig", 2);
        bt1.insert("Nut", 3);
        bt1.insert("Z", 5);
        bt1.insert("Aki", 6);
        bt1.insert("Juan", 7);
        bt1.insert("Sq", 4);
        bt1.insert("Wood", 9);
        list_bt_test(bt1);*/

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

    }

}
