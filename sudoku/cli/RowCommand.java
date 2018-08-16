package sudoku.cli;

import sudoku.puzzle.Grid;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

class RowCommand extends Command
{
    private final static java.util.regex.Pattern syntax
            = Pattern.compile("\\s*row\\s*(\\d*)\\s*([.0-9]{9})\\s*");

    Pattern getSyntax()
    {
        return syntax;
    }
    
    String getHelp ()
    {
        return "row <num> <contents>:\tSet row <num> of the grid (use . for blank cells";
    }

    boolean apply(String cmdline, MatchResult res, Grid g)
    {
        int row = new Integer(res.group(1));
        String rowData = res.group(2);

        if ((row >= 1) && (row <= 9))
        {
            char[] temp = res.group(2).toCharArray();

            for (int col = 0; col < 9; ++col)
            {
                int v;

                switch (temp[col])
                {
                    case '1':
                        v = 1;
                        break;
                    case '2':
                        v = 2;
                        break;
                    case '3':
                        v = 3;
                        break;
                    case '4':
                        v = 4;
                        break;
                    case '5':
                        v = 5;
                        break;
                    case '6':
                        v = 6;
                        break;
                    case '7':
                        v = 7;
                        break;
                    case '8':
                        v = 8;
                        break;
                    case '9':
                        v = 9;
                        break;
                    default:
                        v = 0;
                        break;
                }

                if (v != 0)
                {
                    g.setCell(row - 1, col, v);
                }
            }
        }

        return true;

    }
}