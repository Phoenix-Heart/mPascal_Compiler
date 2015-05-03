import parser.ParseException;
import parser.Parser;
import dispatch.Dispatcher;

/**
 * Created by Christina on 4/13/2015.
 */
public class run {
    public static void main(String[] args) {
        if(args.length==1) {
            String filename = args[0];
            Dispatcher dispatcher = new Dispatcher(filename);

            Parser parser = new Parser(dispatcher, filename);
            try {
                parser.startParse();
            } catch (ParseException e) {
                parser.saveParse();
                e.printStackTrace();
            }
        }
        else {
            System.out.println("One argument expected. " + args.length + " found.");
        }
    }
}