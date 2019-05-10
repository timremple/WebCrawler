package us.remple;

import org.apache.commons.validator.routines.UrlValidator;

// A class to contain utility methods (that can be used by Crawler, and also other classes, as needed.)
// (Could have done this as a static class in Crawler instead, but then it could be used only in that class.)

public class Utils {

    private static final Utils instance = new Utils();

    // do this as a singleton; which avoids Crawler having to explicitly instantiate
    private Utils() { }

    public static Utils getInstance(){
        return instance;
    }

    /**
     *
     * @param url
     * @return true if url is "valid" (i.e. obeys the URL syntax "rules")
     */
    public static boolean urlIsValid(String url) {
        // Built using Apache Commons URL Validation.
        // Could also use Java URL and URI classes, but that is MUCH less robust (e.g., accepts http://.com)
        UrlValidator urlValidator = new UrlValidator();
        return urlValidator.isValid(url);
    }

    /**
     *
     * @param url
     * @return the url with the reference portion (#...) removed
     */
    public static String removeUrlRef(String url) {
        return url.replaceFirst("#.*", "");
    }

    /**
     *
     * @param url
     * @return the url with any trailing "/" removed
     */
    public static String normalizeUrl(String url) {
        return url.replaceFirst("/$", "");
    }

}