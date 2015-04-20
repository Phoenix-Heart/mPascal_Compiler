package scanner;

import core.Selector;
import core.Token;
import core.select.alphaSelector;
import core.select.specialSelector;

import java.io.*;

/**
 * Created by Christina Dunning on 2/4/2015.
 */
public class Dispatcher {
    private int numLine;
    private int column;
    private int nextline;
    private int nextcol;
    String lexeme;
    Token token;
    private Selector select;
    private Selector special;
    private Selector alpha;
    private BufferedReader reader;
    private int ch;
    private boolean done;

    public Dispatcher(String filename) {
        done = false;
        ch = 0;
        select = null;
        special = new specialSelector();
        alpha = new alphaSelector();
        numLine = 1;
        column = 1;
        nextline = 1;
        nextcol = 1;
        lexeme = "";
        try {
            reader = new BufferedReader(new FileReader(filename));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public int getLine() {
        return numLine;
    }
    public int getColumn() {return column;}

    // read the next character and track position.
    private int read() {
        try {
            int c =  reader.read();
            if(isEOL(c)) {
                nextcol = 1;
                nextline++;
            }
            else
                nextcol++;
            return c;
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }
    private void updateLexeme(int c) {
        if(isEOL(c)) {
        //    lexeme += '\\';
        //    lexeme += 'n';
        }
        else {
            lexeme += (char)c;
        }
    }
    public Token getToken() {
        return token;
    }

    public Token nextToken() {
        lexeme = "";
        // update current location prior to scanning
        column = nextcol;
        numLine = nextline;
        if(done) {
            return Token.MP_EOF;
        }

        try {
            if(!reader.ready()) {
                done = true;
            }
            // if no input has been read yet, read the first character.
            // otherwise, this should be set to the first unrecognized character from the last scan.
            if(ch==0)
                ch = read();

            // skip whitespace
            while(isWhiteSpace(ch)) {
                ch = read();
            }
            // skip comments
            if(ch=='{') {
                while(ch!='}') {
                    ch = read();
                    if(isEOL(ch)) {
                        ch = read();
                        return Token.MP_RUN_COMMENT;
                    }
                }
                ch = read();
                return nextToken();
            }

            // prepare to scan for a token. Scan first character and choose appropriate selector.
                boolean valid = true;
                if(isAlpha(ch)) {
                    select = new alphaSelector();
                }
                else if (isSpecial(ch)||isDigit(ch)) {
                    select = new specialSelector();
                }
                else {
                    updateLexeme(ch);
                   // lexeme += (char)ch;
                    // need to read a new character, because of forward-checking.
                    ch = read();
                    return Token.MP_ERROR;
                }
                while(valid) {
                    char c = Character.toLowerCase((char)ch);
                    select.read(c);
                    if(select.hasToken()) {
                        token = select.getToken();
                        updateLexeme(ch);
                        //lexeme += (char) ch;
                        ch = read();
                    }
                    else {
                        valid = false;
                    }
                }
                return token;
        } catch (IOException e) {
            System.out.println("File cannot be read");
        }
        return token;
    }
    public String getLexeme() {
       return lexeme;
    }
    public static boolean isEOL(int ch) {
        if(((char)ch=='\n'))
            return true;
        else return false;
    }
    public static boolean isAlphanumeric(int ch) {
        return (isLetter(ch)||isDigit(ch)||isUnderScore(ch));
    }
    public static boolean isAlpha(int ch) {
        return (isLetter(ch)||isUnderScore(ch));
    }
    public static boolean isLetter(int ch) {
        return Character.isLetter(ch);
    }
    public static boolean isDigit(int ch) {
        return Character.isDigit(ch);
    }
    public static boolean isUnderScore(int ch) {
        return ('_'==(char)ch);
    }
    // white space includes spaces, tabs, carriage returns, line feeds, form feeds and unicode separators.
    public static boolean isWhiteSpace(int ch) {
        return Character.isWhitespace(ch);
    }
    public static boolean isSpecial(int ch) {
        char c = (char) ch;
        switch(c) {
            case '+':
            case '-':
            case ';':
            case ':':
            case '=':
            case ',':
            case '.':
            case '/':
            case '<':
            case '>':
            case '(':
            case ')':
            case '*':
            case '\'':
                return true;
            default:
                return false;
        }
    }
}