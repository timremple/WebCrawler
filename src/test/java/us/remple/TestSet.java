package us.remple;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class TestSet {

    // test fixtures
    private TestFetchWebPage testFetchWebPage = new TestFetchWebPage();
    private TestResultWriter testResultWriter = new TestResultWriter();

    @Before
    public void setup() {
        ProcessPage.setFetchWebPage(testFetchWebPage);
        DisplayResults.setResultWriter(testResultWriter);
    }

    @Test
    public void testEmptyDomain() {
        String exampleUrl = "http://remple.us";

        Map<String, Document> documents = new HashMap();
        documents.put(exampleUrl, new Document(exampleUrl));

        testFetchWebPage.setupPages(documents);
        testResultWriter.reset();

        Crawler.doCrawl(exampleUrl);

        assertEquals("0  " + exampleUrl +"\n", testResultWriter.retrieve());
    }

    @Test
    public void testSinglePage() {
        Map<String, Document> documents = new HashMap();

        String exampleUrl = "http://remple2.us";
        String exampleUrl2 = exampleUrl + "/example";
        String exampleUrl3 = exampleUrl + "/example2";
        String exampleUrl4 = "http://rempletim.us/example";
        String exampleUrl5 = "http://rempletim.us/example2";
        String image1 = "http://www.somedomain.com/image.jpeg";

        String html = "<html><head></head><body>" +
            // test trimming ref from URL
            "<a href=\"" + exampleUrl2 + "#junk \">junk</a>" +
            // test trimming trailing /
            "<a href=\"" + exampleUrl3 + "/ \">" +
                "<img src=\"" + exampleUrl + "/image.png\" />" +
                "</a>" +
            "<a href=\"" + exampleUrl4 + "\">" +
                "<img src=\"" + image1 + "\" />" +
                "</a>" +
            "<a href=\"" + exampleUrl5 + "\">" +
                // invalid image test
                "<img src=\"bogus\" />" +
                "</a>" +
            // test invalid URL
            "<a href=\"" + exampleUrl + "/unknown\">junk3</a>" +
            // test if link appears twice
            "<a href=\"" + exampleUrl2 + "\">junk</a>" +
            // invalid link test
            "<a href=\"" + "bogus" + "\">junk</a>" +
            "</body></html>";
        Document document = Jsoup.parse(html);

        documents.put(exampleUrl, document);
        documents.put(exampleUrl2, new Document(exampleUrl2));
        documents.put(exampleUrl3, new Document(exampleUrl3));
        documents.put(exampleUrl4, new Document(exampleUrl4));
        documents.put(exampleUrl5, new Document(exampleUrl5));

        testFetchWebPage.setupPages(documents);
        testResultWriter.reset();

        Crawler.doCrawl(exampleUrl);

        assertEquals("0  " + exampleUrl + "\n" +
            "0  : " + exampleUrl4 + "\n" +
            "0  : " + exampleUrl5 + "\n" +
            "0  * " + exampleUrl + "/image.png\n" +
            "0  * " + image1 + "\n" +
            "1  .  " + exampleUrl2 + "\n" +
            "1  .  " + exampleUrl3 + "\n",
            testResultWriter.retrieve());
    }

    @Test
    public void testNestedPages() {
        Map<String, Document> documents = new HashMap();

        String exampleUrl = "http://remple3.us";
        String exampleUrl2 = exampleUrl + "/example";
        String exampleUrl3 = exampleUrl + "/example2";

        String html = "<html><head></head><body>" +
            "<a href=\"" + exampleUrl2 + "#ref2 \">junk</a>" +
            "<a href=\"" + exampleUrl3 + "/ \">junk2</a>" +
            "</body></html>";
        Document document = Jsoup.parse(html);

        documents.put(exampleUrl, document);
        documents.put(exampleUrl2, new Document(exampleUrl2));

        String exampleUrl4 = exampleUrl3 + "/example3";
        String exampleUrl5 = exampleUrl3 + "/example4";

        html = "<html><head></head><body>" +
            "<a href=\"" + exampleUrl4 + "\">junk2</a>" +
            "<a href=\"" + exampleUrl5 + "\">junk2</a>" +
            "</body></html>";
        document = Jsoup.parse(html);

        documents.put(exampleUrl3, document);
        documents.put(exampleUrl4, new Document(exampleUrl4));
        documents.put(exampleUrl5, new Document(exampleUrl5));

        testFetchWebPage.setupPages(documents);
        testResultWriter.reset();

        Crawler.doCrawl(exampleUrl);

        assertEquals("0  " + exampleUrl + "\n" +
            "1  .  " + exampleUrl2 + "\n" +
            "1  .  " + exampleUrl3 + "\n" +
            "2  .  .  " + exampleUrl4 + "\n" +
            "2  .  .  " + exampleUrl5 + "\n",
            testResultWriter.retrieve());
    }
}
