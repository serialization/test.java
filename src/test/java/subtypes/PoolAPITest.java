package subtypes;

import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Test;

import subtypes.A;
import subtypes.B;
import subtypes.OGFile;

import ogss.common.java.api.Mode;
import ogss.common.java.api.OGSSException;
import ogss.common.java.internal.DynamicDataIterator;
import ogss.common.java.internal.FieldIterator;
import ogss.common.java.internal.StaticDataIterator;
import ogss.common.java.internal.StaticFieldIterator;

/**
 * Tests with regard to the pool API
 * 
 * These tests are the same as those in the age package but here we have
 * subtypes.
 * 
 * @author Sarah Sophie Stieß
 */
@SuppressWarnings("static-method")
public class PoolAPITest extends common.CommonTest {

    /**
     * Tests name
     */
    @Test
    public void testName() throws Exception {
        OGFile sg = OGFile.open(tmpFile("poolapi"), Mode.Create, Mode.Write);
        Assert.assertEquals("A", sg.As.name());
        Assert.assertEquals("A", sg.As.name);
        Assert.assertEquals("B", sg.Bs.name());
        Assert.assertEquals("B", sg.Bs.name);
    }

    /**
     * Tests owner
     */
    @Test
    public void testOwner() throws Exception {
        OGFile sg = OGFile.open(tmpFile("poolapi"), Mode.Create, Mode.Write);
        Assert.assertEquals(sg, sg.As.owner());
        Assert.assertEquals(sg, sg.Bs.owner());
    }

    /**
     * Tests
     */
    @Test
    public void testBase() throws Exception {
        OGFile sg = OGFile.open(tmpFile("poolapi"), Mode.Create, Mode.Write);
        Assert.assertEquals(sg.As, sg.As.basePool);
        Assert.assertEquals(sg.As, sg.Bs.basePool);
    }

    /**
     * Tests
     * 
     * warum ist superpool eigentlich sichtbar?
     */
    @Test
    public void testSuper() throws Exception {
        OGFile sg = OGFile.open(tmpFile("poolapi"), Mode.Create, Mode.Write);
        Assert.assertEquals(null, sg.As.superPool);
        Assert.assertEquals(null, sg.As.superType());
        Assert.assertEquals(sg.As, sg.Bs.superPool);
        Assert.assertEquals(sg.As, sg.Bs.superType());
    }

    /**
     * Tests THH
     */
    @Test
    public void testTHH() throws Exception {
        OGFile sg = OGFile.open(tmpFile("poolapi"), Mode.Create, Mode.Write);
        Assert.assertEquals(0, sg.As.THH);
        Assert.assertEquals(1, sg.Bs.THH);
    }

    /**
     * Tests size. A has subtypes, thus size and and staticSize may be different
     */
    @Test
    public void testSize() throws Exception {
        OGFile sg = OGFile.open(tmpFile("poolapi"), Mode.Create, Mode.Write);
        sg.As.make();
        Assert.assertEquals(1, sg.As.size());
        Assert.assertEquals(1, sg.As.staticSize());
        Assert.assertEquals(0, sg.Bs.size());
        Assert.assertEquals(0, sg.Bs.staticSize());

        sg.Bs.make();
        Assert.assertEquals(2, sg.As.size());
        Assert.assertEquals(1, sg.As.staticSize());
        Assert.assertEquals(1, sg.Bs.size());
        Assert.assertEquals(1, sg.Bs.staticSize());
    }

    /**
     * Tests toArray
     */
    @Test
    public void testToArray() throws Exception {
        OGFile sg = OGFile.open(tmpFile("poolapi"), Mode.Create, Mode.Write);
        A a0 = sg.As.make();
        B b0 = sg.Bs.make();
        sg.flush();

        A[] as = new A[0];
        as = sg.As.toArray(as);

        B[] bs = new B[0];
        bs = sg.Bs.toArray(bs);

        Assert.assertEquals(2, as.length);
        Assert.assertEquals(1, bs.length);
    }

    /**
     * Tests stream
     */
    @Test
    public void testStream() throws Exception {
        OGFile sg = OGFile.open(tmpFile("poolapi"), Mode.Create, Mode.Write);
        A a0 = sg.As.make();
        B b0 = sg.Bs.make();
        sg.flush();

        // stream of As
        Stream<A> as = sg.As.stream();
        Assert.assertEquals(2, as.count());
        as = sg.As.stream();
        as.anyMatch((A a) -> a.equals(a0));
        as = sg.As.stream();
        as.anyMatch((A a) -> a.equals(b0));

        // stream of Bs
        Stream<B> bs = sg.Bs.stream();
        Assert.assertEquals(1, bs.count());
        bs = sg.Bs.stream();
        bs.anyMatch((B b) -> b.equals(b0));

    }

    /**
     * Tests field iterators.
     * 
     */
    @Test
    public void testFields() throws Exception {
        OGFile sg = OGFile.open(tmpFile("poolapi"), Mode.Create, Mode.Write);

        // assert A's field
        FieldIterator fia = sg.As.allFields();
        StaticFieldIterator sfia = sg.As.fields();
        Assert.assertTrue(fia.hasNext());
        Assert.assertTrue(sfia.hasNext());
        Assert.assertEquals(fia.next(), sfia.next());
        Assert.assertFalse(fia.hasNext());
        Assert.assertFalse(sfia.hasNext());

        // assert B's field
        FieldIterator fib = sg.Bs.allFields();
        StaticFieldIterator sfib = sg.Bs.fields();
        Assert.assertTrue(fib.hasNext());
        Assert.assertTrue(sfib.hasNext());
        Assert.assertEquals(fib.next(), sfib.next());
        Assert.assertTrue(fib.hasNext());
        Assert.assertFalse(sfib.hasNext());

        // assert B's inherited field
        Assert.assertEquals(fib.next(), sg.As.fields().next());
        Assert.assertFalse(fib.hasNext());
    }

    /**
     * Tests instance iterators.
     */
    @Test
    public void testInstances() throws Exception {
        OGFile sg = OGFile.open(tmpFile("poolapi"), Mode.Create, Mode.Write);
        sg.As.make();
        sg.Bs.make();
        sg.flush();

        // assert A's instance
        DynamicDataIterator<A> ddia = sg.As.iterator();
        StaticDataIterator<A> sdia = sg.As.staticInstances();
        Assert.assertTrue(ddia.hasNext());
        Assert.assertTrue(sdia.hasNext());
        Assert.assertEquals(ddia.next(), sdia.next());
        Assert.assertTrue(ddia.hasNext());
        Assert.assertFalse(sdia.hasNext());

        // assert B's instance
        DynamicDataIterator<B> ddib = sg.Bs.iterator();
        StaticDataIterator<B> sdib = sg.Bs.staticInstances();
        Assert.assertTrue(ddib.hasNext());
        Assert.assertTrue(sdib.hasNext());
        Assert.assertEquals(ddib.next(), sdib.next());
        Assert.assertFalse(ddib.hasNext());
        Assert.assertFalse(sdib.hasNext());

        // assert A's instance of B
        Assert.assertEquals(ddia.next(), sg.Bs.staticInstances().next());
        Assert.assertFalse(ddia.hasNext());
    }
}
