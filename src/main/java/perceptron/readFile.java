package main.java.perceptron;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.*;
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
                read_file(filePath);

                System.out.println("Enter 0 to initialize weights to 0, enter 1 to initialize weights to random values between -0.5 and 0.5: ");
                int weightSelection = Integer.valueOf(userIn.nextLine());
                float [][] weights;
                float [] bweights;
                if (weightSelection == 0){
                    //TODO: this needs to initialize all weights to 0, but I don't know where your storing the dimensions of the input data
                    //TODO: set bias to 0 
                }
                else { //TODO: error check for invalid selection
                    //TODO: this needs to create random weights, but again, I don't know what value to use for the for loop
                    //TODO: set bias to random weight
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

    public static void read_file(String filePath){
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
                    count += 1;
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
