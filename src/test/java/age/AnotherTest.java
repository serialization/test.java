package age;

import org.junit.Assert;
import org.junit.Test;

import age.OGFile;

import ogss.common.java.api.Mode;
import ogss.common.java.api.OGSSException;

/**
 * Doing things one should probably not do.
 * Or I'm just trying to break it on purpose. 
 */
@SuppressWarnings("static-method")
public class AnotherTest extends common.CommonTest {
    
	/**
	 * this test adds an instance to a state twice.
	 * as this may causes problems later on, it should be forbidden.
	 */
	@Test(expected = OGSSException.class)
    public void test0() throws Exception {
        OGFile sg = OGFile.open(tmpFile("delete"), Mode.Create, Mode.Write);
        Age age0 = sg.Ages.make();
        sg.Ages.add(age0);
    }
	/**
	 * this test adds an instance to a state twice, and then deletes the instance.
	 * the delete causes problems, thus adding the element twice should be forbidden.
	 */
	@Test(expected = OGSSException.class)
    public void test1() throws Exception {
        OGFile sg = OGFile.open(tmpFile("delete"), Mode.Create, Mode.Write);
        
        Age age0 = sg.Ages.make();
        sg.Ages.add(age0);
        
        sg.flush();
        Assert.assertEquals(2, sg.Ages.size());
        
        sg.delete(age0);
        sg.flush();
    }
}
