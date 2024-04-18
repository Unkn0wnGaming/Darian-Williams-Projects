import java.util.Scanner;
/**
 * Extend your MenuDriver class from Project 1 by adding in code to create a 
 * BasicDLList object and calls its methods. 
 * Additionally, add in an option for "V", that calls the BasicDLList.toString method 
 * to list all of the strings in a comma-seperated list.
 * Required to use the List interface from the textbook and 
 * the Link class for doubly-linked nodes, without a header nor trailer node.
 * 
 * @author Darian Williams
 * @version 2 (Project 2 Ed.)
 * @since 2/9/24
 * Approx. Hours Spent: 25
 */
public class MenuDriver
{
    /**
     * CommandReceived method manages less complex commands, such as curr 
     * movement.
     * 
     * @param String args = the given command, 
     * BasicDLList list = the list to act upon
     * 
     * @return Confirmation that the command has been performed
     */
    public static String CommandReceived(String args, BasicDLList list){
        String command = ""; //set command
        //Switch-case that handles less complex commands
        switch (args){
            case "C": //Display the postion of curr
                int pos = list.currPos();
                command = "Current position number: " + pos;
                break;
            case "D": //Display the element curr is pointed at
                System.out.println("Displaying the string at the current position ...");
                command = "The current value is " + list.display();
                break;
            case "E": //Move curr to the end, this sets curr to null
                command = "Moving the current position to the end ...";
                list.moveToEnd();
                break;
            case "F": //Move to the next element, if curr != null
                command = "Moving the current position forward ...";
                list.next();
                break;
            case "L": //Display the current length of the list
                System.out.println("Displaying the length of the list ... ");
                command = "Length is " + list.length();
                break;
            case "P": //Move to the previous element, if curr != head
                command = "Moving the current position backward ...";
                list.prev();
                break;
            case "R": //Remove the element curr is pointed at
                System.out.println("Removing the string at the current position ... ");
                command = "Removed " + list.remove();
                break;
            case "S": //Move to the start of the list, this sets curr to head
                command = "Moving the current position to the start ...";
                list.moveToStart();
                break;
            default: //Default case for if the given command does not align with the previous cases
                command = args + " is not a valid command";
        }
        return command; //Return the command and the result of the command
    }
 
    /**
     * CommandReceivedI method that inserts text 
     * provided if the "I" option is used
     * 
     * @param The text the user wants to insert
     * @return Confirmation that the text has been inserted
     */
    public static String CommandReceivedI(String I){
        String IX = I; //Set a temp string to act upon
        if (IX.contentEquals("I ")){
            return "I is not a valid command"; //Return if only I was sent
        }
        //Prepare to remove the command chars and extract the given text
        char[] IXArray = IX.toCharArray();
        char[] IXFinal = new char[IXArray.length-2];
        //Copy the chars in the original string, without the command chars
        //The command chars are I at index 0 and the following ' ' at index 2
        for(int x=0,y=0;x<IXArray.length;x++){
            if(x!= 0 && x!=1){
                IXFinal[y]=IXArray[x];
                y++;
            }
        }
        //Return the properly extracted and parsed string
        String ReturnI = String.valueOf(IXFinal);
        return ReturnI;
    }
    
    /**
     * CommandReceivedM method that moves to the given 
     * position if the "M" option is used and the given int
     * is in range of the list size
     * 
     * @param The position of the space to move to
     * @return A confirmation message if movement was successful, or a
     * deny message if the given int was out of bounds
     */
    public static String CommandReceivedM(String M, BasicDLList list){
        String MX = M; //Set a temp string to act upon
        if (MX.contentEquals("M ")){
            return "M is not a valid command"; //Return if only M was sent
        }
        //Prepare to remove the command chars and extract the given int
        char[] MXArray = MX.toCharArray();
        char[] MXFinal = new char[MXArray.length-2];
        //Copy the chars in the original command, without the command chars
        //The command chars are M at index 0 and the following ' ' at index 2
        for(int x=0,y=0;x<MXArray.length;x++){
            if(x!= 0 && x!=1){
                MXFinal[y]=MXArray[x];
                y++;
            }
        }
        //Store the properly extracted and parsed int
        String ReturnM = String.valueOf(MXFinal);
        int moveHere = Integer.parseInt(new String(MXFinal));
        System.out.println("Moving current position to " + moveHere);
        //Use the acquired int to attempt to move to that position
        if(list.moveToPos(moveHere) == true){           
            return "Successfully moved position " + moveHere; //Will succeed if moveHere is in range
        }else{
            return "Failed to move to position " + moveHere; //Will fail if moveHere is out of range
        }
    }
    
    /**
     * CommandReceivedO displays the options again if the "O" option is used
     * 
     * @param N/A
     * @return N/A
     */
    public static void  CommandReceivedO(){
        //Display all options
        System.out.println("Command options:");
        System.out.println("I string_to_insert (insert a string at the current position)");
        System.out.println("A string_to_append (append a string at the end)");
        System.out.println("R (remove the string at the current position)");
        System.out.println("S (move the current position to the start)");
        System.out.println("E (move the current position to the end)");
        System.out.println("P (move the current position backward)");
        System.out.println("F (move the current position forward)");
        System.out.println("L (display the length of the list)");
        System.out.println("C (display the current position number)");
        System.out.println("M [Number] (move the current position to specific position)");
        System.out.println("D (display the string at the current position)");
        System.out.println("V (display all strings)");
        System.out.println("O (display these options)");
        System.out.println("Q (quit)");
        System.out.println();
    }
    
    /**
     * CommandReceivedA method that appends text provided if the 
     * "A" option is used. This means the text will be put at the end of the list.
     * 
     * @param The string that will be appended to the list
     * @return Confirmation of the text being appended
     */
    public static String CommandReceivedA(String A){
        String IA = A; //Set a temp string to act upon
        //If the user does not provide a text, invalidate the command
        if (IA.contentEquals("A ") || IA.contentEquals("A")){
            return "A is not a valid command"; //Return if only A was sent
        }
        //Prepare to remove the command chars and extract the given string
        char[] IAArray = IA.toCharArray();
        char[] IAFinal = new char[IAArray.length-2];
        //Copy the chars in the original command, without the command chars
        //The command chars are A at index 0 and the following ' ' at index 2
        for(int x=0,y=0;x<IAArray.length;x++){
            if(x!= 0 && x!=1){
                IAFinal[y]=IAArray[x];
                y++;
            }
        }
        //Return the properly extracted and parsed string
        String ReturnA = String.valueOf(IAFinal);
        return ReturnA;
    }
    
    /**
     * The main method which recieves one char, 
     * and performs an action based on the char give.
     * Some commands require extra input such as strings or integerss.
     * If a char that corresponds to a command that requires a string or int is used,
     * but no string nor int is given, the command is counted as invalid.
     * The options menu shows all valid commands and parameters if required.
     *
     * @param Required for the program
     * @return N/A
     */
    public static void main(String[] args)
    {
        // Set variables, scanner, and new Doubly Linked List
        String command = "";
        Scanner scan = new Scanner(System.in);
        boolean flag = true;
        BasicDLList<String> list = new BasicDLList<String>();
        
        // Display the menu
        System.out.println("Welcome to the Menu Driver for a bare bones list!");
        System.out.println("Command options:");
        System.out.println("I string_to_insert (insert a string at the current position)");
        System.out.println("A string_to_append (append a string at the end)");
        System.out.println("R (remove the string at the current position)");
        System.out.println("S (move the current position to the start)");
        System.out.println("E (move the current position to the end)");
        System.out.println("P (move the current position backward)");
        System.out.println("F (move the current position forward)");
        System.out.println("L (display the length of the list)");
        System.out.println("C (display the current position number)");
        System.out.println("M [Number] (move the current position to specific position)");
        System.out.println("D (display the string at the current position)");
        System.out.println("V (display all strings)");
        System.out.println("O (display these options)");
        System.out.println("Q (quit)");
        System.out.println();
        // As long as the user doesn't enter "Q", continue operating
        while (flag == true) {
            System.out.print("Please enter a command (or 'O' for all of the options): ");            
            command = scan.nextLine();
            //Perform the given command, more complex commands have their own methods
            if (command.contentEquals("Q") == true){
                //Close the scanner and shutdown the program
                scan.close();
                System.out.println("Good-bye");
                flag = false;
            }   else if (command.contains("I ") == true) {
                //Display the string to be inserted, then insert it
                System.out.println("Inserting '" + CommandReceivedI(command) + "' ...");
                list.insert(CommandReceivedI(command));
            }   else if (command.contains("M ") == true) {
                //Move to the given int, if possible
                System.out.println(CommandReceivedM(command, list));
            }   else if (command.contains("O") == true) {
                //Display command options again
                CommandReceivedO();
            }   else if (command.contains("A ") == true) {
                //Display the string to be appended, then append it to the list
                System.out.println("Appending '" + CommandReceivedA(command) + "' ...");
                list.append(CommandReceivedA(command));
            }   else if(command.contains("V") == true ){
                //Display the whole list, using a toString method.
                System.out.println(list.toString());           
            }   else {
                //Move to switch case to handle simple commands
                System.out.println(CommandReceived(command, list));
            }
        }
    }
}
