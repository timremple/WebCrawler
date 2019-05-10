package us.remple;

public class TestResultWriter implements ResultWriterIntf {
    private StringBuilder buffer = new StringBuilder();

    void reset() {
        buffer.setLength(0);
    }

    String retrieve() {
        return buffer.toString();
    }

    public void writeString(String string) {
        buffer.append(string);
    }

    public void writeLine(String string) {
        buffer.append(string);
        buffer.append("\n");
    }
}
