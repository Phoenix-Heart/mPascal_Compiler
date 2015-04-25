package core;

/**
 * created by Christina Dunning
 * A complete list of all valid token types in microPascal
 */
public enum Token
{
    MP_AND, MP_BEGIN, MP_BOOLEAN, MP_DIV, MP_DO,MP_DOWNTO, MP_ELSE, MP_END, MP_FALSE,MP_FIXED, MP_FLOAT,
    MP_FOR, MP_FUNCTION, MP_IF, MP_INTEGER, MP_MOD, MP_NOT, MP_OR, MP_PROCEDURE,
    MP_PROGRAM, MP_READ, MP_REPEAT, MP_STRING, MP_THEN, MP_TRUE, MP_TO, MP_TYPE, MP_UNTIL, MP_VAR,
    MP_WHILE, MP_WRITE, MP_WRITELN, MP_IDENTIFIER, MP_INTEGER_LIT, MP_FIXED_LIT, MP_FLOAT_LIT, MP_STRING_LIT,
    MP_ASSIGN, MP_COLON, MP_COMMA, MP_EQUAL, MP_FLOAT_DIVIDE, MP_GEQUAL, MP_GTHAN, MP_LEQUAL, MP_LPAREN,
    MP_LTHAN, MP_MINUS, MP_NEQUAL, MP_PERIOD, MP_PLUS, MP_RPAREN, MP_SCOLON, MP_TIMES, MP_EOF, MP_RUN_COMMENT,
    MP_RUN_STRING, MP_ERROR;

    public boolean isLiteral()
    {
        switch(this)
        {
            case MP_INTEGER_LIT:
            case MP_FIXED_LIT:
            case MP_FLOAT_LIT:
            case MP_STRING_LIT:
                return true;
            default:
                return false;
        }
    }
}

