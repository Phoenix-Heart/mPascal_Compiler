/**
 * Created by Christina on 4/13/2015.
 */
package symbolTable;

public enum Type {
    INTEGER, FLOAT, FIXED, STRING, BOOLEAN;

    public boolean isFloatish()
    {
        switch(this)
        {
            case FLOAT:
            case FIXED:
                return true;
        }
        return false;
    }
}
