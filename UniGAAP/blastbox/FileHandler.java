package UniGAAP.blastbox;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class FileHandler {
    private File target;
    private BufferedReader buff;
    private String charSet;
    private CompletableFuture<List<String>> readText;
    public FileHandler(String fileName){
        this.target = new File(fileName);
        if(this.target.exists()){
            readFile();
        } else {
            System.err.println("Error: File does not exist!");
            System.exit(0);
        }
    }
    private void readFile(){
        try {
        FileReader reader = new FileReader(this.target);
            this.charSet = reader.getEncoding();
            this.buff = new BufferedReader(reader, Integer.MAX_VALUE);
            this.buff = new BufferedReader(reader, (int)this.target.length());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(0);
        }
        this.readText = CompletableFuture.supplyAsync(() -> {
            List<String> temp = buff.lines().parallel().collect(Collectors.toList());
            return temp;
        });
    }
    public String getCharset(){
        return this.charSet;
    }
    public Long getFileLength(){
        return this.target.length();
    }
    public boolean buffReady() throws IOException{
        return this.buff.ready();
    }
    public void closeBuff() throws IOException{
        this.buff.close();
    }
    public CompletableFuture<List<String>> getFile(){
        return this.readText;
    }
}
