import java.util.NoSuchElementException;
/**
 * Doubly Linked List, modified to not use header nor trailer nodes
 * @author OpenDSA Data Structures and Algorithms Modules Collection, CHAPTER 9 LINEAR STRUCTURES: https://opendsa-server.cs.vt.edu/ODSA/Books/Everything/html/ListDouble.html. Modified by Darian Williams.
 * 
 * @version 1 (Project 2 Ed.)
 * @since 2/9/24
 */

// Linked list implementation
public class BasicDLList<E> implements List<E> {
    private Link<E> head;      // Pointer to list header
    private Link<E> tail;      // Pointer to last element
    private Link<E> curr;      // Access to current element
    int listSize;      // Size of list

    // Constructors
    BasicDLList(int size) {      // Constructor -- Ignore size
        this();
    }

    BasicDLList() {
        clear();
    }

    /**
     * Clears the list by setting each value to null.
     * 
     * @param N/A
     * @return N/A
     */
    public void clear() {
        int i;
        curr = head;
        //Loop down the list until each value is null 
        for(i=0; i<listSize; i++){
            curr = null;
            curr.next();
        }
        listSize = 0; //Set the empty list size to 0
    }

    /**
     * Inserts the string given by the user. Can function for an empty list,
     * curr being at the start, curr being at the end, or curr being in the middle.
     * 
     * @param The string to insert, parsed as a Link element.
     * @return Confirmation that the insertion was successful.
     */
    public boolean insert(E it){
        if(listSize == 0){ 
            //inserting into an empty list
            curr = new Link( it, null, null );
            head = curr;
            tail = curr;
        }   else if(curr == head){
            //inserting at the beginning of the list
            curr = new Link(it, null, head);
            curr.next().setPrev(curr);
            head = curr;
        }   else if(curr == null){
            //inserting at the end of the list
            curr = new Link(it, tail, null);
            curr.prev().setNext(curr);
            tail = curr;
        }
            else{
            //inserting into the middle of the list
            curr = new Link( it, curr.prev(), curr );
            curr.prev().setNext( curr );
            curr.next().setPrev( curr );
            //if-else statment to handle curr being near head or tail
            if(curr.prev() == head || curr.next() == tail){
                if(curr == head){
                    //When near head, move to after it
                    curr = curr.prev();
                    moveToPos(1); 
                }else if(curr == tail){
                    moveToEnd(); //When near tail, move to null
                }
            }else{
                curr = curr.next(); //When not near head nor tail, move normally
            }
        }
        listSize++; //Increase list size to factor in the new element
        return true; //Return if successful
    }

    /**
     * Appends the give string to the end of the list. It can function if the 
     * list is empty or populated. curr should not be moved.
     * 
     * @param The string to append, parsed as a Link element.
     * @return Confirmation that appending was successful.
     */
    public boolean append(E it){
        int currPos = currPos(); //Store the current position of curr
        if(listSize == 0){
            //Appending in an empty list
            curr = new Link( it, null, null );
            head = curr;
            tail = curr;
        }else{
            //Move curr to end and create the new link
            moveToEnd(); 
            curr = new Link( it, tail, null );
            curr.prev().setNext( curr );  
            tail = curr; //Set teail to the new link
            if(currPos != listSize){
                //If curr is not at the end, return curr to where it was
                moveToPos(currPos);
            }
        }
        listSize++; //Increase list size to factor in the new element
        return true; //Return if successful
    }

    /**
     * Removes the current element, and stores its value for the return statement.
     * It function for a list having only 1 element, curr being at the start,
     * curr being at the end, and curr being in the middle of the list.
     * If there is nothing to remove, it will yield a NoSuchElementException.
     * 
     * @param N/A
     * @return The value that was removed
     */
    public E remove () throws NoSuchElementException {
        E it = head.element();  // Remember value
        if(listSize == 1){
            //Removing the only element in a list, creating an empty list
            curr = null;
            tail = curr;
            head = curr;
            moveToPos(0);
        }else if(curr == null){
            //Removing the element at the end of the list, tail must be moved
            it = tail.element();  // Remember value
            curr = tail;
            curr.prev().setNext( null );
            curr = curr.prev();
            tail = curr;
        }else if(curr == head){
            //Removing the element at the start of the list, head must be moved
            it = curr.element();  // Remember value
            curr.next().setPrev( null );
            curr = curr.next();
            head = curr;
        }else{
            //Removing any element in the middle of the list
            curr.prev().setNext( curr.next() );
            curr.next().setPrev( curr.prev() );
            curr = curr.prev();
        }
        listSize--;  // Decrement node count
        return it;  // Return value removed
    }
    
    /**
     * Moves curr to the start.
     * 
     * @param N/A
     * @return N/A
     */
    public void moveToStart() {
        curr = head; // Set curr at list start
    }

    /**
     * Moves curr to the end.
     * 
     * @param N/A
     * @return N/A
     */
    public void moveToEnd() {
        curr = null; // Set curr at list end
    }

    /**
     * Moves curr to the previous element. If curr == head, move curr to before head (pos 0).
     * If curr == null, move curr to tail.
     * 
     * @param N/A
     * @return N/A
     */
    public void prev() {
        if (curr == head) {
            moveToPos(0); // No previous element
        }   else if(curr == null){
            curr = tail; //Move curr out of null and point it to the tail
        }   else{
            curr = curr.prev(); //Move to the previous position normally
        }
    }

    /**
     * Moves curr to the next element. If curr is already null, do nothing. Otherwise,
     * move to the next position.
     * 
     * @param N/A
     * @return Break if curr == null.
     */
    public void next() { 
        if(curr != null){
            curr = curr.next(); //Move to the next position normally
        }else if(curr == null){
            return; //Return since there is no value allowed after null
        }
    }
    
    /**
     * Returns the length of the list.
     * 
     * @param N/A
     * @return Number of elements in the list.
     */
    public int length() { return listSize; } // Return list length

    /**
     * Returns the value of where curr is. It can handle if curr is at the end of the list,
     * in the middle of the list, or whether curr is before or after the single
     * element in a list.
     * 
     * @param N/A
     * @return Position where curr is.
     */
    public int currPos() {
        //Set a temp node and counter to be used
        Link<E> temp = head;
        int i = 0;
        //Automatically return for special cases
        if(curr == null ){
            //If curr is null, that means it's at the end of the list
            i = listSize;
            return i;
        } else if(listSize == 1 && curr != null){
            //If there is only one link present and curr is NOT null, 
            //curr is at the start of the list
            i = 0;
            return i;
        }else if(listSize == 1 && curr == null){
            //If there is only one link present and curr is null, 
            //curr is at the end of the list
            i = 1;
            return i;
        }
        //If none of the special cases are met, perform a loop until temp matches curr
        while(temp.element() != curr.element()){
            temp = temp.next();
            i++;
        }
        return i; //Return the position normally
    }
    
    /**
     * Moves curr to the given position. The given position must be more than zero
     * and less than the size of the list.
     * 
     * @param Postion curr should be moved to.
     * @return true if curr was successfully move, false if pos is not in range 
     * of the list.
     */
    public boolean moveToPos(int pos) {
        //pos cannot be negative, nor larger than the list size
        if ((pos < 0) || (pos > listSize)) {
            return false;
        }
        //If pos = 0, simply move curr to head
        if(pos == 0){
            curr = head;
            return true;
        }
        //Otherwise, move head to curr, then loop moving to the next element until i > pos
        curr = head;
        for(int i=0; i<pos; i++) { curr = curr.next(); }
        return true; //Return true if movment was successful
    }

    /**
     * Check to see if curr is at the end of the list.
     * 
     * @param N/A
     * @return true if curr is at the end, otherwise false.
     */
    public boolean isAtEnd() { return curr == null; }

    /**
     * Gets the value stored in the curr position. If curr == null, a NoSuchElementException
     * will be thrown.
     * 
     * @param N/A
     * @return The element in the curr position.
     */
    public E getValue() throws NoSuchElementException {
        //Check for if curr is null
        if (curr == null) // No current element
        {
            throw new NoSuchElementException("getValue() in DLList has current of " + curr + " and size of "
                                             + listSize + " that is not a a valid element");
        }
        return curr.element(); //Return the element if curr is not null
    }

    /**
     * Determine if the list is empty.
     * 
     * @param N/A
     * @return true if the list is empty, false if the list is populated.
     */
    public boolean isEmpty() {
        return listSize == 0;
    }
    
    /**
     * Display the element curr is pointed at.
     * 
     * @param N/A
     * @return The element curr is pointed at so that it can be printed.
     */
    public String display(){
        //Set string variable to parse elements as strings
        String showThis;
        if(currPos() == 0){
            //If curr is before head, it still shares the same element as head
            showThis = head.element().toString();
        }else if(curr == null){
            //If curr is null, it is after tail yet still shares it element with tail
            showThis = tail.element().toString();
        }else{
            //If curr is not before of head or after tail, print the element under curr
            showThis = curr.element().toString();
        }
        return showThis; //Return the properly parsed element
    }
    
    /**
     * Required toString() method that uses a StringBuilder to display the whole list
     * in proper order. A comma will be placed after each element except the last one.
     * 
     * @param N/A
     * @return A printable version of the list.
     */
    public String toString(){
        //Set a counter variable and a string builer
        int counter = 0;
        StringBuilder send = new StringBuilder();
        int currPos = currPos(); //Store the current pos of curr 
        curr = head; //Move curr to head to being string building process
        while(counter < listSize){       
            send.append(curr.element()); //Append the element curr is at now
            if(counter < listSize-1){
                //When not at the end of the list, add a comma
                send.append(", ");
            }
            //Move curr to the next element, but stop when at tail
            curr = curr.next();
            counter++; //Increase counter
        }
        /**Because the loop was stopped before curr.next() could equal null,
        * we now append the element at tail so that it won't be left behind.
        *The list must have more than one element for this case
        */
        if(curr == tail && listSize > 1){
            send.append(curr.element());
        }
        moveToPos(currPos); //Return curr to it's original position
        System.out.println(); //Create some space for the list
        return send.toString(); //Return the properly parsed list so it can be printed
    }
}