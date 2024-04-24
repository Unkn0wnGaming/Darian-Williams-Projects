import java.util.Scanner;
/**
 * User interface for a program that tracks stock ticker names and values by use of a hash table and hash function.
 *
 * @author Darian Williams
 * @version 1 (Project 4)
 * 
 * Est. Hours: 30
 */
public class MenuDriver
{
    /**
     * Display the menu on startup and when ever the 'O' command is used
     */
    private static void displayMenu(){
        System.out.println();
        System.out.println("Command options:");
        System.out.println("I ticker_symbol price (insert/update the price of a stock)");
        System.out.println("D ticker_symbol       (display a value)");
        System.out.println("R ticker_symbol       (remove a stock and its price)");
        System.out.println("V                     (display all stocks)");
        System.out.println("A                     (display the load factor alpha)");
        System.out.println("S                     (display the number of stocks being tracked)");
        System.out.println("O                     (display all options)");
        System.out.println("Q                     (quit)");
    }
    
    /**
     * Remove the command char and space from user input to create a string to work with
     * 
     * @param fullComm - The full command line the user entered, including the command char, the ticker symbol, and following value if applicable
     */
    public static String removeCommandChar(String fullComm){
        //Initialize two char arrays; one the stores the full command line, and one that will only store ticker name (and value)
        char[] fullCommArray = fullComm.toCharArray();
        char[] noComm = new char[fullCommArray.length-2];
        //Insert all chars past index 1, which will remove the command chars
        for(int x = 0, y = 0; x < fullCommArray.length; x++){
            if (x!=0 && x!=1){
                noComm[y] = fullCommArray[x];
                y++;
            }
        }
        //Change the char array into a string and return it
        String noCommStr = String.valueOf(noComm);
        return noCommStr;
    }
    
    /**
     * Extract the stock ticker name, and remove the value attached to it
     * 
     * @param tickAndVal - The string that holds both the ticker name and value, in which the value will be removed
     */
    public static String stockName(String tickAndVal){
        //Change the given string into a char array that will be used in future operations
        char[] tickAndValue = tickAndVal.toCharArray();
        //Calculate how many chars the ticker name is made of. Stop when encountering a space or the end of the array.
        int tickLength = 0;
        for(int x=0; tickAndValue[x] != ' ' && x < tickAndValue.length - 1;x++){tickLength++;}
        //Determine how long the new array should be
        int tickChar = 0;
        while(tickAndValue[tickChar] != ' ' && tickChar < tickAndValue.length - 1){
            tickLength++;
            tickChar++;
        }
        char[] tickName = new char[tickChar];
        //Copy the ticker name charas into the new array
        int placeHold = 0;
        for(int y = 0; y < tickChar; y++){
            tickName[placeHold] = tickAndValue[y];
            placeHold++;
        }
        //Change the ticker name char array into a string and return it
        String tickerName = String.valueOf(tickName);
        return tickerName;
    }
    
    /**
     * Extract the stock ticker value, and remove the name attached to it
     * 
     * @param tickAndVal - The string that holds both the ticker name and value, in which the name will be removed
     */
    public static String stockValue(String tickAndVal){
        //Change the given string into a char array that will be used in future operations
        char[] tickAndValue = tickAndVal.toCharArray();
        //Calculate how many chars the ticker value is made of. Stop when encountering the end of the array.
        int tickLength = 0;
        int tickChar = 0;
        while(tickAndValue[tickChar] != ' '){
            tickLength++;
            tickChar++;
        }
        //Determine how long the new array should be
        char[] stockVal = new char[tickAndValue.length-tickLength+1];
        int placeHold = 0;
        //Copy the ticker value charas into the new array
        for(int y = tickLength+1; y < tickAndValue.length-1; y++){
            stockVal[placeHold] = tickAndValue[y];
            placeHold++;
        }
        //Change the ticker value char array into a string and return it
        String stockValue = String.valueOf(stockVal);
        return stockValue;
    }
    
    /**
     * Replace the space that seperates the ticker name and value with a '+' so that the string can be handled in future operations
     * 
     * @param tickAndVal - The ticker name and value pair
     * @return String - A string version of the modified ticker name and value pair
     */
    public static String pairTickAndVal(String tickAndVal){
        char[] tickAndValArr = tickAndVal.toCharArray();
        char[] tickPair = new char[tickAndValArr.length];
        //Duplicate the original name/value pair
        for(int x = 0; x != tickAndValArr.length; x++){
            tickPair[x] = tickAndValArr[x];
        }
        //Replace the space with a '+'
        for(int y = 0; y != tickPair.length; y++){
            if(tickPair[y] == ' '){
                tickPair[y] = '+';
            }
        }
        return String.valueOf(tickPair); //Return the modified string
    }
    
    /**
     * Main method that serves as the menu driver for the stock price/hash table project.
     * 
     * @param String[] args - requried for every main method
     */
    public static void main(String[] args){
        Scanner scan = new Scanner(System.in);
        boolean flag = true;
        HashTable<String> stocks = new HashTable<String>();
        //Display welcome and menu options
        System.out.println("Welcome to Stock Price Tracker!");
        displayMenu();
        while(flag){ //Contine to run until the 'Q' option is used
            System.out.println("Please enter a command (or 'O' for all options)");
            String command = scan.nextLine();
            
            switch(command.charAt(0)){
                case 'I': //Insert the given ticker name and value. If the ticker is already present, update its value
                    String insertThis = removeCommandChar(command);
                    String insertName = stockName(insertThis);
                    System.out.println("Inserting " + insertName + "...");
                    //Insert the requested string AND the decimal value attached to it
                    //For duplicate keys, just update the value
                    insertName = pairTickAndVal(insertThis);
                    stocks.insert(insertName);
                    break;
                case 'D': //Display the requested ticker. If it cannot be found, alert the user
                    String displayThis = removeCommandChar(command);
                    System.out.println("Displaying the price associated with " + displayThis + "...");
                    //Either display the request string, or return that it can't be found
                    //String displayName = pairTickAndVal(displayThis);
                    if(stocks.displayTickVal(displayThis) == false){
                        System.out.println("Unable to find " + displayThis);
                    }
                    break;
                case 'R': //Remove the requested ticker. If it cannot be found, alert the user
                    String delThis = removeCommandChar(command);
                    System.out.println("Removing the price associated with " + delThis + "...");
                    //Either remove the request string, or return that it can't be found
                    if(stocks.remove(delThis) == false){
                        System.out.println("Failed to remove " + delThis);
                    }
                    break;
                case 'V': //Display all elements in the hash table
                    //Make a function/class to display everything
                    System.out.println(stocks.showAll());
                    break;
                case 'A': //Display the load factor alpha
                    //Load factor = # of elements present / array maximum
                    System.out.println("The current load factor alpha is " + stocks.loadFactor());
                    break;
                case 'S': //Display the number of elements in the hash table
                    System.out.println("Currently tracking " + stocks.elesPres() + " stock(s)");
                    break;
                case 'O': //Display the options again
                    displayMenu();
                    break;
                case 'Q': //Halt the program and send a goodbye message
                    flag = false;
                    System.out.println("Goodbye");
                    break;
            }
        }
    }
}
