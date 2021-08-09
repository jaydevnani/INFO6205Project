import java.util.Observable;

public class VirusSimulation extends Observable implements Runnable {

    private Thread thread = null; // the thread that runs my simulation
    private boolean paused = false;
    private int days = 730;
    private boolean running = false; // set true if the simulation is running

    public void startSim() {
        System.out.println("Starting the simulation");
        paused = false;
        running = true;
        if (thread == null) {
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

    @Override
    public void run() {
        runSimLoop();
        thread = null;
    }

    private void runSimLoop() {
        running = true;
        while (days > 0) {
            if (!paused)
                updateSim();
            sleep();
            days--;
        }
        running = false;
    }

    private void sleep() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void updateSim() {
        setChanged();
        notifyObservers(this);
        
    }
}
