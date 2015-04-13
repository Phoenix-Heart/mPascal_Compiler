package scanner;

import parser.Parser;

/**
 * Created by night on 4/13/2015.
 */
public class run {
    static void main(String[] args) {
        if(args.length==1) {
            String filename = args[0];
            Dispatcher dispatcher = new Dispatcher(filename);
            Parser parser = new Parser(dispatcher);
            parser.startParse();
        }
        else {
            System.out.println("One argument expected. " + args.length + " found.");
        }
    }
}
