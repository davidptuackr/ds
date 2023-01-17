package ds_ch03_순차표현;

/*
3.9 선형 리스트 ADT 명세하기

    L: 리스트
    i: 1 <= i <= length(L)인 정수
    x, y: 특정 값

    createList(): 공백 리스트 생성


    isEmpty(L): 리스트 L이 공백인지 검사
        return length(L) == 0

    length(L): 리스트 L의 길이를 계산 >>> 리스트에 포함된 원소의 수 계산
        길이 = 0
        while(L의 원소에 후속자가 있을 때까지) 길이 1씩 증가
        if 길이 == 0 return 0
        else return 길이 + 1 (마지막 원소가 반영되지 않은 것을 감안한 것)

    retrieve(L, i): 리스트 L의 i번째 원소 검색
        return L[i]?

    replace(L, x, y): L의 원소 x를 y로 대체
        *** L에서 중복되는 값이 없다고 가정
        x위치 = 0
        while L[x위치] != x, x위치 1씩 증가
        L[x위치] = y
        return

    delete(L, x): L의 원소 x 제거. 이 때 L 길이 1 감소
        L2 = L보다 길이 1 작은 리스트
        위치 = 0
        while L[위치] != x
            L2[위치] = L[위치]
            위치 1씩 증가
        위치 1 증가 (x 건너뛰기)
        while 위치 != length(L)
            L2[위치-1] = L[위치]
            위치 1씩 증가
        return L2

    insert(L, i, x): L의 i번 위치에 x 추가. 이 때 L 길이 1 증가
        L2 = L보다 길이 1 큰 리스트
        위치 = 0
        while 위치 != i
            L2[위치] = L[위치]
            위치 1씩 증가
        L2[i] = x
        위치 1 증가 (x 삽입 결과 반영)
        while 위치 != length(L)
            L2[위치] = L[위치+1]
            위치 1씩 증가
        return L2

 */

public class ListADT {
}
