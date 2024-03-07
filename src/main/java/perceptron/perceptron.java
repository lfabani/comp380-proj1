
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
    
    public perceptron(float[][] Weights, float[] BWeights, float Alpha, float Theta, int[][] Samples, int[][] T, int MaxEpoch, float WeightThreshold)
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
        //TODO: we need to update this not converged logic to use the weight threshold
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
                        System.out.println(String.valueOf(this.t[sampleCounter][tee])+ " This is the tee\n"+ String.valueOf(y) + " This is Y");
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

            writer.write(String.valueOf(this.theta));
            writer.write("\n"); //add a gap to make it easy to differentiate!
            writer.write(String.valueOf(this.alpha));
            writer.write("\n"); //add a gap to make it easy to differentiate!
            writer.write(String.valueOf(this.weightThreshold));
            // Close the writer
            writer.close();
            fos.close();

        } catch (IOException e) {
            System.err.println(e);
        }
    }
    public boolean[] test(int[][] results)
    {
        boolean[] accuracyTest = new boolean[results[0].length]; 
        int resultCount = 0;
        
        for (int[] result : results)
        {
            boolean pass = true;
            //iterate through each int in result and make sure they all match!
            for (int i = 0; i < result.length; i++)
            {
                int val = result[i];
                int compareT = this.t[resultCount][i];
                if (val != compareT)
                {
                    pass = false;
                }
            }
            accuracyTest[resultCount] = pass;
            resultCount++;
        }
        
        return accuracyTest;
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