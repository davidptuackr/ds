package ds_ch03_순차표현;

/*
03.11 배열을 이용한 다항식 구현

1. 다항식 입/출력 연산
2. polyAdd(): 서로 다른 두 다항식을 더한 결과 반환
3. polyMult(): 서로 다른 두 다항식을 곱한 결과 반환
4. polyEval(): 어떤 값 x0에 대해 다항식 p(x0)의 결과를 계산한 결과 반환

0. 구조
    식: 2*(계수가 0이 아닌 항 개수)
    식[계수][차수] 형태의 이차원 배열
    편의상 계수, 차수 모두 0 이상의 정수로 설정

1.
    1.1 입력
        1. 계수가 0이 아닌 항의 수 입력
        2. 다음을 계수가 0이 아닌 항의 수만큼 반복
            2.1 식[i][0] >>> 계수
            2.2 식[i][1] >>> 차수
    1.2 출력
        1. 식 길이와 같은 크기의 문자열 배열 생성
        2. 다음을 (식 길이-1) 만큼 반복
            2.1 (계수)x^(차수)+ 형태로 문자열 생성
            2.2 2.1을 문자열 배열에 저장
        3. 문자열 배열의 마지막에는 2.1에서 맨 뒤의 +만 뺀 형태로 저장
        4. 문자열 배열을 잘 출력

2.
    1. 일단 p1 항 수 + p2 항 수 구하기
    2. 다음 작업을 둘 중 항 수가 더 적은 식의 길이만큼 반복
        2.1 p1 차수와 p2 차수가 같은지 비교
        2.2 같다면 1의 결과에서 -1
    3. 2에서 차감한 (1) 길이의 다항식 p 생성
    4. loc_p1, loc_p2 = 0으로 초기화 후 다음 작업을 p 길이만큼 반복
        4.1 p1[loc_p1]의 차수 == p2[loc_p2]의 차수일 경우
            1. p의 항 = 양측 계수의 합 & 차수는 그대로
            2. loc_p1 / p2 둘 다 ++
        4.2 p1[loc_p1]의 차수 > p2[loc_p2]의 차수일 경우
            1. p의 항 = p1[loc_p1]의 항
            2. loc_p1++
        4.2 p1[loc_p1]의 차수 > p2[loc_p2]의 차수일 경우
            1. 4.2 반대로
 */
import java.util.Scanner;

class ArrayPolynomial {

    int[][] p;

    public ArrayPolynomial() {    }
    public ArrayPolynomial(int sizeof_p) {
        this.p = new int[sizeof_p][2];
    }

    void read() {
        Scanner sc = new Scanner(System.in);
        this.p = new int[sc.nextInt()][2];

        for (int i = 0; i < this.p.length; i++) {
            p[i][0] = sc.nextInt(); // 계수
            p[i][1] = sc.nextInt(); // 차수
        }
    }

    void print() {
        String[] es = new String[this.p.length];
        for (int i = 0; i < this.p.length-1; i++) {
            es[i] = String.format("%dx^%d + ", this.p[i][0], this.p[i][1]);
        }
        es[this.p.length-1] = String.format(
                "%dx^%d",
                this.p[this.p.length-1][0], this.p[this.p.length-1][1]
        );

        for (int i = 0; i < this.p.length; i++) {
            System.out.print(es[i]);
        }
        System.out.println();
    }

    static ArrayPolynomial polyAdd(ArrayPolynomial p1, ArrayPolynomial p2) {
        int sizeof_p = p1.p.length + p2.p.length;

        for (int i = 0; i < p1.p.length; i++) {
            for (int j = 0; j < p2.p.length; j++) {
                if (p1.p[i][1] == p2.p[j][1]) sizeof_p--;
            }
        }

        ArrayPolynomial p3 = new ArrayPolynomial(sizeof_p);
        int loc_p1 = 0, loc_p2 = 0;
        int longer = Math.min(p1.p.length, p2.p.length);

        for (int i = 0; i < sizeof_p - longer; i++) {
            if (p1.p[loc_p1][1] == p2.p[loc_p2][1]) {
                p3.p[i][0] = p1.p[loc_p1][0] + p2.p[loc_p2][0];
                p3.p[i][1] = p1.p[loc_p1][1];
                loc_p1++; loc_p2++;
            } else if (p1.p[loc_p1][1] >= p2.p[loc_p2][1]) {
                p3.p[i][0] = p1.p[loc_p1][0];
                p3.p[i][1] = p1.p[loc_p1][1];
                loc_p1++;
            } else {
                p3.p[i][0] = p2.p[loc_p2][0];
                p3.p[i][1] = p2.p[loc_p2][1];
                loc_p2++;
            }
        }

        if (loc_p1 < p1.p.length) {
            for (int i = loc_p2; i < sizeof_p; i++) {
                p3.p[i][0] = p1.p[loc_p1][0];
                p3.p[i][1] = p1.p[loc_p1++][1];
            }
        }
        if (loc_p2 < p2.p.length) {
            for (int i = loc_p1; i < sizeof_p; i++) {
                p3.p[i][0] = p1.p[loc_p2][0];
                p3.p[i][1] = p1.p[loc_p2++][1];
            }
        }

        return p3;
    }
}

public class ArrayPolynomial_test {

    public static void main(String[] args) {

        ArrayPolynomial p1 = new ArrayPolynomial();
        ArrayPolynomial p2 = new ArrayPolynomial();
        ArrayPolynomial p3;
        p1.read();
        p2.read();
        p1.print();
        p2.print();

        p3 = ArrayPolynomial.polyAdd(p1, p2);
        p3.print();
    }
}
