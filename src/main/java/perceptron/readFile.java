package main.java.perceptron;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class readFile {
    public static void main(String[] args)
    {
        String filePath = "../sample_data.txt";

        try {
            // Create a FileReader object to read the file
            FileReader fileReader = new FileReader(filePath);

            // Wrap the FileReader in a BufferedReader for efficient reading
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            // Variable to store each line read from the file
            String line;
            int count = 0; //keep track of how many spaces we are at!

            int dimCount = 0; //needed for count of what dim we are on. 

            int matrixRowCount = 0; //needed to know what row to populate

            

            int numInputPatterns;
            int numOutputPatterns;
            int numTrainingSets;

            int[][] inputVals;

            int[] tVals;
            // Read each line of the file until reaching the end
            while ((line = bufferedReader.readLine()) != null) {
                // Process each line here
                if (line.length() == 0)
                {
                    count += 1
                }
                String[] vals = line.split(" ");
                
                if (count == 0)
                {
                    //get the dimensions
                    if (dimCount == 0)
                    {
                        dimCount += 1;
                        numInputPatterns = Integer.parseInt(vals[0]);
                    }
                    else if (dimCount == 1)
                    {
                        dimCount += 1;
                        numOutputPatterns = Integer.parseInt(vals[0]);
                    }
                    else
                    {
                        numTrainingSets = Integer.parseInt(vals[0]);
                    }

                }
                else if (count == 1) //we are reading in matrix now
                {
                    
                    for (int i = 0; i < vals.length; i++) 
                    {
                        inputVals[matrixRowCount][i] = Integer.parseInt(vals[i]);
                    }
                    matrixRowCount ++;

                }
                else if (count == 2) //we are getting tvals
                {
                    for (int i = 0; i < vals.length; i++) 
                    {
                        tVals[i] = Integer.parseInt(vals[i]);
                    }
                }

            }

            // Close the BufferedReader and FileReader to release resources
            bufferedReader.close();
            fileReader.close();
        } catch (IOException e) {
            // Handle any IO exceptions that may occur
            e.printStackTrace();
        }

        
    }
}
