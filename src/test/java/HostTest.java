import java.awt.*;
import java.util.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HostTest {

    @Test
    public void infectedColor1Test(){
        Random r = new Random();
        Host p = new Host(r.nextInt(), r.nextInt(), false, null, new int[]{r .nextBoolean() ? -1 : 1, r .nextBoolean() ? -1 : 1});
        assertEquals(p.getColor(), Color.GREEN);
    }

    @Test
    public void InfectedColor2Test(){
        Random r = new Random();
        Host p = new Host(r.nextInt(), r.nextInt(), true, new Virus(), new int[]{r .nextBoolean() ? -1 : 1, r .nextBoolean() ? -1 : 1});
        assertEquals(p.getColor(), Color.RED);
    }
}
