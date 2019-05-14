package basicTypes;

import java.util.Collection;
import java.util.Iterator;

import org.junit.Assert;
import org.junit.Test;

import basicTypes.OGFile;
import ogss.common.java.api.FieldDeclaration;
import ogss.common.java.api.Mode;
import ogss.common.java.internal.fieldTypes.MapType;
import ogss.common.java.internal.fieldTypes.SingleArgumentType;

/**
 * Test for the fieldtypes of container
 */
@SuppressWarnings("static-method")
public class ContainerFieldTypeTest extends common.CommonTest {

	/**
	 * Make instances of containers and ensure that their fieldtype's size method
	 * returns the correct number of containers
	 */
	@Test
	public void testSize() throws Exception {
		OGFile sg = OGFile.open(tmpFile("fieldtype"), Mode.Create, Mode.Write);

		{
			BasicContainerTypes bct = sg.BasicContainerTypess.make();
			bct.setA(array(1));
			bct.setL(list(1));
			bct.setS(set(1));
			bct.setM(put(map(), 1, 1));
		}
		{
			BasicContainerTypes bct = sg.BasicContainerTypess.make();
			bct.setM(put(map(), 1, 1));
		}
		sg.close();

		OGFile sg1 = OGFile.open(sg.currentPath(), Mode.Read);
		Assert.assertEquals(2, sg.BasicContainerTypess.size());

		// A containerfield's fieldtype's size is the number of containers of that type.
		for (FieldDeclaration<?> f : sg1.BasicContainerTypess.allFields()) {
			if (f.type() instanceof SingleArgumentType<?, ?>) {
				Assert.assertEquals(1, ((SingleArgumentType<?, ?>) f.type()).size());
			} else if (f.type() instanceof MapType<?, ?>) {
				Assert.assertEquals(2, ((MapType<?, ?>) f.type()).size());
			}
		}
	}

	/**
	 * Make instances of containers and ensure that their fieldtype's iterator
	 * method returns an iterator that iterates over the correct number of elements
	 */
	@Test
	public void testIterator() throws Exception {
		OGFile sg = OGFile.open(tmpFile("fieldtype"), Mode.Create, Mode.Write);

		{
			BasicContainerTypes bct = sg.BasicContainerTypess.make();
			bct.setA(array(1));
			bct.setL(list(1));
			bct.setS(set(1));
			bct.setM(put(map(), 1, 1));
		}
		{
			BasicContainerTypes bct = sg.BasicContainerTypess.make();
			bct.setM(put(map(), 1, 1));
		}
		sg.close();

		OGFile sg1 = OGFile.open(sg.currentPath(), Mode.Read);
		Assert.assertEquals(2, sg.BasicContainerTypess.size());

		for (FieldDeclaration<?> f : sg1.BasicContainerTypess.allFields()) {
			if (f.type() instanceof SingleArgumentType<?, ?>) {
				Iterator<?> it = ((SingleArgumentType<?, ?>) f.type()).iterator();
				Assert.assertTrue(it.hasNext());
				it.next();
				Assert.assertFalse(it.hasNext());
			} else if (f.type() instanceof MapType<?, ?>) {
				Iterator<?> it = ((MapType<?, ?>) f.type()).iterator();
				Assert.assertTrue(it.hasNext());
				it.next();
				Assert.assertTrue(it.hasNext());
				it.next();
				Assert.assertFalse(it.hasNext());
			}
		}
	}
}
