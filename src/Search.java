import java.math.BigInteger;
import java.util.Stack;

/**
 * Created by ryanread
 *
 * Sudoku class (SudBoard) and search algorithm designed for AI II Hw 1.
 *
 * Input: 1d array of integers representing a board.  For example, the puzzle given in the slides
 * is:
 *
 * 7 6 - |- 9 - |5 3 - |
 *- 1 - |8 - - |- - - |
 *- - - |5 6 7 |- - 1 |
 *_____________________
 *
 *3 - 4 |7 - 9 |- 8 - |
 *- 8 - |- 2 - |- 9 - |
 *- 9 - |3 - 8 |4 - 2 |
 *_____________________
 *
 *9 - - |6 3 1 |- - - |
 *- - - |- - 4 |- 1 - |
 *- 7 3 |- 8 - |- 4 6 |
 *_____________________
 *
 * can be represented as:
 *
 *  {7,6,0,0,9,0,5,3,0,0,1,0,8,0,0,0,0,0,0,0,0,5,6,7,0,0,1,3,...0,4,6}j
 */
public class Search {

    /**
     * My attempts at a recursive DFS did not work.  Here I implemented a stack explicitly.
     * The search algorithm will continue from the next square with the lowest value of possible
     * inputs, that is, the most restricted node that is not yet assigned.
     * @param initial starting node (sudoku board), aka our initial puzzle.
     */
    public static void informedExplicitSearch(SudBoard initial){
        Stack<SudBoard> stack = new Stack<>();
        stack.push(initial);
        //BigInteger statesExplored = BigInteger.ONE;
        int statesExplored = 1;

        while(!stack.isEmpty()){
            SudBoard current = stack.pop();
            //statesExplored = statesExplored.add(BigInteger.ONE);
            statesExplored++;
            if(current.getAssigned()==81){
                current.printBoard();
                System.out.println("States explored: " + statesExplored);
                return;
            }
            int[] next_node = current.findMinimimumChoices();
            int x = next_node[1];
            int y = next_node[2];
            String num_to_char = String.valueOf(current.getVal(x,y));

            for(int i = 0; i<num_to_char.length();i++){
                int assign = Character.getNumericValue(num_to_char.charAt(i));
                if(current.legalMove(x,y,assign)){
                    SudBoard temp =  new SudBoard(x,y,current,assign);
                    //if(temp.removeInitials(x,y,assign)) {
                        stack.push(temp);

                    //} //something wonky here
                }
            }
        }
        System.out.println("No solutions found");
    }

    /**
     * An attempt at a recursive DFS search.  Not being used.
     * @param prevBoard
     * @return
     */
    public static boolean informedRecSearch(SudBoard prevBoard){
        if(prevBoard.getAssigned()==81){
            prevBoard.printBoard();
            return true;
        }
        else{
            int[] next = prevBoard.findMinimimumChoices();
            int choices = next[0];
            int x = next[1];
            int y = next[2];
            SudBoard newBoard = new SudBoard(x, y,
                    prevBoard, prevBoard.getAssigned());
            while(newBoard.hasValues(x,y)){
                int nextAssignment = 0;
                //nextAssignment = Character.getNumericValue(newBoard.values[x][y].charAt(0));
                String temp = newBoard.getValAsString(x,y);
                nextAssignment = Character.getNumericValue(temp.charAt(0));
                if(newBoard.legalMove(x,y,nextAssignment)){
                    newBoard.setVal(x,y,nextAssignment);
                    return informedRecSearch(newBoard);
                }

            }

        }
        return false;
    }

    /**
     * Initial search attempted.
     * @param prevBoard
     */
    public static void informedSearch(SudBoard prevBoard){
        if(prevBoard.getAssigned()==81){
            //Todo: print solution
            prevBoard.printBoard();
            return;
            //return true;
        }


        int[] next = prevBoard.findMinimimumChoices();
        int choices = next[0];
        int x = next[1];
        int y = next[2];
        SudBoard newBoard = new SudBoard(x, y,
                prevBoard, prevBoard.getAssigned());

        int nextAssignment = 0;
        //nextAssignment = Character.getNumericValue(newBoard.values[x][y].charAt(0));
        String temp = newBoard.getValAsString(x,y);
        nextAssignment = Character.getNumericValue(temp.charAt(0));

        if( !newBoard.legalMove(x,y,nextAssignment)  ){
            String rep = "";
            rep+=String.valueOf(nextAssignment);
            //newBoard.values[x][y].replace(String.valueOf(nextAssignment), "");
            newBoard.replaceVal(x,y,nextAssignment);
            informedSearch(newBoard);
        }
        else{
            newBoard.setVal(x,y,nextAssignment);// = String.valueOf(nextAssignment);
            newBoard.incrementAssigned();
            informedSearch(newBoard);
        }






        /*else {

            int[] next = prevBoard.findMinimimumChoices();
            int choices = next[0];
            int x = next[1];
            int y = next[2];


       /* int ar[] = prevBoard.findMinimimumChoices();
        //board.removeInitials(8, 3, board.getVal(8,3));
        for (int i = 0; i < ar.length; i++) {
            System.out.println("Value: " + ar[i]);
        }
        prevBoard.printBoard();
        }*/

    } //end search

    public static void main(String[] args){
        /*String x = "123456789";
        String y = "4";
        x=x.replace(y, "");
        System.out.println(x);*/


        /*
        Here are three sample sudoku problems formatted for my program, and ranked from least to most difficult.
         */
        //from assignment
        //int[] inputs =      {7,6,0,0,9,0,5,3,0,0,1,0,8,0,0,0,0,0,0,0,0,5,6,7,0,0,1,3,0,4,7,0,9,0,8,0,0,8,0,0,2,0,0,9,0,0,9,0,3,0,8,4,0,2,9,0,0,6,3,1,0,0,0,0,0,0,0,0,4,0,1,0,0,7,3,0,8,0,0,4,6};
        //another easy sudoku
        //int [] inputs =     {0,0,0,2,6,0,7,0,1,6,8,0,0,7,0,0,9,0,1,9,0,0,0,4,5,0,0,8,2,0,1,0,0,0,4,0,0,0,4,6,0,2,9,0,0,0,5,0,0,0,3,0,2,8,0,0,9,3,0,0,0,7,4,0,4,0,0,5,0,0,3,6,7,0,3,0,1,8,0,0,0};
        //intermediate
        //int [] inputs =     {0,2,0,6,0,8,0,0,0,5,8,0,0,0,9,7,0,0,0,0,0,0,4,0,0,0,0,3,7,0,0,0,0,5,0,0,6,0,0,0,0,0,0,0,4,0,0,8,0,0,0,0,1,3,0,0,0,0,2,0,0,0,0,0,0,9,8,0,0,0,3,6,0,0,0,3,0,6,0,9,0};
        //Very hard
        int[] inputs =      {0,2,0,0,0,0,0,0,0,0,0,0,6,0,0,0,0,3,0,7,4,0,8,0,0,0,0,0,0,0,0,0,3,0,0,2,0,8,0,0,4,0,0,1,0,6,0,0,5,0,0,0,0,0,0,0,0,0,1,0,7,8,0,5,0,0,0,0,9,0,0,0,0,0,0,0,0,0,0,4,0};


        SudBoard board = new SudBoard(inputs);

        board.printBoard();
        System.out.println();


        for(int i = 0; i<5; i++) { //preprocess
            for (int x1 = 0; x1 < 9; x1++) {
                for (int y1 = 0; y1 < 9; y1++) {
                    board.removeInitials(x1, y1, board.getVal(x1, y1));
                }
            }
        }


        board.printBoard();
        int ar[] = board.findMinimimumChoices();
        //board.removeInitials(8, 3, board.getVal(8,3));
        for (int i = 0; i < ar.length; i++) {
            System.out.println("Value: " + ar[i]);
        }

        System.out.println("Assigned value: " + board.getAssigned());

        //informedSearch(board);
        //informedRecSearch(board);

        informedExplicitSearch(board);
    }
}
