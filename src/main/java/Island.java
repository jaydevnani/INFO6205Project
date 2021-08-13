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
	VirusMutation vm;
	GeneticAlgorithm ga = new GeneticAlgorithm();
	int ctr = 0;

	public Island() {
		this.totalPopulation = Constants.POPULATION;
		vm = new VirusMutation();
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
		HashMap<Virus, Integer> newVirusSet;
		for (Host per : hostList) {
			if (days % Constants.MUTATION_RATE == 0 && per.isInfected() && rand.nextInt(100) < 32) {
				newVirusSet = addVirusToSet(ga.mutatePopulation(per));
				per.addViruses(ga.mutatePopulation(per));
				virusList.putAll(newVirusSet);
			}
			updateCoordinates(per, virusList);
			updateInfectedDayCount(per);
			recover(per);
			if (days > 365)
				vaccinate(per);
		}
	}
	
	public void updateCrossover() {
		HashMap<Virus, Integer> crossoverSet;
		for(Host per : hostList) {
			if (per.isInfected() && virusList.get(per) >= 2) {
				crossoverSet = addVirusToSet(ga.populationRecombination(per));
				per.addViruses(ga.populationRecombination(per));
				virusList.putAll(crossoverSet);
			}
			updateCoordinates(per, virusList);
		}
	}

	private HashMap<Virus, Integer> addVirusToSet(Virus mutateVirus) {
		
		return null;
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
		boolean isCollide = checkForCollision(nextX, nextY, per, virusList);

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

	private void updateInfectedDayCount(Host p) {
		int infectionPeriod;
		if (p.isInfected()) {
			infectionPeriod = p.getDaysInfected();
			// System.out.println("Host " + p.getId() + " has been infected for " +
			// infectionPeriod);
			infectionPeriod++;
			p.setDaysInfected(infectionPeriod);
			// System.out.println("Infection day count up to " + infectionPeriod);
		}
	}

	private void recover(Host p) {
		if (p.isInfected()) {
			int random = rand.nextInt(100);
			// System.out.println("Random number : " + random + p.getId());
			if (random < 1) {
				p.setInfected(false);
				p.setColor(Color.ORANGE);
			}
			/*
			 * for (Virus v : p.getViruses().keySet()) { if (!p.getViruses().get(v) &&
			 * p.getDaysInfected() > 20) { p.getViruses().replace(v, true);
			 * p.setDaysInfected(0); System.out.println("Host " + p.getId() +
			 * " has recovered from strain " + v); p.setInfected(false);
			 * p.setColor(Color.magenta); } }
			 */
			// if(p.getViruses().values().contains(false)) {
			// continue;
			// }
			// else {
			// }
		}
	}

	private void vaccinate(Host p) {
		if (!p.isInfected() && !p.isVaccinated()) {
			if (rand.nextInt(2500) < 5) {
				p.setVaccinated(true);
				if(p.getViruses().isEmpty()) {
				p.setColor(Color.BLUE);
				p.setHostType(HostType.B1);
				}
				else {
					p.setColor(Color.YELLOW);
					p.setHostType(HostType.B2);
				}
				// System.out.println("Host " + p.getId() + " has been vaccinated against Covid
				// on day " + days);
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
				per.setHostType(HostType.A1);
				if (count == 0) {
					per.setInfected(true);
					per.setColor(Color.RED);
					per.addViruses(new Virus(10));
					per.setHostType(HostType.A2);
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
		/*
		 * if(Host.isInfected()) // checking for infection g2d.setColor(Color.ORANGE);
		 * else if(Host.isInfected()) g2d.setColor(Color.RED); else
		 */
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

	// check for collision
	public boolean checkForCollision(int X, int Y, Host per, HashMap<Virus, Integer> virusList) {
		String key = getKey(X, Y);
		if (map.containsKey(key)) {
			Integer index = map.get(key);
			if (index.intValue() < 0)
				return false; // No collision
			else {
				Host nextPer = hostList.get(index.intValue());
				if ((per.isInfected() && !nextPer.isVaccinated()) || (nextPer.isInfected() && !per.isVaccinated())) // if
					infection(per, nextPer, virusList);
				return true; // collision detected
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

	public void infection(Host per, Host nextPer, HashMap<Virus, Integer> virusList) {
		if (per.getColor() == Color.RED && nextPer.getColor() == Color.GREEN) {
			nextPer.setHostType(HostType.A2);
			nextPer.addViruses(getVirus(per));
			nextPer.setColor(Color.RED);
			nextPer.setInfected(true);
			nextPer.setDaysInfected(1);
		}
		if (per.getColor() == Color.GREEN && nextPer.getColor() == Color.RED) {
			per.setHostType(HostType.A2);
			per.addViruses(getVirus(nextPer));
			per.setColor(Color.RED);
			per.setInfected(true);
			per.setDaysInfected(1);
		}
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
}
