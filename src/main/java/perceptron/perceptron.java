
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Scanner;
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
    private String resultsFilename;
    private int weightThreshold;
    public perceptron(float[][] Weights, float[] BWeights, float Alpha, float Theta, int[][] Samples, int[][] T, int MaxEpoch, String ResultsFilename, int WeightThreshold)
    {  
        this.weights = Weights;
        this.bWeight = BWeights;
        this.alpha = Alpha;
        this.theta = Theta;
        this.sample = Samples;
        this.t = T;
        this.epoch = 0; 
        this.maxEpoch = MaxEpoch;
        this.resultsFilename = ResultsFilename;
        this.weightThreshold = WeightThreshold;
    }

    private void updateWeights(int t,int[] sample)
    {
        
        for (int i = 0; i<this.weights[t].length; i++)
        {
            this.weights[i][t] +=this.alpha*t*sample[i];
        }
        
    }
    private void updateBias(int t)
    {

        this.bWeight[t] += this.alpha*t;
    }

    public void train(int sampleNum)
    {
        float yIn;
        boolean NotConverged = true;
        boolean tempNotConverged = false;
        //TODO: we need to update this not converged logic to use the weight threshold
        while(NotConverged == true && this.epoch <= this.maxEpoch)
        {
           
            for (int tVal : t[sampleNum])
            {
                int[] sample = this.sample[sampleNum];
                {
                    yIn = this.bWeight[tVal];
                    for (int i = 0; i < sample.length; i++)
                    {
                        yIn += sample[i] + this.weights[i][tVal];

                    }
                    float y = activationFunction(yIn);

                    if (y != tVal) //add threshold here (isnt exact match or something)
                    {
                        updateWeights(tVal, sample);
                        updateBias(tVal);
                        tempNotConverged = true;
                    }
                    
                }
            }
            if (tempNotConverged == false)
            {
                NotConverged = false;
            }
            this.epoch += 1;
            
        }
        create_results_file(this.resultsFilename);

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
            writer.write(this.weights + "\n");

            // Writing bweights
            writer.write(this.bWeight + "\n");

            // Close the writer
            writer.close();
            fos.close();

        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public void run()
    {
       
    }

    private int activationFunction(float yIn)
    {
        if (yIn < this.theta)
        {
            return -1;
        }
        else if (yIn > this.theta)
        {
            return 1;
        }
        else
        {
            return 0;
        }
    }
}