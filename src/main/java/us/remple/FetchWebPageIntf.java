package us.remple;

import org.jsoup.nodes.Document;

interface FetchWebPageIntf {
    /**
     *
     * @param url
     * @return true if fetch succeeded and url references an HTML page
     */
    boolean fetchPage(String url);

    /**
     * WARNING: this will return a non-null only if fetchPage() returned true, or isn't invoked first.
     * @return the jsoup Document retrieve by the most recent invocation of fetchPage(), or null.
     */
    Document getDocument();
}
