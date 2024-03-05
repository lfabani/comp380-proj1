
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.Scanner;


public class readFile {
    public static void main(String[] args)
    {
        Scanner userIn = new Scanner(System.in);

        System.out.println("Welcome to my first neural network - A Perceptron Net!");
        System.out.println("Enter 1 to train using a training data file, enter 2 to use a trained weight settings data file: ");

        int trainingSelection = Integer.valueOf(userIn.nextLine());
        
        boolean invalidSelection = true;
        String filePath;
        while (invalidSelection){
            if (trainingSelection == 1){
                invalidSelection = false;

                System.out.println("Enter the training data file path: ");
                filePath = userIn.nextLine(); //TODO: error check for invalid filename
                int[] dimensions = read_file(filePath);

                System.out.println("Enter 0 to initialize weights to 0, enter 1 to initialize weights to random values between -0.5 and 0.5: ");
                int weightSelection = Integer.valueOf(userIn.nextLine());
                float [][] weights = new float[dimensions[0]][dimensions[1]];
                float [] bweights = new float [dimensions[1]];
                if (weightSelection == 0){
                    //initials weights
                    for (int i = 0; i < dimensions[0]; i++)
                    {
                        for (int j = 0; j < dimensions[1]; j++)
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
                    for (int i = 0; i < dimensions[0]; i++)
                    {
                        for (int j = 0; j < dimensions[1]; j++)
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
                int alpha = Integer.valueOf(userIn.nextLine());

                System.out.println("Enter the threshold theta: ");
                int theta = Integer.valueOf(userIn.nextLine());

                System.out.println("Enter the threshold to be used for measuring weight changes: ");
                int weightThreshold = Integer.valueOf(userIn.nextLine());

                perceptron p = new perceptron(weights, bweights, alpha, theta, null, null, maxEpoch);
            }
            else if (trainingSelection == 2){
                invalidSelection = false;

                System.out.println("Enter the name of the trained weight settings data file: ");
                filePath = userIn.nextLine(); //TODO: error check for invalid filename
                read_file(filePath);
                //make perceptron here using default constructor
            }

            else{
                System.out.println("Invalid selection! Please choose 1 or 2: ");
                trainingSelection = Integer.valueOf(userIn.nextLine());
            }
        }

        userIn.close();

        
    }

    public static int[] read_file(String filePath){
        int[] returnVals = new int[3];
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
            int tCount = 0;
            
            //to return input values
            returnVals[0] = Integer.parseInt(bufferedReader.readLine().split(" ")[0]);
            //to return output values
            returnVals[1] = Integer.parseInt(bufferedReader.readLine().split(" ")[0]);
            //to return training pairs
            returnVals[2] = Integer.parseInt(bufferedReader.readLine().split(" ")[0]);

            int[][] inputVals = new int[returnVals[2]][100];
            String[][] indivSample = new String[100][100];
            int[] tVals;

            String[] letters = new String[returnVals[2]];
            int sampleCount = 0;
            // Read each line of the file until reaching the end
            while ((line = bufferedReader.readLine()) != null) {
                
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

                        sampleCount ++;
                        //tval logic
                        
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
                    indivSample[dimCount] = line.strip().split(" ");
                    dimCount += 1;
                }

            }

            // Close the BufferedReader and FileReader to release resources
            bufferedReader.close();
            fileReader.close();
  
        } catch (IOException e) {
            // Handle any IO exceptions that may occur
            e.printStackTrace();
        }

        return returnVals;
    }

    public static int[] convertTo1D(String[][] array2D)
    {
        int rows = array2D.length;
        int cols = array2D.length;

        int totalElements = rows * cols;

        int[] resultArray = new int[totalElements];

        int index = 0;
        for (String[] row : array2D)
        {
            for (String numStr : row)
            {
                resultArray[index++] = Integer.parseInt(numStr);
            }
        }
        return resultArray;
    }
}
