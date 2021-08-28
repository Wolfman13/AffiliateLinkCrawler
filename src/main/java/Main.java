import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<String> startingLinks = new ArrayList<>();

        System.out.println("Enter a link to be crawled or nothing to start the program.");
        System.out.println("If nothing is entered the program will exit.");
        System.out.println("Try to keep your amount of links short, as too many might crash the program.");
        System.out.println();

        while (true) {
            System.out.print("Enter next link: ");
            String link = scanner.nextLine();

            if (link.isEmpty()) {
                break;
            }

            startingLinks.add(link);
        }

        Set<String> links = Collections.synchronizedSet(new HashSet<String>());
        Set<String> affiliateLinks = Collections.synchronizedSet(new HashSet<String>());
        ArrayList<Thread> threads = new ArrayList<>();

        for (String link : startingLinks) threads.add(new Thread(new BasicCrawler(links, affiliateLinks, link)));
        for (Thread thread : threads) thread.start();

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
