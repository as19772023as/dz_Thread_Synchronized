import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Main {
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();
    private static int length = 100;
    private static  String letter = "RLRFR";
    private static int AMOUNT_OF_THREAD = 1000;


    public static void main(String[] args) throws InterruptedException {
        List<Thread>threadList = new ArrayList<>();

        Thread printer = new Thread(()->{
            while (!Thread.interrupted()){
                synchronized (sizeToFreq){
                    try {
                        sizeToFreq.wait();
                    } catch (InterruptedException e) {
                        return;
                    }
                    printLeader();
                }
            }
        });
        printer.start();

        for (int i = 0; i < AMOUNT_OF_THREAD; i++) {
            threadList.add(getThread());
        }
        for (Thread thread : threadList) {
            thread.start();
            thread.join();
        }

        printer.interrupt();

    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }

    public static void printLeader(){
        Map.Entry<Integer, Integer> max = sizeToFreq.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .get();
        System.out.println("Самое частое количество повторений " + max.getKey() +
                " (встретилось " + max.getValue() + "  раз)");

    }

    public  static Thread getThread(){
        return new Thread(()->{
            String route = generateRoute(letter, length );
            int frequency = (int) route.chars().filter(ch->ch == 'R').count();

            synchronized (sizeToFreq){
                if(sizeToFreq.containsKey(frequency)){
                    sizeToFreq.put(frequency, sizeToFreq.get(frequency) + 1);
                } else {
                    sizeToFreq.put(frequency, 1);
                }
                sizeToFreq.notify();
            }
        });
    }
}
