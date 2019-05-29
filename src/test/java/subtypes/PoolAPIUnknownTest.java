package subtypes;

import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Test;

import subtypes.A;
import subtypes.B;
import subtypes.OGFile;
import ogss.common.java.api.Access;
import ogss.common.java.api.Mode;
import ogss.common.java.api.OGSSException;
import ogss.common.java.internal.DynamicDataIterator;
import ogss.common.java.internal.FieldIterator;
import ogss.common.java.internal.Obj;
import ogss.common.java.internal.Pool;
import ogss.common.java.internal.StaticDataIterator;
import ogss.common.java.internal.StaticFieldIterator;

/**
 * Tests with regard to the pool API
 * 
 * These tests are the same as those in the age package but here we have
 * subtypes. These test read the files such that all types are unknown types.
 * 
 * Many of these test rely on the state's pool method to access the pools and
 * thus require pool to work correctly.
 * 
 * @author Sarah Sophie Stieß
 */
@SuppressWarnings("static-method")
public class PoolAPIUnknownTest extends common.CommonTest {

    /**
     * Each name is there exactly once.
     */
    @Test
    public void testName() throws Exception {
        OGFile sg = OGFile.open(tmpFile("poolapi"), Mode.Create, Mode.Write);
        sg.close();

        String[] ns = { "A", "B", "D", "C" };
        HashSet<String> names = new HashSet<String>(Arrays.asList(ns));

        empty.OGFile sg1 = empty.OGFile.open(sg.currentPath(), Mode.Read);
        for (Access<? extends Obj> t : sg.allTypes()) {
            Assert.assertTrue(names.contains(t.name()));
            names.remove(t.name());
        }
        Assert.assertTrue(names.isEmpty());
    }

    /**
     * Tests owner
     */
    @Test
    public void testOwner() throws Exception {
        OGFile sg = OGFile.open(tmpFile("poolapi"), Mode.Create, Mode.Write);
        sg.close();

        empty.OGFile sg1 = empty.OGFile.open(sg.currentPath(), Mode.Read);
        for (Access<? extends Obj> t : sg1.allTypes()) {
            Assert.assertEquals(sg1, t.owner());
        }
    }

    /**
     * Tests basepool
     */
    @Test
    public void testBase() throws Exception {
        OGFile sg = OGFile.open(tmpFile("poolapi"), Mode.Create, Mode.Write);
        sg.close();

        empty.OGFile sg1 = empty.OGFile.open(sg.currentPath(), Mode.Read);

        for (Access<? extends Obj> t : sg.allTypes()) {
            Pool<Obj> p = (Pool<Obj>) t;
            Assert.assertEquals(sg1.pool("A"), p.basePool);
        }
    }

    /**
     * Tests superpool/-type
     * 
     */
    @Test
    public void testSuper() throws Exception {
        OGFile sg = OGFile.open(tmpFile("poolapi"), Mode.Create, Mode.Write);
        sg.close();

        empty.OGFile sg1 = empty.OGFile.open(sg.currentPath(), Mode.Read);

        Assert.assertEquals(null, sg1.pool("A").superPool);
        Assert.assertEquals(null, sg1.pool("A").superType());

        Assert.assertEquals(sg1.pool("A"), sg1.pool("B").superPool);
        Assert.assertEquals(sg1.pool("A"), sg1.pool("B").superType());

        Assert.assertEquals(sg1.pool("A"), sg1.pool("C").superPool);
        Assert.assertEquals(sg1.pool("A"), sg1.pool("C").superType());

        Assert.assertEquals(sg1.pool("B"), sg1.pool("D").superPool);
        Assert.assertEquals(sg1.pool("B"), sg1.pool("D").superType());
    }

    /**
     * Tests THH
     */
    @Test
    public void testTHH() throws Exception {
        OGFile sg = OGFile.open(tmpFile("poolapi"), Mode.Create, Mode.Write);
        sg.close();

        empty.OGFile sg1 = empty.OGFile.open(sg.currentPath(), Mode.Read);

        Assert.assertEquals(0, sg1.pool("A").THH);

        Assert.assertEquals(1, sg1.pool("B").THH);

        Assert.assertEquals(1, sg1.pool("C").THH);

        Assert.assertEquals(2, sg1.pool("D").THH);
    }

    /**
     * Tests size. A has subtypes, thus size and and staticSize may be different
     */
    @Test
    public void testSize() throws Exception {
        OGFile sg = OGFile.open(tmpFile("poolapi"), Mode.Create, Mode.Write);
        sg.flush();
        {
            empty.OGFile sg1 = empty.OGFile.open(sg.currentPath(), Mode.Read);

            Assert.assertEquals(0, sg1.pool("A").size());
            Assert.assertEquals(0, sg1.pool("B").size());
            Assert.assertEquals(0, sg1.pool("C").size());
            Assert.assertEquals(0, sg1.pool("D").size());

            Assert.assertEquals(0, sg1.pool("A").staticSize());
            Assert.assertEquals(0, sg1.pool("B").staticSize());
            Assert.assertEquals(0, sg1.pool("C").staticSize());
            Assert.assertEquals(0, sg1.pool("D").staticSize());
        }
        // make an A
        sg.As.make();
        sg.flush();
        {
            empty.OGFile sg1 = empty.OGFile.open(sg.currentPath(), Mode.Read);

            Assert.assertEquals(1, sg1.pool("A").size());
            Assert.assertEquals(0, sg1.pool("B").size());
            Assert.assertEquals(0, sg1.pool("C").size());
            Assert.assertEquals(0, sg1.pool("D").size());

            Assert.assertEquals(1, sg1.pool("A").staticSize());
            Assert.assertEquals(0, sg1.pool("B").staticSize());
            Assert.assertEquals(0, sg1.pool("C").staticSize());
            Assert.assertEquals(0, sg1.pool("D").staticSize());
        }
        // make a B
        sg.Bs.make();
        sg.flush();
        {
            empty.OGFile sg1 = empty.OGFile.open(sg.currentPath(), Mode.Read);

            Assert.assertEquals(2, sg1.pool("A").size());
            Assert.assertEquals(1, sg1.pool("B").size());
            Assert.assertEquals(0, sg1.pool("C").size());
            Assert.assertEquals(0, sg1.pool("D").size());

            Assert.assertEquals(1, sg1.pool("A").staticSize());
            Assert.assertEquals(1, sg1.pool("B").staticSize());
            Assert.assertEquals(0, sg1.pool("C").staticSize());
            Assert.assertEquals(0, sg1.pool("D").staticSize());
        }
        // make a C
        sg.Cs.make();
        sg.flush();
        {
            empty.OGFile sg1 = empty.OGFile.open(sg.currentPath(), Mode.Read);

            Assert.assertEquals(3, sg1.pool("A").size());
            Assert.assertEquals(1, sg1.pool("B").size());
            Assert.assertEquals(1, sg1.pool("C").size());
            Assert.assertEquals(0, sg1.pool("D").size());

            Assert.assertEquals(1, sg1.pool("A").staticSize());
            Assert.assertEquals(1, sg1.pool("B").staticSize());
            Assert.assertEquals(1, sg1.pool("C").staticSize());
            Assert.assertEquals(0, sg1.pool("D").staticSize());
        }
        // make a D
        sg.Ds.make();
        sg.flush();
        {
            empty.OGFile sg1 = empty.OGFile.open(sg.currentPath(), Mode.Read);

            Assert.assertEquals(4, sg1.pool("A").size());
            Assert.assertEquals(2, sg1.pool("B").size());
            Assert.assertEquals(1, sg1.pool("C").size());
            Assert.assertEquals(1, sg1.pool("D").size());

            Assert.assertEquals(1, sg1.pool("A").staticSize());
            Assert.assertEquals(1, sg1.pool("B").staticSize());
            Assert.assertEquals(1, sg1.pool("C").staticSize());
            Assert.assertEquals(1, sg1.pool("D").staticSize());
        }
    }

    /**
     * Tests toArray
     */
    @Test
    public void testToArray() throws Exception {
        OGFile sg = OGFile.open(tmpFile("poolapi"), Mode.Create, Mode.Write);
        sg.As.make();
        sg.Bs.make();
        sg.Cs.make();
        sg.Ds.make();
        sg.flush();

        empty.OGFile sg1 = empty.OGFile.open(sg.currentPath(), Mode.Read);
        Obj[] as = new Obj[0];
        as = ((Pool<Obj>) sg1.pool("A")).toArray(as);
        Assert.assertEquals(4, as.length);

        Obj[] bs = new Obj[0];
        bs = ((Pool<Obj>) sg1.pool("B")).toArray(bs);
        Assert.assertEquals(2, bs.length);

        Obj[] cs = new Obj[0];
        cs = ((Pool<Obj>) sg1.pool("C")).toArray(cs);
        Assert.assertEquals(1, cs.length);

        Obj[] ds = new Obj[0];
        ds = ((Pool<Obj>) sg1.pool("D")).toArray(ds);
        Assert.assertEquals(1, ds.length);

    }

    /**
     * Tests stream
     */
    @Test
    public void testStream() throws Exception {
        OGFile sg = OGFile.open(tmpFile("poolapi"), Mode.Create, Mode.Write);
        sg.As.make();
        sg.Bs.make();
        sg.Cs.make();
        sg.Ds.make();
        sg.close();

        empty.OGFile sg1 = empty.OGFile.open(sg.currentPath(), Mode.Read);

        Stream<Obj> as = ((Pool<Obj>) sg1.pool("A")).stream();
        Assert.assertEquals(4, as.count());

        Stream<Obj> bs = ((Pool<Obj>) sg1.pool("B")).stream();
        Assert.assertEquals(2, bs.count());

        Stream<Obj> cs = ((Pool<Obj>) sg1.pool("C")).stream();
        Assert.assertEquals(1, cs.count());

        Stream<Obj> ds = ((Pool<Obj>) sg1.pool("D")).stream();
        Assert.assertEquals(1, ds.count());
    }

    /**
     * Tests field iterators.
     */
    @Test
    public void testFields() throws Exception {
        OGFile sg = OGFile.open(tmpFile("poolapi"), Mode.Create, Mode.Write);
        sg.close();

        empty.OGFile sg1 = empty.OGFile.open(sg.currentPath(), Mode.Read);

        // assert A's field
        FieldIterator fia = sg1.pool("A").allFields();
        StaticFieldIterator sfia = sg1.pool("A").fields();
        Assert.assertTrue(fia.hasNext());
        Assert.assertTrue(sfia.hasNext());
        Assert.assertEquals(fia.next(), sfia.next());
        Assert.assertFalse(fia.hasNext());
        Assert.assertFalse(sfia.hasNext());

        // assert B's field
        FieldIterator fib = sg1.pool("B").allFields();
        StaticFieldIterator sfib = sg1.pool("B").fields();
        Assert.assertTrue(fib.hasNext());
        Assert.assertTrue(sfib.hasNext());
        Assert.assertEquals(fib.next(), sfib.next());
        Assert.assertTrue(fib.hasNext());
        Assert.assertFalse(sfib.hasNext());

        // assert B's inherited field
        Assert.assertEquals(fib.next(), sg1.pool("A").fields().next());
        Assert.assertFalse(fib.hasNext());

        // assert C's field
        FieldIterator fic = sg1.pool("C").allFields();
        StaticFieldIterator sfic = sg1.pool("C").fields();
        Assert.assertTrue(fic.hasNext());
        Assert.assertTrue(sfic.hasNext());
        Assert.assertEquals(fic.next(), sfic.next());
        Assert.assertTrue(fic.hasNext());
        Assert.assertFalse(sfic.hasNext());

        // assert C's inherited field
        Assert.assertEquals(fic.next(), sg1.pool("A").fields().next());
        Assert.assertFalse(fic.hasNext());

        // assert D's field
        FieldIterator fid = sg1.pool("D").allFields();
        StaticFieldIterator sfid = sg1.pool("D").fields();
        Assert.assertTrue(fid.hasNext());
        Assert.assertTrue(sfid.hasNext());
        Assert.assertEquals(fid.next(), sfid.next());
        Assert.assertTrue(fid.hasNext());
        Assert.assertFalse(sfid.hasNext());

        // assert D's inherited field from B
        Assert.assertEquals(fid.next(), sg1.pool("B").fields().next());
        Assert.assertTrue(fid.hasNext());

        // assert D's inherited field from A
        Assert.assertEquals(fid.next(), sg1.pool("A").fields().next());
        Assert.assertFalse(fid.hasNext());
    }

    /**
     * Tests instance iterators.
     */
    @Test
    public void testInstances() throws Exception {
        OGFile sg = OGFile.open(tmpFile("poolapi"), Mode.Create, Mode.Write);
        sg.As.make();
        sg.Bs.make();
        sg.Cs.make();
        sg.Ds.make();
        sg.close();

        empty.OGFile sg1 = empty.OGFile.open(sg.currentPath(), Mode.Read);

        // assert A's instance
        DynamicDataIterator<Obj> ddia = ((Pool<Obj>) sg1.pool("A")).iterator();
        StaticDataIterator<Obj> sdia = ((Pool<Obj>) sg1.pool("A")).staticInstances();
        Assert.assertTrue(ddia.hasNext());
        Assert.assertTrue(sdia.hasNext());
        Assert.assertEquals(ddia.next(), sdia.next());
        Assert.assertTrue(ddia.hasNext());
        Assert.assertFalse(sdia.hasNext());

        // assert B's instance
        DynamicDataIterator<Obj> ddib = ((Pool<Obj>) sg1.pool("B")).iterator();
        StaticDataIterator<Obj> sdib = ((Pool<Obj>) sg1.pool("B")).staticInstances();
        Assert.assertTrue(ddib.hasNext());
        Assert.assertTrue(sdib.hasNext());
        Assert.assertEquals(ddib.next(), sdib.next());
        Assert.assertTrue(ddib.hasNext());
        Assert.assertFalse(sdib.hasNext());

        // assert C's instance
        DynamicDataIterator<Obj> ddic = ((Pool<Obj>) sg1.pool("C")).iterator();
        StaticDataIterator<Obj> sdic = ((Pool<Obj>) sg1.pool("C")).staticInstances();
        Assert.assertTrue(ddic.hasNext());
        Assert.assertTrue(sdic.hasNext());
        Assert.assertEquals(ddic.next(), sdic.next());
        Assert.assertFalse(ddic.hasNext());
        Assert.assertFalse(sdic.hasNext());

        // assert D's instance
        DynamicDataIterator<Obj> ddid = ((Pool<Obj>) sg1.pool("D")).iterator();
        StaticDataIterator<Obj> sdid = ((Pool<Obj>) sg1.pool("D")).staticInstances();
        Assert.assertTrue(ddid.hasNext());
        Assert.assertTrue(sdid.hasNext());
        Assert.assertEquals(ddid.next(), sdid.next());
        Assert.assertFalse(ddid.hasNext());
        Assert.assertFalse(sdid.hasNext());

        // assert A's instance of B
        Assert.assertEquals(ddia.next(), ((Pool<Obj>) sg1.pool("B")).staticInstances().next());
        Assert.assertTrue(ddia.hasNext());

        // assert A's instance of D
        Assert.assertEquals(ddia.next(), ((Pool<Obj>) sg1.pool("D")).staticInstances().next());
        Assert.assertTrue(ddia.hasNext());

        // assert A's instance of C
        Assert.assertEquals(ddia.next(), ((Pool<Obj>) sg1.pool("C")).staticInstances().next());
        Assert.assertFalse(ddia.hasNext());

        // assert B's instance of D
        Assert.assertEquals(ddib.next(), ((Pool<Obj>) sg1.pool("D")).staticInstances().next());
        Assert.assertFalse(ddib.hasNext());

    }
}
