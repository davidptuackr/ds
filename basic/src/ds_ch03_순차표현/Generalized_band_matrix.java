package ds_ch03_순차표현;

/*
3-8. 일반밴드행렬

1. A(n, a, b)안의 원소 수
    (n^2)-(((n-a)^2 + (n-b)^2)/2)

2. A(n, a, b)의 원소 a(ij)에서 i, j의 관계
    i >= j >>> 아래쪽 대각선 (주대각선 포함), 1차원으로 표현 시 j가 오프셋
    i < j >>> 위쪽 대각선, 1차원으로 표현 시 i가 오프셋

3. 1차원 배열에 A(n, a, b)를 표현하려면
    ㄴ
 */

public class Generalized_band_matrix {

    public static void main(String[] args) {

        int n = 4;
        int a = 3;
        int s = 0;
        int[][] A = {
                {1, 1, 1, 0},
                {1, 2, 2, 2},
                {1, 2, 3, 3},
                {0, 2, 3, 4},
        };
        int[] B = {1, 2, 1, 2, 3, 1, 2, 3, 4, 1, 2, 3, 1, 2};

        int i = 2;
        int j = 1;
        int bloc = 0;

        if (i >= j) {
            for(int x = 1; x <= (a-(i-j)-1); x++) { s += n-(a-(x)); }
            bloc = s + j;
        }
        else {
            for(int x = 1; x <= (a); x++) {
                s += n-(a-(x));
            }
            for(int x = 1; x <= (j-i)-1; x++) { s += n-x; }
            bloc = s + i;
        }

        System.out.format("A[%d][%d]: %d >>> B[%d]: %d\n",
                i, j, A[i][j],
                bloc, B[bloc]
        );

    }
}
