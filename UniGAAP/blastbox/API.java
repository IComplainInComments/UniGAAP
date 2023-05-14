package blastbox;

import java.util.Arrays;

public class API {
    public static void main(String args[]){
        Text test = new Text("test1", "/Users/edward/UniGAAP/database.sql");
                test.process_file("/Users/edward/UniGAAP/Asample01.txt");

        System.out.println("Row Name: "+test.getRowName());
        System.out.println("Date: "+test.getDate());
        System.out.println("Charset: "+test.getCharset());
        System.out.println("Data Raw: "+Arrays.toString(test.getRawData()));
        //System.out.println("Normalized Data: "+test.getNormalizedData());
    }
}
