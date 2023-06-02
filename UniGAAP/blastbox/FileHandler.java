package blastbox;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.charset.spi.CharsetProvider;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

/**
 * The FileHandler class is responsible for taking a text file and inputting the
 * data
 * to be used inside this program.
 * 
 * @author IComplainInComments
 * @version 1.0
 */
public class FileHandler implements Runnable {
    private File target;
    private BufferedReader buff;
    private ByteBuffer raw, normalized;
    private String charSet;
    Thread myThread;

    /**
     * Constructor for the class.
     * 
     * Checks for the file's existence.
     * 
     * @param fileName String
     */
    public FileHandler(String fileName) {
        this.target = new File(fileName);
        if (this.target.exists()) {
            readFile();
        } else {
            System.err.println("ERROR: File does not exist, or is not at this location");
            System.exit(0);
        }
    }

    /**
     * readFile() method takes the file location, and opens a file reader.
     * The method also sets the variable charSet to the targeted files encoding
     * and creates a BufferedReader to process the file.
     */
    private void readFile() {
        try {
            FileReader reader = new FileReader(this.target);
            this.charSet = reader.getEncoding();
            this.buff = new BufferedReader(reader, Math.toIntExact(this.target.length()));
            this.raw = ByteBuffer.allocate(Math.toIntExact(this.target.length()));
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            System.exit(0);
        }

    }

    /**
     * run() Method uses CompleteableFutures to process the file.
     * Data is stored in Char/Byte Buffers.
     * 
     * @see java.util.concurrent.CompletableFuture
     * @see java.nio.ByteBuffer
     * @see java.nio.CharBuffer
     */
    @Override
    public void run() {
        Thread read = new Thread(new Runnable() {
            @Override
            public void run(){
                try {
                    buff.read(raw.asCharBuffer());
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                    e.printStackTrace();
                }
            }
        });
        read.start();
        try {
            read.join();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } finally {
            System.out.println("Array: "+Arrays.toString(raw.array()));
            // Check if the file needs normalized.
            if (charSet.compareToIgnoreCase(StandardCharsets.UTF_16.name()) != 0) {
                normalized = ByteBuffer.allocate(Math.toIntExact(this.target.length()));
                // This second future converts the data from its current encoding into UTF-16
                // and is stored in the normalized buffer.
                Charset charset = Charset.forName(StandardCharsets.UTF_16.name());
                CharsetEncoder encoder = charset.newEncoder();
                try {
                    normalized = encoder.encode(raw.asCharBuffer());
                } catch (CharacterCodingException e) {
                    System.err.println(e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Gives Characterset.
     * 
     * @return String
     */
    public String getCharset() {
        return this.charSet;
    }

    /**
     * Gives the file length.
     * 
     * @return long
     * @see java.io.File
     */
    public long getFileLength() {
        return this.target.length();
    }

    /**
     * Checks if the file is now in a buffer.
     * 
     * @return boolean
     */
    public boolean rawTextReady() {
        return this.myThread.isAlive();
    }

    /**
     * Empties the buffer.
     */
    public void clearRawBuffer() {
        this.raw.clear();
    }

    /**
     * Empties the buffer.
     */
    public void clearNormalizedBuffer() {
        this.normalized.clear();
    }

    /**
     * Gives the buffer with the raw text.
     * 
     * @return CharBuffer
     */
    public ByteBuffer getRawText() {
        return this.raw;
    }

    /**
     * Gives the buffer with the converted text.
     * 
     * @return CharBuffer
     */
    public ByteBuffer getNormalizedText() {
        return this.normalized;
    }
}
