package scanner;

import MP_IDENTIFIER.State_ID_Empty;
import regex.Selector;
import regex.Token;
import regex.alphaSelector;
import regex.specialSelector;

import java.io.*;
import java.util.*;

/**
 * Created by Christina on 2/4/2015.
 */
public class Scanner {
    private int numLine;
    private int column;
    String lexeme;
    Token token;
    String line;
    // most basic tokens can be resolved in a hash table instead of using more elaborate states
    private Selector select;
    private Selector special;
    private Selector alpha;
    private BufferedReader reader;
    /*
    Need to implement:
    read and scan file one char at a time
    1. eat whitespace and comments
    2. generate tokens
    3. reset state


     */
    Scanner(String filename) {
        select = null;
        special = new specialSelector();
        alpha = new alphaSelector();
        numLine = 1;
        column = 1;
        try {
            reader = new BufferedReader(new FileReader(filename));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            line = reader.readLine();
        } catch (IOException e) {
            System.out.println("File cannot be read");
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
            if(isEOL(c))
                numLine++;
            else
                column++;
            return c;
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }
    public Token getToken() {
        int ch;
        String lexeme = "";

        try {
            if(!reader.ready()) {
                token = Token.MP_EOF;

            }
            // prepare to scan for a token. Scan first character and choose appropriate selector.
            else {
                ch =  read();
                if(isAlphanumeric(ch)) {
                    select = alpha;
                    select.reset();
                }
                else if (isSpecial(ch)||isDigit(ch)) {
                    select = special;
                    select.reset();
                }
            }
        } catch (IOException e) {
            System.out.println("File cannot be read");
        }

        return token;
    }
    private boolean isEOL(int ch) {
        if(((char)ch=='\n')||((char)ch=='\r'))
            return true;
        else return false;
    }
    private boolean isAlphanumeric(int ch) {
        return (isLetter(ch)||isDigit(ch)||isUnderScore(ch));
    }
    private boolean isLetter(int ch) {
        return Character.isLetter(ch);
    }
    private boolean isDigit(int ch) {
        return Character.isDigit(ch);
    }
    private boolean isUnderScore(int ch) {
        return ('_'==(char)ch);
    }
    // white space includes spaces, tabs, carriage returns, line feeds, form feeds and unicode separators.
    private boolean isWhiteSpace(int ch) {
        return Character.isWhitespace(ch);
    }
    private boolean isSpecial(int ch) {
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
                return true;
            default:
                return false;
        }
    }
}