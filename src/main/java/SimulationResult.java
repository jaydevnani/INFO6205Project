import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SimulationResult {
    List<Host> population;
    int total;
    int counter;
    int daysToVaccinate;
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
        if(getTotalPopulation(this.population) == getTotalVaccinated(this.population)) daysToVaccinate = counter;
    }
    
	public void printFinalResult(HashMap<Virus, Integer> virusList) {
		System.out.println("Days to vaccinate total surviving population : " + daysToVaccinate);
		System.out.println("Total casualties : " + (Constants.POPULATION - getTotalPopulation(this.population)));
		System.out.println("All variants documented with highest fitness levels : ");
		System.out.println(virusList);
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
