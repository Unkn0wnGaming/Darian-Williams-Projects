import java.util.Scanner;
import java.io.*;
/**
 * Solve a maze by using recursion.
 *
 * @author Darian Williams
 * @version 1 (Project 3)
 * 
 * Est. Hours: 30
 */
public class oldCode
{
    /**
     * Print method which reads the maze file, and prints the maze without
     * the height or length at the first line.
     *
     * @param filename - Name of the file that features the maze
     * @return String version of the maze
     */
    public static String mazePrinter(String filename){
        StringBuilder strMaze = new StringBuilder(); //set string builder
        //Try to find the file, catch otherwise
        try{
            File maze = new File(filename); //File to act on
            Scanner mazeScan = new Scanner(maze); //Scan the file
            //While loop to add each line of the file to the string builder
            //Does not add the first lines that hold height and length
            while(mazeScan.hasNextLine()){
                String mazeLine = mazeScan.nextLine();
                if(mazeLine.contains("#") == true || mazeLine.charAt(0) == ' '){
                    strMaze.append(mazeLine).append("\n");
                }
            }
            mazeScan.close();
        }catch(FileNotFoundException err){
            System.out.println("Maze not found.");
            err.printStackTrace();
        }
        //Return the parsed maze
        return strMaze.toString();
    }
    
    /**
     * Handles base cases where the maze cannot be solved
     *
     * @param maze - 2D char array of the maze. startRow - Row of the given starting position. startCol - Column of the given starting position
     * height - number of row in the maze. length - number of columns in the maze. file - name of the file that hold the maze.
     * posExits - 2D char array of where possible exits on the perimeter are.
     * @return N/A
     */
    public static void mazeSolverBase(char[][] maze, int startRow, int startCol, int height, int length, String file, char[][] posExits){
        int trueStartRow = startRow -1;
        int trueStartCol = startCol -1;
        int perimeter = (height * 2 + length * 2) - 4; //Should (and does) = 16 in maze D
        int trackEdges = 0;
        //Base case 1: The starting position lands on a '#' char
        if(maze[trueStartRow][trueStartCol] == '#'){
            System.out.println("Unable to start at invalid location " + startRow + ", " + startCol + "!");
            return;
        }
        System.out.println("Starting from " + startRow + ", " + startCol + ".");
        //Base case 2: The maze is unsolvable due to there being no exits on the perimeter
        for(int i = 0; i < height; i++){
            for(int j = 0; j < length; j++){
                if(maze[i][j] == '#' && ((i == 0 || j == 0) || (i == height-1 || j == length-1))){
                    trackEdges++;
                }
            }
        }
        //System.out.println(trackEdges);
        if(perimeter == trackEdges){
            System.out.println("Unsolvable");
            return;
        }
        //Start recursive case if neither of the base cases are met
        mazeSolverRec(maze, trueStartRow, trueStartCol,trueStartRow, trueStartCol, height, length, file, 0, posExits);
    }
    
    /**
     * Solves the maze by using recurssion.
     *
     * @param maze - 2D char array of the maze. startRow - Row of the given starting position. startCol - Column of the given starting position
     * currRow - the row curr is at. currCol - the column curr is at. 
     * height - number of row in the maze. length - number of columns in the maze. file - name of the file that hold the maze.
     * iterations - how many times the method has ran, determines if a maze is unsolvable. posExits - 2D char array of where possible exits on the perimeter are.
     * @return N/A
     */
    public static void mazeSolverRec(char[][] maze, int startRow, int startCol, int currRow, int currCol, int height, int length, String file, int iterations, char[][] posExits){
        //boolean full = false;
        int spaces = height * length;
        int iter = iterations;
        for(int i = 0; i < height; i++){
            for(int j = 0; j < length; j++){
                if(maze[i][j] != ' '){
                    spaces--;
                }
            }
        }
        //Send results after the maze is solved or there are no more valid spaces to search
        if(spaces == 0 || iter > height * length){
            System.out.println("Done");
            System.out.println(mazeMakerStr(maze, file));
            System.out.println(verifyExits(maze, posExits, file));
            return;
        }
        
        //Take starting position
        /**if(iter == 0){
            currRow = startRow;
            currCol = startCol;
        }*/
        
        //Handle exits and its open
        if(currRow == 0 || currCol == 0 || currRow == height-1 || currCol == length - 1){
            maze[currRow][currCol] = 'E';
            currRow = findSpaceRow(maze, file);
            currCol = findSpaceCol(maze, file);
        }
        
        //Move up
        if(currRow != 0 && (currCol > 0 && currCol < length -1)){
            if(maze[currRow-1][currCol] == ' '){
                maze[currRow][currCol] = 'U';
                currRow--;
            }
            if(currRow != 0){
                if(maze[currRow][currCol+1] != ' ' && maze[currRow][currCol-1] != ' '){
                    maze[currRow][currCol] = 'X';
                    currRow = findSpaceRow(maze, file);
                    currCol = findSpaceCol(maze, file);
                }
            }
        }
        //Move down
        if(currRow < height - 1 && (currCol > 0 && currCol < length -1)){
            if(maze[currRow+1][currCol] == ' '){
                maze[currRow][currCol] = 'D';
                currRow++;
            }
            if(currRow < height - 1){
                if(maze[currRow+1][currCol] != ' ' && maze[currRow][currCol-1] != ' '){
                    maze[currRow][currCol] = 'X';
                    currRow = findSpaceRow(maze, file);
                    currCol = findSpaceCol(maze, file);
                }
            }
        }
        //Move right
        if(currCol < length - 1 && (currRow > 0 && currRow < height -1)){
            if(maze[currRow][currCol+1] == ' '){
                maze[currRow][currCol] = 'R';
                currCol++;
            }
            if(currCol < length - 1){
                if(maze[currRow][currCol+1] != ' ' && maze[currRow+1][currCol] != ' '){
                    maze[currRow][currCol] = 'X';
                    currRow = findSpaceRow(maze, file);
                    currCol = findSpaceCol(maze, file);
                }
            }
        }
        //Move left
        if(currCol != 0 && (currRow > 0 && currRow < height -1)){
            if(maze[currRow][currCol-1] == ' '){
                maze[currRow][currCol] = 'L';
                currCol--;
            }
            if(currCol != 0){
                if(maze[currRow][currCol-1] != ' ' && maze[currRow-1][currCol] != ' ' && maze[currRow+1][currCol] != ' '){
                    maze[currRow][currCol] = 'X';
                    currRow = findSpaceRow(maze, file);
                    currCol = findSpaceCol(maze, file);
                }
            }
        }
        //Exit at the top side
        
        //Exit at the bottom side
        
        //Exit at the left side
        
        //Exit at the right side
        
        //System.out.println(mazeMakerStr(maze, file));
        iter++;
        mazeSolverRec(maze, startRow, startCol, currRow, currCol, height, length, file, iter, posExits);
    }
    
    /**
     * Find the max height (top to bottom) of the maze
     *
     * @param filename - maze to find the height of
     * @return Height of the maze
     */
    public static Integer maxHeight(String filename){
        int height = 0;
        //Try to get the first int which holds the height
        try{
            File maze = new File(filename);
            Scanner heightScan = new Scanner(maze);
            height = heightScan.nextInt();
            //int length = mazeScan.nextInt();
            //send.append("height: " + height).append("\n");
            //send.append("Length: " + length).append("\n");
            heightScan.close();
        }catch(FileNotFoundException err){
            System.out.println("Maze not found.");
            err.printStackTrace();
        }
        //Return the height to be used in future operations
        return height;
    }
    
    /**
     * Find the max length (left to right) of the maze
     *
     * @param filename - maze to find the length of
     * @return Length of the maze
     */
    public static Integer maxLength(String filename){
        int length = 0;
        //Try to get the second int which holds the length
        try{
            File maze = new File(filename);
            Scanner lenScan = new Scanner(maze);
            length = lenScan.nextInt(); //Skip this int since it's the height
            length = lenScan.nextInt();
            //send.append("height: " + height).append("\n");
            //send.append("Length: " + length).append("\n");
            lenScan.close();
        }catch(FileNotFoundException err){
            System.out.println("Maze not found.");
            err.printStackTrace();
        }
        //Return the length to be used in future operations
        return length;
    }
    
    /**
     * Turns the given maze into a 2D char array for future methods.
     *
     * @param file - the name of the file that holds the maze.
     * @return 2D array thats a replica of the maze.
     */
    public static char[][] mazeMaker(String file){
        int height = maxHeight(file);
        int length = maxLength(file);
        char [][] maze = new char[height][length];
        
        try{
            //Insert characters into the 2D array one at a time
            File mazeTxt = new File(file);
            Scanner mazeScan = new Scanner(mazeTxt);
            mazeScan.nextLine();
            for(int i = 0; mazeScan.hasNextLine() && i < height; i++){
                char[] line = mazeScan.nextLine().toCharArray();
                for(int j = 0; j < length && j < line.length; j++){
                    maze[i][j] = line[j];
                }
            }
        }catch(FileNotFoundException err){
            System.out.println("Maze not found.");
            err.printStackTrace();
        }
        
        return maze;
    }
    
    /**
     * toString method for the maze.
     *
     * @param maze - the maze being solved. file - the name of the file that holds the maze.
     * @return A printable version of the maze.
     */
    public static String mazeMakerStr(char[][] maze, String file){
        StringBuilder strMaze = new StringBuilder();
        int height = maxHeight(file);
        int length = maxLength(file);
        
        //Add each char to the string, the seperate rows with a line break
        for(int i = 0; i < height; i++){
            for(int j = 0; j < length; j++){
                strMaze.append(maze[i][j]);
            }
            strMaze.append("\n");
        }
        
        return strMaze.toString();
    }
    
    /**
     * Store the location of all spaces on the perimeter, no matter if they're accessible or not.
     *
     * @param maze - the maze being solved. file - the name of the file that holds the maze.
     * @return 2D array that holds the location of all spaces on the perimeter.
     */
    public static char[][] possibleExits(char[][] maze, String file){
        //StringBuilder posExits = new StringBuilder();
        int height = maxHeight(file);
        int length = maxLength(file);
        char[][] posExits = new char[height][length];
        int x = 0;
        
        //Mark spaces on the perimeter, since they can be exits
        for(int i = 0; i < height; i++){
            for(int j = 0; j < length; j++){
                if(maze[i][j] == ' ' && ((i == 0 || j == 0) || (i == height-1 || j == length-1))){
                    posExits[i][j] = 'e';
                    
                    //Check all chars within one space of an exit
                    if(i == 0 && (j != 0 && j!= length - 1)){
                        if(maze[i+1][j] != ' ' && maze[i][j+1] != ' ' && maze[i][j-1] != ' '){
                            posExits[i][j] = '!';
                        }
                    }
                    
                    if(j == 0 && (i != 0 && i!= height - 1)){
                        if(maze[i+1][j] != ' ' && maze[i][j+1] != ' ' && maze[i-1][j] != ' '){
                            posExits[i][j] = '!';
                        }
                    }
                    
                    if(i == height - 1 && (j != 0 && j!= length - 1)){
                        if(maze[i-1][j] != ' ' && maze[i][j+1] != ' ' && maze[i][j-1] != ' '){
                            posExits[i][j] = '!';
                        }
                    }
                    
                    if(j == length - 1 && (i != 0 && i!= height - 1)){
                        if(maze[i-1][j] != ' ' && maze[i+1][j] != ' ' && maze[i][j-1] != ' '){
                            posExits[i][j] = '!';
                        }
                    }
                    
                    
                    //Check all chars within 2 spaces of an exit
                    if(length > 14 && height > 12){
                        if(i == 0 && (j > 1 && j < length - 2)){
                            if((maze[i+1][j] != ' ' || maze[i+2][j] != ' ') && (maze[i][j+1] != ' ' || maze[i][j+2] != ' ') && (maze[i][j-1] != ' ' || maze[i][j-2] != ' ')){
                                posExits[i][j] = '!';
                            }
                        }
                    
                        if(j == 0 && (i > 1 && i < height - 2)){
                            if((maze[i+1][j] != ' ' || maze[i+2][j] != ' ') && (maze[i][j+1] != ' ' || maze[i][j+2] != ' ') && (maze[i-1][j] != ' ' || maze[i-2][j] != ' ')){
                                posExits[i][j] = '!';
                            }
                        }
                    
                        if(i == height - 1 && (j > 1 && j < length - 2)){
                            if((maze[i-1][j] != ' ' || maze[i-2][j] != ' ') && (maze[i][j+2] != ' '|| maze[i][j+1] != ' ') && (maze[i][j-2] != ' ' || maze[i][j-1] != ' ')){
                                posExits[i][j] = '!';
                            }
                        }
                    
                        if(j == length - 1 && (i > 1 && i < height - 2)){
                            if((maze[i-2][j] != ' ' || maze[i-1][j] != ' ') && (maze[i+2][j] != ' ' || maze[i+1][j] != ' ') && (maze[i][j-2] != ' ' || maze[i][j-1] != ' ')){
                                posExits[i][j] = '!';
                            }
                        }
                    }
                    
                    //Check all chars within 3 spaces of an exit
                    if(length > 14 && height > 12){
                        if(i == 0 && (j > 2 && j < length - 3)){
                            if((maze[i+1][j] != ' ' || maze[i+2][j] != ' ' || maze[i+3][j] != ' ') && (maze[i][j+1] != ' ' || maze[i][j+2] != ' ' || maze[i][j+3] != ' ') && (maze[i][j-1] != ' ' || maze[i][j-2] != ' ' || maze[i][j-3] != ' ')){
                                posExits[i][j] = '!';
                            }
                        }
                    
                        if(j == 0 && (i > 2 && i < height - 3)){
                            if((maze[i+1][j] != ' ' || maze[i+2][j] != ' ' || maze[i+3][j] != ' ') && (maze[i][j+1] != ' ' || maze[i][j+2] != ' ' || maze[i][j+3] != ' ') && (maze[i-1][j] != ' ' || maze[i-2][j] != ' ' || maze[i-3][j] != ' ')){
                                posExits[i][j] = '!';
                            }
                        }
                    
                        if(i == height - 1 && (j > 2 && j < length - 3)){
                            if((maze[i-1][j] != ' ' || maze[i-2][j] != ' ' || maze[i-3][j] != ' ') && (maze[i][j+2] != ' '|| maze[i][j+1] != ' ' || maze[i][j+3] != ' ') && (maze[i][j-2] != ' ' || maze[i][j-1] != ' ' || maze[i][j-3] != ' ')){
                                posExits[i][j] = '!';
                            }
                        }
                    
                        if(j == length - 1 && (i > 2 && i < height - 3)){
                            if((maze[i-2][j] != ' ' || maze[i-1][j] != ' ' || maze[i-3][j] != ' ') && (maze[i+2][j] != ' ' || maze[i+1][j] != ' ' || maze[i+3][j] != ' ') && (maze[i][j-2] != ' ' || maze[i][j-1] != ' ' || maze[i][j-3] != ' ')){
                                posExits[i][j] = '!';
                            }
                        }
                    }
                    
                    //Check all chars within 4 spaces of an exit
                    if(length > 14 && height > 12){
                        if(i == 0 && (j > 3 && j < length - 4)){
                            if((maze[i+1][j] != ' ' || maze[i+2][j] != ' ' || maze[i+3][j] != ' ' || maze[i+4][j] != ' ') && (maze[i][j+1] != ' ' || maze[i][j+2] != ' ' || maze[i][j+3] != ' ' || maze[i][j+4] != ' ') && (maze[i][j-1] != ' ' || maze[i][j-2] != ' ' || maze[i][j-3] != ' ' || maze[i][j-4] != ' ')){
                                posExits[i][j] = '!';
                            }
                        }
                    
                        if(j == 0 && (i > 3 && i < height - 4)){
                            if((maze[i+1][j] != ' ' || maze[i+2][j] != ' ' || maze[i+3][j] != ' ' || maze[i+4][j] != ' ') && (maze[i][j+1] != ' ' || maze[i][j+2] != ' ' || maze[i][j+3] != ' ' || maze[i][j+4] != ' ') && (maze[i-1][j] != ' ' || maze[i-2][j] != ' ' || maze[i-3][j] != ' ' || maze[i-4][j] != ' ')){
                                posExits[i][j] = '!';
                            }
                        }
                    
                        if(i == height - 1 && (j > 3 && j < length - 4)){
                            if((maze[i-1][j] != ' ' || maze[i-2][j] != ' ' || maze[i-3][j] != ' ' || maze[i-4][j] != ' ') && (maze[i][j+2] != ' '|| maze[i][j+1] != ' ' || maze[i][j+3] != ' ' || maze[i][j+4] != ' ') && (maze[i][j-2] != ' ' || maze[i][j-1] != ' ' || maze[i][j-3] != ' ' || maze[i][j-4] != ' ')){
                                posExits[i][j] = '!';
                            }
                        }
                    
                        if(j == length - 1 && (i > 3 && i < height - 4)){
                            if((maze[i-2][j] != ' ' || maze[i-1][j] != ' ' || maze[i-3][j] != ' ' || maze[i-4][j] != ' ') && (maze[i+2][j] != ' ' || maze[i+1][j] != ' ' || maze[i+3][j] != ' ' || maze[i+4][j] != ' ') && (maze[i][j-2] != ' ' || maze[i][j-1] != ' ' || maze[i][j-3] != ' ' || maze[i][j-4] != ' ')){
                                posExits[i][j] = '!';
                            }
                        }
                    }
                }
            }
        }
        return posExits;
    }
    
    /**
     * Verify if spaces on the perimeter were reachable by the solver. Also handles invalid starting positions
     *
     * @param maze - the maze being solved. posExits - A 2D array that stores the location of spaces on the perimeter.
     * file - the name of the file that holds the maze.
     * @return The number of exits found and their locations.
     */
    public static String verifyExits(char[][] maze, char[][] posExits, String file){
        StringBuilder exits = new StringBuilder();
        int height = maxHeight(file);
        int length = maxLength(file);
        int validExits = 0;
        
        //Check if a space on the perimeter was reachable by the solver
        for(int i = 0; i < height; i++){
            for(int j = 0; j < length; j++){
                if(maze[i][j] != ' ' && posExits[i][j] == 'e'){
                    int exitRow = i + 1;
                    int exitCol = j + 1;
                    exits.append(exitRow + ", " + exitCol);
                    exits.append("\n");
                    validExits++;
                }
            }
        }
        //Handle starting positions that cause the maze to be unsolvable
        if(validExits == 0){return "Unsolvable , no exits found!";}
        
        exits.insert(0, "Found " + validExits + " exits at the following positions:" + "\n");
        
        return exits.toString();
    }
    
    public static int findSpaceRow(char[][] maze, String file){
        int height = maxHeight(file);
        int length = maxLength(file);
        int spaceRow = 0;
        
        for(int i = 0; i < height; i++){
            for(int j = 0; j < length; j++){
                if(maze[i][j] == ' '){
                    spaceRow = i;
                    //posExits.append(exitRow + ", " + exitCol);
                    //posExits.append("\n");
                }
            }
        }
        return spaceRow;
    }
    
    public static int findSpaceCol(char[][] maze, String file){
        int height = maxHeight(file);
        int length = maxLength(file);
        int spaceCol = 0;
        
        for(int i = 0; i < height; i++){
            for(int j = 0; j < length; j++){
                if(maze[i][j] == ' '){
                    spaceCol = j;
                    //posExits.append(exitRow + ", " + exitCol);
                    //posExits.append("\n");
                }
            }
        }
        return spaceCol;
    }
    
    /**
     * The main method which will recieve which maze to act on, where to start in the maze, and more.
     *
     * @param args - required for the program
     * @return N/A
     */
    public static void main(String[] args)
    {
        String file = ""; //set file placeholder
        Scanner scan = new Scanner(System.in); //set scanner
        //Get maze file
        System.out.println("Please enter the maze filename: "); 
        file = "j.txt"; //"j.txt"; scan.nextLine();
        System.out.println("You entered " + file);
        //System.out.println(mazePrinter(file));
        //String mazeString = mazePrinter(file);
        //Create the maze in a 2D array
        char[][] maze = mazeMaker(file);
        //System.out.println(mazeMakerStr(maze, file));
        
        //Get max height and length
        System.out.println("Height: " + maxHeight(file));
        int height = maxHeight(file);
        System.out.println("Length: " + maxLength(file));
        
        int length = maxLength(file);
        //Get initial row and column
        System.out.println("Please enter the starting row (1.." + height + ")" );
        int startRow = 9; //9; scan.nextInt();
        System.out.println("You entered: " + startRow);
        System.out.println("Please enter the starting column (1.." + length + ")" );
        int startCol = 12; //9; scan.nextInt();
        System.out.println("You entered: " + startCol);
        
        //Find possible exits, which would be any spaces chars on the perimeter
        char[][] posExits = possibleExits(maze, file);
        
        //Print the maze
        System.out.println(mazePrinter(file));
        //Handle bases cases, then perform recursion when necessary
        mazeSolverBase(maze, startRow, startCol, height, length, file, posExits);
    }
    /**
     * Keep updating the file/string builder where each space
     * *in a line* gets replace/filled by a "breadcrumb". 
     * The updated file/string builder should be passed 
     * back into the method until no spaces remain.
     *
     * How to explore the maze:
     * Keep count of rows and columns.
     * Height setter which sets max height
     * Length setter which sets the max length
     * (Updated) "String" which holds the max being worked on.
     * First line in .txt has height, then length
     * Calc location of where exits can be.
     * Exits are spaces on the perimeter that can be reached from starting position.
     * If a space char (' ') corresponds to a perimeter location, it's an exit if it
     * can be reached from starting pos.
     * 
     */
}
