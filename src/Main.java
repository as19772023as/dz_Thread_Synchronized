import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Main {
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();
    private static final int length = 1000;
    private static final String letter = "RLRFR";
    public static int countKey = 0;

    public static void main(String[] args) throws InterruptedException {

        for (int i = 0; i < length; i++) {
            new Thread(() -> {
                String letters = generateRoute(letter, 100);
                int frequency = (int) letters.chars().filter(ch->ch == 'R').count();

                synchronized (sizeToFreq){
                    if(sizeToFreq.containsKey(frequency)){
                        sizeToFreq.put(frequency, sizeToFreq.get(frequency) + 1);
                    } else {
                        sizeToFreq.put(frequency, 1);
                    }
                }
            }).start();
        }

        Map.Entry<Integer, Integer> max = sizeToFreq.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .get();
        System.out.println("Самое частое количество повторений " + max.getKey() +
                " (встретилось " + max.getValue() + "  раз)");

        System.out.println("Другие размеры: ");
        sizeToFreq.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .forEach(e-> System.out.println(" - " + e.getKey()  + " встретилось " + e.getValue() + "  раз)"));

    }


    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }

//    public static Map<Integer, Integer> countFreq(String routes) {
//        for (int i = 0; i < routes.length(); i++) {
//            if (routes.charAt(i) == 'R') {
//                countKey++;
//            } else if (countKey > 0) {
//                  synchronized (sizeToFreq) {
//                sizeToFreq.put(countKey, sizeToFreq.getOrDefault(countKey, 0) + 1);
//                 }
//                countKey = 0;
//            }
//        }
//        return sizeToFreq;
//    }


 //   public static void maxCountFreq(Map<Integer, Integer> map) {
//        int max = 0;
//        for (Integer key : map.keySet()) {
//            Integer value = map.get(key);
//            if(max < key) max = key;
//        Map.Entry<Integer, Integer> maxFreq = map.entrySet()
//                .stream()
//                .sorted(Map.Entry.comparingByKey(Comparator.reverseOrder()))
//                .findFirst()
//                .get();
//        System.out.println("Самое частое количество повторений " + maxFreq.getKey() +
//                " (встретилось " + maxFreq.getValue() + "  раз)");
//        }
    }

