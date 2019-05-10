package us.remple;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

class FetchWebPage implements FetchWebPageIntf {

    // pretend to be a "standard" browser
    private static final String BROWSER_ID =
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, " +
                    "like Gecko) Chrome/33.0.1750.152 Safari/537.36";

    private static final int TIMEOUT = 10000;

    private Document htmlDocument;      // defaults to null

    public boolean fetchPage(String url) {
        Connection connection = Jsoup.connect(url).userAgent(BROWSER_ID);
        connection.timeout(TIMEOUT);

        try {
            // be explicit about this
            htmlDocument = null;

            htmlDocument =  connection.get();

            // deal with not successfully GETing the page
            if (connection.response().statusCode() != 200) {
                return false;
            }

            // validate that the page is HTML
            if (!connection.response().contentType().contains("text/html")) {
                return false;
            }

            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public Document getDocument() {
        return htmlDocument;
    }

}
