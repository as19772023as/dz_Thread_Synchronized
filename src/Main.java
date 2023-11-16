import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Main {
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();
    private static final int length = 1000;
    private static final String letter = "RLRFR";
    public static int countKey = 0;

    public static void main(String[] args) throws InterruptedException {
        String letters = generateRoute(letter, length);
        for (int i = 0; i < length; i++) {
            new Thread(() -> {
                countFreq(letters);
            }).start();
        }

        Thread.sleep(300);
        maxCountFreq(sizeToFreq);
    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }

    public static Map<Integer, Integer> countFreq(String routes) {
        for (int i = 0; i < routes.length(); i++) {
            if (routes.charAt(i) == 'R') {
                countKey++;
            } else if (countKey > 0) {
                synchronized (sizeToFreq) {
                    sizeToFreq.put(countKey, sizeToFreq.getOrDefault(countKey, 0) + 1);
                }
                countKey = 0;
            }
        }
        return sizeToFreq;
    }


    public static void maxCountFreq(Map<Integer, Integer> map) {
        Map.Entry<Integer, Integer> maxFreq = map
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey(Comparator.reverseOrder()))
                .findFirst()
                .get();
        System.out.println("Самое частое количество повторений " + maxFreq.getKey() +
                " (встретилось " + maxFreq.getValue() + "  раз)");

        System.out.println("Другие размеры: ");

        for (Map.Entry<Integer, Integer> maxFreq2 : map.entrySet()) {
            if (maxFreq.getKey() == maxFreq2.getKey()) {
                continue;
            }
            System.out.println("- " + maxFreq2.getKey() + " ( " + maxFreq2.getValue() + "  раз)");
        }
    }
}
