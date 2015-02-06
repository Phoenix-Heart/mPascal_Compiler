package scanner;

import regex.Token;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by night on 2/4/2015.
 */
public class mp {
    public static void main(String[] args) {
        ArrayList<Token> tokens = new ArrayList<Token>();
        Token token = null;
        if(args.length==1) {
            String filename = args[0];
            Scanner s = new Scanner(filename);
            while(token!= Token.MP_EOF) {
                token = s.getToken();
                tokens.add(token);
            }
        }
        else {
            System.out.println("One argument expected. " + args.length + " found.");
        }
    }
}
