package us.remple;

public class ResultWriter implements ResultWriterIntf {
    public void writeString(String string) {
        System.out.print(string);
    }

    public void writeLine(String string) {
        System.out.println(string);
    }
}
