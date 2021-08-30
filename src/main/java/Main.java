import java.io.*;
import java.util.*;

enum ConfigState {
    COMMENT,
    EMPTYLINE,
    STARTINGLINKS,
    BLOCKEDDOMAINS,
    DEPTHLEVEL
}

public class Main {
    private static ConfigState currentState;
    private static ArrayList<String> startingLinks;
    private static Set<String> blockedDomains;
    private static int depthLevel;

    public static void main(String[] args) {
        currentState = null;                                                        // The current state of the config.
        startingLinks = new ArrayList<>();                                          // All the links we want to start of with.
        blockedDomains = Collections.synchronizedSet(new HashSet<>());              // A set of all the domains we don't want to crawl.
        depthLevel = -1;                                                            // How deep you want the bot to crawl.

        Set<String> links = Collections.synchronizedSet(new HashSet<>());           // All the links that have been crawled.
        Set<String> affiliateLinks = Collections.synchronizedSet(new HashSet<>());  // All the affiliate links that have been found.
        ArrayList<Thread> threads = new ArrayList<>();                              // A list of all threads.

        File file = new File("./config.txt");
        if(file.exists()) {
            System.out.println("Reading in config.");

            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    configReader(line);
                }
            } catch (IOException ignored) {}
        } else {
            System.out.println("We could not find a config.txt file in the current directory.");
            System.exit(0);
        }

        System.out.println("Current config:");
        System.out.println("Starting links: " + startingLinks);
        System.out.println("Blocked domains: " + blockedDomains);

        if (depthLevel == -1) {
            System.out.println("Depth level: N/A");
        } else {
            System.out.println("Depth level: " + depthLevel);
        }

        System.out.println();

        for (String startingLink : startingLinks) threads.add(new Thread(new BasicCrawler(links, affiliateLinks, blockedDomains, startingLink, depthLevel)));
        for (Thread thread : threads) thread.start();

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void configReader(String line) {
        // Check the current line to make sure what the current state is.
        if (line.startsWith("//")) {
            currentState = ConfigState.COMMENT;
        } else if (line.isEmpty()) {
            currentState = ConfigState.EMPTYLINE;
        } else if (line.equals("# StartingLinks")) {
            currentState = ConfigState.STARTINGLINKS;
        } else if (line.equals("# BlockedDomains")) {
            currentState = ConfigState.BLOCKEDDOMAINS;
        } else if (line.equals("# DepthLevel")) {
            currentState = ConfigState.DEPTHLEVEL;
        }

        // Based on the current state, handle the information.
        if (currentState == ConfigState.STARTINGLINKS && !line.equals("# StartingLinks")) {
            startingLinks.add(line);
        } else if (currentState == ConfigState.BLOCKEDDOMAINS && !line.equals("# BlockedDomains")) {
            blockedDomains.add(line);
        } else if (currentState == ConfigState.DEPTHLEVEL && !line.equals("# DepthLevel")) {
            depthLevel = Integer.parseInt(line);
        }
    }
}
