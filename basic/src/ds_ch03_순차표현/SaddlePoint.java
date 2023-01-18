package ds_ch03_순차표현;

/*
03.10 안장점 찾아내기

    안장점: m*n 행렬에서 i행에선 최소, j열에선 최대인 원소 a_{i, j}

탐색
    1. 각 행의 최소값을 찾아낸 뒤 열 위치 정보를 저장해둔다
    2. 해당 열에서 최대값인지 확인한다
    3. 안장점인지 확인되면 출력한다
 */

import java.util.Random;

public class SaddlePoint {

    static int[][] init(int m, int n) {
        Random rand = new Random();
        int[][] A = new int[m][n];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                A[i][j] = rand.nextInt(10);
            }
        }

        return A;
    }

    static void print_matrix(int[][] A) {
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[i].length; j++) {
                System.out.format("%5d", A[i][j]);
            }
            System.out.println();
        }
    }

    static void search_saddle_point(int[][] A) {

        for (int i = 0; i < A.length; i++) {
            int min_i = 999, max_j = -1, min_j_loc = -1, max_i_loc = -1;

            for (int j = 0; j < A[i].length; j++) { // i 행의 최소값 & 위치한 열 탐색
                if (min_i > A[i][j]) {
                    min_i = A[i][j];
                    min_j_loc = j;
                }
            }
            for (int j = 0; j < A[i].length; j++) { // 최소값이 있는 열의 최대값 & 위치한 행 탐색
                if (max_j < A[j][min_j_loc]) {
                    max_j = A[j][min_j_loc];
                    max_i_loc = j;
                }
            }
            if (i == max_i_loc) {
                System.out.format("Saddle point at A[%d][%d]: %d\n",
                        i, min_j_loc, A[i][min_j_loc]
                );
            }
        }
    }

    public static void main(String[] args) {

        int[][] A = {
                {1, 2, 3},
                {2, 3, 4},
                {0, 4, 0}
        };
        print_matrix(A);
        search_saddle_point(A);
    }

}

/*
평가

시간복잡도: big_O(m^2n)
 */
