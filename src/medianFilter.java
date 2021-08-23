import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.lang.Math;
import java.util.Collections;
import java.io.FileWriter;
import java.io.IOException;

public class medianFilter{
    public static double totalTime;

      /**
   * This is the main method which makes use of applyMedianFilter 
   * and printToFile methods.
   * @param args input from the terminal (eg. <input_file>  <filter_size> <output_file>)
   * @return Nothing (prints to terminal).
   * @exception FileNotFoundException On file error.
   * @see FileNotFoundException
   */
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
            System.out.println("Oopsie daisy we could not find "+filename);
            System.exit(0);
        }


        double averageTime=0.00;
        ArrayList<Double> filteredList = new ArrayList<Double>();

        for (int i=0;i<20; i++){
            filteredList=(applyFilter(items, filterSize));
            averageTime+=totalTime;
            System.out.println(i+ " Time taken = "+Double.toString(totalTime) + " milliseconds");
        } System.out.println("avg Time taken = "+Double.toString(averageTime/20.0) + " milliseconds");

        printToFile(filteredList, outputName);
        
    }

       /**
   * This applies a median filter sequentially
   * @param input the input arraylist that you want to apply the median filter on
   * @param filterSize the filter size
   * @return resultant ArrayList of Double with filter applied to it
   */

    public static ArrayList<Double> applyFilter(ArrayList<Double> input,int filterSize){
        
        ArrayList<Double> result = new ArrayList<Double>();
        int border= (int) Math.ceil(filterSize/2);
        ArrayList<Double> localItems = new ArrayList<Double>();
        int size = (input.size());
        
        long startTime = System.nanoTime();

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
        
         totalTime= (System.nanoTime()-startTime)/1000000.00;

        


        return result;
    }




   /**
   * This prints an arraylist of double to an output text file
   * @param input the input arraylist that you want to print
   * @param outputName the name of the text file that will be produced or overwritten
   * @return nothing to System.out or any return types
   */
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