
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.Buffer;
import java.util.*;
import java.util.Scanner;

import javax.annotation.processing.Filer;


public class readFile {
    public static void main(String[] args)
    {
        Scanner userIn = new Scanner(System.in);

        System.out.println("Welcome to my first neural network - A Perceptron Net!");
        System.out.println("Enter 1 to train using a training data file\nEnter 2 to use a trained weight settings data file\nEnter any other character to quit ");

        int trainingSelection = Integer.valueOf(userIn.nextLine());
        
        boolean invalidSelection = true;
        String filePath;
        int[][] samples; //TODO: change hard coded values
        int[][] t; //TODO: change hard coded values
        while (invalidSelection){
            if (trainingSelection == 1){
                

                System.out.println("Enter the training data file path: ");
                filePath = userIn.nextLine(); //TODO: error check for invalid filename
                int[] dimensions = read_file_dimensions(filePath);
                samples = new int[dimensions[2]][dimensions[0]];
                t = new int[dimensions[2]][dimensions[1]];
                read_samples_file(filePath, samples, t);

                System.out.println("Enter 0 to initialize weights to 0, enter 1 to initialize weights to random values between -0.5 and 0.5: ");
                int weightSelection = Integer.valueOf(userIn.nextLine());
                float [][] weights = new float[dimensions[1]][dimensions[0]];
                float [] bweights = new float [dimensions[1]];
                if (weightSelection == 0){
                    //initials weights
                    for (int i = 0; i < dimensions[1]; i++)
                    {
                        for (int j = 0; j < dimensions[0]; j++)
                        {
                            weights[i][j] = 0;
                        }
                    }
                    //initialize bias weights
                    for (int i = 0; i < dimensions[1]; i++)
                        {
                            bweights[i] = 0;
                        }
                }
                else { //TODO: error check for invalid selection
                    //initials weights
                    Random random = new Random();
                    for (int i = 0; i < dimensions[1]; i++)
                    {
                        for (int j = 0; j < dimensions[0]; j++)
                        {
                            weights[i][j] = (random.nextFloat()-0.5f);
                        }
                    }
                    //initialize bias weights
                    for (int i = 0; i < dimensions[1]; i++)
                        {
                            bweights[i] = (random.nextFloat()-0.5f);
                        }
                }

                System.out.println("Enter the max number of training Epochs: ");
                int maxEpoch = Integer.valueOf(userIn.nextLine());

                System.out.println("Enter a file name to save the trained weight settings: ");
                String resultsFilename = userIn.nextLine();

                System.out.println("Enter the learning rate alpha from 0 to 1 but not including 0: ");
                float alpha = userIn.nextFloat();

                System.out.println("Enter the threshold theta: ");
                float theta = userIn.nextFloat();

                System.out.println("Enter the threshold to be used for measuring weight changes: ");
                float weightThreshold = userIn.nextFloat();

                perceptron p = new perceptron(weights, bweights, alpha, theta, samples, t, maxEpoch, weightThreshold);
                
                p.train();
                p.create_results_file(resultsFilename);
                
    
                
            }
            else if (trainingSelection == 2){
                

                System.out.println("Enter the name of the file to classify: ");
                filePath = userIn.nextLine();
                //read_samples_file(filePath, t, t);

                System.out.println("Enter the name of the trained weight settings data file: ");
                
                
                //read_trained_weights_file(filePath);
                String weightsFile = userIn.next();
                
                
                int[] dimensions = read_file_dimensions(filePath);
                samples = new int[dimensions[2]][dimensions[0]];
                t = new int[dimensions[2]][dimensions[1]];
                read_samples_file(filePath, samples, t);

                float[][] weights = read_trained_weights_file(weightsFile, dimensions[0], dimensions[2]);
                float[] bWeights = read_trained_Bweights_file(weightsFile, dimensions[1]);
                float[] specials = read_trained_thresholds_file(weightsFile);
                float theta = specials[0];
                float alpha = specials[1];
                float weightThresh = specials[2];

                perceptron p = new perceptron(weights, bWeights, alpha, theta, samples, t, 100, weightThresh);
                int[][] results = p.run();
                boolean[] test = p.test(results);
                System.out.println("trained");
                for (boolean tes: test)
                {
                    System.out.println(tes);
                }
                
            }

            else{
                System.out.println("Quit ");
                invalidSelection = false;
                
            }
        }

        userIn.close();

        
    }

    public static int[] read_file_dimensions(String filePath){
        int[] returnVals = new int[3];
        try {
            // Create a Filereader object to read the file 
            FileReader fr = new FileReader(filePath);

            //Wrap the FileReader in a BufferedReader for efficient reading
            BufferedReader br = new BufferedReader(fr);

            // Read first three lines and return dimensions
            //to return input values
            returnVals[0] = Integer.parseInt(br.readLine().strip());
            //to return output values
            returnVals[1] = Integer.parseInt(br.readLine().strip());
            //to return training pairs
            returnVals[2] = Integer.parseInt(br.readLine().strip());
            
            // Close BufferedReader and FileReader
            br.close();
            fr.close();

        }
        catch (IOException e){
            e.printStackTrace();
        }
        return returnVals;
    }
    public static float[] read_trained_thresholds_file(String filePath)
    {
        /*@Returns: returns
            * returns[0]  -> theta
            * returns[1] -> alpha
            * returns[2] -> weight threshold
         */
        float[] returns = new float[3];

        try {
            // Create a Filereader object to read the file 
            FileReader fr = new FileReader(filePath);

            //Wrap the FileReader in a BufferedReader for efficient reading
            BufferedReader br = new BufferedReader(fr);
            String line;
            line = br.readLine();
            int blankCount = 0;
            
            
            while (line != null) {
                if (line.length() == 0) //this is what separates each val
                {
                    blankCount ++;
                }
                
                else if (blankCount == 2) //we are at theta!
                { 
                    if (!line.equals("") && !line.equals(" "))
                    {
                        returns[0] = Float.parseFloat(line);
                        
                    }
                }
                else if (blankCount == 3) //we are at alpha!
                { 
                    if (!line.equals("") && !line.equals(" "))
                    {
                        returns[1] = Float.parseFloat(line);
                        
                    }
                }
                else if (blankCount == 4) //we are at threshold!
                { 
                    if (!line.equals("") && !line.equals(" "))
                    {
                        returns[2] = Float.parseFloat(line);
                        
                    }
                }
                

            
            }
            // Close BufferedReader and FileReader
            br.close();
            fr.close();
            

        }
        catch (IOException e){
            e.printStackTrace();
        }
        return returns;
    }
    public static float[] read_trained_Bweights_file(String filePath, int numWeights)
    {
        /*@Returns: float[] biasWeights -> an array containing each bias (each index is bias for output neuron)
         * 
         */
        
        float[] biasWeights = new float[numWeights];

        try {
            // Create a Filereader object to read the file 
            FileReader fr = new FileReader(filePath);

            //Wrap the FileReader in a BufferedReader for efficient reading
            BufferedReader br = new BufferedReader(fr);
            String line;
            line = br.readLine();
            int blankCount = 0;
            int weightIndex = 0;
            
            while (line != null) {
                if (line.length() == 0) //this is what separates each val
                {
                    blankCount ++;
                }
                
                else if (blankCount == 1) //we are at bias weights!
                {
                    String[] splitLine = line.split(" ");
                    
                    for (String weight : splitLine)
                    {
                        if (!weight.equals("") && !weight.equals(" "))
                        {
                            biasWeights[weightIndex] = Float.parseFloat(weight);
                            weightIndex++;
                        }
                        
                    }
                }

            
            }
            // Close BufferedReader and FileReader
            br.close();
            fr.close();
            

        }
        catch (IOException e){
            e.printStackTrace();
        }
        return biasWeights;
    }

    public static float[][] read_trained_weights_file(String filePath, int numWeights, int numSamples){
        /*@Returns: float[][] biasWeights -> an array containing each weight 
            * float[0] -> array of all weights for output neuron '0'
                *each of the rows are weight array for that index output neuron
         */
        float[][] weights = new float[numSamples][numWeights];

        try {
            // Create a Filereader object to read the file 
            FileReader fr = new FileReader(filePath);

            //Wrap the FileReader in a BufferedReader for efficient reading
            BufferedReader br = new BufferedReader(fr);
            String line;
            line = br.readLine();
            int blankCount = 0;
            int weightIndex = 0;
            int sampleCounter = 0;
            while (line != null) {
                if (line.length() == 0) //this is what separates each val
                {
                    blankCount ++;
                }
                else if (blankCount == 0) //weights w/o bias
                {
                    String[] splitLine = line.split(" ");
                    
                    for (String weight : splitLine)
                    {
                        if (!weight.equals("") && !weight.equals(" "))
                        {
                            weights[sampleCounter][weightIndex] = Float.parseFloat(weight);
                            weightIndex++;
                        }
                        
                    }
                    sampleCounter ++;
                }
                else if (blankCount == 1) //we are at bias weights!
                {
                    break;
                }
            
            }
            // Close BufferedReader and FileReader
            br.close();
            fr.close();
            

        }
        catch (IOException e){
            e.printStackTrace();
        }
        return weights;

    }

    public static void read_samples_file(String filePath, int[][] inputVals, int[][] tVals){
        int[] dimensions = new int[3];
        try {
            // Create a FileReader object to read the file
            FileReader fileReader = new FileReader(filePath);

            // Wrap the FileReader in a BufferedReader for efficient reading
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            // Variable to store each line read from the file
            String line;
            int count = 0; //keep track of how many spaces we are at!
            int dimCount = 0; //needed for count of what dim we are on. 
            int tCount = 0;
            
            //to bypass input values
            dimensions[0] = Integer.parseInt(bufferedReader.readLine().strip());
            //to return output values
            dimensions[1] = Integer.parseInt(bufferedReader.readLine().strip());
            //to return training pairs
            dimensions[2] = Integer.parseInt(bufferedReader.readLine().strip());

            String[][] indivSample = new String[9][7];  //TODO: change hard coded values

            String[] letters = new String[dimensions[2]];
            int sampleCount = 0;
            // Read each line of the file until reaching the end
            line = bufferedReader.readLine();
            while (line != null) {
                
                // Process each line here
                if (line.length() == 0)
                {
                    count++;
                }
                else if (count == 2) //we know we are gonna read the tval
                {
                    if (tCount == 0) //TVals
                    {   
                        dimCount = 0;
                        inputVals[sampleCount] = convertTo1D(indivSample);

                        //tval logic
                        tVals[sampleCount] = convertStringListInt(line.split(" "));
                        sampleCount ++;

                        count = 0;
                    }
                    else
                    {
                        letters[sampleCount-1] = line.split(" ")[0];
                    }
                    tCount ++;
                }
                else if (count == 1) //sample stuff
                {
                    tCount = 0;
                    String tempArray[] = line.strip().split(" ");
                    int i = 0;
                    for (String num : tempArray)
                    {
                        if (!num.equals(""))
                        {
                        indivSample[dimCount][i] = num;
                        i++;
                        }
                    }
                    dimCount += 1;
                }
                line = bufferedReader.readLine();
            }

            // Close the BufferedReader and FileReader to release resources
            bufferedReader.close();
            fileReader.close();
  
        } catch (IOException e) {
            // Handle any IO exceptions that may occur
            e.printStackTrace();
        }
    }

    public static int[] convertStringListInt (String[] strList)
    {
        int[] intList = new int[7];  //TODO: change hard coded values
        int i = 0;
        for (String bruh : strList)
        {
            if (!bruh.equals(""))
            {
            intList[i] = Integer.parseInt(bruh);
            i++;
            }
        }
        return intList;
    }

    public static int[] convertTo1D(String[][] array2D)
    {
        int rows = array2D.length;
        int cols = array2D[0].length;

        int totalElements = rows * cols;

        int[] resultArray = new int[totalElements];

        int index = 0;
        for (String[] row : array2D)
        {
            for (String numStr : row)
            {
                if (numStr != null && !numStr.equals(" ") && !numStr.equals(""))
                {
                    resultArray[index] = Integer.parseInt(numStr);
                    index++;
                } 
            }
        }
        return resultArray;
    }
}
