package basicTypes;

import java.io.File;
import java.nio.file.Path;

import org.junit.Assert;
import org.junit.Test;

import basicTypes.OGFile;
import ogss.common.java.api.Mode;

/**
 * Tests that did not belong anywhere else.
 */
@SuppressWarnings("static-method")
public class AnotherTest extends common.CommonTest {

    /**
     * test that a state can handle very long strings (a string of length 2^27) as well.
     */
    @Test
    public void testString() throws Exception {
        final String s;
        {
            // make very long string
            StringBuilder sb = new StringBuilder((int) Math.pow(2, 27));
            for (int i = 0; i < (int) Math.pow(2, 23); i++)
                sb.append("aaaaaaaaaaaaaaaa");
            Assert.assertEquals((int) Math.pow(2, 27), sb.toString().length());
            s = sb.toString();
        }

        final Path currentPath;
        try (OGFile sg = OGFile.open(tmpFile("border"), Mode.Create, Mode.Write)) {
            currentPath = sg.currentPath();
            sg.BasicStrings.make().setBasicString(s);
        }

        try (OGFile sg = OGFile.open(currentPath, Mode.Read)) {

            Assert.assertEquals(1, sg.BasicStrings.size());
            Assert.assertEquals(s, sg.BasicStrings.get(1).getBasicString());
        }
    }

    /**
     * bools are saved as bitvectors. thus a file with one bool and a file with eight bools have the same length whereas
     * a file with nine bools is larger.
     */
    @Test
    public void testBool() throws Exception {
        OGFile sg = OGFile.open(tmpFile("border"), Mode.Create, Mode.Write);
        sg.BasicBools.make().setBasicBool(true);
        sg.flush();

        OGFile sg1 = OGFile.open(tmpFile("border"), Mode.Create, Mode.Write);
        for (int i = 0; i < 8; i++)
            sg1.BasicBools.make().setBasicBool(true);
        sg1.flush();

        File f = new File(sg.currentPath().toUri());
        File f1 = new File(sg1.currentPath().toUri());

        Assert.assertEquals(f.length(), f1.length());

        sg1.BasicBools.make().setBasicBool(true);
        sg1.flush();
        f1 = new File(sg1.currentPath().toUri());
        Assert.assertEquals(f.length() + 1, f1.length());
    }
}
