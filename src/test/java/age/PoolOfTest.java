package age;

import org.junit.Assert;
import org.junit.Test;

import age.OGFile;
import ogss.common.java.api.Mode;

/**
 * Test for a state's pool method
 */
@SuppressWarnings("static-method")
public class PoolOfTest extends common.CommonTest {

	/**
	 * if argument is null there is no corresponding pool
	 */
	@Test
	public void testPoolOfNull() throws Exception {
		OGFile sg = OGFile.open(tmpFile("contains"), Mode.Create, Mode.Write);
		Age age = null;
		Assert.assertEquals(null, sg.pool(age));

		String name = null;
		Assert.assertEquals(null, sg.pool(name));
	}

	/**
	 * should be case sensitive
	 */
	@Test
	public void testPoolOfCaseSensivity() throws Exception {
		OGFile sg = OGFile.open(tmpFile("pool"), Mode.Create, Mode.Write);
		Assert.assertEquals(sg.Ages, sg.pool("Age"));
		Assert.assertEquals(null, sg.pool("aGe"));
	}

	/**
	 * Not sure...? 
	 * But as state sg1 does not manage age, it should also not know to
	 * which pool age belongs
	 */
	@Test
	public void testPoolOfForeignObj() throws Exception {
		OGFile sg = OGFile.open(tmpFile("pool"), Mode.Create, Mode.Write);
		Age age = sg.Ages.make();

		OGFile sg1 = OGFile.open(tmpFile("pool"), Mode.Create, Mode.Write);

		Assert.assertEquals(sg.Ages, sg.pool(age));
		Assert.assertEquals(null, sg1.pool(age));
	}

	/**
	 * state should not have pools for instances of another states types
	 */
	@Test
	public void testPoolOfForeignObj1() throws Exception {
		OGFile sg = OGFile.open(tmpFile("pool"), Mode.Create, Mode.Write);

		basicTypes.OGFile sg1 = basicTypes.OGFile.open(tmpFile("pool"), Mode.Create, Mode.Write);
		basicTypes.BasicBool bb = sg1.BasicBools.make();

		Assert.assertNull(sg.pool(bb));
	}
	
	/**
	 * empty state should not have pools for any instances at all.
	 */
	@Test
	public void testPoolOfForeignObjs() throws Exception {
		empty.OGFile sg = empty.OGFile.open(tmpFile("pool"), Mode.Create, Mode.Write);

		OGFile sg1 = OGFile.open(tmpFile("pool"), Mode.Create, Mode.Write);
		Age age = sg1.Ages.make();

		Assert.assertNull(sg.pool(age));
	}

	/**
	 * state should have pools for instances of unknown types.
	 */
	@Test
	public void testPoolOfUnknownType() throws Exception {
		OGFile sg = OGFile.open(tmpFile("pool"), Mode.Create, Mode.Write);
		sg.Ages.make();
		sg.close();

		empty.OGFile sg1 = empty.OGFile.open(sg.currentPath(), Mode.Read);
		// empty has no known types, thus only type should be age
		Assert.assertEquals("Age", sg1.pool(sg1.allTypes().iterator().next().iterator().next()).name());
		Assert.assertEquals("Age", sg1.pool("Age").name());
	}

	/**
	 * state should have pools for instances of known types.
	 */
	@Test
	public void testPoolOfKnownType() throws Exception {
		OGFile sg = OGFile.open(tmpFile("pool"), Mode.Create, Mode.Write);
		sg.Ages.make();
		sg.flush();

		Assert.assertEquals(sg.Ages, sg.pool(sg.Ages.get(1)));
		Assert.assertEquals(sg.Ages, sg.pool("Age"));
	}

}
