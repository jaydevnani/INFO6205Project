import io.cucumber.messages.internal.com.google.common.annotations.VisibleForTesting;
import org.junit.Test;

import java.awt.*;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

public class MyPanelTest {

    @Test
    public void keyTest(){
        Island panel = new Island();
        assertEquals(new String("5-3"), panel.getKey(5,3));
    }

    @Test
    public void populationCountTest(){
        Island panel = new Island();

        List<Host> pop1 = panel.generatePopulation(100);
        assertNotNull(pop1);
        assertEquals(100, pop1.size());

        List<Host> pop2 = panel.generatePopulation(500);
        assertNotNull(pop2);
        assertEquals(500, pop2.size());
    }

    @Test
    public void populationTest(){
        Island panel = new Island();
        assertEquals(20, panel.getActualPopulation(100));
        assertEquals(50, panel.getActualPopulation(250));
        assertEquals(100, panel.getActualPopulation(500));
    }

    @Test
    public void quarantineCheckTest(){
        Island panel = new Island();

        Host p_q = new Host();
        p_q.setInfected(true);
        p_q.setFoll_quarantine(true);
        assertTrue(panel.following_quarantine(p_q));

        Host p_nq = new Host();
        p_nq.setInfected(true);
        p_nq.setFoll_quarantine(false);
        assertFalse(panel.following_quarantine(p_nq));
    }

    @Test
    public void spreadTest1(){
        Island panel = new Island(100);
        Host per = new Host();
        Host nextPer = new Host();
        nextPer.setFoll_quarantine(true);
        assertFalse(panel.check_for_spread(per, nextPer));
    }

    @Test
    public void spreadTest2(){
        Random r = new Random();

        Island panel = new Island(100);
        Host per = new Host();
        Host nextPer = new Host();

        if(r.nextBoolean())
            per.setWearing_mask(true);
        else
            nextPer.setWearing_mask(true);

        assertFalse(panel.check_for_spread(per, nextPer));
    }

    @Test
    public void spreadTest3(){
        Random r = new Random();

        Island panel = new Island(100);
        Host per = new Host();
        Host nextPer = new Host();

        per.setWearing_mask(false);
        nextPer.setWearing_mask(false);

        if(r.nextBoolean())
            per.setInfected(true);
        else
            nextPer.setInfected(true);

        boolean check = panel.check_for_spread(per, nextPer);

        assertTrue(per.isInfected());
        assertTrue(nextPer.isInfected());
        assertTrue(check);
    }

    @Test
    public void changeDirectionTest1(){
        Random r = new Random();

        Island panel = new Island(100);
        Host per = new Host(r.nextInt(), r.nextInt(), new int[]{1,1});
        panel.changeDirection(per);

        assertEquals(-1,per.getDir()[0]);
        assertEquals(1,per.getDir()[1]);
    }

    @Test
    public void changeDirectionTest2(){
        Random r = new Random();

        Island panel = new Island(100);
        Host per = new Host(r.nextInt(), r.nextInt(), new int[]{-1,-1});
        panel.changeDirection(per);

        assertEquals(1,per.getDir()[0]);
        assertEquals(-1,per.getDir()[1]);
    }

    @Test
    public void changeDirectionTest3(){
        Random r = new Random();

        Island panel = new Island(100);
        Host per = new Host(r.nextInt(), r.nextInt(), new int[]{1,-1});
        panel.changeDirection(per);

        assertEquals(-1,per.getDir()[0]);
        assertEquals(-1,per.getDir()[1]);
    }

    @Test
    public void changeDirectionTest4(){
        Random r = new Random();

        Island panel = new Island(100);
        Host per = new Host(r.nextInt(), r.nextInt(), new int[]{-1,-1});
        panel.changeDirection(per);

        assertEquals(1,per.getDir()[0]);
        assertEquals(-1,per.getDir()[1]);
    }

    @Test
    public void collisionTest(){
        Random r = new Random();
        Island panel = new Island(100);
        Host per = new Host();
        assertFalse(panel.checkForCollision(r.nextInt(), r.nextInt(), per));
    }
}
