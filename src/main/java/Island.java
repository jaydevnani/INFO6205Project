import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import javax.swing.JPanel;

public class Island extends JPanel implements Observer, Constants {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static int days = 0;
	private int totalPopulation;
	private Graphics2D g2d;
	Random rand = new Random();
	Boolean isInstanceCreated = false;
	List<Host> hostList = new ArrayList<>();
	HashMap<Virus, Integer> virusList;
	HashMap<String, Integer> map = new HashMap<>();
	SimulationResult benchMark;
	//VirusMutation vm;
	GeneticAlgorithm ga = new GeneticAlgorithm();
	int ctr = 0;

	public Island() {
		this.totalPopulation = Constants.POPULATION;
		//vm = new VirusMutation();
	}

	public void paint(Graphics g) {
		days++;
		g2d = (Graphics2D) g;
		g2d.setColor(Color.DARK_GRAY);
		g2d.fillRect(0, 0, 1200, 900);
		g2d.setColor(Color.WHITE);
		g2d.fillRect(StartX, StartY, BOX_HEIGHT + 20, BOX_WIDTH + 20);
		createHostInstances();
		benchMark.printSimulationResult();
	}

	@Override
	public void update(Observable observable, Object o) {
		if (o instanceof VirusSimulation) {
			updateLocations();
			repaint();
		}
	}

	public void updateLocations() {
		for (Host per : hostList) {
			if (days % Constants.MUTATION_RATE == 0 && per.isInfected() && rand.nextInt(100) < 32) {
				addVirusToMap(ga.mutatePopulation(per), per);
				per.addViruses(ga.mutatePopulation(per));
			}
			if (per.isInfected() && per.getViruses().size() >= 2) {
				addVirusToMap(ga.populationRecombination(per), per);
				per.addViruses(ga.populationRecombination(per));
			}
			updateCoordinates(per, virusList);
			recover(per);
			succumb(per);
			if (days > 365)
				vaccinate(per);
		}
	}

	/*
	 * public void updateCrossover() { HashMap<Virus, Integer> crossoverSet;
	 * for(Host per : hostList) { if (per.isInfected() && virusList.get(per) >= 2) {
	 * crossoverSet = addVirusToSet(ga.populationRecombination(per));
	 * per.addViruses(ga.populationRecombination(per));
	 * virusList.putAll(crossoverSet); } updateCoordinates(per, virusList); } }
	 */

	private void succumb(Host per) {
		if(per.isInfected()) {
			if(rand.nextInt(100) < Constants.DEATH_RATE) {
				hostList.remove(per);
				totalPopulation--;
			}
		}
	}

	private void addVirusToMap(Virus virus, Host per) {
		int fitness = fitnessCalculation(virus, per);
		if(virusList.containsKey(virus)) {
			int existingFitness = virusList.get(virus);
			if(existingFitness < fitness)
				virusList.replace(virus, fitness);
		}
	}

	private void updateCoordinates(Host per, HashMap<Virus, Integer> virusList) {

		int[] dir = per.getDir();
		int X = per.getX();
		int Y = per.getY();

		int dirX = dir[0];
		int dirY = dir[1];

		if (X <= StartX)
			dirX = 1;
		if (X > StartX + BOX_WIDTH)
			dirX = -1;

		if (Y <= StartY)
			dirY = 1;
		if (Y > StartY + BOX_HEIGHT)
			dirY = -1;

		// co-ordinated which needs to null;
		int nextX = X + dirX * 5, nextY = Y + dirY * 5;
		boolean isCollide = checkForCollision(nextX, nextY, per);

		if (!isCollide) {
			per.setDir(new int[] { dirX, dirY });
			per.setX(X + dirX * 5);
			per.setY(Y + dirY * 5);
			safeToMove(per);
			updateHashMap(X, Y, per);
		} else {
			per = changeDirection(per);
			int[] updatedDir = per.getDir();
			per.setX(X + (updatedDir[0] * 5));
			per.setY(Y + (updatedDir[1] * 5));
			safeToMove(per);
			updateHashMap(per.getX(), per.getY(), per);
		}
	}

	private void recover(Host p) {
		int fitness;
		List<Virus> viruses = getVirusListByHost(p);
		if (p.isInfected() && viruses.size() != 0) {
			for (int i = 0; i < viruses.size(); i++) {
				fitness = virusList.get(viruses.get(i));
				if (fitness < rand.nextInt(100)) {
					p.recoverFrom(viruses.get(i));
				}
			}

			if (getVirusListByHost(p).size() == 0) {
				p.setInfected(false);
				p.setColor(Color.ORANGE);
				p.setInfectionStatus(InfectionStatus.RECOVERED);
			}
		}
	}

	private void vaccinate(Host p) {
		if (!p.isInfected() && !p.isVaccinated()) {
			if (rand.nextInt(2500) < 5) {
				p.setVaccinated(true);
				p.setInfectionStatus(InfectionStatus.VACCINATED);
				if (p.getViruses().isEmpty()) {
					p.setColor(Color.BLUE);
				} else {
					p.setColor(Color.YELLOW);
				}
			}
		}
	}

	private void createHostInstances() {
		if (!isInstanceCreated) {

			for (int count = 0; count < totalPopulation; count++) {
				int xVal = rand.nextInt(upperBound), yVal = rand.nextInt(upperBound);

				Host per = new Host((xVal * 5) + 400, (yVal * 5) + 100, false, null,
						new int[] { rand.nextBoolean() ? -1 : 1, rand.nextBoolean() ? -1 : 1 });
				per.setIndex(new Integer(count));
				per.setVaccinated(false);
				per.setInfectionStatus(InfectionStatus.NAIVE);
				if (rand.nextInt(4) == 1)
					per.setHostGenoType(HostGenoType.A1);
				if (rand.nextInt(4) == 2)
					per.setHostGenoType(HostGenoType.A2);
				if (rand.nextInt(4) == 3)
					per.setHostGenoType(HostGenoType.B1);
				if (rand.nextInt(4) == 4)
					per.setHostGenoType(HostGenoType.B2);

				if (count == 0) {
					per.setInfected(true);
					per.setColor(Color.RED);
					per.addViruses(new Virus(10));
				} else
					per.setInfected(false);

				boolean result = addToHashMap(xVal, yVal, count);
				hostList.add(per);
				drawCircle(per);
			}
			benchMark = new SimulationResult(hostList);
			benchMark.printHeaderResult();
			isInstanceCreated = true;
		} else {
			for (Host per : hostList) {
				drawCircle(per);
			}
		}
	}

	private void drawCircle(Host host) {

		Shape circle = new Ellipse2D.Double(host.getX(), host.getY(), DIAMETER, DIAMETER);
		g2d.setColor(host.getColor());
		g2d.fill(circle);

	}

	private boolean addToHashMap(int xVal, int yVal, int perIndex) {
		// adding xVal-yVal to map
		String key = getKey(xVal, yVal);
		if (map.containsKey(key)) {
			Integer index = map.get(key);

			if (index.intValue() < 0) {
				map.put(key, new Integer(perIndex));
				return true;
			} else
				return false;
		} else {
			map.put(key, new Integer(perIndex));
			return true;
		}
	}

	public boolean checkForCollision(int X, int Y, Host per) {
		String key = getKey(X, Y);
		if (map.containsKey(key)) {
			Integer index = map.get(key);
			if (index.intValue() < 0)
				return false;
			else {
				Host nextPer = hostList.get(index.intValue());
				if ((per.isInfected() && !nextPer.isVaccinated()) || (nextPer.isInfected() && !per.isVaccinated())) // if
					infection(per, nextPer);
				return true;
			}
		} else
			return false;
	}

	public Host changeDirection(Host per) {
		int X = per.getDir()[0], Y = per.getDir()[1];
		int newX, newY;

		if (X == 1 && Y == 1) {
			newX = -1;
			newY = 1;
		} else {
			if (X == -1 && Y == -1) {
				newX = 1;
				newY = -1;
			} else if (X == 1 && Y == -1) {
				newX = -1;
				newY = -1;
			} else {
				newX = 1;
				newY = -1;
			}
		}

		per.setDir(new int[] { newX, newY });
		return per;
	}

	private void updateHashMap(int prevX, int prevY, Host per) {
		String preKey = getKey(prevX, prevY);
		map.put(preKey, new Integer(-1));

		String currKey = getKey(per.getX(), per.getY());
		map.put(currKey, new Integer(per.getIndex()));
	}

	public String getKey(int X, int Y) {
		return "" + X + "-" + Y;
	}

	public void infection(Host per, Host nextPer) {
		Virus virus;
		if (per.getColor() == Color.RED && nextPer.getColor() == Color.GREEN) {
			virus = getVirus(per);
			nextPer.addViruses(virus);
			nextPer.setColor(Color.RED);
			nextPer.setInfected(true);
			addVirusToMap(virus, nextPer);
		}
		if (per.getColor() == Color.GREEN && nextPer.getColor() == Color.RED) {
			virus = getVirus(nextPer);
			per.addViruses(getVirus(nextPer));
			per.setColor(Color.RED);
			per.setInfected(true);
			addVirusToMap(virus, per);
		}
	}

	private int fitnessCalculation(Virus virus, Host per) {
		int independentFitness = virus.getFitness();
		int finalFitness = 0;
		
		if(per.getHostGenoType() == HostGenoType.A1 && per.getInfectionStatus() == InfectionStatus.NAIVE){ 
			finalFitness = independentFitness * 20;
		}
		
		if(per.getHostGenoType() == HostGenoType.A1 && per.getInfectionStatus() == InfectionStatus.RECOVERED){ 
			finalFitness = independentFitness * 17;
		}

		if(per.getHostGenoType() == HostGenoType.A1 && per.getInfectionStatus() == InfectionStatus.VACCINATED){ 
			finalFitness = independentFitness * 14;
		}

		if(per.getHostGenoType() == HostGenoType.A2 && per.getInfectionStatus() == InfectionStatus.NAIVE){ 
			finalFitness = independentFitness * 7;
		}

		if(per.getHostGenoType() == HostGenoType.A2 && per.getInfectionStatus() == InfectionStatus.RECOVERED){ 
			finalFitness = independentFitness * 5;
		}

		if(per.getHostGenoType() == HostGenoType.A2 && per.getInfectionStatus() == InfectionStatus.VACCINATED){ 
			finalFitness = independentFitness * 4;
		}
		
		if(per.getHostGenoType() == HostGenoType.B1 && per.getInfectionStatus() == InfectionStatus.NAIVE){ 
			finalFitness = independentFitness * 22;
		}
		
		if(per.getHostGenoType() == HostGenoType.B1 && per.getInfectionStatus() == InfectionStatus.RECOVERED){ 
			finalFitness = independentFitness * 13;
		}

		if(per.getHostGenoType() == HostGenoType.B1 && per.getInfectionStatus() == InfectionStatus.VACCINATED){ 
			finalFitness = independentFitness * 3;
		}

		if(per.getHostGenoType() == HostGenoType.B2 && per.getInfectionStatus() == InfectionStatus.NAIVE){ 
			finalFitness = independentFitness * 19;
		}

		if(per.getHostGenoType() == HostGenoType.B2 && per.getInfectionStatus() == InfectionStatus.RECOVERED){ 
			finalFitness = independentFitness * 17;
		}

		if(per.getHostGenoType() == HostGenoType.B2 && per.getInfectionStatus() == InfectionStatus.VACCINATED){ 
			finalFitness = independentFitness * 15;
		}
		return finalFitness;
	}

	private Virus getVirus(Host per) {

		Iterator virusIterator = per.getViruses().entrySet().iterator();

		while (virusIterator.hasNext()) {
			Map.Entry<Virus, Boolean> viruses = (Map.Entry) virusIterator.next();
			Virus virus = viruses.getKey();
			Boolean antibodies = viruses.getValue();
			if (!antibodies)
				return virus;
		}
		return null;
	}

	private void safeToMove(Host per) {
		if (per.getY() < StartY - 5) {
			int preX = per.getX(), preY = per.getY();
			per.setX((rand.nextInt(upperBound) * 5) + 400);
			per.setY((rand.nextInt(upperBound) * 5) + 100);
			updateHashMap(preX, preY, per);
		}
	}

	public List<Host> generatePopulation(int density) {
		List<Host> population = new ArrayList<>();

		for (int count = 0; count < density; count++) {
			int xVal = rand.nextInt(upperBound);
			int yVal = rand.nextInt(upperBound);
			int dirX = rand.nextBoolean() ? -1 : 1;
			int dirY = rand.nextBoolean() ? -1 : 1;

			Host per = new Host((xVal * 5) + 400, (yVal * 5) + 100, false, null, new int[] { dirX, dirY });
			per.setIndex(new Integer(count));

			if (count % 5 == 0)
				per.setInfected(true);
			else
				per.setInfected(false);

			population.add(per);
		}
		return population;
	}

	public List<Virus> getVirusListByHost(Host per) {
		Iterator virusIterator = per.getViruses().entrySet().iterator();
		List<Virus> infectedViruses = new ArrayList<>();
		while (virusIterator.hasNext()) {
			Map.Entry<Virus, Boolean> viruses = (Map.Entry) virusIterator.next();
			Virus virus = viruses.getKey();
			Boolean antibodies = viruses.getValue();
			if (!antibodies)
				infectedViruses.add(virus);
		}
		return infectedViruses;
	}
}
