import java.util.Observable;

@SuppressWarnings("deprecation")
public class VirusSimulation extends Observable implements Runnable {

    private Thread thread = null; // the thread that runs my simulation
    private boolean paused = false;
    private boolean done = false; // set true to end the simulation loop
    private int ctr = 0;
    private boolean running = false; // set true if the simulation is running

    public void startSim() {
        System.out.println("Starting the simulation");
        paused = false;
        running = true;
        done = false; // reset the done flag.
        if (thread == null) {
            ctr = 0; // reset the loop counter
            thread = new Thread(this); // Create a worker thread
        }
        thread.start();
    }

    public void pauseSim() {
        paused = !paused;
        System.out.println("Pause the simulation: " + paused);
    }

    public boolean isPaused() {
        return paused;
    }


    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public void run() {
        runSimLoop();
        thread = null; // flag that the simulation thread is finished
    }

    private void runSimLoop() {
        running = true;
        while (!done) {
            if (!paused)
                updateSim();
            sleep();

        }
        running = false;
    }

    /*
     * Make the current thread sleep a little
     */
    private void sleep() {
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Allow access to the simulation data
    public int getCtr() {
        return ctr;
    }

    private void updateSim() {
        ctr++;

        //System.out.println("Updating the simulation " + ctr++);
        setChanged();
        notifyObservers(this); // Send a copy of the simulation
        
    }
}
