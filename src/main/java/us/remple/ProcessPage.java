package us.remple;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

public class ProcessPage {

    // Use Sets for page and media links since multiple references do not need to be retained.
    private Set<String> pageLinks;
    private Set<String> pageMedia;

    // shared amongst all instances
    private static FetchWebPageIntf fetchWebPage= new FetchWebPage();

    /**
     * support providing a FetchWebPage for test purposes
     *
     * @param fetchWebPageImplm a FetchWebPageIntf
     */
    static void setFetchWebPage(FetchWebPageIntf fetchWebPageImplm) {
        fetchWebPage = fetchWebPageImplm;
    }

    /**
     * For each invocation of this method, collect set of page links on the page
     *
     * @param pageUrl
     * @return
     */
    boolean processPage(String pageUrl) {
        // Use TreeSet for elements of the set are in "natural" order; i.e., a specific order, and
        // an order independent of order of appearance in the page.
        pageLinks = new TreeSet<String>();
        pageMedia = new TreeSet<String>();

        if (!fetchWebPage.fetchPage(pageUrl)) return false;

        Document htmlDocument = fetchWebPage.getDocument();

        Elements pageLinksOnPage = htmlDocument.select("a[href]");
        for (Element link : pageLinksOnPage) {
            // ensure URL is an absolute path (namely, not relative)
            String absUrl = link.absUrl("href");
            // allow for case URL isn't valid
            if (!absUrl.isEmpty()) pageLinks.add(absUrl);
        }

        // likewise, find all media links
        Elements mediaLinksOnPage = htmlDocument.select("[src]");
        for (Element link : mediaLinksOnPage) {
            String absUrl = link.absUrl("src");
            if (!absUrl.isEmpty()) pageMedia.add(absUrl);
        }

        return true;
    }

    /**
     * @return the Set of page link URLs in the page retrieved by the most recent invocation of processPage(); else empty Set.
     */
    Set<String> getPageLinks() {
        // ensure we return a List in all cases
        if (pageLinks != null) return pageLinks;
        else return Collections.emptySet();
    }

    /**
     * @return the Set of media link URLs in the page retrieved by the most recent invocation of processPage(); else empty Set.
     */
    Set<String> getPageMedia() {
        // ensure we return a List in all cases
        if (pageMedia != null) return pageMedia;
        else return Collections.emptySet();
    }

}
