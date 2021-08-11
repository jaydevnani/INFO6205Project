import java.util.Arrays;

public class VirusMutation {
    public Population mutate() {
        Population population = new Population(GeneticAlgorithm.POPULATION_SIZE).initializePopulation();
        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm();
        System.out.println("----------------------");
        System.out.println("Generation 0 | Fittest chromosome fitness: " + population.getVirus()[0].getFitness());
        printPopulation(population, "Target Chromosome:" + Arrays.toString(GeneticAlgorithm.TARGET_VIRUS));
        int generationNumber = 0;
        while (population.getVirus()[0].getFitness() < GeneticAlgorithm.TARGET_VIRUS.length) {
            generationNumber++;
            System.out.println("\n----------------------");
            population = geneticAlgorithm.evolve(population);
            population.sortVirusByFitness();
            System.out.println("Generation " + generationNumber + " | Fittest Chromosome fitness: " + population.getVirus()[0].getFitness());
            printPopulation(population, "Target Chromosome:" + Arrays.toString(GeneticAlgorithm.TARGET_VIRUS));
        }
        return null;
    }
    public static void printPopulation(Population population, String heading)
    {
        System.out.println(heading);
        System.out.println("----------------------");
        for (int i = 0; i < population.getVirus().length; i++)
        {
            System.out.println("Chromosome " + i + " : " + Arrays.toString(population.getVirus()[i].getGenes()) + " | Fitness: " + population.getVirus()[i].getFitness());
        }
    }
}
