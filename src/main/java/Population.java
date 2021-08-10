import java.util.Arrays;

public class Population {
    private Chromosome[] chromosomes;
    public Population(int length)
    {
        chromosomes = new Chromosome[length];
    }
    public Population initializePopulation() {
        for (int i = 0; i < chromosomes.length; i++)
        {
            chromosomes[i] = new Chromosome(GeneticAlgorithm.TARGET_CHROMOSOME.length).initializeChromosome();
        }
        sortChromosomeByFitness();
        return this;
    }
    public Chromosome[] getChromosomes()
    {
        return chromosomes;
    }
    public void sortChromosomeByFitness()
    {
        Arrays.sort(chromosomes, (chromosome1, chromosome2) -> {
            int flag = 0;
            if (chromosome1.getFitness() > chromosome2.getFitness()) flag = -1;
            else if (chromosome2.getFitness() > chromosome1.getFitness()) flag = 1;
            return flag;
        });
    }
}
