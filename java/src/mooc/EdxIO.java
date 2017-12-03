package mooc;

import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;

/**
 * This is a simple I/O library for JVM-based programming languages, such as Java, Scala and Kotlin,
 * intended to be used in edX-based courses on programming taught by ITMO University.
 *
 * @author Maxim Buzdalov
 */
public class EdxIO implements Closeable {
    /**
     * Creates a default instance of EdxIO
     * which reads input from "input.txt"
     * and writes output to "output.txt".
     *
     * @return the new EdxIO instance.
     */
    public static EdxIO create() {
        return new EdxIO("input.txt", "output.txt");
    }

    /**
     * This is an abstract class for an entry point intended to be used from Scala.
     *
     * It has an abstract method {#receive(EdxIO)} for doing the real work when the EdxIO instance is created,
     * and an implementation of public-static-void-main which creates an instance and delegates to {#receive(EdxIO)}.
     */
    public static abstract class Receiver {
        /**
         * This is an abstract method for doing the real work.
         *
         * @param io an instance of EdxIO for reading and writing.
         */
        protected abstract void receive(EdxIO io);

        /**
         * This is what will become the public-static-void-main
         * when this class is extended by a Scala object containing the solution.
         */
        public final void main(String[] args) {
            try (EdxIO io = EdxIO.create()) {
                receive(io);
            }
        }
    }

    /*-************************* Public high-level API for input *************************-*/

    /**
     * Reads from the input file and returns the next non-whitespace token.
     *
     * This will:
     * <ul>
     * <li>skip all whitespace until the next non-whitespace symbol;</li>
     * <li>consume all non-whitespace symbols until the next whitespace symbol;</li>
     * <li>return the consumed symbols as a {@link String}.</li>
     * </ul>
     *
     * When the operation succeeds, the stream pointer for the input file points to the next
     * symbol after the last symbol of the consumed token.
     *
     * @return the next non-whitespace token.
     */
    public String next() {
        skipWhiteSpace();
        int start = inputPosition;
        if (start >= inputCapacity) {
            return null;
        }
        int finish = start;
        while (finish < inputCapacity && inputBuffer.get(finish) > 32) {
            ++finish;
        }
        char[] chars = new char[finish - start];
        for (int i = 0, iMax = chars.length; i < iMax; ++i) {
            chars[i] = (char) (inputBuffer.get(start + i));
        }
        inputPosition = finish;
        return new String(chars);
    }

    /**
     * Reads from the input file and returns the next non-whitespace character.
     *
     * This will:
     * <ul>
     * <li>skip all whitespace until the next non-whitespace symbol;</li>
     * <li>read the first symbol, removes it from the stream and returns it.</li>
     * </ul>
     *
     * @return the next non-whitespace character.
     * @throws IllegalStateException when there is no character to return.
     */
    public char nextChar() {
        skipWhiteSpace();
        if (inputPosition >= inputCapacity) {
            throw new IllegalStateException("Unexpected end-of-file");
        }
        char rv = (char) currentSymbol();
        ++inputPosition;
        return rv;
    }

    /**
     * Reads from the input file and returns the next int.
     *
     * This will:
     * <ul>
     * <li>skip all whitespace until the next non-whitespace symbol;</li>
     * <li>try consuming as much non-whitespace symbols as needed to read an int;</li>
     * <li>if done successfully, returns an int, otherwise leaves the stream at the position
     *     which stopped correct parsing of an int and throws an exception.</li>
     * </ul>
     *
     * When the operation succeeds, the stream pointer for the input file points to the next
     * symbol after the last symbol of the consumed int value.
     *
     * When the operation fails, the stream pointer for the input file points to the first
     * symbol which made the symbol sequence not interpretable as an int.
     *
     * @return the int value which has just been read from the input file.
     */
    public int nextInt() {
        skipWhiteSpace();
        boolean isNegative = false;
        if (currentSymbol() == '-') {
            isNegative = true;
            ++inputPosition;
        }
        boolean hasDigits = false;
        int value = 0;
        while (true) {
            byte next = currentSymbol();
            if (next >= '0' && next <= '9') {
                hasDigits = true;
                ++inputPosition;
                int add = next - '0';
                if (nextIntImplIsSafe(value, add, isNegative)) {
                    value = value * 10 + add;
                } else {
                    throw new NumberFormatException();
                }
            } else {
                if (hasDigits) {
                    return isNegative ? -value : value;
                } else {
                    throw new NumberFormatException();
                }
            }
        }
    }

    /**
     * Reads from the input file and returns the next long.
     *
     * This will:
     * <ul>
     * <li>skip all whitespace until the next non-whitespace symbol;</li>
     * <li>try consuming as much non-whitespace symbols as needed to read a long;</li>
     * <li>if done successfully, returns a long, otherwise leaves the stream at the position
     *     which stopped correct parsing of an int and throws an exception.</li>
     * </ul>
     *
     * When the operation succeeds, the stream pointer for the input file points to the next
     * symbol after the last symbol of the consumed long value.
     *
     * When the operation fails, the stream pointer for the input file points to the first
     * symbol which made the symbol sequence not interpretable as a long.
     *
     * @return the long value which has just been read from the input file.
     */
    public long nextLong() {
        skipWhiteSpace();
        boolean isNegative = false;
        if (currentSymbol() == '-') {
            isNegative = true;
            ++inputPosition;
        }
        return nextLongImpl(isNegative);
    }

    /**
     * Reads from the input file and returns the next double.
     * Internally, {@link Double.parseDouble(String)} is used.
     * This is slow, as {@link String}s and {@code Matcher}s are involved.
     * To read doubles faster, but in a not so conformant way, use {@link nextDoubleFast()}.
     *
     * This will:
     * <ul>
     * <li>skip all whitespace until the next non-whitespace symbol;</li>
     * <li>consume all non-whitespace symbols until the next whitespace symbol;</li>
     * <li>invoke {@link Double.parseDouble(String)} on the resulting character sequence and return the result.</li>
     * </ul>
     *
     * Regardless of whether double parsing is successful, the stream pointer for the input file points to the first
     * whitespace symbol after the sequence of non-whitespace characters which has just been read.
     *
     * @return the double value which has just been read from the input file.
     */
    public double nextDoublePrecise() {
        doubleBuffer.setLength(0);
        skipWhiteSpace();
        if (inputPosition >= inputCapacity) {
            throw new InputMismatchException("Unexpected end of file");
        }
        byte next;
        while ((next = currentSymbol()) > 32) {
            doubleBuffer.append((char) (next));
            ++inputPosition;
        }
        return Double.parseDouble(doubleBuffer.toString());
    }

    /**
     * Reads from the input file and returns the next double.
     * This is a custom implementation which is not guaranteed to read all doubles {@link nextDoublePrecise()} does,
     * nor guaranteed to return the same values as {@link nextDoublePrecise()} does on the same input.
     * However, it is very fast.
     *
     * This will:
     * <ul>
     * <li>skip all whitespace until the next non-whitespace symbol;</li>
     * <li>try reading a long;</li>
     * <li>if the next symbol is a dot, skip this dot and continue, otherwise return the long value which has just been read;</li>
     * <li>try reading a long, interpret it as a decimal part of the double, add it up with the first long, checking signs.</li>
     * <li>if the next symbol is either 'e' or 'E', skip this symbol and continue, otherwise return the result of the previous step.</li>
     * <li>try reading an int, interpret it as an exponent, apply it and return the value.</li>
     * </ul>
     *
     * The state of the stream after returning from this function depends on the input as if exactly the same sequence as above
     * is executed, see {@link nextLong()}.
     *
     * @return the double value which has just been read from the input file.
     */
    public double nextDoubleFast() {
        skipWhiteSpace();
        boolean isNegative = false;
        if (currentSymbol() == '-') {
            isNegative = true;
            ++inputPosition;
        }
        long first = nextLongImpl(isNegative);
        if (currentSymbol() == '.') {
            int startPosition = ++inputPosition;
            double second = nextLongImpl(false);
            int endPosition = inputPosition;
            for (int i = startPosition; i < endPosition; ++i) {
                second /= 10;
            }
            double rv = isNegative ? first - second : first + second;
            if (currentSymbol() == 'e' || currentSymbol() == 'E') {
                ++inputPosition;
                if (currentSymbol() == '+') {
                    ++inputPosition;
                }
                int exponent = nextInt();
                while (exponent > 0) {
                    --exponent;
                    rv *= 10;
                }
                while (exponent < 0) {
                    ++exponent;
                    rv /= 10;
                }
            }
            return rv;
        } else {
            return first;
        }
    }

    /*-************************* Public low-level API for input *************************-*/

    /**
     * Returns the symbol which is pointed by the stream pointer for the input file.
     * If the file has been entirely read, returns the value 0xFF, which is the same as -1.
     *
     * @return the current symbol, or -1 of EOF is reached.
     */
    public byte currentSymbol() {
        return inputPosition < inputCapacity ? inputBuffer.get(inputPosition) : (byte)-1;
    }

    /**
     * Skips the specified number of symbols from the input.
     * @param nSymbols the number of symbols to skip.
     */
    public void skipSymbols(int nSymbols) {
        if (nSymbols < 0) {
            throw new IllegalArgumentException("Negative number of symbols to skip");
        }
        int diff = inputCapacity - inputPosition;
        if (diff > nSymbols) {
            inputCapacity += nSymbols;
        } else {
            inputCapacity = inputPosition;
        }
    }

    /**
     * Skips all whitespace symbols in the input file until the first non-whitespace symbol is found,
     * such that the subsequent call to {#currentSymbol()} returns this symbol.
     */
    public void skipWhiteSpace() {
        while (inputPosition < inputCapacity && inputBuffer.get(inputPosition) <= 32) {
            ++inputPosition;
        }
    }

    /**
     * Skips all whitespace symbols in the input file until the first non-whitespace symbol is found,
     * such that the subsequent call to {#currentSymbol()} returns this symbol.
     */
    public void skipNonWhiteSpace() {
        while (inputPosition < inputCapacity && inputBuffer.get(inputPosition) > 32) {
            ++inputPosition;
        }
    }

    /*-************************* Public high-level API for output *************************-*/

    /**
     * Prints a newline sequence.
     *
     * @return this instance of {@code EdxIO} to be used with chaining calls.
     */
    public EdxIO println() {
        write(lineSeparatorChars, 0, lineSeparatorChars.length);
        return this;
    }

    /**
     * Prints a character to the output file.
     * A '\n' char is converted to the native newline byte sequence.
     *
     * @param ch the character to be printed.
     * @return this instance of {@code EdxIO} to be used with chaining calls.
     */
    public EdxIO print(char ch) {
        if (ch == '\n') {
            println();
        } else {
            write((byte) (ch));
        }
        return this;
    }

    /**
     * Prints a character to the output file, and then puts a newline.
     *
     * @param ch the character to be printed.
     * @return this instance of {@code EdxIO} to be used with chaining calls.
     */
    public EdxIO println(char ch) {
        write((byte) (ch));
        return println();
    }

    /**
     * Prints a String to the output file.
     * Every '\n' char is converted to the native newline byte sequence.
     *
     * @param s the string to be printed.
     * @return this instance of {@code EdxIO} to be used with chaining calls.
     */
    public EdxIO print(CharSequence s) {
        for (int i = 0, iMax = s.length(); i < iMax; ++i) {
            print(s.charAt(i));
        }
        return this;
    }

    /**
     * Prints a String to the output file, and then puts a newline.
     * Every '\n' char is converted to the native newline byte sequence.
     *
     * @param s the string to be printed.
     * @return this instance of {@code EdxIO} to be used with chaining calls.
     */
    public EdxIO println(CharSequence s) {
        for (int i = 0, iMax = s.length(); i < iMax; ++i) {
            print(s.charAt(i));
        }
        return println();
    }

    /**
     * Prints an int to the output file.
     *
     * @param value the int to be printed.
     * @return this instance of {@code EdxIO} to be used with chaining calls.
     */
    public EdxIO print(int value) {
        if (value == Integer.MIN_VALUE) {
            return print("-2147483648");
        } else {
            if (value < 0) {
                print('-');
                value = -value;
            }
            int pos = intToBuffer(value, numberBuffer.length);
            write(numberBuffer, pos, numberBuffer.length - pos);
            return this;
        }
    }

    /**
     * Prints an int to the output file, and then puts a newline.
     *
     * @param value the int to be printed.
     * @return this instance of {@code EdxIO} to be used with chaining calls.
     */
    public EdxIO println(int value) {
        return print(value).println();
    }

    /**
     * Prints a long to the output file.
     *
     * @param value the long to be printed.
     * @return this instance of {@code EdxIO} to be used with chaining calls.
     */
    public EdxIO print(long value) {
        if (value == Long.MIN_VALUE) {
            return print("-9223372036854775808");
        } else {
            if (value < 0) {
                print('-');
                value = -value;
            }
            // This is adapted from String.valueOf(long)
            int pos = numberBuffer.length;
            while (value > Integer.MAX_VALUE) {
                long q = value / 100;
                int r = (int) (value - ((q << 6) + (q << 5) + (q << 2)));
                value = q;
                numberBuffer[--pos] = digitOnes[r];
                numberBuffer[--pos] = digitTens[r];
            }
            pos = intToBuffer((int) value, pos);
            write(numberBuffer, pos, numberBuffer.length - pos);
            return this;
        }
    }

    /**
     * Prints a long to the output file, and then puts a newline.
     *
     * @param value the long to be printed.
     * @return this instance of {@code EdxIO} to be used with chaining calls.
     */
    public EdxIO println(long value) {
        return print(value).println();
    }

    /**
     * Prints a double to the output file.
     *
     * @param value the double to be printed.
     * @return this instance of {@code EdxIO} to be used with chaining calls.
     */
    public EdxIO print(double value) {
        doubleBuffer.setLength(0);
        return print(doubleBuffer.append(value));
    }

    /**
     * Prints a double to the output file, and then puts a newline.
     *
     * @param value the double to be printed.
     * @return this instance of {@code EdxIO} to be used with chaining calls.
     */
    public EdxIO println(double value) {
        return print(value).println();
    }

    /*-************************* Fields and initialization *************************-*/

    private FileInputStream inputStream;
    private FileChannel inputChannel;
    private MappedByteBuffer inputBuffer;
    private int inputPosition;
    private int inputCapacity;

    private byte[] outputBuffer;
    private int outputSize;
    private FileOutputStream outputStream;

    private final byte[] numberBuffer = new byte[32];
    private final byte[] lineSeparatorChars = System.lineSeparator().getBytes();
    private final StringBuilder doubleBuffer = new StringBuilder(100);

    private EdxIO(String inputFileName, String outputFileName) {
        try {
            File inputFile = new File(inputFileName);
            inputStream = new FileInputStream(inputFile);
            inputChannel = inputStream.getChannel();
            inputBuffer = inputChannel.map(FileChannel.MapMode.READ_ONLY, 0, inputFile.length());
            inputPosition = 0;
            inputCapacity = inputBuffer.capacity();

            outputStream = new FileOutputStream(outputFileName);
            outputBuffer = new byte[8192];
            outputSize = 0;
        } catch (IOException ex) {
            closeImpl();
            throw new UncheckedIOException(ex);
        }
    }

    /*-************************* Implementation of nextInt *************************-*/

    private boolean nextIntImplIsSafe(int value, int add, boolean isNegative) {
        if (value < 214748364) {
            return true;
        }
        if (value > 214748364) {
            return false;
        }
        return isNegative ? add <= 8 : add < 8;
    }

    private long nextLongImpl(boolean isNegative) {
        boolean hasDigits = false;
        long value = 0;
        while (true) {
            byte next = currentSymbol();
            if (next >= '0' && next <= '9') {
                hasDigits = true;
                ++inputPosition;
                int add = next - '0';
                if (nextLongImplIsSafe(value, add, isNegative)) {
                    value = value * 10 + add;
                } else {
                    throw new NumberFormatException();
                }
            } else {
                if (hasDigits) {
                    return isNegative ? -value : value;
                } else {
                    throw new NumberFormatException();
                }
            }
        }
    }

    private boolean nextLongImplIsSafe(long value, int add, boolean isNegative) {
        if (value < 922337203685477580L) {
            return true;
        }
        if (value > 922337203685477580L) {
            return false;
        }
        return isNegative ? add <= 8 : add < 8;
    }

    /*-************************* Implementation of print *************************-*/

    private void write(byte b) {
        outputBuffer[outputSize++] = b;
        if (outputSize == outputBuffer.length) {
            flush();
        }
    }

    private void flush() {
        try {
            outputStream.write(outputBuffer, 0, outputSize);
            outputSize = 0;
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    private void write(byte[] buffer, int offset, int length) {
        if (outputSize + length <= outputBuffer.length) {
            System.arraycopy(buffer, offset, outputBuffer, outputSize, length);
            outputSize += length;
            if (outputSize == outputBuffer.length) {
                flush();
            }
        } else {
            flush();
            try {
                outputStream.write(buffer, offset, length);
            } catch (IOException ex) {
                throw new UncheckedIOException(ex);
            }
        }
    }

    private final static byte[] digitTens = {
        48, 48, 48, 48, 48, 48, 48, 48, 48, 48,
        49, 49, 49, 49, 49, 49, 49, 49, 49, 49,
        50, 50, 50, 50, 50, 50, 50, 50, 50, 50,
        51, 51, 51, 51, 51, 51, 51, 51, 51, 51,
        52, 52, 52, 52, 52, 52, 52, 52, 52, 52,
        53, 53, 53, 53, 53, 53, 53, 53, 53, 53,
        54, 54, 54, 54, 54, 54, 54, 54, 54, 54,
        55, 55, 55, 55, 55, 55, 55, 55, 55, 55,
        56, 56, 56, 56, 56, 56, 56, 56, 56, 56,
        57, 57, 57, 57, 57, 57, 57, 57, 57, 57,
    };

    private final static byte[] digitOnes = {
        48, 49, 50, 51, 52, 53, 54, 55, 56, 57,
        48, 49, 50, 51, 52, 53, 54, 55, 56, 57,
        48, 49, 50, 51, 52, 53, 54, 55, 56, 57,
        48, 49, 50, 51, 52, 53, 54, 55, 56, 57,
        48, 49, 50, 51, 52, 53, 54, 55, 56, 57,
        48, 49, 50, 51, 52, 53, 54, 55, 56, 57,
        48, 49, 50, 51, 52, 53, 54, 55, 56, 57,
        48, 49, 50, 51, 52, 53, 54, 55, 56, 57,
        48, 49, 50, 51, 52, 53, 54, 55, 56, 57,
        48, 49, 50, 51, 52, 53, 54, 55, 56, 57,
    } ;

    private int intToBuffer(int value, int pos) {
        // This is adapted from String.valueOf(int)
        while (value >= 65536) {
            int q = value / 100;
            // really: r = i - (q * 100);
            int r = value - ((q << 6) + (q << 5) + (q << 2));
            value = q;
            numberBuffer[--pos] = digitOnes[r];
            numberBuffer[--pos] = digitTens[r];
        }
        while (true) {
            int q = (value * 52429) >>> (16+3);
            int r = value - ((q << 3) + (q << 1));  // r = i-(q*10) ...
            numberBuffer[--pos] = digitOnes[r];
            value = q;
            if (value == 0) break;
        }
        return pos;
    }

    /*-************************* Closing *************************-*/

    @Override
    public void close() {
        IOException ex = closeImpl();
        if (ex != null) {
            throw new UncheckedIOException(ex);
        }
    }

    private IOException closeImpl() {
        IOException first = null;

        inputBuffer = null;
        if (inputChannel != null) {
            try {
                inputChannel.close();
            } catch (IOException ex) {
                if (first == null) {
                    first = ex;
                }
            }
            inputChannel = null;
        }
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException ex) {
                if (first != null) {
                    first = ex;
                }
            }
        }

        if (outputSize > 0 && outputBuffer != null && outputStream != null) {
            try {
                outputStream.write(outputBuffer, 0, outputSize);
            } catch (IOException ex) {
                if (first != null) {
                    first = ex;
                }
            }
        }

        outputBuffer = null;
        outputSize = 0;
        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException ex) {
                if (first != null) {
                    first = ex;
                }
            }
        }

        return first;
    }
}