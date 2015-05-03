package dispatch;

import core.Token;

import java.util.ArrayList;

/**
 * Created by Christina on 2/4/2015.
 */
public class mp {
    public static void main(String[] args) {
        ArrayList<Token> tokens = new ArrayList<Token>();
        Token token = null;
        if(args.length==1) {
            String filename = args[0];
            Dispatcher s = new Dispatcher(filename);
            while(token!= Token.MP_EOF && token != Token.MP_RUN_COMMENT && token != Token.MP_RUN_STRING && token != Token.MP_ERROR) {
                token = s.nextToken();
                tokens.add(token);
                System.out.println();
                System.out.print("Token " + token.name());
                if(token.name().length() <6) System.out.print("\t");
                if(token.name().length() <10) System.out.print("\t");
                if(token.name().length() <15) System.out.print("\t");
                System.out.print("\tLine "+s.getLine());
                System.out.print("\tColumn "+s.getColumn());
                System.out.print("\tLexeme  "+s.getLexeme());

                if(token==Token.MP_RUN_COMMENT || token==Token.MP_ERROR || token==Token.MP_RUN_STRING) {
                    System.out.println();
                    System.out.print("Error found, exiting dispatch.");
                }
            }
        }
        else {
            System.out.println("One argument expected. " + args.length + " found.");
        }
    }
}
