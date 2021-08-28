import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<String> startingLinks = new ArrayList<>();

        File file = new File("./StartingLinks.txt");
        if(file.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    startingLinks.add(line);
                }
            } catch (IOException ignored) {}
        } else {
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
        }

        Set<String> links = Collections.synchronizedSet(new HashSet<>());
        Set<String> affiliateLinks = Collections.synchronizedSet(new HashSet<>());
        Set<String> blockedDomains = Collections.synchronizedSet(new HashSet<>());
        ArrayList<Thread> threads = new ArrayList<>();

        blockedDomains.add("google");
        blockedDomains.add("facebook");
        blockedDomains.add("twitter");
        blockedDomains.add("youtube");
        blockedDomains.add("twitterstat");
        blockedDomains.add("aboutads");
        blockedDomains.add("medallia");
        blockedDomains.add("networkadvertising");
        blockedDomains.add("support");

        for (String link : startingLinks) threads.add(new Thread(new BasicCrawler(links, affiliateLinks, blockedDomains, link)));
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
