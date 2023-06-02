package blastbox;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class API {
    public static void main(String args[]){
        Text test = new Text("test1", "/Users/edward/UniGAAP/database.sql");
                test.process_file("/Users/edward/UniGAAP/Asample01.txt");

        System.out.println("Row Name: "+test.getRowName());
        System.out.println("Date: "+test.getDate());
        System.out.println("Charset: "+test.getCharset());
        System.out.println("Data Raw: "+Arrays.toString(test.getRawData()));
        try {
            System.out.println("Data Raw as String: "+new String(test.getRawData(),test.getCharset()));
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
