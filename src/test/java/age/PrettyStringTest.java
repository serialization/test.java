package age;

import org.junit.Assert;
import org.junit.Test;

import age.OGFile;
import ogss.common.java.api.Mode;
import ogss.common.java.internal.Obj;

/**
 * Tests for an obj's prettyString method.
 */
@SuppressWarnings("static-method")
public class PrettyStringTest extends common.CommonTest {

	
    @Test
    public void testKnownPrettyString() throws Exception {
        OGFile sg = OGFile.open(tmpFile("prettystring"), Mode.Create, Mode.Write);
        Age age = sg.Ages.make();
        age.prettyString(sg);
    }
    
    @Test
    public void testUnknownPrettyString() throws Exception {
        OGFile sg = OGFile.open(tmpFile("prettystring"), Mode.Create, Mode.Write);
        Age age = sg.Ages.make();
        sg.close();

        empty.OGFile sg1 = empty.OGFile.open(sg.currentPath(), Mode.Read);
        Obj o = sg1.allTypes().iterator().next().get(age.ID());
        Assert.assertNotNull(o);
        o.prettyString(sg1);        
    }
}
