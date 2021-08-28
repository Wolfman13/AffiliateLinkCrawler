import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.Set;

public class BasicCrawler implements Runnable {
    private final Set<String> links;
    private final Set<String> affiliateLinks;
    private final String firstLink;
    private final String fileName;

    BasicCrawler(Set<String> links, Set<String> affiliateLinks, String firstLink) {
        fileName = "./AffiliateLinks.txt";
        this.links = links;
        this.affiliateLinks = affiliateLinks;
        this.firstLink = firstLink;
    }

    @Override
    public void run() {
        getPageLinks(this.firstLink);
    }

    private void getPageLinks(String URL) {
        // Check if you have already crawled the URLs
        if (!links.contains(URL)) {
            try {
                links.add(URL);

                // Fetch the HTML code.
                Document document = Jsoup.connect(URL).get();

                document.select("a[href*=google]").remove();
                document.select("a[href*=facebook]").remove();
                document.select("a[href*=twitter]").remove();
                document.select("a[href*=youtube]").remove();
                document.select("a[href*=twitterstat]").remove();
                document.select("a[href*=aboutads]").remove();
                document.select("a[href*=medallia]").remove();
                document.select("a[href*=networkadvertising]").remove();
                document.select("a[href*=support]").remove();

                // Parse the HTML to extract links to other URLs
                Elements linksOnPage = document.select("a[href]");

                // For each extracted URL... go back to Step 4.
                for (Element page : linksOnPage) {
                    if (page.text().toLowerCase(Locale.ROOT).contains("affiliate") && !affiliateLinks.contains(page.attr("abs:href"))) {
                        System.out.println();
                        System.out.println("New affiliate link discovered!");
                        System.out.println("Link was marked because it contained: " + page.text());
                        System.out.println();
                        affiliateLinks.add(page.attr("abs:href"));
                        links.add(page.attr("abs:href"));
                        writeToFile(page.attr("abs:href"));
                    } else {
                        getPageLinks(page.attr("abs:href"));
                    }
                }
            } catch (IOException | IllegalArgumentException ignored) {}
        }
    }

    private void writeToFile(String link) {
        FileWriter writer;
        PrintWriter printer;

        try {
            writer = new FileWriter(fileName, true);
            printer = new PrintWriter(writer);

            printer.println(link);

            printer.close();
            writer.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
