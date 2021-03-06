package us.remple;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

class CrawlChildPages {

    // A fairly conventional limit; e.g., used in XML sitemap world.
    private static final int MAX_PAGES = 50000;

    // If this app was built using a Dependency Injection framework like Spring, these could be injected instead.
    private ProcessPage processPage = new ProcessPage();
    private DisplayResults displayResults = new DisplayResults();

    private Set<String> pagesVisited = new HashSet<String>();

    // Cople of things that get set at level fo domain being crawled; i.e, apply to every page in domain.
    private String domain;
    private int maxDepth;

    boolean crawlDomain(String url, int maxDepth) {
        // Treat domain portion of URL as case insensitive.
        domain = url.toLowerCase();

        this.maxDepth = maxDepth;

        return crawl(url, 0);
    }

    private boolean crawl(String url, int depth) {
        if (pagesVisited.size() >= MAX_PAGES) return true;

        if (!pagesVisited.contains(url)) {
            if (processPage.processPage(url)) {
                pagesVisited.add(url);

                displayResults.displayUrl(url, depth);

                if (depth >= maxDepth) return true;

                // Set of links to pages outside domain.
                Set<String> externalPageLinks =
                        // Use TreeSet to enable iteration of links in a well defined order.
                        new TreeSet<String>();

                Set<String> allPageLinks = processPage.getPageLinks();
                for (String link : allPageLinks) {
                    // Again, treat domain portion of URL as case insensitive.
                    if (!linkIsInDomain(link, domain)) {
                        externalPageLinks.add(link);
                    }
                }

                for (String link : externalPageLinks) {
                    displayResults.displayLink(link, depth);
                }

                for (String media : processPage.getPageMedia()) {
                    displayResults.displayMedia(media, depth);
                }

                for (String link : allPageLinks) {
                    if (linkIsInDomain(link, domain)) {
                        crawl(Utils.normalizeUrl(Utils.removeUrlRef(link)), depth + 1);
                    }
                }

                return true;
            } else return false;
        } else return true;
    }

    private final String WWW = "www.";
    private final String HTTP = "http:";
    private final String HTTPS = "https:";

    private boolean linkIsInDomain(String link, String domain) {
        String lcLink = link.toLowerCase();
        // domain is assumed to be all lower case
        if (domain.contains(WWW) && !lcLink.contains(WWW)) {
            domain = domain.replace(WWW, "");
        }
        else if (!domain.contains(WWW) && lcLink.contains(WWW)) {
            lcLink = lcLink.replace(WWW, "");
        }

        if (domain.contains(HTTP) && lcLink.contains(HTTPS)) {
            domain = domain.replace(HTTP, HTTPS);
        }
        else if (domain.contains(HTTPS) && lcLink.contains(HTTP)) {
            domain = domain.replace(HTTPS, HTTP);
        }

        return lcLink.startsWith(domain);
    }

}
