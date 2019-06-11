package age;

import org.junit.Assert;
import org.junit.Test;

import ogss.common.java.api.Mode;

/**
 * Tests for a pool's get method
 * 
 * @author Sarah Stieß, Timm Felden
 */
@SuppressWarnings("static-method")
public class PoolGetTest extends common.CommonTest {
     
    /**
     * Test get on a not flushed state with one instance.
     * 
     * Assert Exception or enforce flush? Or add a comment that get is not possible
     * on newly created, unflushed states.
     */
    @Test
    public void testGetUnflushed0() throws Exception {
        OGFile sg = OGFile.open(tmpFile("poolapi"), Mode.Create, Mode.Write);
        Age a  = sg.Ages.make();

        // @note one cannot use get before flush
        Assert.assertNull(sg.Ages.get(a.ID()));
        
        Assert.assertNull(sg.Ages.get(-1));
        Assert.assertNull(sg.Ages.get(0));
        Assert.assertNull(sg.Ages.get(sg.Ages.size() + 1));
    }

    /**
     * Test get on a flushed state with one instance.
     */
    @Test
    public void testGetFlushed0() throws Exception {
        OGFile sg = OGFile.open(tmpFile("poolapi"), Mode.Create, Mode.Write);
        Age a = sg.Ages.make();
        sg.flush();

        Assert.assertEquals(a, sg.Ages.get(a.ID()));
        
        Assert.assertNull(sg.Ages.get(-1));
        Assert.assertNull(sg.Ages.get(0));
        Assert.assertNull(sg.Ages.get(sg.Ages.size() + 1));
    }

    /**
     * Test get on a not flushed state without instances.
     * 
     * Assert Exception or enforce flush? Or add a comment that get is not possible
     * on newly created, unflushed states.
     */
    @Test
    public void testGetUnflushed1() throws Exception {
        OGFile sg = OGFile.open(tmpFile("poolapi"), Mode.Create, Mode.Write);

        Assert.assertNull(sg.Ages.get(-1));
        Assert.assertNull(sg.Ages.get(0));
        Assert.assertNull(sg.Ages.get(sg.Ages.size() + 1));
    }

    /**
     * Test get on a flushed state without instances.
     */
    @Test
    public void testGetFLushed1() throws Exception {
        OGFile sg = OGFile.open(tmpFile("poolapi"), Mode.Create, Mode.Write);
        sg.flush();

        Assert.assertNull(sg.Ages.get(-1));
        Assert.assertNull(sg.Ages.get(0));
        Assert.assertNull(sg.Ages.get(sg.Ages.size() + 1));
    }

}
