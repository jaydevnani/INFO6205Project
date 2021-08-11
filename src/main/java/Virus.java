import java.util.Arrays;

public class Virus {
    private int fitness = 0;
    private int[] genes;
    private boolean isFitnessChanged = true;
    public Virus(int length)
    {
        genes = new int[length];
    }
    public Virus initializeVirus()
    {
        for(int i = 0; i < genes.length; i++)
        {
            if(Math.random() >= 0.5)
                genes[i] = 1;
            else
                genes[i] = 0;
        }
        return this;
    }

    public int[] getGenes() {
        isFitnessChanged = true;
        return genes;
    }
    public int getFitness() {
        if (isFitnessChanged) {
            fitness = recalculateFitness();
            isFitnessChanged = false;
        }
        return fitness;
    }
    public int recalculateFitness() {
        int chromosomeFitness = 0;
        for(int i = 0; i < genes.length; i++)
        {
            if (genes[i] == GeneticAlgorithm.TARGET_VIRUS[i])
                chromosomeFitness++;
        }
        return chromosomeFitness;
    }
    
    public String toString() {
        return Arrays.toString(this.genes);
    }
}
