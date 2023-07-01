package blastbox;


import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TextProcess {
    private Path target;
    private String encoding, language;
    private byte[] bArray;
    public TextProcess(String filename, String encoding, String language){
        processFile(filename);
        this.encoding = encoding;
        this.language = language;
    }
    public void processFile(String filename){
        try {
            this.target = Paths.get(filename);
            this.bArray = Files.readAllBytes(this.target);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public int getFileSize() {
        return this.bArray.length;
    }
    public String getEncoding() {
        return encoding;
    }
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }
    public String getLanguage() {
        return language;
    }
    public void setLanguage(String language) {
        this.language = language;
    }
    public ByteBuffer getRawBytes(){
        return ByteBuffer.wrap(bArray);
    }
    public CharBuffer genCharBuffer(){
        return ByteBuffer.wrap(bArray).asCharBuffer();
    }
}