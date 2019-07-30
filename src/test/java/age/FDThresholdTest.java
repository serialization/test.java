package age;

import org.junit.Assert;
import org.junit.Test;

import age.OGFile;

import ogss.common.java.api.Mode;

/**
 * Tests the file reading capabilities.
 */
@SuppressWarnings("static-method")
public class FDThresholdTest extends common.CommonTest {

    @Test
    public void testEqual() throws Exception {
        OGFile sf = OGFile.open(tmpFile("make.sg"), Mode.Create, Mode.Write);

        // FD_Threshold = 1048576;
        final int count =  1048576;     
        for (int i = 0; i < count; i++) {
        	// create objects and set field
        	sf.Ages.make().setAge(42);
        }
        sf.close();

        { // read back and assert correctness
            OGFile sf2 = OGFile.open(sf.currentPath(), Mode.Read, Mode.ReadOnly);
            // check count per Type
            Assert.assertEquals(count, sf.Ages.staticSize());
            
            for (int i = 0; i < count; i++) {
            	// create objects from file
            	age.Age age = sf2.Ages.get(i+1);

            	// assert fields
            	Assert.assertEquals(42, age.getAge());
            }
            
        }
    }  
    
    @Test
    public void testSmaller() throws Exception {
        OGFile sf = OGFile.open(tmpFile("make.sg"), Mode.Create, Mode.Write);

        // FD_Threshold = 1048576;
        final int count =  1048575;     
        for (int i = 0; i < count; i++) {
        	// create objects and set field
        	sf.Ages.make().setAge(42);
        }
        sf.close();

        { // read back and assert correctness
            OGFile sf2 = OGFile.open(sf.currentPath(), Mode.Read, Mode.ReadOnly);
            // check count per Type
            Assert.assertEquals(count, sf.Ages.staticSize());
            
            for (int i = 0; i < count; i++) {
            	// create objects from file
            	age.Age age = sf2.Ages.get(i+1);

            	// assert fields
            	Assert.assertEquals(42, age.getAge());
            }
            
        }
    }  
    
    @Test
    public void testGreater() throws Exception {
        OGFile sf = OGFile.open(tmpFile("make.sg"), Mode.Create, Mode.Write);

        // FD_Threshold = 1048576;
        final int count =  1048577;     
        for (int i = 0; i < count; i++) {
        	// create objects and set field
        	sf.Ages.make().setAge(42);
        }
        sf.close();

        { // read back and assert correctness
            OGFile sf2 = OGFile.open(sf.currentPath(), Mode.Read, Mode.ReadOnly);
            // check count per Type
            Assert.assertEquals(count, sf.Ages.staticSize());
            
            for (int i = 0; i < count; i++) {
            	// create objects from file
            	age.Age age = sf2.Ages.get(i+1);

            	// assert fields
            	Assert.assertEquals(42, age.getAge());
            }
            
        }
    }  
}
