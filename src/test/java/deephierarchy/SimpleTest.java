package deephierarchy;

import org.junit.Assert;
import org.junit.Test;

import deephierarchy.OGFile;
import ogss.common.java.api.Access;
import ogss.common.java.api.Mode;
import ogss.common.java.internal.Obj;

/**
 * This is the deepest hierarchy possible, because of some array of length 32 in
 * the Creator
 */
@SuppressWarnings("static-method")
public class SimpleTest extends common.CommonTest {
	@Test
	public void manyInstancesTest() throws Exception {
		OGFile sg0 = OGFile.open(tmpFile("hierarchy"), Mode.Create, Mode.Write);
		for (Access<? extends Obj> t : sg0.allTypes()) {
			t.make();
		}
		sg0.close();

		OGFile sg = OGFile.open(sg0.currentPath(), Mode.Read);

		int ts = 31;
		for (Access<? extends Obj> t : sg.allTypes()) {
			Assert.assertEquals(ts, t.size());
			ts--;
		}
		Assert.assertEquals(0, ts);
	}
}
