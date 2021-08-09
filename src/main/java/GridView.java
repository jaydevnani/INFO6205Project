
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

public class GridView implements ActionListener, Constants {
    private MyPanel panel = null;
    private VirusSimulation sim = new VirusSimulation();
    private JButton btnStart;
    JSplitPane splitPane;
    JFrame frame;
    /**
     * Params for factor
     *
     */
    private int total_population;
    private int infected_population;
    private int death_rate;

    public int getTotalPopulation() {
        return total_population;
    }

    public void setTotalPopulation(int total_population) {
        this.total_population = total_population;
    }

    public int getInfectedPopulation() {
        return infected_population;
    }

    public void setInfectedPopulation(int infected_population) {
        this.infected_population = infected_population;
    }

    public int getDeathRate() {
        return death_rate;
    }

    public void setDeathRate(int death_rate) {
        this.death_rate = death_rate;
    }

    // constructor
    public GridView() {
        initializeParams();

        // Initialize gird
        initializeGrid();
    }

    private void initializeParams(){
        setTotalPopulation(1000);
        setInfectedPopulation(1000);
        setDeathRate(8);
    }

    private void initializeGrid(){
        //Simulation main screen frame.
        frame = new JFrame();
        frame.setTitle(APP_NAME);
        frame.setResizable(false);
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);

        frame.setBackground(Color.GREEN);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);



        splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        Dimension maxSize = new Dimension(1000, 500);
        JPanel paramPanel = getParamsPanel();
        paramPanel.setMaximumSize(maxSize);
        splitPane.setTopComponent(paramPanel);
        JPanel emptyPanel = new JPanel();

        splitPane.setBottomComponent(emptyPanel);

        splitPane.setDividerLocation(300);
        frame.add(splitPane);

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new GridView();
    }


    public void actionPerformed(ActionEvent actionEvent) {

        int population = getTotalPopulation();
/*

        System.out.println("Total Population = "+getTotalPopulation());
        System.out.println("Infected Population = "+getInfectedPopulation());
        System.out.println("Social Distancing = "+getSocialDistancing());
        System.out.println("Infection Rate = "+getInfectionRate());
        System.out.println("Death Rate = "+getDeathRate());
*/

       if (sim.isPaused()) {
           btnStart.setText("Pause Simulation");
           sim.startSim();
       } else if (sim.isRunning()) {
           sim.pauseSim();
           sim.setRunning(false);
           btnStart.setText("Resume Simulation");
       } else {
           System.out.println("Simulation has been started");
           panel = new MyPanel(population);
           System.out.println("Population count = " + population);
           splitPane.setRightComponent(panel);

           sim.addObserver(panel);
           sim.startSim();
           sim.setRunning(true); // force this on early, because we're about to reset the buttons
           btnStart.setText("Pause Simulation");
       }
    }


    private JPanel getParamsPanel() {
        btnStart = new JButton("Start");
        btnStart.setActionCommand("Start");
        btnStart.addActionListener(this);

        JPanel paramsPanel = new JPanel();

        paramsPanel.setLayout(new BoxLayout(paramsPanel, BoxLayout.Y_AXIS));

        paramsPanel.add(btnStart);

        return paramsPanel;
    }

}