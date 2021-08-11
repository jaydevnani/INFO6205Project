import java.util.Arrays;

public class Population {
    private Virus[] virus;
    public Population(int length)
    {
        virus = new Virus[length];
    }
    public Population initializePopulation() {
        for (int i = 0; i < virus.length; i++)
        {
            virus[i] = new Virus(Constants.GENES).initializeVirus();
        }
        sortVirusByFitness();
        return this;
    }
    public Virus[] getVirus()
    {
        return virus;
    }
    public void sortVirusByFitness()
    {
        Arrays.sort(virus, (Virus1, Virus2) -> {
            int flag = 0;
            if (Virus1.getFitness() > Virus2.getFitness()) flag = -1;
            else if (Virus2.getFitness() > Virus1.getFitness()) flag = 1;
            return flag;
        });
    }
}
