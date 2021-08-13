
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

public class SimulationDemo implements ActionListener, Constants {
	private Island panel = null;
	private VirusSimulation sim = new VirusSimulation();
	private JButton btnStart;
	JSplitPane splitPane;
	JFrame frame;
	/**
	 * Params for factor
	 *
	 */
	private int totalPopulation;
	private int infectedPopulation;

	public SimulationDemo() {
		initializeParams();
		initializeGrid();
	}

	public int getTotalPopulation() {
		return totalPopulation;
	}

	public void setTotalPopulation(int totalPopulation) {
		this.totalPopulation = totalPopulation;
	}

	public int getInfectedPopulation() {
		return infectedPopulation;
	}

	public void setInfectedPopulation(int infectedPopulation) {
		this.infectedPopulation = infectedPopulation;
	}

	private void initializeParams() {
		setTotalPopulation(Constants.POPULATION);
		setInfectedPopulation(1);
	}

	private void initializeGrid() {
		// Simulation main screen frame.
		frame = new JFrame();
		frame.setTitle(APP_NAME);
		frame.setResizable(true);
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

		splitPane.setDividerLocation(30);
		frame.add(splitPane);

		frame.setVisible(true);
	}

	public void actionPerformed(ActionEvent actionEvent) {

		int population = getTotalPopulation();

		if (sim.isPaused()) {
			btnStart.setText("Pause Simulation");
			sim.startSim();
		} else if (sim.isRunning()) {
			sim.pauseSim();
			sim.setRunning(false);
			btnStart.setText("Resume Simulation");
		} else {
			System.out.println("Simulation has been started");
			panel = new Island();
			System.out.println("Population count = " + population);
			splitPane.setRightComponent(panel);

			sim.addObserver(panel);
			sim.startSim();
			sim.setRunning(true);
			btnStart.setText("Pause Simulation");
		}
	}

	private JPanel getParamsPanel() {
		btnStart = new JButton("Start Simulation");
		btnStart.setActionCommand("Start");
		btnStart.addActionListener(this);

		JPanel paramsPanel = new JPanel();

		paramsPanel.setLayout(new BoxLayout(paramsPanel, BoxLayout.Y_AXIS));

		paramsPanel.add(btnStart);

		return paramsPanel;
	}

	public static void main(String[] args) {
		new SimulationDemo();
	}
}