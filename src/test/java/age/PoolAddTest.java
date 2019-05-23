package age;

import org.junit.Assert;
import org.junit.Test;

import age.Age;
import age.OGFile;

import ogss.common.java.api.Mode;
import ogss.common.java.api.OGSSException;
import ogss.common.java.internal.StaticDataIterator;

/**
 * Tests for a pool's add method
 */
@SuppressWarnings("static-method")
public class PoolAddTest extends common.CommonTest {

    /**
     * do not add an instance that already belongs to that state. 
     */
    @Test(expected = OGSSException.class)
    public void testAddTwice0() throws Exception {
        OGFile sg = OGFile.open(tmpFile("add"), Mode.Create, Mode.Write);
        Age age0 = sg.Ages.make();
        sg.Ages.add(age0);        
        // should not be able to pass over this point
        Assert.fail("adding same instance twice should be forbidden");
    }
    
    /**
     * do not add an instance that already belongs to that state.
     * test again because flush changes the id
     */
    @Test(expected = OGSSException.class)
    public void testAddTwice1() throws Exception {
        OGFile sg = OGFile.open(tmpFile("add"), Mode.Create, Mode.Write);
        Age age0 = sg.Ages.make();
        sg.flush();
        sg.Ages.add(age0);        
        // should not be able to pass over this point
        Assert.fail("adding same instance twice should be forbidden");
    }

    /**
     * adding formerly deleted instance is allowed
     */
    @Test
    public void testAddDeleted0() throws Exception {
        OGFile sg = OGFile.open(tmpFile("add"), Mode.Create, Mode.Write);
        
        Age age0 = sg.Ages.make();
        Assert.assertEquals(1, sg.Ages.size());
        
        sg.delete(age0);
        sg.flush(); 
        Assert.assertEquals(0, sg.Ages.size());
        
        sg.Ages.add(age0);
        Assert.assertEquals(1, sg.Ages.size());
        
        sg.flush();
    }
    
    /**
     * adding formerly deleted instance is allowed cross state
     */
    @Test
    public void testAddDeleted1() throws Exception {
        OGFile sg = OGFile.open(tmpFile("add"), Mode.Create, Mode.Write);
        OGFile sg1 = OGFile.open(tmpFile("add"), Mode.Create, Mode.Write);
        
        Age age0 = sg.Ages.make();
        Assert.assertEquals(1, sg.Ages.size());
        
        sg.delete(age0);
        sg.flush(); 
        Assert.assertEquals(0, sg.Ages.size());
        
        sg1.Ages.add(age0);
        Assert.assertEquals(1, sg1.Ages.size());
        
        sg1.flush();
    }
    
    /**
     * just don't add null. 
     */
    @Test(expected = NullPointerException.class)
    public void test2() throws Exception {
        OGFile sg = OGFile.open(tmpFile("add"), Mode.Create, Mode.Write);
        sg.Ages.add(null);
    }
}
