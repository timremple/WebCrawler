package us.remple;

public class Crawler {

    private static final int MAX_PAGE_DEPTH = 9;

    // Could have built CrawlChildPages to be a singleton, but this is more straightforward (IMHO.)
    private static CrawlChildPages crawlChildPages = new CrawlChildPages();

    public static void main(String[] args) {
	    if (args.length == 0 || args.length > 1) {
	        System.err.println("usage: crawler <url>");
	        System.exit(1);
        }

	    String url = Utils.normalizeUrl(Utils.removeUrlRef(args[0].trim()));

	    if (!Utils.urlIsValid(url)) {
            System.err.println("crawler: invaid url: " + url);
            System.exit(2);
        }

	    doCrawl(url);

        // explicit indication of success
	    System.exit(0);
    }

    // factor this out so it does not exit
    static void doCrawl(String url) {
        // TODO add max page depth as a optional command line argument
        if (!crawlChildPages.crawlDomain(url, MAX_PAGE_DEPTH)) {
            System.err.println("crawler: cannot access domain: " + url);
            System.exit(3);
        }
    }

}
