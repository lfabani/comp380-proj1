

class perceptron{
    private float[][] weights;
    private float[] bWeight;
    private float alpha;
    private float theta; 
    private int[][] sample;
    private int[][] t;
    private int epoch;
    private int maxEpoch;
    public perceptron(float[][] Weights, float[] BWeights, float Alpha, float Theta, int[][] Samples, int[][] T, int MaxEpoch)
    {  
        this.weights = Weights;
        this.bWeight = BWeights;
        this.alpha = Alpha;
        this.theta = Theta;
        this.sample = Samples;
        this.t = T;
        this.epoch = 0; 
        this.maxEpoch = MaxEpoch;


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

    public float compute(int sampleNum)
    {
        float yIn;
        boolean NotConverged = true;
        boolean tempNotConverged = false;
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
        
    }
    public float train()
    {

    }

    private int activationFunction(float yIn)
    {
        if (yIn < 0)
        {
            return -1;
        }
        else if (yIn > 0)
        {
            return 1;
        }
        else
        {
            return 0;
        }
    }
}