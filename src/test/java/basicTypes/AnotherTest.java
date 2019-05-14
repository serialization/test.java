package basicTypes;

import java.io.File;

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
		String s;
		{
			// make very long string 
			StringBuilder sb = new StringBuilder((int) Math.pow(2, 27));
			for (int i = 0; i < (int) Math.pow(2, 27); i++)
				sb.append('a');
			Assert.assertEquals((int) Math.pow(2, 27), sb.toString().length());
			s = sb.toString();
		}

		OGFile sg = OGFile.open(tmpFile("border"), Mode.Create, Mode.Write);
		sg.BasicStrings.make().setBasicString(s);
		sg.close();

		OGFile sg1 = OGFile.open(sg.currentPath(), Mode.Read);

		Assert.assertEquals(1, sg1.BasicStrings.size());
		Assert.assertEquals(s.toString(), sg1.BasicStrings.get(1).getBasicString());
	}

	/**
	 * bools are saved as bitvectors. thus a file with one bool and a file with
	 * eight bools have the same length whereas a file with nine bools is larger.
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
		Assert.assertEquals(f.length()+1, f1.length());
	}
}
