import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {
    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static int countMaxChar(BlockingQueue<String> queue, char letter) throws InterruptedException {
        int maxCounter = 0;
        int counter;

        for (int i = 0; i < 10_000; i++) {
            counter = 0;
            String str = queue.take();
            for (char currentLetter : str.toCharArray()) {
                if (currentLetter == letter) {
                    counter++;
                }
            }
            if (counter > maxCounter) maxCounter = counter;
        }
        return maxCounter;
    }

    public static BlockingQueue<String> queue1 = new ArrayBlockingQueue<>(100);
    public static BlockingQueue<String> queue2 = new ArrayBlockingQueue(100);
    public static BlockingQueue<String> queue3 = new ArrayBlockingQueue(100);

    public static void main(String[] args) throws InterruptedException {
        Thread makeTexts = new Thread(() -> {
            for (int i = 0; i < 10_000; i++) {
                String text = generateText("abc", 100_000);
                try {
                    queue1.put(text);
                    queue2.put(text);
                    queue3.put(text);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }

        });
        makeTexts.start();

        Thread letterA = new Thread(() -> {
            try {
                System.out.println("Максимальное количество символов 'a' " + countMaxChar(queue1, 'a'));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        letterA.start();

        Thread letterB = new Thread(() -> {
            try {
                System.out.println("Максимальное количество символов 'b' " + countMaxChar(queue2, 'b'));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        letterB.start();

        Thread letterC = new Thread(() -> {
            try {
                System.out.println("Максимальное количество символов 'c' " + countMaxChar(queue3, 'c'));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        letterC.start();

        letterA.join();
        letterB.join();
        letterC.join();
    }
}
