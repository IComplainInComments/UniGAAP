package blastbox;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import org.apache.tika.io.TikaInputStream;

public class TextProcess {
    private File theFile;
    private TikaInputStream theText;
    private String charSet;
    private int length;
    public TextProcess(String filename) throws FileNotFoundException{
        this.theFile = new File(filename);
        if(!this.theFile.exists()){
            throw new FileNotFoundException("TextProcess Constructor: File not found!");
        } else {
            this.theText = TikaInputStream.get(this.theFile);
            gatherMetaData();
        }
    }
    private void gatherMetaData(){
    }
}
