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
        double x=5.0, y=9.0, s = 20.0, t = 30.0;
        CVector2 vec = new CVector2(x, y);
        CVector2 instance = new CVector2(t, s);
        instance.add(vec);
        assertEquals(x+t, instance.getX(), 0.00001);
        assertEquals(y+s, instance.getY(), 0.00001);
    }

    /**
     * Test of add_cpy method, of class CVector2.
     */
    @Test
    public void testAdd_cpy() {
        System.out.println("add_cpy");
        CVector2 vec = new CVector2();
        CVector2 instance = new CVector2();
        CVector2 expResult = null;
        CVector2 result = instance.add_cpy(vec);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of add method, of class CVector2.
     */
    @Test
    public void testAdd_double_double() {
        System.out.println("add");
        double x = 0.0;
        double y = 0.0;
        CVector2 instance = new CVector2();
        instance.add(x, y);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of sub method, of class CVector2.
     */
    @Test
    public void testSub_CVector2() {
        System.out.println("sub");
        CVector2 vec = null;
        CVector2 instance = new CVector2();
        instance.sub(vec);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of sub method, of class CVector2.
     */
    @Test
    public void testSub_double_double() {
        System.out.println("sub");
        double x = 0.0;
        double y = 0.0;
        CVector2 instance = new CVector2();
        instance.sub(x, y);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of sub_cpy method, of class CVector2.
     */
    @Test
    public void testSub_cpy() {
        System.out.println("sub_cpy");
        CVector2 vec = null;
        CVector2 instance = new CVector2();
        CVector2 expResult = null;
        CVector2 result = instance.sub_cpy(vec);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of mul method, of class CVector2.
     */
    @Test
    public void testMul() {
        System.out.println("mul");
        double a = 0.0;
        CVector2 instance = new CVector2();
        instance.mul(a);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of mul_cpy method, of class CVector2.
     */
    @Test
    public void testMul_cpy() {
        System.out.println("mul_cpy");
        double a = 0.0;
        CVector2 instance = new CVector2();
        CVector2 expResult = null;
        CVector2 result = instance.mul_cpy(a);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of div method, of class CVector2.
     */
    @Test
    public void testDiv() {
        System.out.println("div");
        double a = 0.0;
        CVector2 instance = new CVector2();
        instance.div(a);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of div_cpy method, of class CVector2.
     */
    @Test
    public void testDiv_cpy() {
        System.out.println("div_cpy");
        double a = 0.0;
        CVector2 instance = new CVector2();
        CVector2 expResult = null;
        CVector2 result = instance.div_cpy(a);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of norm method, of class CVector2.
     */
    @Test
    public void testNorm() {
        System.out.println("norm");
        CVector2 instance = new CVector2();
        double expResult = 0.0;
        double result = instance.norm();
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of get method, of class CVector2.
     */
    @Test
    public void testGet() {
        System.out.println("get");
        CVector2 instance = new CVector2();
        Point expResult = null;
        Point result = instance.get();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getX method, of class CVector2.
     */
    @Test
    public void testGetX() {
        System.out.println("getX");
        CVector2 instance = new CVector2();
        double expResult = 0.0;
        double result = instance.getX();
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getY method, of class CVector2.
     */
    @Test
    public void testGetY() {
        System.out.println("getY");
        CVector2 instance = new CVector2();
        double expResult = 0.0;
        double result = instance.getY();
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of rotate method, of class CVector2.
     */
    @Test
    public void testRotate() {
        System.out.println("rotate");
        double a = 0.0;
        CVector2 instance = new CVector2();
        instance.rotate(a);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of rotate_cpy method, of class CVector2.
     */
    @Test
    public void testRotate_cpy() {
        System.out.println("rotate_cpy");
        double a = 0.0;
        CVector2 instance = new CVector2();
        CVector2 expResult = null;
        CVector2 result = instance.rotate_cpy(a);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of invert method, of class CVector2.
     */
    @Test
    public void testInvert() {
        System.out.println("invert");
        CVector2 instance = new CVector2();
        instance.invert();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of invert_cpy method, of class CVector2.
     */
    @Test
    public void testInvert_cpy() {
        System.out.println("invert_cpy");
        CVector2 instance = new CVector2();
        CVector2 expResult = null;
        CVector2 result = instance.invert_cpy();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of resize method, of class CVector2.
     */
    @Test
    public void testResize() {
        System.out.println("resize");
        double l = 0.0;
        CVector2 instance = new CVector2();
        instance.resize(l);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of resize_cpy method, of class CVector2.
     */
    @Test
    public void testResize_cpy() {
        System.out.println("resize_cpy");
        double l = 0.0;
        CVector2 instance = new CVector2();
        CVector2 expResult = null;
        CVector2 result = instance.resize_cpy(l);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of cpy method, of class CVector2.
     */
    @Test
    public void testCpy() {
        System.out.println("cpy");
        CVector2 instance = new CVector2();
        CVector2 expResult = null;
        CVector2 result = instance.cpy();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDistanceTo method, of class CVector2.
     */
    @Test
    public void testGetDistanceTo() {
        System.out.println("getDistanceTo");
        CVector2 vec = null;
        CVector2 instance = new CVector2();
        double expResult = 0.0;
        double result = instance.getDistanceTo(vec);
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of equals method, of class CVector2.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");
        CVector2 c = null;
        CVector2 instance = new CVector2();
        boolean expResult = false;
        boolean result = instance.equals(c);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}