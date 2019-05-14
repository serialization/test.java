package age;

import org.junit.Assert;
import org.junit.Test;

import age.OGFile;
import ogss.common.java.api.Mode;

/**
 * TODO 
 */
@SuppressWarnings("static-method")
public class ChangeModeTest extends common.CommonTest {
    
	/**
	 * Changing the state after flush is allowed
	 */
	@Test
    public void testChangeStateAfterFlush() throws Exception {
        OGFile sg = OGFile.open(tmpFile("mode"), Mode.Create, Mode.Write);
        sg.flush();
        sg.Ages.make();
        sg.close();
        
        OGFile sg1 = OGFile.open(sg.currentPath(), Mode.Read);
        Assert.assertEquals(1, sg1.Ages.size());
    }
	
	@Test (expected = ogss.common.java.api.OGSSException.class)
    public void testCloseAndFlush() throws Exception {
        OGFile sg = OGFile.open(tmpFile("mode"), Mode.Create, Mode.Write);
        sg.close();
        sg.flush();
	}

	/**
	 * Changing the mode to writable after close is forbidden
	 */
	@Test (expected = IllegalArgumentException.class)
    public void testChangeModeAfterClose() throws Exception {
        OGFile sg = OGFile.open(tmpFile("mode"), Mode.Create, Mode.Write);
        sg.close();      
        sg.changeMode(Mode.Write);
	}
	
	/**
	 * Changing the mode to writable after close is forbidden
	 */
	@Test (expected = IllegalArgumentException.class)
    public void testChangeModeAfterClose1() throws Exception {
        OGFile sg = OGFile.open(tmpFile("mode"), Mode.Create, Mode.Write);
        sg.close();      
        sg.changeMode(Mode.Read);
	}
}
