import java.awt.Color;
import java.util.HashMap;
import java.util.Random;

public class Host {
	private String id;
	private int X;
	private int Y;
	private boolean isInfected;
	private boolean isVaccinated;
	private Color color;
	HashMap<Virus, Boolean> viruses = new HashMap<>();
	HostGenoType hostGenoType;
	InfectionStatus infectionStatus;
	int[] dir;
	int daysInfected;
	Integer index;
	Random rand = new Random();

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	final Random r = new Random();

	public int getX() {
		return X;
	}

	public void setX(int x) {
		X = x;
	}

	public int getY() {
		return Y;
	}

	public void setY(int y) {
		Y = y;
	}

	public boolean isInfected() {
		return isInfected;
	}

	public void setInfected(boolean infected) {
		isInfected = infected;
	}

	public boolean isVaccinated() {
		return isVaccinated;
	}

	public void setVaccinated(boolean isVaccinated) {
		this.isVaccinated = isVaccinated;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public int[] getDir() {
		return dir;
	}

	public void setDir(int[] dir) {
		this.dir = dir;
	}

	public Random getR() {
		return r;
	}

	public HostGenoType getHostGenoType() {
		return hostGenoType;
	}

	public void setHostGenoType(HostGenoType hostGenoType) {
		this.hostGenoType = hostGenoType;
	}

	public InfectionStatus getInfectionStatus() {
		return infectionStatus;
	}

	public void setInfectionStatus(InfectionStatus infectionStatus) {
		this.infectionStatus = infectionStatus;
	}

	public HashMap<Virus, Boolean> getViruses() {
		return viruses;
	}

	public void setViruses(HashMap<Virus, Boolean> viruses) {
		this.viruses = viruses;
	}

	public void addViruses(Virus virus) {
		this.viruses.put(virus, false);
	}

	public int getDaysInfected() {
		return daysInfected;
	}

	public void setDaysInfected(int daysInfected) {
		this.daysInfected = daysInfected;
	}

	public Host(int X, int Y, boolean isInfected, Virus virus, int[] dir) {
		setId(generateID());
		this.X = X;
		this.Y = Y;
		this.isInfected = isInfected;
		if (virus != null)
			this.viruses.put(virus, false);
		this.dir = dir;
		if (isInfected)
			this.color = Color.RED;
		else
			this.color = Color.GREEN;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Host() {
		this.X = r.nextInt();
		this.Y = r.nextInt();

		int dirX = rand.nextBoolean() ? -1 : 1;
		int dirY = rand.nextBoolean() ? -1 : 1;
		this.dir = new int[] { dirX, dirY };
	}

	public Host(int X, int Y, int[] dir) {
		this.X = X;
		this.Y = Y;
		this.dir = dir;
	}

	public String generateID() {
		int i = 4;
		String uid = "H-";

		while (i-- > 0) {
			uid += r.nextInt(9);
		}
		return uid;
	}
	
	public void recoverFrom(Virus virus) {
		viruses.replace(virus, true);
	}
}
