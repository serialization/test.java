package age;

import java.util.Iterator;

import org.junit.Assert;
import org.junit.Test;

import age.OGFile;
import ogss.common.java.api.Access;
import ogss.common.java.api.Mode;
import ogss.common.java.internal.Obj;

/**
 *  Tests for the state's contains method. 
 */
@SuppressWarnings("static-method")
public class ContainsTest extends common.CommonTest {

	/**
	 * supposed to be true because documentation says so
	 */
	@Test
	public void testContainsNull() throws Exception {
		OGFile sg = OGFile.open(tmpFile("contains"), Mode.Create, Mode.Write);

		Assert.assertTrue(sg.contains(null));
	}

	/**
	 * state must contain all of its instances of known type
	 */
	@Test
	public void testContainsKnown() throws Exception {
		OGFile sg = OGFile.open(tmpFile("contains"), Mode.Create, Mode.Write);
		sg.Ages.make().setAge(0);
		sg.Ages.make().setAge(1);

		sg.close();

		OGFile sg1 = OGFile.open(sg.currentPath(), Mode.Read);

		for (Age age : sg1.Ages) {
			Assert.assertTrue(sg1.contains(age));
		}
	}

	/**
	 * state must contain all of its instances of unknown type
	 */
	@Test
	public void testContainsUnknown() throws Exception {
		OGFile sg = OGFile.open(tmpFile("contains"), Mode.Create, Mode.Write);
		sg.Ages.make().setAge(0);
		sg.Ages.make().setAge(1);

		sg.close();

		empty.OGFile sg1 = empty.OGFile.open(sg.currentPath(), Mode.Read);

		Assert.assertTrue("Missing Types", sg1.allTypes().iterator().hasNext());

		for (Access<? extends Obj> acc : sg1.allTypes()) {
			Iterator<? extends Obj> it = acc.iterator();
			// empty has no known types, thus only type must be ages
			Assert.assertEquals(2, acc.size());
			while (it.hasNext()) {
				Assert.assertTrue(sg.contains(it.next()));
			}
		}
	}

	/**
	 * a state should not contain instances managed by another state.
	 */
	@Test
	public void testContainsForeign() throws Exception {
		OGFile sg = OGFile.open(tmpFile("contains"), Mode.Create, Mode.Write);
		sg.Ages.make().setAge(0);
		sg.Ages.make().setAge(1);

		sg.close();

		OGFile sg1 = OGFile.open(sg.currentPath(), Mode.Read);
		Assert.assertEquals(2, sg1.Ages.size());

		for (Age age : sg1.Ages) {
			Assert.assertFalse(sg.contains(age));
		}
	}

	/**
	 * a very new and very empty state should not contain any instances at all.
	 */
	@Test
	public void testContainsForeign1() throws Exception {
		OGFile sg = OGFile.open(tmpFile("contains"), Mode.Create, Mode.Write);
		sg.Ages.make().setAge(0);
		sg.Ages.make().setAge(1);
		sg.flush();
		Assert.assertEquals(2, sg.Ages.size());

		OGFile sg1 = OGFile.open(tmpFile("contains"), Mode.Create, Mode.Write);
		Assert.assertEquals(0, sg1.Ages.size());

		for (Age age : sg.Ages) {
			Assert.assertFalse(sg1.contains(age));
		}
	}

	/**
	 * a state should not contain a deleted instance, no matter whether the state
	 * has been flushed or not.
	 */
	@Test
	public void testContainsDeleted() throws Exception {
		OGFile sg = OGFile.open(tmpFile("contains"), Mode.Create, Mode.Write);
		Age age = sg.Ages.make();
		sg.delete(age);
		Assert.assertFalse(sg.contains(age));
		sg.flush();
		Assert.assertFalse(sg.contains(age));
	}
}
