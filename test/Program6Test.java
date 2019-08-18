import Lesson6.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(value = Parameterized.class)
public class Program6Test {
    private static Program6 test = null;

    private int[] checkArr;

    public Program6Test(int[] checkArr) {
        this.checkArr = checkArr;
    }

    @Parameterized.Parameters
    public static Collection abracadabra() {
        return Arrays.asList(new Object[][]{
                        {new int[]{5, 5}},
                        {new int[]{50, 5}},
                        {new int[]{15, 50}}
                }
        );
    }

    @Test
    public void checkArr1_4() {
        Assert.assertEquals(test.checkArr1_4(checkArr), false);
    }

    @BeforeClass
    public static void init() {
        System.out.println("init tests ...");
        test = new Program6();
    }

    @After
    public void tearDown() throws Exception {
        test = null;
    }

}
