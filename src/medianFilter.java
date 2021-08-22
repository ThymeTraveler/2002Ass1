import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.lang.Math;
import java.util.Collections;
import java.io.FileWriter;
import java.io.IOException;

public class medianFilter{
    
    public static void main(String[] args){

        String filename = args[0];
        String outputName = args[2];
        int filterSize = Integer.parseInt(args[1]);

        String line ="";
        String item="";
        ArrayList<Double> items = new ArrayList<Double>();
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
                items.add(Double.parseDouble(item));
            }

        } catch(FileNotFoundException e){
            System.out.println("Oopsie diasy we could not find "+filename);
            System.exit(0);

        }

        ArrayList<Double> filteredList = (applyFilter(items, filterSize));
        printToFile(filteredList, outputName);
        
    }

    public static ArrayList<Double> applyFilter(ArrayList<Double> input1,int filterSize){
        ArrayList<Double> input = new ArrayList<Double>(input1);
        ArrayList<Double> result = new ArrayList<Double>();
        int border= (int) Math.ceil(filterSize/2);
        ArrayList<Double> localItems = new ArrayList<Double>();
        int size = (input.size());
        
        for (int i= 0; i<border;i++){
            result.add(input.get(i));
            
            
        }
        
        for (int i= border; i<size-border;i++){
            for (int x =i-border;x<i+border+1;x++){
                localItems.add(input.get(x));
                
                
            }
            Collections.sort(localItems);
            result.add(localItems.get(border));
            localItems.clear();
            
        }

        for (int i= size-border; i<size;i++){
            result.add(input.get(i));
        }
        


        return result;
    }

    public static void printToFile(ArrayList<Double> input, String outputName){

       try{
        FileWriter output = new FileWriter(outputName);
       
            output.write(Integer.toString(input.size())+"\n");
            for(int i=0;i<input.size();i++){
                output.write(String.format(i + " "+ "%.5f",input.get(i))+"\n");  
            }
            output.close();

       }catch(IOException e){
           System.out.println("Damn bro, looks like I can't do that one");
       }




        

    }

}