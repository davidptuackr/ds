package ds_ch03_순차표현;

/*
3-8. 일반밴드행렬

1. A(n, a, b)안의 원소 수
    (n^2)-(((n-a)^2 + (n-b)^2)/2)

2. A(n, a, b)의 원소 a(ij)에서 i, j의 관계
    i >= j >>> 아래쪽 대각선 (주대각선 포함), 1차원으로 표현 시 j가 오프셋
    i < j >>> 위쪽 대각선, 1차원으로 표현 시 i가 오프셋

3.
    1. 1차원 배열 C에 A(n, a, b)를 표현하려면?
    2. A(n, a, b)의 원소 a(ij)를 결정하는 메소드 value(n, a, b, i, j, C)를 구현하시오.

행렬 초기화
    1. 아래쪽 & 주대각선
        for (int x = a-1, x >= 0, x--) {
            for (int y = 0, y <= (n-x), y++) {
                iloc = x+y;
                jloc = y;
                A[iloc][jloc] = y
            }
        }
    2. 위쪽
        for (int y = 1, x >= n-a+1, y++) {
            for (int x = 0, x <= (n-y), y++) {
                iloc = x;
                jloc = x+y;
                A[iloc][jloc] = x
            }
        }

 */



public class Generalized_band_matrix {

    static int[][] init(int n, int a, int b) {
        int[][] A = new int[n][n];

        for (int x = a-1; x >= 0; x--) {
            for (int y = 0; y <= (n-x-1); y++) {
                int iloc = x + y;
                int jloc = y;
                A[iloc][jloc] = y+1;
            }
        }

        for (int y = 1; y <= b-1; y++) {
            for (int x = 0; x <= (n-y-1); x++) {
                int iloc = x;
                int jloc = x + y;
                A[iloc][jloc] = x+1;
            }
        }
        return A;
    }

    static int[] flat(int[][] A, int a, int b) {

        int n = A.length;
        int s = 0, cnt0_u = 0, cnt0_d = 0;

        /*
        an = 1, 3, 6, 10, ...
        bn = 2, 3, 4, ...
           = n+1

        Sbn
            = Sigma(k=1 to n) k+1
        an
            = 1+Sigma(k=1 to n-1) k+1
            = 1+n(n-1)/2+n-1
            = n+n(n-1)/2
            = n/2 (n+1)
            = (n^2+n)/2
        San
            = 1/2 Sigma(k=1 to n) (k^2+k)
            = 1/2 (n(n+1)(2n+1)/6+n(n+1)/2)
         */

        cnt0_d = ((n-a)+1)*(n-a)/2;
        cnt0_u = ((n-b)+1)*(n-b)/2;

        int[] B = new int[n*n-(cnt0_d+cnt0_u)];

        for (int x = a-1; x >= 0; x--) {
            for (int y = 0; y <= (n-x-1); y++) {
                int iloc = x + y;
                int jloc = y;
                B[s + jloc] = A[iloc][jloc];
            }
            s += (n-x);
        }

        for (int y = 1; y <= b-1; y++) {
            for (int x = 0; x <= (n-y-1); x++) {
                int iloc = x;
                int jloc = x + y;
                B[s + iloc] = A[iloc][jloc];
            }
            s += (n-y);
        }

        return B;
    }

    static void value(int n, int a, int b, int i, int j, int[] B, int[][] A) {

        int bloc = 0;
        int s = 0;

        if (i >= j) {
            for(int x = 1; x <= (a-(i-j)-1); x++) { s += n-(a-(x)); }
            bloc = s + j;
        }
        else {
            for(int x = 1; x <= (a); x++) { s += n-(a-(x)); }
            for (int y = 1; y <= (j-i)-1; y++) { s += (n-y); }
            bloc = s + i;
        }


        System.out.format(
                "A[%d][%d]: %d >>> B[%d]: %d\n",
                i, j, A[i][j],
                bloc, B[bloc]
        );
    }

    static void print_matrix(int[][] A) {
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                System.out.format("%5d", A[i][j]);
            }
            System.out.println();
        }
    }

    static void print_array(int[] B, int n, int a, int b) {

        int s = 0;

        for (int i = a-1; i <= 0; i--) {
            for (int j = 0; j <= n-i-1; j++) {
                System.out.format("%5d", B[s+j]);
            }
            s += (n-i);
            System.out.println();
        }

        for (int j = 1; j < b; j++) {
            for (int i = 0; i <= n-j-1; i++) {
                System.out.format("%5d", B[s+i]);
            }
            s += (n-j);
            System.out.println();
        }
    }

    public static void main(String[] args) {

        int n = 4;
        int a = 3;
        int b = 2;

        int[][] A = init(n, a, b);
        int[] B=  flat(A, a, b);

        value(n, a, b, 2, 1, B, A);

    }
}
