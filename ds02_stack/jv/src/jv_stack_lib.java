import java.util.Stack;

public class jv_stack_lib {
    public static void main(String[] args) {

        Stack<Integer> s1 = new Stack<>();

        System.out.printf("EMPTY STACK? -> %s%n", s1.empty());

        s1.add(99);
        s1.add(27);
        s1.add(4444);
        s1.add(0);
        s1.add(61);

        System.out.println(s1);
        for (int i = 0; i < s1.size(); i++) {
            System.out.print(s1.get(i) + " ");
        }

        int popped = s1.pop();
        System.out.printf("POPPED : %d\n", popped);
        System.out.printf("AFTER POP -> %s\n", s1);
    }
}
