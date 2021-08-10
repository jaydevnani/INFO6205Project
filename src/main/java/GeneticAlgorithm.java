public class GeneticAlgorithm {
    public static final int POPULATION_SIZE = 8;
    public static final int[] TARGET_CHROMOSOME = {1, 1, 0, 1, 0, 0, 1, 1, 1, 0};
    public static final double mutationrate = 0.25;
    public static final int eliteChromosome = 1;
    public static final int selectionSize = 4;
    public Population evolve(Population population)
    {
        return mutatePopulation(crossoverPopulation(population));
    }

    private Population crossoverPopulation(Population population)
    {
        Population crossoverPopulation = new Population(population.getChromosomes().length);
        for (int i = 0; i < eliteChromosome; i++)
            crossoverPopulation.getChromosomes()[i] = population.getChromosomes()[i];
        for (int i = eliteChromosome; i < population.getChromosomes().length; i++)
        {
            Chromosome chromosome1 = selectPopulationSize(population).getChromosomes()[0];
            Chromosome chromosome2 = selectPopulationSize(population).getChromosomes()[0];
            crossoverPopulation.getChromosomes()[i] = crossoverChromosome(chromosome1, chromosome2);
        }
        return crossoverPopulation;
    }
    private Population mutatePopulation(Population population)
    {
        Population mutatePopulation = new Population(population.getChromosomes().length);
        for (int i = 0; i < eliteChromosome; i++)
            mutatePopulation.getChromosomes()[i] = population.getChromosomes()[i];
        for (int i = eliteChromosome; i < population.getChromosomes().length; i++)
        {
            mutatePopulation.getChromosomes()[i] = mutateChromosome(population.getChromosomes()[i]);
        }
        return mutatePopulation;
    }
    private Chromosome crossoverChromosome(Chromosome chromosome1, Chromosome chromosome2)
    {
        Chromosome crossoverChromosome = new Chromosome(TARGET_CHROMOSOME.length);
        for (int i = 0; i < chromosome1.getGenes().length; i++)
        {
            if (Math.random() < 0.5)
                crossoverChromosome.getGenes()[i] = chromosome1.getGenes()[i];
            else
                crossoverChromosome.getGenes()[i] = chromosome2.getGenes()[i];
        }
        return crossoverChromosome;
    }
    private Chromosome mutateChromosome(Chromosome chromosome)
    {
        Chromosome mutateChromosome = new Chromosome(TARGET_CHROMOSOME.length);
        for(int i = 0; i < chromosome.getGenes().length; i++)
        {
            if (Math.random() < mutationrate)
            {
                if (Math.random() < 0.5) mutateChromosome.getGenes()[i] = 1;
                else
                    mutateChromosome.getGenes()[i] = 0;
            }
            else
                mutateChromosome.getGenes()[i] = chromosome.getGenes()[i];
        }
        return mutateChromosome;
    }
    private Population selectPopulationSize(Population population)
    {
        Population selectedPopulation = new Population(selectionSize);
        for (int i = 0; i < selectionSize; i++)
        {
            selectedPopulation.getChromosomes()[i] = population.getChromosomes()[(int)(Math.random() * population.getChromosomes().length)];
        }
        selectedPopulation.sortChromosomeByFitness();
        return selectedPopulation;
    }
}
