import java.util.LinkedList;
import java.text.DecimalFormat;
//import java.util.Iterator;

/**
 * HashTable class that manages the hash table and hash function used to store stock ticker names and values.
 *
 * @author Darian Williams
 * @version 1 (Project 4)
 */
public class HashTable<E>
{
    private LinkedList<String>[] hashTable;
    private int elements;
    private int maxSize;
    private int rehasher;
    private double alpha;
    /**
     * Initialize the hash table, featuring 3 slots (constructer)
     */
    public HashTable(){
        int startSize = 3;
        maxSize = startSize;
        hashTable = new LinkedList[startSize];
        rehasher = 0;
        int x = 0;
        //Initialize all linked list that will be featured in the array
        while(x != startSize){
            if(hashTable[x] == null){
                hashTable[x] = new LinkedList<String>();
            }
            x++;
        }
    }
    
    /**
     * Display all elements in the hash table
     */
    public void printTable(){
        int x = 0;
        while(x != maxSize){
            System.out.println(hashTable[x]);
            x++;
        }
    }
    
    /**
     * Insert the given ticker name and value into the hash table
     * If there is an attempt to enter a duplicate entry, update the value attached to that pair instead
     * 
     * @param stockName - The name (and value) of the stock to insert
     * @return boolean - Used to notify the user if insertion was successful
     */
    public boolean insert(String stockName){
        String insertThis = getName(stockName); //Get the name from the name/value pair
        if(checkDupe(insertThis)){ //Check for duplicate entries
            String newVal = getVal(stockName);
            update(insertThis, newVal);
        }else{ //If the entry is not a dupe, simply insert it
            hashTable[hashFunction(insertThis)].add(stockName);
            elements++;
            loadFactor();
        }
        if(alpha > .75){ //Rehash if load factor alpha gets to high
            rehash();
        }
        return true;
    }
    
    /**
     * Display the name (and) value of a desired stock ticker
     * 
     * @param pairTickAndVal - The key-value pair of the ticker name and value
     * @return boolean - Used to notify the user if the ticker can be display or is not found
     */
    public boolean displayTickVal(String pairTickAndVal){
        int searchHere = hashFunction(pairTickAndVal); //Find the specific list to search in
        int z = 0;
        boolean find = false;
        String target = "";
        while(z != hashTable[searchHere].size()){ //Search for the name/value pair
            char[] tar = pairTickAndVal.toCharArray();
            String findIt = getName(hashTable[searchHere].get(z));
            char[] findItArr = findIt.toCharArray();
            String key = String.valueOf(findItArr);
            if(hashFunction(key) == hashFunction(pairTickAndVal)){
                target = hashTable[searchHere].get(z);
                find = true;
            }
            z++;
        }
        if(find == false){    
            return false;
        }
        //Find the '+' and print only the chars after it, this is the value of the name/value pair
        char[] tickAndVal = target.toCharArray();
        int findPlus = 0;
        int x = 0;
        while(tickAndVal[x] != '+' && x != tickAndVal.length-1){
            findPlus++;
            x++;
        }
        char[] val = new char[tickAndVal.length-findPlus-1];
        int placeHold = 0;
        for(int y = findPlus+1; y < tickAndVal.length; y++){
            val[placeHold] = tickAndVal[y];
            placeHold++;
        }
        String valStr = String.valueOf(val);
        System.out.println("Found " + valStr);
        return true;
    }
    
    /**
     * Get the name from the name/value pair
     * 
     * @param tickAndVal - The full name/value pair, featuring a '+'
     * @return String - Only the name of the name/value pair
     */
    public static String getName(String tickAndVal){
        char[] tickAndValArr = tickAndVal.toCharArray(); //Turn the given string into a char[]
        int findPlus = 0;
        int x = 0;
        //Locate where the '+' is
        while(tickAndValArr[x] != '+' && x != tickAndValArr.length-1){
            findPlus++;
            x++;
        }
        //Create a new char[] that will only hold the name chars
        char[] name = new char[findPlus];
        int placeHold = 0;
        for(int y = 0; y != findPlus; y++){ //Place only the name chars into the new char[]
            name[placeHold] = tickAndValArr[y];
            placeHold++;
        }
        return String.valueOf(name); //Return only the name
    }
    
    /**
     * Get the value from the name/value pair
     * 
     * @param tickAndVal - The full name/value pair, featuring a '+'
     * @return String - Only the value of the name/value pair
     */
    public static String getVal(String tickAndVal){
        char[] tickAndValArr = tickAndVal.toCharArray(); //Turn the given string into a char[]
        int findPlus = 0;
        int x = 0;
        //Locate where the '+' is
        while(tickAndValArr[x] != '+'){
            findPlus++;
            x++;
        }
        //Create a new char[] that will only hold the value chars
        char[] val = new char[tickAndValArr.length-findPlus-1];
        int placeHold = 0;
        for(int y = findPlus+1; y < tickAndValArr.length; y++){ //Place only the value chars into the new char[]
            val[placeHold] = tickAndValArr[y];
            placeHold++;
        }
        return String.valueOf(val); //Return only the name
    }
    
    /**
     * Delete a ticker name (and value) from the hash table
     * 
     * @param stockName - The name of the stock to remove
     * @return boolean - Used to notify the user if the ticker was deleted or cannot be found
     */
    public boolean remove(String stockName){
        int delHere = hashFunction(stockName); //Find the specific list to search in
        int z = 0;
        boolean find = false;
        while(z < hashTable[delHere].size()){ //Search for the name of the ticker to delete
            //System.out.println("Rehashed: " + rehashed);
            String key = getName(hashTable[delHere].get(z)); //Check each value in the linked list
            //If the value matches the key, remove it
            if(key.equals(stockName)){ //hashFunction(key) == hashFunction(findIt)
                //Doesn't work properly on a rehashed table
                hashTable[delHere].remove(z); 
                elements--;
                System.out.println("Successfully removed " + stockName);
                find = true;
                break;
            }
            z++;
        }
        /**int a = 0; //Back up remove for rehashed tables
        if(find == false){
            while(a < hashTable[delHere+rehasher].size()){ //Search for the name of the ticker to delete
                //System.out.println("Rehashed: " + rehashed);
                String key = getName(hashTable[delHere+rehasher].get(a)); //Check each value in the linked list
                //If the value matches the key, remove it
                if(key.equals(stockName)){ //hashFunction(key) == hashFunction(findIt)
                    //Doesn't work properly on a rehashed table
                    hashTable[delHere+rehasher].remove(a); 
                    elements--;
                    System.out.println("Successfully removed " + stockName);
                    find = true;
                    break;
                }
                a++;
            }
        }*/

        
        /**if(hashTable[delHere] != null){//Single out the index of the proper list
            Iterator<String> targetList = hashTable[delHere].iterator();//Use tterator to look thorugh the list
            while(targetList.hasNext()){
                String key = getName(targetList.next());
                if(key.equals(stockName)){//Remove the element if it is successfully found
                    targetList.remove();
                    elements--;
                    System.out.println("Successfully removed " + stockName);
                    find = true;
                    break;
                }
            }
        }*/
        
        /**
         * Failsafe operation - linear search (brute force)
         * Is there anyway to avoid using this option? Is it okay to use this option?
         */
        int x = 0;
        String key = "";
        //Search for every element in every list
        while(x != maxSize && find != true){
            if(hashTable[x].size() != 0){
                int y = 0;
                while(y != hashTable[x].size()){
                    key = getName(hashTable[x].get(y));
                    if(key.equals(stockName)){
                        hashTable[x].remove(y);
                        //hashTable[delHere].set(z, "tomb");
                        elements--;
                        System.out.println("Successfully removed " + stockName);
                        //System.out.println("List: " + x + ", Index: " + y);
                        find = true;
                        break; 
                    }
                    y++;
                }
            }
            x++;
        }
        return find;
    }
    
    /**
     * Show all entries in the hash table
     * 
     * @return fullTable - A printable string version of all the elements in the table 
     */
    public String showAll(){
        StringBuilder fullTable = new StringBuilder();
        String valsPres = " ";
        if(elements == 1){ //Determine if "pair" or "pairs" should be used
            valsPres = "(1 key-value pair ):";
        }else{
            valsPres = "(" + elements + " key-value pairs)";
        }
        
        fullTable.append("Basic hash table " + valsPres + "\n");
        int x = 0;
        //Search for every element in every list
        while(x != maxSize){
            //fullTable.append("Key: ");
            if(hashTable[x].size() >= 0){
                int y = 0;
                //fullTable.append("Key: ");
                while(y != hashTable[x].size()){
                    fullTable.append("Key: ");
                    fullTable.append(getName(hashTable[x].get(y)));
                    String deciVal = getVal(hashTable[x].get(y));
                    if(deciVal.contains(".") == false){
                        deciVal = deciVal + ".0";
                    }
                    fullTable.append("  Value: " + deciVal + "\n");
                    y++;
                }
            }
            x++;
        }
        return fullTable.toString();
    }
    
    /**
     * Calculate the load factor of the table
     * 
     * @return loadFactor - The load factor of the table which can be displayed to the user or notify when rehashing must occur
     */
    public String loadFactor(){
        alpha = (double) elements / (double) maxSize;
        DecimalFormat lfa = new DecimalFormat("#.##"); //Limit decimal places
        String loadFactor = lfa.format(alpha);
        if(alpha == 0.0){
            loadFactor = "0.0";
        }
        return loadFactor;
    }
    
    /**
     * The hash function used by the program to determine where an entry should be stored
     * 
     * @param stockName - The name of the stock so that it can be used by the hash function
     * @return limitHash - The hash value of the entry, properly limited by the table size
     */
    public int hashFunction(String stockName){
        String hashThis = getName(stockName); //Use only the name as the key
        int javaHash = hashThis.hashCode(); //Use the innate hash function
        //Cap the range of the hash function by using modulo with the max table size
        int limitHash = Math.abs(javaHash) % maxSize;
        return limitHash;
    }
    
    /**
     * Return the number of elements present in the table
     * 
     * @return elements - The number of elements in all of the table
     */
    public int elesPres(){
        return elements;
    }
    
    /**
     * Check to see if the new possible table size is prime
     * 
     * @param possPrime - The new, theoratical max table size
     * @return boolean - Confirmation if the new size is prime or not
     */
    public boolean testPrime(int possPrime){
        if(possPrime % 2 == 0){
            return false;
        }
        if(possPrime % 3 == 0){
            return false;
        }
        if(possPrime % 4 == 0){
            return false;
        }
        if(possPrime % 5 == 0){
            return false;
        }
        if(possPrime % 6 == 0){
            return false;
        }
        if(possPrime % 7 == 0 && possPrime > 7){
            return false;
        }
        if(possPrime % 8 == 0){
            return false;
        }
        if(possPrime % 9 == 0){
            return false;
        }
        return true;
    }
    
    /**
     * Calculate the new table size for rehashing
     * 
     * @return newSize - A prime number at least twice as large as the old table size
     */
    public int newSize(){
        int newSize = maxSize * 2;
        boolean foundPrime = false;
        //Check to see if the new size is prime
        while(foundPrime == false){
            newSize++;
            if(testPrime(newSize)){
                foundPrime = true;
            }
        }
        return newSize;
    }
    
    /**
     * Rehash the table when load factor alpah gets too high (above .75)
     */
    public void rehash(){
        rehasher = newSize() - maxSize;
        maxSize = newSize(); //Calculate new table size
        alpha = (double) elements / (double) maxSize; //Calculate the new load factor
        LinkedList<String>[] temp = new LinkedList[maxSize]; //Create a temp copy
        //Initialize all linked list that will be featured in the array
        for(int x = 0; x < maxSize; x++){
            temp[x] = new LinkedList<String>();
        }
        //Copy all values in the old hash table while using the updated hash function
        for(LinkedList<String> list : hashTable){
            for(String element : list){
                int rehash = hashFunction(element);
                temp[rehash].add(element);
            }
        }
        System.out.println("Rehashed with a new size of " + maxSize + " slots");
        //Create the new, upsized table
        hashTable = new LinkedList[maxSize];
        //Transfer from temp to the new table
        for(int y = 0; y < maxSize; y++){
            hashTable[y] = new LinkedList<String>();
        }
        
        for(int z = 0; z < maxSize; z++){
            hashTable[z].addAll(temp[z]);
        }
        System.out.println(showAll());
    }
    
    /**
     * Check to see if an insertion would lead to a dupe entry
     * 
     * @param possNew - The name/value pair the user wants to insert
     * @return boolean - Confirmation on if the entry is a dupe or not
     */
    public boolean checkDupe(String possNew){
        int findDupe = hashFunction(possNew);
        boolean find = false;
        //Search through the proper list for if dupes
        if(hashTable[findDupe] != null){
            int z = 0;
            int boundary = hashTable[findDupe].size();
            while(z < boundary){
                String key = getName(hashTable[findDupe].get(z));
                if(key.equals(possNew)){
                    return true;
                }
                z++;
            }
        }
        return find;
    }
    
    /**
     * Update the value for dupe name entries
     * 
     * @param updateThis - The name of the name/value pair to update. newVal - The new value of the name/value pair
     */
    public void update(String updateThis, String newVal){
        int x = 0;
        int list = 0;
        int place = 0;
        boolean search = false;
        //Search for where the dupe is
        while(search != true && x != hashTable[hashFunction(updateThis)].size()){
            if(updateThis == hashTable[hashFunction(updateThis)].get(x)){
                //changeVal(hashTable[hashFunction(updateThis)].get(x));
                place = x;
                search = true;
            }
            list++;
            x++;
        }
        changeVal(list, place, newVal); //Change only the val for the dupe entry
    }
    
    /**
     * Display a message when a dupe entry is found
     * 
     * @param list - The list location of the dupe. place - the index of the dupe. newVal - The new value of the dupe.
     */
    public void changeVal(int list, int place, String newVal){
        System.out.println("Updating");
        hashTable[list].set(place, newVal(hashTable[list].get(place), newVal));
    }
    
    /**
     * Change only the value of a dupe name/value pair
     * 
     * @param oldVal - The old value of the name/value pair. newVal - THe new value that will take the place of oldVal.
     * @return newStock - The name/value pair with the updated value
     */
    public String newVal(String oldVal, String newVal){
        char[] oldValArr = oldVal.toCharArray();
        char[] newValArr = newVal.toCharArray();
        int findMark = 0;
        while(oldValArr[findMark] != '+'){ //Locate where the '+' is
            findMark++;
        }
        //Create a new char[] that will hold the new value
        char[] updateVal = new char[newValArr.length + findMark];
        int placeHold = 0;
        int entry = 0;
        //Calculate the length of the old and new values, then replace the old value
        while(oldValArr[placeHold] != '+'){
            updateVal[placeHold] = oldValArr[placeHold];
            placeHold++;
            entry++;
        }
        int y = 0;
        while(entry != findMark + newValArr.length){
            updateVal[entry] = newValArr[y];
            y++;
            entry++;
        }
        String newStock = getName(oldVal) + "+" + String.valueOf(updateVal);
        return newStock;
    }
}
