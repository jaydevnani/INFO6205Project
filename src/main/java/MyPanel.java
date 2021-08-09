import javax.swing.*;
import javax.xml.crypto.dsig.keyinfo.KeyValue;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.*;
import java.util.List;

@SuppressWarnings("deprecation")
public class MyPanel extends JPanel implements Observer, Constants {
    private int density;
    private int totalPopulation;
    private Graphics2D g2d;
    Random rand = new Random();
    Boolean isInstanceCreated = false;
    List<Host> HostList = new ArrayList<>();
    HashMap<String, Integer> map = new HashMap<>();
    SimulationResult benchMark;
    LinkedHashMap<Host, List<Host>> contact_list = new LinkedHashMap<>(); // type key value for contact tracing
    int ctr=0;

    public MyPanel(int density){
        this.density = density;
        this.totalPopulation = getActualPopulation(this.density);
    }


    public void paint(Graphics g) {
        g2d = (Graphics2D) g;
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0 , 1200, 900);
        g2d.setColor(Color.GRAY);
        g2d.fillRect(StartX,StartY, BOX_HEIGHT + 20 , BOX_WIDTH + 20);
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

    public void updateLocations(){
        for(Host per : HostList){
            if(!(per.isInfected()))
                updateCoordinates(per);
        }
    }
    private void updateCoordinates(Host per){

        if(per.isInfected())
            return;

        int[] dir = per.getDir();
        int X = per.getX();
        int Y = per.getY();

        int dirX = dir[0];
        int dirY = dir[1];

        if(X <= StartX  )
            dirX = 1;
        if(X > StartX + BOX_WIDTH )
            dirX = -1;

        if(Y <= StartY )
            dirY = 1;
        if(Y > StartY + BOX_HEIGHT )
            dirY = -1;

        // co-ordinated which needs to null;
        int nextX = X+ dirX * 5, nextY = Y+ dirY * 5;
        boolean isCollide = checkForCollision(nextX, nextY,per);

        if(!isCollide){
            per.setDir(new int[]{dirX, dirY});
            per.setX(X+ dirX * 5);
            per.setY(Y+ dirY * 5);
            safeToMove(per);
            updateHashMap( X, Y, per);
        }
        else{
            per = changeDirection(per);
            int[] updatedDir = per.getDir();
                per.setX(X+ (updatedDir[0] * 5));
                per.setY(Y+ (updatedDir[1] * 5));
                safeToMove(per);
                updateHashMap( per.getX(), per.getY(), per);
        }
    }

    private void createHostInstances(){
        if(!isInstanceCreated){

            for(int count = 0; count < totalPopulation; count++){
                int xVal = rand.nextInt(upperBound) , yVal = rand.nextInt(upperBound);

                Host per = new Host((xVal * 5 ) + 400, (yVal * 5) + 100, false, new int[]{rand .nextBoolean() ? -1 : 1,rand .nextBoolean() ? -1 : 1});
                per.setIndex(new Integer(count));

                if(count % 5 == 0){
                    per.setInfected(true);
                    addSingleSource(per);
                }
                else
                    per.setInfected(false);

                boolean result = addToHashMap(xVal, yVal,count );
                HostList.add(per);
                drawCircle(per);
            }
            benchMark = new SimulationResult(HostList, contact_list);
            benchMark.printHeaderResult();
            isInstanceCreated = true;
        }
        else{
            for(Host per : HostList){
                drawCircle(per);
            }
        }
    }

    private void drawCircle(Host Host){

            Shape circle = new Ellipse2D.Double(Host.getX(),Host.getY(), DIAMETER, DIAMETER);
            if(Host.isInfected()) // checking for infection
                g2d.setColor(Color.ORANGE);
            else if(Host.isInfected())
                    g2d.setColor(Color.RED);
                else
                    g2d.setColor(Color.GREEN);
            g2d.fill(circle);

    }

    private boolean addToHashMap(int xVal, int yVal, int perIndex){
        // adding xVal-yVal to map
        String key = getKey(xVal, yVal);
        if(map.containsKey(key)){
            Integer index = map.get(key);

            if(index.intValue() < 0){
                map.put(key, new Integer(perIndex));
                return true;
            }
            else
                return false;
        }
        else{
            map.put(key, new Integer(perIndex));
            return true;
        }
    }

    // check for collision
    public boolean checkForCollision(int X , int Y, Host per){
        String key = getKey(X, Y);
        if(map.containsKey(key)){
            Integer index = map.get(key);
            if(index.intValue() < 0)
                return false; // No collision
            else{
                Host nextPer = HostList.get(index.intValue());
                if(per.isInfected() || nextPer.isInfected()) // if Host is infected
                {
                    boolean spread = check_for_spread(per, nextPer);
                    if(spread)
                    {
                        // for contact tracing
                        addForContactTracing(per, nextPer);
                        addForContactTracing(nextPer, per);
                    }
                }
                if(!(following_quarantine(nextPer)))
                    changeDirection(nextPer);

                return true; // collision detected
            }
        }
        else
            return false;
    }

    public Host changeDirection(Host per){
        int X = per.getDir()[0], Y = per.getDir()[1];
        int newX, newY;

        if(X == 1 && Y == 1){
            newX = -1; newY = 1;
        }else{
            if(X == -1 && Y == -1){
                newX = 1; newY = -1;
            }else
            if(X == 1 && Y == -1){
                newX = -1; newY = -1;
            }
            else{
                newX = 1; newY = -1;
            }
        }

        per.setDir(new int[]{newX, newY});
        return per;
    }

    private void updateHashMap(int prevX, int prevY, Host per){
        String preKey = getKey(prevX, prevY);
        map.put(preKey, new Integer(-1));

        String currKey = getKey(per.getX() , per.getY());
        map.put(currKey, new Integer(per.getIndex()));
    }

    public String getKey(int X, int Y){
        return "" + X +"-" +  Y;
    }

    // get population based density
    public int getActualPopulation(int density){
        return density  /5;
    }

    // checking wearing mask factor and preventing the spread
    public boolean check_for_spread(Host per, Host nextPer){
        //if(nextPer.isFoll_quarantine()) // if the infected Host following quarantine then No spread
            return false;

        //if(per.isWearing_mask() || nextPer.isWearing_mask())
           // return false; // Virus spread prevented by mask
       // else{
            //per.setInfected(true);
            //nextPer.setInfected(true);
            //return true; // spreading of virus continue
        //}
    }

    // checking if Host is infected and following quarantine
    public boolean following_quarantine(Host per){
        if(per.isInfected())
            return true;
        else
            return false;
    }

    private void safeToMove(Host per){
        if (per.getY() < StartY - 5) {
            int preX = per.getX(), preY = per.getY();
            per.setX( (rand.nextInt(upperBound) * 5 ) + 400);
            per.setY( (rand.nextInt(upperBound) * 5) + 100 );
            updateHashMap(preX, preY, per);
        }
    }

    public List<Host> generatePopulation(int density){
        List<Host> population = new ArrayList<>();

        for(int count = 0; count< density; count++){
            int xVal = rand.nextInt(upperBound);
            int yVal = rand.nextInt(upperBound);
            int dirX = rand.nextBoolean() ? -1 : 1;
            int dirY = rand.nextBoolean() ? -1 : 1;

            Host per = new Host((xVal * 5 ) + 400, (yVal * 5) + 100, false, new int[]{dirX,dirY});
            per.setIndex(new Integer(count));

            if(count % 5 == 0)
                per.setInfected(true);
            else
                per.setInfected(false);

            population.add(per);
        }
        return population;
    }

    // add single source for contact tracing
    public LinkedHashMap<Host, List<Host>> addSingleSource(Host p){
        if(!contact_list.containsKey(p))
            contact_list.put(p, new ArrayList<Host>());

        return contact_list;
    }
    // for contact tracing
    public void addForContactTracing(Host per, Host nextHost){
        List<Host> list;
        if(contact_list.containsKey(per)){
            list = contact_list.get(per);
            if(!list.contains(nextHost)){
                list.add(nextHost);
                contact_list.put(per, list);
            }
        }
        else{
            list = new ArrayList<>();
            list.add(nextHost);
            contact_list.put(per, list);
        }
    }

    // get LinkedHashMap for contact tracing
    public LinkedHashMap<Host, List<Host>> getContact_tracing_list(){
        return contact_list;
    }
}




