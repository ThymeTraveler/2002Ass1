import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class medianFilter{
    
    public static void main(String[] args){

        String filename = "sampleInput1000.txt";
        String line ="";
        try{
            File myFile = new File(filename);
            Scanner lines = new Scanner(myFile);
            ArrayList<Integer> items = new ArrayList<Integer>();

            while (lines.hasNextLine()){
                line = lines.nextLine();
                items.add(Integer.parseInt(line));
            }

        } catch(FileNotFoundException e){
            System.out.println("Oopsie diasy we could not find "+filename);
            System.exit(0);

        }   
        
    }

}