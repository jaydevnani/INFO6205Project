import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SimulationResult {
    List<Host> population;
    int total;
    int counter;
    public SimulationResult(List<Host> population){
        this.population = population;
        this.total = getTotalPopulation(this.population);
    }

    public void printHeaderResult(){
        System.out.println("Total population: " + getTotalPopulation(this.population));
        System.out.format("%10s%10s%10s%15s%15s%n", "Day","Total", "Healthy", "Infected", "Vaccinated");
        System.out.format("============================================================================================%n");
    }
    public void printSimulationResult(){
        counter++;
        System.out.format("%10s%10s%10s%15s%15s%n", counter, total, getHealthyCount(this.population), getTotalInfected(this.population), getTotalVaccinated(this.population));
    }

    public int getTotalPopulation(List<Host> population){
        return population.size();
    }

    public int getHealthyCount(List<Host> population){
        int counter = 0;
        for(Host p: population){
            if(!p.isInfected())
                counter++;
        }
        return counter;
    }
    
    public int getTotalInfected(List<Host> population){
        int counter = 0;
        for(Host p: population){
            if(p.isInfected())
                counter++;
        }
        return counter;
    }
    
    public int getTotalVaccinated(List<Host> population){
        int counter = 0;
        for(Host p: population){
            if(p.isVaccinated())
                counter++;
        }
        return counter;
    }
    
    public void printContactTracing(LinkedHashMap<Host, List<Host>> contact_graph){
        for(Map.Entry<Host,List<Host>> entry : contact_graph.entrySet()){
            Host source = entry.getKey();
            List<Host> contactList = entry.getValue();

            System.out.print("Host " + source.getId() + " was infected and contracted to --> ");
            for(Host p: contactList){
                System.out.print("  "  + p.getId());
            }
            System.out.println();
        }
    }
}
