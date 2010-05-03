package common;

import java.awt.Point;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Administrator
 */
public class CVector2Test {

    public CVector2Test() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of set method, of class CVector2.
     */
    @Test
    public void testSet() {
        System.out.println("set");
        double x = 0.0;
        double y = 0.0;
        CVector2 instance = new CVector2();
        instance.set(x, y);
        assertEquals(x, instance.getX(), 0.00001);
        assertEquals(y, instance.getY(), 0.00001);
    }

    /**
     * Test of add method, of class CVector2.
     */
    @Test
    public void testAdd_CVector2() {
        System.out.println("add");
        double a = 5.0, b = 9.0, c = 20.0, d = 30.0;
        CVector2 vec = new CVector2(a, b);
        CVector2 instance = new CVector2(d, c);
        instance.add(vec);
        assertEquals(a + d, instance.getX(), 0.00001);
        assertEquals(b + c, instance.getY(), 0.00001);
    }

    /**
     * Test of add_cpy method, of class CVector2.
     */
    @Test
    public void testAdd_cpy() {
        System.out.println("add_cpy");
        double a = 5.0, b = 9.0, c = 20.0, d = 30.0;
        CVector2 vec = new CVector2(a, b);
        CVector2 instance = new CVector2(c, d);
        CVector2 expResult = new CVector2(a+c, b+d);
        CVector2 result = instance.add_cpy(vec);
        assertEquals(expResult.get(), result.get());
    }

    /**
     * Test of add method, of class CVector2.
     */
    @Test
    public void testAdd_double_double() {
        System.out.println("add");
        double x = 3.0;
        double y = 4.0;
        CVector2 instance = new CVector2(1, 2);
        instance.add(x, y);
        assertEquals(4, instance.getX(), 0.0001);
        assertEquals(6, instance.getY(), 0.0001);

    }

    /**
     * Test of sub method, of class CVector2.
     */
    @Test
    public void testSub_CVector2() {
        System.out.println("sub");
        double x = 3.0;
        double y = 4.0;
        CVector2 vec = new CVector2(x, y);
        CVector2 instance = new CVector2();
        instance.sub(vec);
        assertEquals(-x, instance.getX(), 0.0001);
        assertEquals(-y, instance.getY(), 0.0001);
    }

    /**
     * Test of sub method, of class CVector2.
     */
    @Test
    public void testSub_double_double() {
        System.out.println("sub");
        double x = 7.0;
        double y = 8.0;
        CVector2 instance = new CVector2();
        instance.sub(x, y);
        assertEquals(-x, instance.getX(), 0.0001);
        assertEquals(-y, instance.getY(), 0.0001);
    }

    /**
     * Test of sub_cpy method, of class CVector2.
     */
    @Test
    public void testSub_cpy() {
        System.out.println("sub_cpy");
        double a = 5.0, b = 9.0, c = 20.0, d = 30.0;
        CVector2 vec = new CVector2(a, b);
        CVector2 instance = new CVector2(c, d);
        CVector2 expResult = new CVector2(c-a, d-b);
        CVector2 result = instance.sub_cpy(vec);
        assertEquals(expResult.get(), result.get());
    }

    /**
     * Test of mul method, of class CVector2.
     */
    @Test
    public void testMul() {
        System.out.println("mul");
        double a = 2.0;
        double x = 5.0, y = 9.0;
        CVector2 instance = new CVector2(x, y);
        instance.mul(a);
        assertEquals(x*a, instance.getX(), 0.0001);
        assertEquals(y*a, instance.getY(), 0.0001);
    }

    /**
     * Test of mul_cpy method, of class CVector2.
     */
    @Test
    public void testMul_cpy() {
        System.out.println("mul_cpy");
        double a = 6.0;
        double x = 5.0, y = 9.0;
        CVector2 instance = new CVector2(x, y);
        CVector2 result = instance.mul_cpy(a);
        assertEquals(x*a, result.getX(), 0.0001);
        assertEquals(y*a, result.getY(), 0.0001);
    }

    /**
     * Test of div method, of class CVector2.
     */
    @Test
    public void testDiv() {
        System.out.println("div");
        double a = 2.0;
        double x = 5.0, y = 9.0;
        CVector2 instance = new CVector2(x, y);
        instance.div(a);
        assertEquals(x/a, instance.getX(), 0.0001);
        assertEquals(y/a, instance.getY(), 0.0001);
    }

    /**
     * Test of div_cpy method, of class CVector2.
     */
    @Test
    public void testDiv_cpy() {
        System.out.println("div_cpy");
        double a = 6.0;
        double x = 5.0, y = 9.0;
        CVector2 instance = new CVector2(x, y);
        CVector2 result = instance.div_cpy(a);
        assertEquals(x/a, result.getX(), 0.0001);
        assertEquals(y/a, result.getY(), 0.0001);
    }

    /**
     * Test of norm method, of class CVector2.
     */
    @Test
    public void testNorm() {
        System.out.println("norm");
        double x = 10.0, y = 6.0;
        CVector2 instance = new CVector2(x, y);
        double expResult = Math.sqrt((x*x) + (y*y));
        double result = instance.norm();
        assertEquals(expResult, result, 0.0001);
    }

    /**
     * Test of get method, of class CVector2.
     */
    @Test
    public void testGet() {
        System.out.println("get");
        CVector2 instance = new CVector2(3.0, 5.0);
        Point expResult = new Point(3, 5);
        Point result = instance.get();
        assertEquals(expResult, result);
    }

    /**
     * Test of getX method, of class CVector2.
     */
    @Test
    public void testGetX() {
        System.out.println("getX");
        double x = 4.2, y = 1.2;
        CVector2 instance = new CVector2(x, y);
        double expResult = x;
        double result = instance.getX();
        assertEquals(expResult, result, 0.0001);
    }

    /**
     * Test of getY method, of class CVector2.
     */
    @Test
    public void testGetY() {
        System.out.println("getY");
        double x = 4.2, y = 1.2;
        CVector2 instance = new CVector2(x, y);
        double expResult = y;
        double result = instance.getY();
        assertEquals(expResult, result, 0.0001);
    }

    /**
     * Test of rotate method, of class CVector2.
     */
    @Test
    public void testRotate() {
        System.out.println("rotate");
        double a = 3.1;
        double xx = 5.1;
        double yy = 7.1;
        double x = xx*Math.cos(a) - yy*Math.sin(a);
        double y = xx*Math.sin(a) + yy*Math.cos(a);
        CVector2 instance = new CVector2(xx, yy);
        instance.rotate(a);
        assertEquals(x, instance.getX(), 0.0001);
        assertEquals(y, instance.getY(), 0.0001);
    }

    /**
     * Test of rotate_cpy method, of class CVector2.
     */
    @Test
    public void testRotate_cpy() {
        System.out.println("rotate_cpy");
        double a = 3.1;
        double xx = 5.1;
        double yy = 7.1;
        double x = xx*Math.cos(a) - yy*Math.sin(a);
        double y = xx*Math.sin(a) + yy*Math.cos(a);
        CVector2 instance = new CVector2(xx, yy);
        CVector2 result = instance.rotate_cpy(a);
        assertEquals(result.get(), new CVector2(x, y).get());
    }

    /**
     * Test of invert method, of class CVector2.
     */
    @Test
    public void testInvert() {
        System.out.println("invert");
        double x = 3.1, y = 5.4;
        CVector2 instance = new CVector2(x, y);
        instance.invert();
        assertEquals(-x, instance.getX(), 0.0001);
        assertEquals(-y, instance.getY(), 0.0001);
    }

    /**
     * Test of invert_cpy method, of class CVector2.
     */
    @Test
    public void testInvert_cpy() {

        System.out.println("invert_cpy");
        double x = 3.1, y = 5.4;
        CVector2 instance = new CVector2(x, y);
        CVector2 result = instance.invert_cpy();
        instance.invert();
        assertEquals(instance.get(), result.get());
    }

    /**
     * Test of resize method, of class CVector2.
     */
    @Test
    public void testResize() {
        System.out.println("resize");
        double l = 1.0;
        CVector2 instance = new CVector2(1.0, 2.0);
        instance.resize(l);
        assertEquals(0.4472135954999579d, instance.getX(), 0.0001);
        assertEquals(0.8944271909999159d, instance.getY(), 0.0001);
    }

    /**
     * Test of resize_cpy method, of class CVector2.
     */
    @Test
    public void testResize_cpy() {
        System.out.println("resize_cpy");
        double l = 1.0;
        CVector2 instance = new CVector2(1.0, 2.0);
        CVector2 result = instance.resize_cpy(l);
        instance.resize(l);
        assertEquals(instance.get(), result.get());
    }

    /**
     * Test of cpy method, of class CVector2.
     */
    @Test
    public void testCpy() {
        System.out.println("cpy");
        double x= 3.6, y= 7.3;
        CVector2 instance = new CVector2(x, y);
        CVector2 result = instance.cpy();
        assertEquals(instance.get(), result.get());
    }

    /**
     * Test of getDistanceTo method, of class CVector2.
     */
    @Test
    public void testGetDistanceTo() {
        System.out.println("getDistanceTo");
        double x = 5.4, y = 12.3, a = 4.56, b = 7.89;
        CVector2 vec = new CVector2(a, b);
        CVector2 instance = new CVector2(x, y);
        double diffX = x-a, diffY = y-b;
        double contr = Math.sqrt((diffX * diffX) + (diffY * diffY));
        double result = instance.getDistanceTo(vec);
        assertEquals(contr, result, 0.0);
    }

    /**
     * Test of equals method, of class CVector2.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");
        double x= 3.6, y= 7.3;
        CVector2 c = new CVector2(x, y);
        CVector2 instance = new CVector2(x, y);
        boolean result = instance.equals(c);
        assertEquals(true, result);
    }

}