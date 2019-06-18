package a;

import org.junit.Assert;
import org.junit.Test;

import ogss.common.java.api.Mode;
import ogss.common.java.api.OGSSException;

/**
 * Tests the file reading capabilities.
 */
@SuppressWarnings("static-method")
public class UnknownWriteEmptyTest extends common.CommonTest {

    
    /*
     * write a file, read it such that the type a and its fields are unknown / lazy.
     * do nothing, write it and read it again.
     */
    @Test
    public void nothing_nothingTest() throws Exception {  
        aWithFields.OGFile sg = aWithFields.OGFile.open(tmpFile("aaaaa"), Mode.Create, Mode.Write);
        sg.close();
        
        empty.OGFile sg1 = empty.OGFile.open(sg.currentPath(), Mode.Read, Mode.Write);
        Assert.assertTrue(sg1.allTypes().iterator().hasNext());
        Assert.assertEquals(0, sg1.allTypes().iterator().next().size());
        sg1.close();
        
        aWithFields.OGFile sg2 = aWithFields.OGFile.open(sg.currentPath(), Mode.Read, Mode.Write);
        Assert.assertEquals(0, sg2.As.size());
        sg1.close();
    }
        
    /*
     * make instance of A and write it to file.
     * read file such that the type a and its fields are unknown / lazy.
     * do nothing, write it and read it again.
     */
    @Test
    public void make_nothingTest() throws Exception {  
        aWithFields.OGFile sg = aWithFields.OGFile.open(tmpFile("aaaaa"), Mode.Create, Mode.Write);
        sg.As.make();
        sg.close();
        
        empty.OGFile sg1 = empty.OGFile.open(sg.currentPath(), Mode.Read, Mode.Write);
        Assert.assertTrue(sg1.allTypes().iterator().hasNext());
        Assert.assertEquals(1, sg1.allTypes().iterator().next().size());
        sg1.close();
        
        aWithFields.OGFile sg2 = aWithFields.OGFile.open(sg.currentPath(), Mode.Read, Mode.Write);
        Assert.assertEquals(1, sg2.As.size());
        sg1.close();
    }
    
    /*
     * write no instances to file.
     * read file such that the type a and its fields are unknown / lazy.
     * make instance of a, write it and read it again.
     */
    @Test (expected = OGSSException.class)
    public void nothing_makeTest() throws Exception {  
        aWithFields.OGFile sg = aWithFields.OGFile.open(tmpFile("aaaaa"), Mode.Create, Mode.Write);
        sg.close();
        
        empty.OGFile sg1 = empty.OGFile.open(sg.currentPath(), Mode.Read, Mode.Write);
        Assert.assertTrue(sg1.allTypes().iterator().hasNext());
        Assert.assertEquals(0, sg1.allTypes().iterator().next().size());
        sg1.allTypes().iterator().next().make();
        
        Assert.fail("make of unknown instances should cause an error");
//        
//        Assert.assertEquals(1, sg1.allTypes().iterator().next().size());
//        sg1.close();
//        
//        aWithFields.OGFile sg2 = aWithFields.OGFile.open(sg.currentPath(), Mode.Read, Mode.Write);
//        Assert.assertEquals(1, sg2.As.size());
//        sg1.close();
    }

    /*
     * make instance of A and write it to file.
     * read file such that the type a and its fields are unknown / lazy.
     * make instance of a, write it and read it again.
     */
    @Test (expected = OGSSException.class)
    public void make_makeTest() throws Exception {  
        aWithFields.OGFile sg = aWithFields.OGFile.open(tmpFile("aaaaa"), Mode.Create, Mode.Write);
        sg.As.make();
        sg.close();
        
        empty.OGFile sg1 = empty.OGFile.open(sg.currentPath(), Mode.Read, Mode.Write);
        Assert.assertTrue(sg1.allTypes().iterator().hasNext());
        Assert.assertEquals(1, sg1.allTypes().iterator().next().size());
        sg1.allTypes().iterator().next().make();
        
        Assert.fail("make of unknown instances should cause an error");
        
//        Assert.assertEquals(2, sg1.allTypes().iterator().next().size());
//        sg1.close();
//        
//        aWithFields.OGFile sg2 = aWithFields.OGFile.open(sg.currentPath(), Mode.Read, Mode.Write);
//        Assert.assertEquals(2, sg2.As.size());
//        sg1.close();
    }
}
