package us.remple;

import org.jsoup.nodes.Document;

import java.util.Map;

public class TestFetchWebPage implements FetchWebPageIntf {
    private Map<String, Document> documents;

    void setupPages(Map<String, Document> documents) {
        this.documents = documents;
    }

    private String url;

    public boolean fetchPage(String url) {
        this.url = url;
        return documents.containsKey(url);
    }

    public Document getDocument() {
        return documents.get(url);
    }
}
