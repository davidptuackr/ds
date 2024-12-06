import java.util.Hashtable;
import java.util.HashMap;

public class Jv_Hashtable {
    public static void main(String[] args) {
        Hashtable<String, Integer> table = new Hashtable<>();

        // 값 추가
        table.put("Apple", 1);
        table.put("Banana", 2);

        // 값 가져오기
        System.out.println("Apple: " + table.get("Apple"));

        // 키-값 쌍 출력
        table.forEach((k, v) -> System.out.println(k + ": " + v));

        HashMap<String, Integer> map = new HashMap<>();

        // 값 추가
        map.put("Apple", 1);
        map.put("Banana", 2);
        map.put(null, 3); // null 키 허용

        // 값 가져오기
        System.out.println("Apple: " + map.get("Apple"));

        // 키-값 쌍 출력
        map.forEach((k, v) -> System.out.println(k + ": " + v));

        
    }
}
