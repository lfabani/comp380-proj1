
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Scanner;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.SystemMenuBar;

import java.io.FileOutputStream;

class perceptron{
    private float[][] weights;
    private float[] bWeight;
    private float alpha;
    private float theta; 
    private int[][] sample;
    private int[][] t;
    private int epoch;
    private int maxEpoch;
    private float weightThreshold;
    private String[] readableT;
    private float accuracyPercent;
    
    public perceptron(float[][] Weights, float[] BWeights, float Alpha, float Theta, int[][] Samples, int[][] T, int MaxEpoch, float WeightThreshold, String[] tALias)
    {  
        this.weights = Weights;
        this.bWeight = BWeights;
        this.alpha = Alpha;
        this.theta = Theta;
        this.sample = Samples;
        this.t = T;
        this.epoch = 0; 
        this.maxEpoch = MaxEpoch;
        this.weightThreshold = WeightThreshold;
        this.readableT = tALias;
        this.accuracyPercent = 0f;
        
    }

    private void updateWeights(int t,int[] sample, int sampleCounter)
    {
        System.out.println("updateWeights called\n");
        for (int i = 0; i<this.weights[t].length; i++)
        {
            this.weights[t][i] +=this.alpha*this.t[sampleCounter][t]*sample[i];
        }
        
    }
    private void updateBias(int t, int sampleCounter)
    {

        this.bWeight[t] += this.alpha*this.t[sampleCounter][t];
    }

    public void train()
    {
        float yIn;
        boolean NotConverged = true;
        boolean tempNotConverged = false;
        int sampleCounter = 0;

        while(NotConverged == true && this.epoch <= this.maxEpoch)
        {
            sampleCounter = 0;
            for (int[] sample : this.sample)
            {

                for (int tee = 0; tee < this.t[0].length; tee++)
                {

                    
                        yIn = this.bWeight[tee];
                        for (int i = 0; i < sample.length; i++)
                        {
                            yIn += sample[i] * this.weights[tee][i];

                        }
                        float y = activationFunction(yIn);
                        if (Math.abs(y - this.t[sampleCounter][tee]) > this.weightThreshold) //add threshold here (isnt exact match or something)
                        {
                            updateWeights(tee, sample, sampleCounter);
                            updateBias(tee,sampleCounter);
                            tempNotConverged = true;
                        }
                }
                sampleCounter += 1; 
            }
            
            
            if (tempNotConverged == false)
            {
                NotConverged = false;
            }
            else{
                tempNotConverged = false;
            }
            this.epoch += 1;
            
        }
        System.out.println(String.valueOf(this.epoch) + " epochs\n");

    }

    public void create_results_file(String filename) {
        File file = new File(filename);
        try {
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
            } else {
                System.out.println("File already exists.");
            }

            // Write weights and bweights to the file
            FileOutputStream fos = new FileOutputStream(file);
            OutputStreamWriter writer = new OutputStreamWriter(fos);

            // Writing weights
            String weightStr;
            for (float[] wt: this.weights){
                weightStr = "";
                for (float w : wt){
                    weightStr += String.valueOf(w) + " ";
                }
                writer.write(weightStr + "\n");
            }


            // Writing bweights
            String bWString = "";
            for (float wt : this.bWeight)
            {
                bWString += String.valueOf(wt) + " ";
            }
            writer.write("\n"); //add a gap to make it easy to differentiate!
            writer.write(bWString + "\n");
            writer.write("\n"); //add a gap to make it easy to differentiate!

            //now we gotta write the theta, alpha, and weight Threshold

            writer.write(String.valueOf(this.theta) + "\n");
            writer.write("\n"); //add a gap to make it easy to differentiate!
            writer.write(String.valueOf(this.alpha) + "\n");
            writer.write("\n"); //add a gap to make it easy to differentiate!
            writer.write(String.valueOf(this.weightThreshold) + "\n");
            writer.write("\n"); //add a gap to make it easy to differentiate!
            String aliasString = "";
            for (String alias : this.readableT)
            {
                aliasString += alias + " ";
            }
            writer.write(aliasString);
            // Close the writer
            writer.close();
            fos.close();

        } catch (IOException e) {
            System.err.println(e);
        }
    }

    private static String[] whatLetter(int[] predicted, String[] aliases)
    {
        String[] letter = new String[predicted.length];
        int letterCount = 0;
        for (int num = 0; num < predicted.length; num++)
        {
            if (predicted[num] == 1)
            {
                letter[letterCount] = aliases[num];
                letterCount++;
            }
        }
        
        return letter;
    }
    public String[] test(int[][] results)
    {
        String[] accuracyTest = new String[results.length]; 
        int resultCount = 0;
        String expectedVal;
        String Predicted;
        String megaString;
        String[] alias;
        String[] realAlias;
        float numRight = 0f;

        for (int[] result : results)
        {
            boolean pass = false;
            expectedVal = "";
            Predicted = "";
            megaString = "";
            alias = new String[result.length];
            realAlias = new String[result.length];;

            //iterate through each int in result and make sure they all match!
            for (int i = 0; i < result.length; i++)
            {
                expectedVal += String.valueOf(this.t[resultCount][i])+ " ";
                Predicted += String.valueOf(result[i]) + " ";
                
            }
            //figure out alias
            realAlias = whatLetter(this.t[resultCount], this.readableT);
            alias = whatLetter(result, this.readableT);
            String finalAlias = alias[0];
            String realFinal = realAlias[0];
            for (int i = 0; i < alias.length; i++)
            {
                String al = alias[i];
                for (int j = 0; j < realAlias.length; j++)
                {
                    String compareAl = realAlias[j];

                    if (compareAl != null && al != null && compareAl.equals(al))
                    {
                        finalAlias = al;
                        pass = true;
                        numRight += 1;
                    }
                }
            }
            
            megaString += "Expected Value: " + expectedVal + " AKA: " + realFinal +"\nPredicted Value: " + Predicted + " AKA: " + finalAlias + "\nMatch: " + String.valueOf(pass);
            accuracyTest[resultCount] = megaString;
            resultCount++;
        }
        this.accuracyPercent = numRight/(resultCount-1);
        
        return accuracyTest;
    }
    public void printAccuracy()
    {
        System.out.println("The accuracy of the model is: " + String.valueOf(this.accuracyPercent));
    }

    public int[][] run()
    {
        int[][] results = new int[this.sample.length][this.t[0].length];
        int sampleCount = 0;
       for (int[] sample : this.sample)
       {

            //iterate through each sample
            //each sample has a combo of y's (each output neuron!)
            for (int outputNeuron = 0; outputNeuron < this.t[0].length; outputNeuron++)
            {
                float yIn = 0f;
            //each output neuron has a set of wieghts and inputs
                for (int i = 0; i < sample.length; i++)
                    {
                        yIn += sample[i]*this.weights[outputNeuron][i];
                    }
                //now run activation func to get the y
                int y = activationFunction(yIn);
                results[sampleCount][outputNeuron] = y;
                
            }
            sampleCount ++; 
       }
       return results;
    }

    private int activationFunction(float yIn)
    {
        if (yIn < this.theta)
        {
            return -1;
        }
        else
        {
            return 1;
        }
        
    }
}