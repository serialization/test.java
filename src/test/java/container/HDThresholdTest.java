package container;

import org.junit.Assert;
import org.junit.Test;

import container.OGFile;

import ogss.common.java.api.Mode;
import ogss.common.java.api.OGSSException;
import ogss.common.java.internal.HullType;

/**
 * Tests the file reading capabilities.
 */
@SuppressWarnings("static-method")
public class HDThresholdTest extends common.CommonTest {

    @Test
    public void testEqual() throws Exception {
        OGFile sf = OGFile.open(tmpFile("make.sg"), Mode.Create, Mode.Write);

        // HD_Threshold = 16384
        final int count =  16384;     
        for (int i = 0; i < count; i++) {
        	// create objects
        	container.Container cont = sf.Containers.make();
        	
        	// set fields
        	cont.setL(list(0L));        	
        }
        sf.close();

        { // read back and assert correctness
            OGFile sf2 = OGFile.open(sf.currentPath(), Mode.Read, Mode.ReadOnly);
            // check count per Type
            Assert.assertEquals(count, sf.Containers.staticSize());
            
            for (int i = 0; i < count; i++) {
            	// create objects from file
            	container.Container cont_2 = sf2.Containers.get(i+1);

            	// assert fields
            	Assert.assertTrue(cont_2.getL() != null && cont_2.getL().equals(list(0L)));
            }
            
        }
    }

    @Test
    public void testGreater() throws Exception {
        OGFile sf = OGFile.open(tmpFile("make.sg"), Mode.Create, Mode.Write);

        // HD_Threshold = 16384
        final int count =  16385;     
        for (int i = 0; i < count; i++) {
        	// create objects
        	container.Container cont = sf.Containers.make();
        	
        	// set fields
        	cont.setL(list(0L));        	
        }
        sf.close();

        { // read back and assert correctness
            OGFile sf2 = OGFile.open(sf.currentPath(), Mode.Read, Mode.ReadOnly);
            // check count per Type
            Assert.assertEquals(count, sf.Containers.staticSize());
            
            for (int i = 0; i < count; i++) {
            	// create objects from file
            	container.Container cont_2 = sf2.Containers.get(i+1);

            	// assert fields
            	Assert.assertTrue(cont_2.getL() != null && cont_2.getL().equals(list(0L)));
            }
            
        }
    }
    
    
    @Test
    public void testSmaller() throws Exception {
        OGFile sf = OGFile.open(tmpFile("make.sg"), Mode.Create, Mode.Write);

        // HD_Threshold = 16384
        final int count =  16383;     
        for (int i = 0; i < count; i++) {
        	// create objects
        	container.Container cont = sf.Containers.make();
        	
        	// set fields
        	cont.setL(list(0L));        	
        }
        sf.close();

        { // read back and assert correctness
            OGFile sf2 = OGFile.open(sf.currentPath(), Mode.Read, Mode.ReadOnly);
            // check count per Type
            Assert.assertEquals(count, sf.Containers.staticSize());
            
            for (int i = 0; i < count; i++) {
            	// create objects from file
            	container.Container cont_2 = sf2.Containers.get(i+1);

            	// assert fields
            	Assert.assertTrue(cont_2.getL() != null && cont_2.getL().equals(list(0L)));
            }
            
        }
    }
    
    
    @Test
    public void testEvenSmaller() throws Exception {
        OGFile sf = OGFile.open(tmpFile("make.sg"), Mode.Create, Mode.Write);

        // HD_Threshold = 16384
        final int count =  16382;     
        for (int i = 0; i < count; i++) {
        	// create objects
        	container.Container cont = sf.Containers.make();
        	
        	// set fields
        	cont.setL(list(0L));        	
        }
        sf.close();

        { // read back and assert correctness
            OGFile sf2 = OGFile.open(sf.currentPath(), Mode.Read, Mode.ReadOnly);
            // check count per Type
            Assert.assertEquals(count, sf.Containers.staticSize());
            
            for (int i = 0; i < count; i++) {
            	// create objects from file
            	container.Container cont_2 = sf2.Containers.get(i+1);

            	// assert fields
            	Assert.assertTrue(cont_2.getL() != null && cont_2.getL().equals(list(0L)));
            }
            
        }
    }
}
