import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Locale;
import java.util.Set;

public class BasicCrawler implements Runnable {
    private final Set<String> links;
    private final Set<String> affiliateLinks;
    private final Set<String> blockedDomains;
    private final String firstLink;
    private final String fileName;

    BasicCrawler(Set<String> links, Set<String> affiliateLinks, Set<String> blockedDomains, String firstLink) {
        fileName = "./AffiliateLinks.txt";
        this.links = links;
        this.affiliateLinks = affiliateLinks;
        this.firstLink = firstLink;
        this.blockedDomains = blockedDomains;
    }

    @Override
    public void run() {
        getPageLinks(this.firstLink);
    }

    private void getPageLinks(String URL) {
        if (!links.contains(URL)) {
            try {
                if (links.add(URL)) System.out.println("Scanning " + URL + "...");

                Document document = Jsoup.connect(URL).get();

                for (String domain : blockedDomains) document.select("a[href*=" + domain + "]").remove();

                Elements linksOnPage = document.select("a[href]");

                for (Element page : linksOnPage) {
                    if (page.attr("abs:href").toLowerCase(Locale.ROOT).contains("affiliate") && !affiliateLinks.contains(page.attr("abs:href"))) {
                        System.out.println();
                        System.out.println("New affiliate link discovered!");
                        System.out.println();

                        affiliateLinks.add(page.attr("abs:href"));
                        links.add(page.attr("abs:href"));
                        writeToFile(page.attr("abs:href"));

                        URL tempURL = new URL(URL);
                        blockedDomains.add(tempURL.getHost());
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
