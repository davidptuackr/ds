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

public class StackTest {

}
