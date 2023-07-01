package JEP;

import jep.Interpreter;
import jep.SharedInterpreter;

public class Experiment {
    public static void main(String args[]){
        Object result;
        Interpreter i = new SharedInterpreter();
        i.eval("x = 1 + 1");
        result = i.getValue("x");
        System.out.println((long)result);
    }
}
