package basicTypes;

import org.junit.Assert;
import org.junit.Test;

import basicTypes.OGFile;
import ogss.common.java.api.Mode;
import ogss.common.java.internal.DynamicDataIterator;

/**
 * Ensure that SeqParser can read files with dropped blocks.
 * 
 * @author Sarah Stieß, Timm Felden
 */
@SuppressWarnings("static-method")
public class SimpleTest extends common.CommonTest {

    /**
     * make 2^20 + 1 instances of T and set b for all but the last to false. write file, read it back and ensure that
     * exactly on b is of value true.
     */
    @Test
    public void manyInstancesTest() throws Exception {
        OGFile sg = OGFile.open(tmpFile("make"), Mode.Create, Mode.Write);

        // 2^20 = 1048576
        for (int i = 0; i < 1048576; i++)
            sg.BasicBools.make().setBasicBool(false);
        sg.BasicBools.make().setBasicBool(true);
        sg.close();

        {
            OGFile sg1 = OGFile.open(sg.currentPath(), Mode.ReadOnly);

            DynamicDataIterator<BasicBool> it = sg1.BasicBools.iterator();

            Assert.assertEquals(1048577, sg1.BasicBools.size());

            int f = 0;
            while (it.hasNext()) {
                BasicBool t = it.next();
                if (t.getBasicBool())
                    f++;
            }
            Assert.assertEquals(1, f);

        }
    }

    /**
     * read 2^20 + 1 instances from genbinary.
     */
    @Test
    public void readManyInstancesTest() throws Exception {
        try (OGFile sg = OGFile.open("../../src/test/resources/binarygen/[[empty]]/accept/manyBools.sg", Mode.Read, Mode.ReadOnly)) {

            DynamicDataIterator<BasicBool> it = sg.BasicBools.iterator();

            Assert.assertEquals(1048577, sg.BasicBools.size());

            int f = 0;
            while (it.hasNext()) {
                BasicBool t = it.next();
                if (t.getBasicBool())
                    f++;
            }
            Assert.assertEquals(1, f);

        }
    }

}
