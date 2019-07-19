package age;

import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Test;

import age.Age;
import age.OGFile;

import ogss.common.java.api.Mode;
import ogss.common.java.api.OGSSException;
import ogss.common.java.internal.DynamicDataIterator;
import ogss.common.java.internal.FieldIterator;
import ogss.common.java.internal.StaticDataIterator;
import ogss.common.java.internal.StaticFieldIterator;

/**
 * Tests with regard to the pool API. These tests do not cover methods that are package private and they do not cover
 * add and get because those methods got their own test cases. package.
 * 
 * @author Sarah Sophie Stieß
 */
@SuppressWarnings("static-method")
public class PoolAPITest extends common.CommonTest {

    /**
     * Tests HintNewObjectSize to ensure that nothing unexpected happens
     */
    @Test
    public void testHintNewObjectSize() throws Exception {
        OGFile sg = OGFile.open(tmpFile("poolapi"), Mode.Create, Mode.Write);
        sg.Ages.make();
        sg.Ages.make();
        sg.Ages.hintNewObjectsSize(0);
        sg.Ages.make();

        sg.flush();

        Assert.assertEquals(3, sg.Ages.size());
    }

    /**
     * Tests name
     */
    @Test
    public void testName() throws Exception {
        OGFile sg = OGFile.open(tmpFile("poolapi"), Mode.Create, Mode.Write);
        Assert.assertEquals("Age", sg.Ages.name());
        Assert.assertEquals("Age", sg.Ages.name);
    }

    /**
     * Tests owner
     */
    @Test
    public void testOwner() throws Exception {
        OGFile sg = OGFile.open(tmpFile("poolapi"), Mode.Create, Mode.Write);
        Assert.assertEquals(sg, sg.Ages.owner());
    }

    /**
     * Tests basepool
     */
    @Test
    public void testBase() throws Exception {
        OGFile sg = OGFile.open(tmpFile("poolapi"), Mode.Create, Mode.Write);
        Assert.assertEquals(sg.Ages, sg.Ages.basePool);
    }

    /**
     * Tests supertype warum ist superpool eigentlich sichtbar?
     */
    @Test
    public void testSuper() throws Exception {
        OGFile sg = OGFile.open(tmpFile("poolapi"), Mode.Create, Mode.Write);
        Assert.assertEquals(null, sg.Ages.superPool);
        Assert.assertEquals(null, sg.Ages.superType());
    }

    /**
     * Tests THH
     */
    @Test
    public void testTHH() throws Exception {
        OGFile sg = OGFile.open(tmpFile("poolapi"), Mode.Create, Mode.Write);
        Assert.assertEquals(0, sg.Ages.THH);
    }

    /**
     * Tests size. Age has no subtypes, thus size and and staticSize are equal
     */
    @Test
    public void testSize() throws Exception {
        OGFile sg = OGFile.open(tmpFile("poolapi"), Mode.Create, Mode.Write);
        Assert.assertEquals(0, sg.Ages.size());
        sg.Ages.make();
        Assert.assertEquals(1, sg.Ages.size());
        Assert.assertEquals(1, sg.Ages.staticSize());
    }

    /**
     * Tests toArray
     */
    @Test
    public void testToArray() throws Exception {
        OGFile sg = OGFile.open(tmpFile("poolapi"), Mode.Create, Mode.Write);
        Age a0 = sg.Ages.make();
        Age a1 = sg.Ages.make();
        sg.flush();

        Age[] ages = new Age[0];
        ages = sg.Ages.toArray(ages);

        Assert.assertEquals(2, ages.length);
        Assert.assertEquals(a0, ages[0]);
        Assert.assertEquals(a1, ages[1]);
    }

    /**
     * Tests stream
     */
    @Test
    public void testStream() throws Exception {
        OGFile sg = OGFile.open(tmpFile("poolapi"), Mode.Create, Mode.Write);
        Age a0 = sg.Ages.make();
        Age a1 = sg.Ages.make();
        sg.flush();

        Stream<Age> ages = sg.Ages.stream();
        Assert.assertEquals(2, ages.count());
        ages = sg.Ages.stream();
        ages.anyMatch((Age a) -> a.equals(a0));
        ages = sg.Ages.stream();
        ages.anyMatch((Age a) -> a.equals(a1));
    }

    /**
     * Tests field iterators. Age has no subtypes, thus both iterator iterate over the same fields
     */
    @Test
    public void testFields() throws Exception {
        OGFile sg = OGFile.open(tmpFile("poolapi"), Mode.Create, Mode.Write);

        FieldIterator fi = sg.Ages.allFields();
        StaticFieldIterator sfi = sg.Ages.fields();
        Assert.assertTrue(fi.hasNext());
        Assert.assertTrue(sfi.hasNext());
        Assert.assertEquals(fi.next(), sfi.next());
        Assert.assertFalse(fi.hasNext());
        Assert.assertFalse(sfi.hasNext());
    }

    /**
     * Tests instance iterators. Age has no subtypes, thus both iterator iterate over the same instances
     */
    @Test
    public void testInstances() throws Exception {
        OGFile sg = OGFile.open(tmpFile("poolapi"), Mode.Create, Mode.Write);
        sg.Ages.make();
        sg.flush();

        DynamicDataIterator<Age> ddi = sg.Ages.iterator();
        StaticDataIterator<Age> sdi = sg.Ages.staticInstances();
        Assert.assertTrue(ddi.hasNext());
        Assert.assertTrue(sdi.hasNext());
        Assert.assertEquals(ddi.next(), sdi.next());
        Assert.assertFalse(ddi.hasNext());
        Assert.assertFalse(sdi.hasNext());
    }

    /**
     * Ensure that allocation of unknown instances is possible.
     */
    @Test
    public void testUnknownMake() throws Exception {
        OGFile sg = OGFile.open(tmpFile("poolapi"), Mode.Create, Mode.Write);
        sg.close();
        empty.OGFile sge = empty.OGFile.open(sg.currentPath(), Mode.Read);

        Assert.assertNotNull(sge.pool("Age").make());

        Assert.assertEquals(1, sge.pool("Age").size());
    }
}
