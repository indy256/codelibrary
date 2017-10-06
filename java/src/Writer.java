import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class Writer {

    private BufferedWriter output;

    public Writer() {
        output = new BufferedWriter(new OutputStreamWriter(System.out));
    }

    public Writer(String s) {
        try {
            output = new BufferedWriter(new FileWriter(s));
        } 
        catch(Exception ex) { 
            ex.printStackTrace(); System.exit(0);
        }
    }

    public void println() {
        try {
            output.append("\n");
        } catch(IOException io) { 
            io.printStackTrace(); System.exit(0);
        }
    }

    public void print(Object o) {
        try {
            output.append(o.toString());
        } catch(IOException io) { 
            io.printStackTrace(); System.exit(0);
        }
    }

    public void println(Object o) {
        try {
            output.append(o.toString()+"\n");
        } catch(IOException io) { 
            io.printStackTrace(); System.exit(0);
        }
    }

    public void printf(String format, Object... args) {
        try {
            output.append(String.format(format, args));
        } catch(IOException io) { 
            io.printStackTrace(); System.exit(0);
        }
    }

    public void printfln(String format, Object... args) {
        try {
            output.append(String.format(format, args)+"\n");
        } catch(IOException io) { 
            io.printStackTrace(); System.exit(0);
        }
    }

    public void flush() {
        try {
            output.flush();
        } catch(IOException io) { 
            io.printStackTrace(); System.exit(0);
        }
    }

    public void close() {
        try {
            output.close();
        } catch(IOException io) { 
            io.printStackTrace(); System.exit(0);
        }
    }
}
