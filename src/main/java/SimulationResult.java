import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SimulationResult {
    List<Host> population;
    LinkedHashMap<Host, List<Host>> contact_tracingList;
    int total;
    int counter = 0; // counter for contact tracing
    public SimulationResult(List<Host> population, LinkedHashMap<Host, List<Host>> contact_tracingList){
        this.population = population;
        this.total = getTotalPopulation(this.population);
        this.contact_tracingList = contact_tracingList;
    }

    public void printHeaderResult(){
        System.out.println("Total population: " + getTotalPopulation(this.population));
        System.out.println("Total following quarantine: " + getQuarantineCount(this.population));
        System.out.println("Total population following social distancing" + getPopulationFoll_SocialDistancing(this.population));
        System.out.format("%10s%10s%15s%30s%n", "Total", "Healthy", "Infected", "Infected & Quarantined");
        System.out.format("============================================================================================%n");
    }
    public void printSimulationResult(){
        counter++;
        System.out.format("%10s%10s%15s%30s%n", total, getHealthyCount(this.population), getTotalInfected(this.population), getInfectedQuarantine(this.population) );

        // printing contract tracing after 300 simulation
        if(counter % 300 == 0){
            printContactTracing(this.contact_tracingList);
        }
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

    public int getQuarantineCount(List<Host> population){
        int counter = 0;
        for(Host p: population){
                counter++;
        }
        return counter;
    }


    public int getPopulationFoll_SocialDistancing(List<Host> population){
        int counter = 0;
        for(Host p: population){
                counter++;
        }
        return counter;
    }

    public int getInfectedQuarantine(List<Host> population){
        int counter = 0;
        for(Host p: population){
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
