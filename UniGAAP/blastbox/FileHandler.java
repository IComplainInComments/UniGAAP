package UniGAAP.blastbox;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class FileHandler {
    private File target;
    private BufferedReader buff;
    private String charSet;
    private CompletableFuture<List<String>> readText;
    private CompletableFuture<List<String>> convertText;
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
            this.buff = new BufferedReader(reader, (int)this.target.length());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(0);
        }
        this.readText = CompletableFuture.supplyAsync(() -> {
            List<String> temp = buff.lines().parallel().collect(Collectors.toList());
            return temp;
        });
        if(this.charSet == StandardCharsets.UTF_16.name()){
            this.convertText = this.readText.thenApplyAsync(func ->  temp = func.parallelStream().forEach(null));

        }
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
    public boolean fileReady(){
        return this.readText.isDone();
    }
    public void closeBuff() throws IOException{
        this.buff.close();
    }
    public List<String> getRawText() throws InterruptedException, ExecutionException{
        return this.readText.get();
    }
}
