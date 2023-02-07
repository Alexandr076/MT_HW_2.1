import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.stream.Collectors.toMap;


public class Main {
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();
    private static final int COUNT_OF_THREADS = 1000;
    private static final String STRING_FOR_GENERATOR = "RLRFR";
    private static final int STRING_LENGTH_FOR_GENERATOR = 100;

    public static Integer generateRoute(String letters, int length) {
        Random random = new Random();
        //StringBuilder route = new StringBuilder();
        char tempLetter;
        Integer countOfR = 0;
        for (int i = 0; i < length; i++) {
            tempLetter = letters.charAt(random.nextInt(letters.length()));
            //route.append(tempLetter);
            if (tempLetter == 'R') {
                countOfR++;
            }
        }
        return countOfR;
    }

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(COUNT_OF_THREADS);

        for (int i = 1; i <= COUNT_OF_THREADS; i++) {
            Runnable thread = new Thread(() -> {
                Integer countOfR = generateRoute(STRING_FOR_GENERATOR, STRING_LENGTH_FOR_GENERATOR);
                synchronized (sizeToFreq) {
                    if (sizeToFreq.get(countOfR) == null) {
                        sizeToFreq.put(countOfR, 1);
                    } else {
                        sizeToFreq.replace(countOfR, sizeToFreq.get(countOfR) + 1);
                    }
                }
            });
            executor.execute(thread);
        }

        executor.shutdown();

        Map<Integer, Integer> sortedMap = sizeToFreq.entrySet()
                .stream()
                .sorted(
                        Map.Entry.<Integer, Integer>comparingByValue().reversed())
                .collect(toMap(Map.Entry::getKey,
                        Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        System.out.print("Самое частое количество повторений " +
                sortedMap.entrySet().toArray()[0].toString().replace("=", " (встретилось "));
        System.out.println(" раз(а))");
        System.out.println("Другие размеры:");
        Integer firstElement = Integer.valueOf(sortedMap.entrySet().toArray()[0].toString().substring(0,
                sortedMap.entrySet().toArray()[0].toString().indexOf("=")));
        sortedMap.remove(firstElement);
        for (Map.Entry<Integer, Integer> entry : sortedMap.entrySet()) {
            System.out.println("- " + entry.getKey() + " (" + entry.getValue() + " раз)");
        }
    }
}
