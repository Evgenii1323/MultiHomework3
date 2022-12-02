import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static AtomicInteger counting1 = new AtomicInteger(0);
    public static AtomicInteger counting2 = new AtomicInteger(0);
    public static AtomicInteger counting3 = new AtomicInteger(0);
    public static volatile String[] texts = new String[100_000];

    public static void main(String[] args) throws InterruptedException {
        Random random = new Random();
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }

        Thread thread1 = new Thread(() -> {
            for (String i : texts) {
                if (i.length() == 3 && firstCheck(i)) {
                        counting1.getAndIncrement();
                } else if (i.length() == 4 && firstCheck(i)) {
                        counting2.getAndIncrement();
                } else if (i.length() == 5 && firstCheck(i)) {
                        counting3.getAndIncrement();
                }
            }
        });
        thread1.start();

        Thread thread2 = new Thread(() -> {
            for (String i : texts) {
                if (i.length() == 3 && secondCheck(i)) {
                    counting1.getAndIncrement();
                } else if (i.length() == 4 && secondCheck(i)) {
                    counting2.getAndIncrement();
                } else if (i.length() == 5 && secondCheck(i)) {
                    counting3.getAndIncrement();
                }
            }
        });
        thread2.start();

        Thread thread3 = new Thread(() -> {
            for (String i : texts) {
                if (i.length() == 3 && thirdCheck(i)) {
                    counting1.getAndIncrement();
                } else if (i.length() == 4 && thirdCheck(i)) {
                    counting2.getAndIncrement();
                } else if (i.length() == 5 && thirdCheck(i)) {
                    counting3.getAndIncrement();
                }
            }
        });
        thread3.start();

        thread1.join();
        thread2.join();
        thread3.join();

        System.out.println("Красивых слов с длиной 3 " + counting1 + " шт ");
        System.out.println("Красивых слов с длиной 4 " + counting2 + " шт ");
        System.out.println("Красивых слов с длиной 5 " + counting3 + " шт ");
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static boolean firstCheck(String string) {
        char[] chars = string.toCharArray();
        char c = chars[0];
        int counting = 0;
        for (char i : chars) {
            if (i == c) {
                counting += 1;
            }
        }
        if (counting == string.length()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean secondCheck(String string) {
        char[] chars = string.toCharArray();
        int counting = 0;
        List<Character> list = new LinkedList<>();
        for (int i = chars.length - 1; i >= 0; i--) {
            list.add(chars[i]);
        }
        for (int j = 0; j < chars.length; j++ ) {
            if (chars[j] == list.get(j)) {
                counting += 1;
            }
        }
        if (counting == string.length()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean thirdCheck(String string) {
        char[] chars = string.toCharArray();
        int counting = 0;
        for (int i = 0; i < chars.length - 1; i++) {
            if (chars[i + 1] >= chars[i]) {
                counting += 1;
            }
        }
        if (counting == string.length() - 1) {
            return true;
        } else {
            return false;
        }
    }
}