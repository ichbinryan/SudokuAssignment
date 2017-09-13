import java.util.Stack;

/**
 * Created by ryanread on 9/12/17.
 */
public class scratch {

    public static void informedExplicitSearch(SudBoard initial){
        Stack<SudBoard> stack = new Stack<>();
        stack.push(initial);

        while(!stack.isEmpty()){
            SudBoard current = stack.pop();
            if(current.getAssigned()==81){
                current.printBoard();
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
                    stack.push(temp);
                }
            }
        }
        System.out.println("No solutions found");
    }

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

    }

    public static void main(String[] args){
        /*String x = "123456789";
        String y = "4";
        x=x.replace(y, "");
        System.out.println(x);*/

        //from assignment
        //int[] inputs =      {7,6,0,0,9,0,5,3,0,0,1,0,8,0,0,0,0,0,0,0,0,5,6,7,0,0,1,3,0,4,7,0,9,0,8,0,0,8,0,0,2,0,0,9,0,0,9,0,3,0,8,4,0,2,9,0,0,6,3,1,0,0,0,0,0,0,0,0,4,0,1,0,0,7,3,0,8,0,0,4,6};
        //another easy sudoku
        //int [] inputs =     {0,0,0,2,6,0,7,0,1,6,8,0,0,7,0,0,9,0,1,9,0,0,0,4,5,0,0,8,2,0,1,0,0,0,4,0,0,0,4,6,0,2,9,0,0,0,5,0,0,0,3,0,2,8,0,0,9,3,0,0,0,7,4,0,4,0,0,5,0,0,3,6,7,0,3,0,1,8,0,0,0};
        //intermediate
        int [] inputs =       {0,2,0,6,0,8,0,0,0,5,8,0,0,0,9,7,0,0,0,0,0,0,4,0,0,0,0,3,7,0,0,0,0,5,0,0,6,0,0,0,0,0,0,0,4,0,0,8,0,0,0,0,1,3,0,0,0,0,2,0,0,0,0,0,0,9,8,0,0,0,3,6,0,0,0,3,0,6,0,9,0};
        //said to be made to be not computable
        //int[] inputs =        {0,2,0,0,0,0,0,0,0,0,0,0,6,0,0,0,0,3,0,7,4,0,8,0,0,0,0,0,0,0,0,0,3,0,0,2,0,8,0,0,4,0,0,1,0,6,0,0,5,0,0,0,0,0,0,0,0,0,1,0,7,8,0,5,0,0,0,0,9,0,0,0,0,0,0,0,0,0,0,4,0};


        SudBoard board = new SudBoard(inputs);

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
