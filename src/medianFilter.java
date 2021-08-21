import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class medianFilter{
    
    public static void main(String[] args){

        String filename = "sampleInput100.txt";
        String line ="";
        String item="";
        ArrayList<Float> items = new ArrayList<Float>();
        try{
            File myFile = new File(filename);
            Scanner lines = new Scanner(myFile);
            int length = Integer.parseInt(lines.nextLine());
            while (lines.hasNextLine()){
                line = lines.nextLine();
                Scanner itemScanner = new Scanner(line);
                itemScanner.useDelimiter("\\s* \\s*");
                itemScanner.next();
                item=itemScanner.next();
                item=item.replaceAll(",",".");
                items.add(Float.parseFloat(item));
            }

        } catch(FileNotFoundException e){
            System.out.println("Oopsie diasy we could not find "+filename);
            System.exit(0);

        }
        
        
    }

}