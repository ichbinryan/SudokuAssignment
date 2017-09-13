import java.util.HashMap;

/**
 * Created by ryanread on 9/12/17.
 */
public class SudBoard {

    private String possible = "123456789";
    private String[][] values = new String[9][9];
    private int[][] remaining = new int[9][9];
    private int assigned;

    public SudBoard(int[] inputs){
        int x = 0 , y = 0;
        possible = "123456789";
        assigned = 0;

        for(int y1 = 0; y1<9; y1++){
            for(int x1 = 0; x1<9; x1++){
                remaining[x1][y1] = 9;
                values[x1][y1] = possible;
            }
        }

        for(int i = 0; i < 81; i++){
            if(i%9!=9){
                x = i%9;
            }
            else x = 9;
            y = ((int)i/9);
            if(inputs[i] == 0){
                values[x][y] = possible;
            }
            if(inputs[i]!=0){
                values[x][y] = String.valueOf(inputs[i]);
                assigned++;
                //this.removeInitials(x,y,inputs[i]);
            }

        }
    }

    /**
     * copy constructor so we don't delete nodes when we expand into bad frontier nodes
     * @param x
     * @param y
     *
     * @param oldBoard
     */
    public SudBoard(int x, int y, SudBoard oldBoard, int assignment){
        /*SudBoard newBoard = new SudBoard();
        for(int i = 0; i<9; i++){
            for(int j = 0; j<9; j++){

            }
        }*/

        this.values    = new String[9][9];
        for(int i = 0; i<9; i++){
            for (int j = 0; j < 9; j++) {
                this.values[i][j] = oldBoard.values[i][j];
            }
        }
        //this.possible  = oldBoard.possible;
        //this.remaining = oldBoard.remaining;

        this.setVal(x,y,assignment);

        //this.values[x][y] = String.valueOf(assignment);
        this.remaining[x][y] = 1;
        this.assigned = this.checkAssigned();
        /*for(int x1 = 0; x1<9; x1++){
            for(int y1 = 0; y1<9; y1++){
                removeInitials(x1,y1,assignment);
            }
        }*/

    }

    /**
     * Used to preprocess graph to reduce search space.
     * No longer using return value, need to get rid of that;
     * @param x
     * @param y
     * @param option
     */
    public boolean removeInitials(int x, int y, int option){
        if(option>9) return false;

        //boolean flag = false;

        String rep = "";
        rep+=(char) option;
        for(int c = 0; c<9; c++){
            //remove everything in specific row iterating over columns
            if(c!=y) {
                if(values[x][c].contains(String.valueOf(option))) {
                    values[x][c] = values[x][c].replace(String.valueOf(option), "");

                    if(values[x][c].length() == 0){
                        return false;
                    }
                }
            }
        }

        for(int r = 0; r<9; r++){
            if(r!=x) {
                if(values[r][y].contains(String.valueOf(option))) {
                    values[r][y] = values[r][y].replace(String.valueOf(option), "");

                    if(values[r][y].length() == 0){
                        return false;
                    }
                }
            }
        }

        int xoff = (x/3)*3;
        int yoff = (y/3)*3;

        for(int i = 0; i<3; i++){
            for(int j = 0; j<3; j++){
                if(x!=(i+xoff) && y!=(j+yoff)) {
                    values[xoff+i][yoff+j] = values[xoff+i][yoff+j].replace(rep, "");
                }

                if(values[xoff+i][yoff+j].length() == 0){
                    return false;
                }

            }
        }
        assigned = checkAssigned();
        return true;

    }

    public int checkAssigned(){
        int count = 0;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if(values[i][j].length()==1) count++;
            }
        }

        return count;
    }

    public boolean legalMove(int x, int y, int value){
        for(int i = 0; i<9; i++){
            System.out.println("Testing: " + Character.getNumericValue(values[i][y].charAt(0)) + " with length " + values[i][y].length());
            if(Character.getNumericValue(values[i][y].charAt(0)) == value && values[i][y].length()==1){
                return false;
            }
        }

        for(int j = 0; j<9; j++){
            if(Character.getNumericValue( values[x][j].charAt(0)  ) == value && values[x][j].length()==1){
                return false;
            }
        }

        int xoff = (x/3)*3;
        int yoff = (y/3)*3;

        for(int i = 0; i<3; i++){
            for(int j = 0; j<3; j++){
                if(Character.getNumericValue(  values[xoff+i][yoff+j].charAt(0)  ) == value && values[xoff+i][yoff+j].length() == 1){
                    return false;
                }
            }
        }

        return true;
    }

    public int getVal(int x, int y){
        return Integer.parseInt(values[x][y]);
    }

    public String getValAsString(int x, int y){
        return values[x][y];
    }

    public void printBoard() {
        System.out.println("What??? "  + values[8][3]);
        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
                if(values[x][y].length()!=1) System.out.print("- ");
                else System.out.print(values[x][y] + " ");
                if(x%3 == 2)
                    System.out.print("|");
            }
            if(y%3 == 2) {
                System.out.println();
                System.out.println("_____________________");
            }
            System.out.println();
        }
    }

    /**
     * used to find the area with the least remaining values, to aid in a simple informed search.
     * @return
     */

    public int[] findMinimimumChoices(){
        int[] ret = new int[3];
        int x_coord = 0, y_coord = 0, remain;
        remain = Integer.MAX_VALUE;
        for(int x = 0; x<9; x++){
            for(int y = 0; y<9; y++){
                if(values[x][y].length()<remain && values[x][y].length()!=1){
                    x_coord = x;
                    y_coord = y;
                    remain = values[x][y].length();
                }
            }

        }
        ret[0] = remain; //how many values remain
        ret[1] = x_coord; //the x coordinate
        ret[2] = y_coord; //the y coordinate
        System.out.println("Remaining value for squares: " + values[x_coord][y_coord]);
        System.out.println("Assigned thus far: " + assigned);
        return ret;
    }

    //search with solved total in mind
/*
   public boolean searchInformed(int assigned, SudBoard state) {
        if(assigned == 81) return true;
        int[] lowest_possible = this.findMinimimumChoices();
        int x = lowest_possible[1];
        int y = lowest_possible[2];
        int assign = lowest_possible[0]; //next value to assign
        if(assign<=0) return false;
        assign = values[x][y].charAt(0);




        SudBoard newBoard = new SudBoard(x, y, assign, state);

        if(!newBoard.removeInitials(x,y, assign)){
            return false;
        }

        return searchInformed(assigned+1, newBoard);





        //return false;
    }*/

    public int getAssigned(){
        return assigned;
    }

    public void incrementAssigned(){
        assigned++;
    }

    public void replaceVal(int x, int y, int option){
        values[x][y] = values[x][y].replace(String.valueOf(option), "");
    }

    public void setVal(int x, int y, int assignment){
        values[x][y] = String.valueOf(assignment);
    }

    public boolean hasValues(int x, int y){
        if(values[x][y].length()>0) return true;
        else return false;
    }
}
