package scanner;

import MP_IDENTIFIER.State_ID_Empty;
import regex.Selector;
import regex.Token;

import java.io.*;
import java.util.*;

/**
 * Created by Christina on 2/4/2015.
 */
public class Scanner {
    private int line;
    private int column;
    // most basic tokens can be resolved in a hash table instead of using more elaborate states
    private Queue<Token> tokens;
    private Selector select;
    /*
    Need to implement:
    read and scan file one char at a time
    1. eat whitespace and comments
    2. generate tokens
    3. reset state


     */
    Scanner() {
            line = 1;
            column = 1;
            tokens =  new LinkedList<Token>();
            select = new Selector();
    }
    private void next(char ch) {
        select.read(ch);
    }
    public int getLine() {
        return line;
    }
    public int getColumn() {
        return column;

    }
    public Token getToken() {
        return null;

    }

}