import java.util.HashMap;

public class GeneticAlgorithm {
    public static final int POPULATION_SIZE = 8;
    public static final int[] TARGET_VIRUS = {1, 1, 0, 1, 0, 0, 1, 1, 1, 0};
    public static final double mutationrate = 0.25;
    public static final int eliteChromosome = 1;
    public static final int selectionSize = 4;
    /*public Population evolve(Population population)
    {
        return mutatePopulation(crossoverPopulation(population));
    }*/

    private Population crossoverPopulation(Population population)
    {
        Population crossoverPopulation = new Population(population.getVirus().length);
        for (int i = 0; i < eliteChromosome; i++)
            crossoverPopulation.getVirus()[i] = population.getVirus()[i];
        for (int i = eliteChromosome; i < population.getVirus().length; i++)
        {
            Virus chromosome1 = selectPopulationSize(population).getVirus()[0];
            Virus chromosome2 = selectPopulationSize(population).getVirus()[0];
            crossoverPopulation.getVirus()[i] = crossoverChromosome(chromosome1, chromosome2);
        }
        return crossoverPopulation;
    }
    public Virus mutatePopulation(Host per)
    {
        Virus mutatedVirus = new Virus(10);
        for (int i = 0; i < eliteChromosome; i++)
        	mutatedVirus.getGenes()[i] = per.getViruses().entrySet().iterator().next().getKey().getGenes()[i];
        for (int i = eliteChromosome; i < 10; i++)
        {
        	mutatedVirus.getGenes()[i] = mutateChromosome(per.getViruses().entrySet().iterator().next().getKey().getGenes()[i]);
        }
        return mutatedVirus;
    }
    private Virus crossoverChromosome(Virus chromosome1, Virus chromosome2)
    {
        Virus crossoverChromosome = new Virus(TARGET_VIRUS.length);
        for (int i = 0; i < chromosome1.getGenes().length; i++)
        {
            if (Math.random() < 0.5)
                crossoverChromosome.getGenes()[i] = chromosome1.getGenes()[i];
            else
                crossoverChromosome.getGenes()[i] = chromosome2.getGenes()[i];
        }
        return crossoverChromosome;
    }
    private int mutateChromosome(int gene)
    {
            if (Math.random() < mutationrate)
            {
                if (Math.random() < 0.5) return 1;
                else
                	return 0;
            }
            else return gene;
    }
    private Population selectPopulationSize(Population population)
    {
        Population selectedPopulation = new Population(selectionSize);
        for (int i = 0; i < selectionSize; i++)
        {
            selectedPopulation.getVirus()[i] = population.getVirus()[(int)(Math.random() * population.getVirus().length)];
        }
        selectedPopulation.sortVirusByFitness();
        return selectedPopulation;
    }
}
