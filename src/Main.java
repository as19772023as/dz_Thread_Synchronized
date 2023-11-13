import java.util.*;

public class Main {
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();
    public static int countKey = 0;

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
            } else {
                if (sizeToFreq.containsKey(countKey)) {
                    int countValue = sizeToFreq.get(countKey) + 1;
                    sizeToFreq.replace(countKey, countValue);
                    countKey = 0;
                }
                sizeToFreq.put(countKey, 1);
                countKey = 0;
            }
        }
       // System.out.println(sizeToFreq);
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


    public static void main(String[] args) {
        int length = 1000;
        String letter = "RLRFR";
        String letters = generateRoute(letter, length);

       // System.out.println(letters);
        //maxCountFreq(countFreq(s));

        for (int i = 0; i < length; i++) {
            new Thread(() -> {
               synchronized (sizeToFreq) {
                   countFreq(letters);
                   sizeToFreq.notify();
               }
            }).start();
        }

        for (int i = 0; i < length; i++) {
            new Thread(()->{
                synchronized (sizeToFreq){
                    try {
                        sizeToFreq.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    maxCountFreq(sizeToFreq);
                }

            }).start();
        }
    }
}
