
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.Scanner;

public class readFile {
    public static void main(String[] args)
    {
        /* 
            * Main method to get user input and create and call our perceptron object
         */
        Scanner userIn = new Scanner(System.in);

        System.out.println("Welcome to my first neural network - A Perceptron Net!");
        System.out.println("Enter 1 to train using a training data file\nEnter 2 to use a trained weight settings data file\nEnter any other number to quit ");
        int trainingSelection = Integer.valueOf(userIn.nextLine());

        boolean invalidSelection = true;
        String filePath;
        int[][] samples; 
        int[][] t; 
        while (invalidSelection){

            if (trainingSelection == 1){
                invalidSelection = false;
                System.out.println("Enter the training data file path: ");
                filePath = userIn.nextLine(); 
                int[] dimensions = read_file_dimensions(filePath);
                samples = new int[dimensions[2]][dimensions[0]];
                t = new int[dimensions[2]][dimensions[1]];
                String[] letters = new String[dimensions[1]];
                read_samples_file(filePath, samples, t, dimensions, letters);

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
                else { 
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

                perceptron p = new perceptron(weights, bweights, alpha, theta, samples, t, maxEpoch, weightThreshold, letters);
                
                p.train();
                p.create_results_file(resultsFilename);
                
    
                
            }
            else if (trainingSelection == 2){
                invalidSelection = false;

                System.out.println("Enter the name of the file to classify: ");
                filePath = userIn.nextLine();

                System.out.println("Enter the name of the trained weight settings data file: ");
                
                String weightsFile = userIn.next();
                
                
                int[] dimensions = read_file_dimensions(filePath);
                String[] letters = new String[dimensions[1]];
                letters = read_trained_alias_file(weightsFile, dimensions[1]);
                samples = new int[dimensions[2]][dimensions[0]];
                t = new int[dimensions[2]][dimensions[1]];
                
                read_samples_file(filePath, samples, t, dimensions,letters);
                
                float[][] weights = read_trained_weights_file(weightsFile, dimensions[0], dimensions[1]);
                float[] bWeights = read_trained_Bweights_file(weightsFile, dimensions[1]);
                float[] specials = read_trained_thresholds_file(weightsFile);
                float theta = specials[0];
                float alpha = specials[1];
                float weightThresh = specials[2];

                perceptron p = new perceptron(weights, bWeights, alpha, theta, samples, t, 100, weightThresh,letters);
                int[][] results = p.run();
                String[] test = p.test(results);
                System.out.println("trained");
                for (String tes: test)
                {
                    System.out.println(tes);
                }
                p.printAccuracy();
                
            }

            else{
                System.out.println("Invalid selection: ");                
            }
        }

        userIn.close();  
    }

    public static int[] read_file_dimensions(String filePath){
        /*
         * Function to read the dimensions of the samples
         * 
         * @param filePath The name of the file that we are reading
         * 
         * @return returnVals A list of integers containing the number of input neurons
         *      the number of output neurons, 
         *      the number of samples, 
         *      the number of rows in each sample,
         *      and the number of columns in each sample
         */
        int[] returnVals = new int[5];
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

            //skip blank line
            br.readLine();

            //find how many lines the input neurons are split into
            String line;
            int lineCounter = 0; 
            while (!(line = br.readLine()).equals("")){
                lineCounter++;
            } 
            returnVals[3] = lineCounter; //number of rows
            returnVals[4] = returnVals[0] / lineCounter; //number of columns
            
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
        /*
         * This function reads the chosen theta, alpha, and weight threshold out of the output file
         * 
         * @param filePath The filename of the output file that we are reading from 
         * 
         * @return returns A list containing the chosen theta, 
         *      the chosen alpha, 
         *      and the chosen weight threshold
         */
        float[] returns = new float[3];

        try {
            // Create a Filereader object to read the file 
            FileReader fr = new FileReader(filePath);

            //Wrap the FileReader in a BufferedReader for efficient reading
            BufferedReader br = new BufferedReader(fr);
            String line;
            int blankCount = 0;
            
            
            while ((line = br.readLine()) != null) {
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
        /*
         * This function reads the trained bias weights out of the output file
         * 
         * @param filePath The filename of the output file that we are reading from
         * @param numWeights The number of bias weights that we are reading in
         * 
         * @return biasWeights An array containing each trained bias
         */
        
        float[] biasWeights = new float[numWeights];

        try {
            // Create a Filereader object to read the file 
            FileReader fr = new FileReader(filePath);

            //Wrap the FileReader in a BufferedReader for efficient reading
            BufferedReader br = new BufferedReader(fr);
            String line;
            int blankCount = 0;
            int weightIndex = 0;
            
            while ((line = br.readLine()) != null) {
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
    public static String[] read_trained_alias_file(String filePath, int numOutputNeurons)
    {
        /*
         * This function reads the alias names from the output file
         * 
         * @param filePath The name of the output file that we are reading from
         * @param numOutputNeurons The number of aliases that we are reading
         * 
         * @return aliases An array containing each alias
         */
        String[] aliases = new String[numOutputNeurons];

        try {
            // Create a Filereader object to read the file 
            FileReader fr = new FileReader(filePath);

            //Wrap the FileReader in a BufferedReader for efficient reading
            BufferedReader br = new BufferedReader(fr);
            String line;
            int blankCount = 0;
            int weightIndex = 0;
            
            while ((line = br.readLine()) != null) {
                if (line.length() == 0) //this is what separates each val
                {
                    blankCount ++;
                }
                
                else if (blankCount == 5) //we are at bias weights!
                {
                    String[] splitLine = line.split(" ");
                    
                    for (String weight : splitLine)
                    {
                        if (!weight.equals("") && !weight.equals(" "))
                        {
                            aliases[weightIndex] = weight;
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
        return aliases;
    }

    public static float[][] read_trained_weights_file(String filePath, int numWeights, int numOutputNeurons){
        /*
         * This function reads the trained weights out of the training output file
         * 
         * @param filePath The name of the output file that we are reading from
         * @param numWeights The number of weights per sample
         * @param numOutputNeurons The number of sets of weights that were generated, one for each output neuron
         * 
         * @returns weights An array containing an array of weights for each output neuron
         */
        float[][] weights = new float[numOutputNeurons][numWeights];

        try {
            // Create a Filereader object to read the file 
            FileReader fr = new FileReader(filePath);

            //Wrap the FileReader in a BufferedReader for efficient reading
            BufferedReader br = new BufferedReader(fr);
            String line;
            int blankCount = 0;
            int weightIndex = 0;
            int sampleCounter = 0;
            while ((line = br.readLine()) != null) {

                if (line.length() == 0) //this is what separates each val
                {
                    blankCount ++;
                }
                else if (blankCount == 0) //weights w/o bias
                {
                    String[] splitLine = line.split(" ");
                    weightIndex = 0;
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

    public static void read_samples_file(String filePath, int[][] inputVals, int[][] tVals, int[] dimensionsSizes, String[] letters){
        /*
         * This function reads in our file of training or testing samples
         * 
         * @param filePath The file to read from
         * @param inputVals An array contating arrays of the values of each of the input vectors (1 or -1) for each sample
         * @param tVals An array containg arrays of the expected values of the output neurons for each sample
         * @param dimensionsSizes An array containing the dimensions of how the set of weights is split between lines into a matrix
         * @param letters An array containing the alias that is connected to each output neuron when it is "on"
         */
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
            int index = 0; //index of first 1 tVals to classify output neurons as aliases 
            
            //to bypass input values
            dimensions[0] = Integer.parseInt(bufferedReader.readLine().strip());
            //to return output values
            dimensions[1] = Integer.parseInt(bufferedReader.readLine().strip());
            //to return training pairs
            dimensions[2] = Integer.parseInt(bufferedReader.readLine().strip());

            String[][] indivSample = new String[dimensionsSizes[3]][dimensionsSizes[4]]; 

            
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
                        tVals[sampleCount] = convertStringListInt(line.split(" "), dimensionsSizes[4]);

                        sampleCount ++;

                        //Find index of letter
                        for (int num = 0; num < tVals[sampleCount-1].length; num++)
                        {
                            if (tVals[sampleCount-1][num] == 1)
                            {
                                index = num;
                            }
                        }                        
                    }
                    else
                    {
                        letters[index] = line.strip();
                        count = 0;
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

    public static int[] convertStringListInt (String[] strList, int numColumns)
    {
        /*
         * This function converts a list of Strings to a list of integers
         * 
         * @param strList The list of strings to convert
         * @param numColumns The number of items in the list to convert
         * 
         * @return intList The list of integers after conversion
         */
        int[] intList = new int[numColumns];  
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
        /*
         * This functions converts a matrix of String weights to a single array of integer weights
         * 
         * @param array2D The matrix of Strings to be converted to a single array of integers
         * 
         * @return resultsArray The one dimensional array of integers after conversion
         */
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
