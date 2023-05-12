package UniGAAP.blastbox;
import java.io.IOException;

import UniGAAP.database.Database;

public class BlastBox {
    Database db;
    FileHandler text;
    String charSet;
    long fileLength;
    public BlastBox(String database){
        this.db = new Database(database);
    }
    public void process_file(String fileName) throws IOException{
        this.text = new FileHandler(fileName);
        if(text.buffReady()){
            this.charSet = this.text.getCharset();
            this.fileLength = this.text.getFileLength();
        }
    }
}
