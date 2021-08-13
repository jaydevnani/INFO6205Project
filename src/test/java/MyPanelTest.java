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
    public void changeDirectionTest1(){
        Random r = new Random();

        Island panel = new Island();
        Host per = new Host(r.nextInt(), r.nextInt(), new int[]{1,1});
        panel.changeDirection(per);

        assertEquals(-1,per.getDir()[0]);
        assertEquals(1,per.getDir()[1]);
    }

    @Test
    public void changeDirectionTest2(){
        Random r = new Random();

        Island panel = new Island();
        Host per = new Host(r.nextInt(), r.nextInt(), new int[]{-1,-1});
        panel.changeDirection(per);

        assertEquals(1,per.getDir()[0]);
        assertEquals(-1,per.getDir()[1]);
    }

    @Test
    public void changeDirectionTest3(){
        Random r = new Random();

        Island panel = new Island();
        Host per = new Host(r.nextInt(), r.nextInt(), new int[]{1,-1});
        panel.changeDirection(per);

        assertEquals(-1,per.getDir()[0]);
        assertEquals(-1,per.getDir()[1]);
    }

    @Test
    public void changeDirectionTest4(){
        Random r = new Random();

        Island panel = new Island();
        Host per = new Host(r.nextInt(), r.nextInt(), new int[]{-1,-1});
        panel.changeDirection(per);

        assertEquals(1,per.getDir()[0]);
        assertEquals(-1,per.getDir()[1]);
    }

    @Test
    public void collisionTest(){
        Random r = new Random();
        Island panel = new Island();
        Host per = new Host();
        assertFalse(panel.checkForCollision(r.nextInt(), r.nextInt(), per));
    }
}
