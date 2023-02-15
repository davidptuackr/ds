package ds_ch05_스택;

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
 */


public class DS_ch05 {

    public static void main(String[] args) {

        LR_Stack lr1 = new LR_Stack(5);
        lr1.push("Zydeco", 0);
        lr1.push("Matt", 0);
        lr1.push("Amber", 1);
        lr1.push("Bengal", 1);

        System.out.println(lr1);

        lr1.push("Zydeco", 0);

    }

}
