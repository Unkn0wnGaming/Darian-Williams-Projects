import java.util.Scanner;
import java.io.*;
/**
 * Solve a maze by using recursion.
 *
 * @author Darian Williams
 * @version 1 (Project 3)
 * 
 * Est. Hours: 38
 */
public class MazeSolver
{
    /**
     * Print method which reads the maze file, and prints the maze without
     * the height or length at the first line.
     *
     * @param filename - Name of the file that features the maze
     * @return String version of the maze
     */
    public static String mazePrinter(String filename){
        StringBuilder strMaze = new StringBuilder();
        //Try to find the file, catch otherwise
        try{
            File maze = new File(filename);
            Scanner mazeScan = new Scanner(maze);
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
     * Handles base case where the maze cannot be solved
     *
     * @param maze - the maze to solve. startRow - row to start at. startCol - column to start at.
     * height - number of rows in the maze. length - number of columns in the maze. trackExits - track any exits the solver finds.
     * @return N/A
     */
    public static void mazeSolverBase(char[][] maze, int startRow, int startCol, int height, int length, char[][] trackExits){
        int trueRow = startRow - 1;
        int trueCol = startCol - 1;
        System.out.println("Starting from " + startRow + ", " + startCol);
        if(maze[trueRow][trueCol] == '#'){
            System.out.println("Unable to start at invalid location " + startRow + ", " + startCol + "!");
        }else{
            mazeSolverRec(maze, trueRow, trueCol, height, length, trackExits);
            printExits(trackExits,height,length); //Print exits after solving is complete
        }
    }
    
    /**
     * Solves the maze by using recurssion.
     *
     * @param maze - the maze to solve. currRow - row curr is pointed at. currCol - column is pointed at.
     * height - number of rows in the maze. length - number of columns in the maze. trackExits - track any exits the solver finds.
     * @return N/A
     */
    public static void mazeSolverRec(char[][] maze, int currRow, int currCol, int height, int length, char[][] trackExits){
        if(maze[currRow][currCol] == ' '){
            maze[currRow][currCol] = '*'; //Place "breadcrumb"
            //Move up
            if(currRow != 0){
                mazeSolverRec(maze, currRow - 1, currCol, height, length, trackExits);
            }else{
                trackExits[currRow][currCol] = '!';
            }
            //Move down
            if(currRow < height - 1){
                mazeSolverRec(maze, currRow + 1, currCol, height, length, trackExits);
            }else{
                trackExits[currRow][currCol] = '!';
            }
            //Move left
            if(currCol != 0){
                mazeSolverRec(maze, currRow, currCol -1, height, length, trackExits);
            }else{
                trackExits[currRow][currCol] = '!';
            }
            //Move right
            if(currCol < length - 1){
                mazeSolverRec(maze, currRow, currCol + 1, height, length, trackExits);
            }else{
                trackExits[currRow][currCol] = '!';
            }
        }
    }
    
    /**
     * Display valid exits
     *
     * @param exits - store spaces where exits were. height - number of rows. length - number of columns.
     * @return N/A
     */
    public static void printExits(char[][] exits, int height, int length){
        StringBuilder foundExits = new StringBuilder();
        int numOfExits = 0;
        //Locate which row and column has an '!' char, which is used to mark exits
        for(int i = 0; i < height; i++){
            for(int j = 0; j < length; j++){
                if(exits[i][j] == '!'){
                    int exitRow = i + 1;
                    int exitCol = j + 1;
                    foundExits.append(exitRow + ", " + exitCol).append("\n");
                    numOfExits++;
                }
            }
        }
        if(numOfExits == 0){
            System.out.println("Unsolvable!");
        }
        System.out.println("Found " + numOfExits + " exits at the following positions:");
        System.out.println(foundExits.toString());
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
     * The main method which will recieve which maze to act on, where to start in the maze, and more.
     *
     * @param args - required for the program
     * @return N/A
     */
    public static void main(String[] args)
    {
        String file = ""; 
        Scanner scan = new Scanner(System.in);
        //Get maze file
        System.out.println("Please enter the maze filename: "); 
        file = scan.nextLine();
        System.out.println("You entered " + file);
        //Create the maze in a 2D array
        char[][] maze = mazeMaker(file);
        char[][] trackExits = maze;
        //Get max height and length
        System.out.println("Height: " + maxHeight(file));
        int height = maxHeight(file);
        System.out.println("Length: " + maxLength(file));
        int length = maxLength(file);
        //Get initial row and column
        System.out.println("Please enter the starting row (1.." + height + ")" );
        int startRow = scan.nextInt();
        System.out.println("You entered: " + startRow);
        System.out.println("Please enter the starting column (1.." + length + ")" );
        int startCol = scan.nextInt();
        System.out.println("You entered: " + startCol);
        //Print the maze
        System.out.println(mazePrinter(file));
        //Handle bases case, then perform recursion when necessary
        mazeSolverBase(maze, startRow, startCol, height, length, trackExits);
    }
}
