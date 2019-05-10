package us.remple;

class DisplayResults {

    private static ResultWriterIntf resultWriterIntf = new ResultWriter();

    /**
     * support providing a ResultWriter for test purposes
     * @param resultWriterImplm a ResultWriterIntf
     */
    static void setResultWriter(ResultWriterIntf resultWriterImplm) {
        resultWriterIntf = resultWriterImplm;
    }

    void displayUrl(String url, int depth) {
        displayDepthAndIndent(url, depth);

        resultWriterIntf.writeLine(" " + url);
    }

    void displayLink(String url, int depth) {
        displayDepthAndIndent(url, depth);

        resultWriterIntf.writeLine(" : " + url);
    }

    void displayMedia(String url, int depth) {
        displayDepthAndIndent(url, depth);

        resultWriterIntf.writeLine(" * " + url);
    }

    // Use a single StringBuilder, instead of multiple instances of String.
    private StringBuilder indent = new StringBuilder();

    private void displayDepthAndIndent(String url, int depth) {
        resultWriterIntf.writeString(Integer.toString(depth));

        indent.setLength(0);
        indent.append(" ");
        for (int i = 0; i < depth; i++) indent.append(" . ");
        // Admittedly, given the toString() below, not sure using StringBuilder saves that many String creations.
        resultWriterIntf.writeString(indent.toString());
    }

}
