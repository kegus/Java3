import Lesson6.*;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(value = Parameterized.class)
public class Program6Test {
    private static Program6 test = null;

    private static int[] checkArr;
    private static boolean res;

    public Program6Test(int[] checkArr, boolean res) {
        this.checkArr = checkArr;
        this.res = res;
    }

    @Parameterized.Parameters
    public static Collection abracadabra() {
        return Arrays.asList(new Object[][]{
                        {new int[]{5, 5, 1, 5, 8, 4}, true},
                        {new int[]{50, 2, 6, 5, 1}, false},
                        {new int[]{15, 23, 56, 4, 50}, false}
                }
        );
    }

    @Test
    public void checkArr1_4() {
        Assert.assertEquals(test.checkArr1_4(checkArr), res);
    }

    @Before
    public void start() {
        System.out.println("init tests ...");
        test = new Program6();
    }

    @After
    public void fiish() throws Exception {
        checkArr = null;
        test = null;
    }

}
