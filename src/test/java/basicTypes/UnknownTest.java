package basicTypes;

import java.util.Collection;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import basicTypes.OGFile;
import ogss.common.java.api.FieldDeclaration;
import ogss.common.java.api.Mode;
import ogss.common.java.internal.Pool;
import ogss.common.java.internal.fieldTypes.SingleArgumentType;

/**
 * These test cases make an instance of a type, write them to file, read the file
 * to a state that does not know the written type and then they ensure that the
 * instances of the unknown type are correct.
 */
@SuppressWarnings("static-method")
public class UnknownTest extends common.CommonTest {

	@Test
	public void testUnknownBasicFloat32() throws Exception {
		OGFile sg = OGFile.open(tmpFile("unknown"), Mode.Create, Mode.Write);
		sg.BasicFloat32s.make().setBasicFloat(1f);
		sg.close();

		empty.OGFile sg1 = empty.OGFile.open(sg.currentPath(), Mode.ReadOnly);

		Pool<?> p = sg1.pool("BasicFloat32");
		Assert.assertEquals(1, p.size());
		Assert.assertNotNull(p.get(1));
		Assert.assertNotNull(p.allFields().next());
		Assert.assertEquals(1f, p.allFields().next().get(p.get(1)));
	}

	@Test
	public void testUnknownBasicFloat64() throws Exception {
		OGFile sg = OGFile.open(tmpFile("unknown"), Mode.Create, Mode.Write);
		sg.BasicFloat64s.make().setBasicFloat(1d);
		sg.close();

		empty.OGFile sg1 = empty.OGFile.open(sg.currentPath(), Mode.ReadOnly);

		Pool<?> p = sg1.pool("BasicFloat64");
		Assert.assertEquals(1, p.size());
		Assert.assertNotNull(p.get(1));
		Assert.assertNotNull(p.allFields().next());
		Assert.assertEquals(1d, p.allFields().next().get(p.get(1)));
	}

	@Test
	public void testUnknownBasicInt8() throws Exception {
		OGFile sg = OGFile.open(tmpFile("unknown"), Mode.Create, Mode.Write);
		sg.BasicInt8s.make().setBasicInt((byte) 1);
		sg.close();

		empty.OGFile sg1 = empty.OGFile.open(sg.currentPath(), Mode.ReadOnly);

		Pool<?> p = sg1.pool("BasicInt8");
		Assert.assertEquals(1, p.size());
		Assert.assertNotNull(p.get(1));
		Assert.assertNotNull(p.allFields().next());
		Assert.assertEquals((byte) 1, p.allFields().next().get(p.get(1)));
	}

	@Test
	public void testUnknownBasicInt16() throws Exception {
		OGFile sg = OGFile.open(tmpFile("unknown"), Mode.Create, Mode.Write);
		sg.BasicInt16s.make().setBasicInt((short) 1);
		sg.close();

		empty.OGFile sg1 = empty.OGFile.open(sg.currentPath(), Mode.ReadOnly);

		Pool<?> p = sg1.pool("BasicInt16");
		Assert.assertEquals(1, p.size());
		Assert.assertNotNull(p.get(1));
		Assert.assertNotNull(p.allFields().next());
		Assert.assertEquals((short) 1, p.allFields().next().get(p.get(1)));
	}

	@Test
	public void testUnknownBasicInt32() throws Exception {
		OGFile sg = OGFile.open(tmpFile("unknown"), Mode.Create, Mode.Write);
		sg.BasicInt32s.make().setBasicInt(1);
		sg.close();

		empty.OGFile sg1 = empty.OGFile.open(sg.currentPath(), Mode.ReadOnly);

		Pool<?> p = sg1.pool("BasicInt32");
		Assert.assertEquals(1, p.size());
		Assert.assertNotNull(p.get(1));
		Assert.assertNotNull(p.allFields().next());
		Assert.assertEquals(1, p.allFields().next().get(p.get(1)));
	}

	@Test
	public void testUnknownBasicInt64I() throws Exception {
		OGFile sg = OGFile.open(tmpFile("unknown"), Mode.Create, Mode.Write);
		sg.BasicInt64Is.make().setBasicInt(1L);
		sg.close();

		empty.OGFile sg1 = empty.OGFile.open(sg.currentPath(), Mode.ReadOnly);

		Pool<?> p = sg1.pool("BasicInt64I");
		Assert.assertEquals(1, p.size());
		Assert.assertNotNull(p.get(1));
		Assert.assertNotNull(p.allFields().next());
		Assert.assertEquals(1L, p.allFields().next().get(p.get(1)));
	}

	@Test
	public void testUnknownBasicInt64V() throws Exception {
		OGFile sg = OGFile.open(tmpFile("unknown"), Mode.Create, Mode.Write);
		sg.BasicInt64Vs.make().setBasicInt(1L);
		sg.close();

		empty.OGFile sg1 = empty.OGFile.open(sg.currentPath(), Mode.ReadOnly);

		Pool<?> p = sg1.pool("BasicInt64V");
		Assert.assertEquals(1, p.size());
		Assert.assertNotNull(p.get(1));
		Assert.assertNotNull(p.allFields().next());
		Assert.assertEquals(1L, p.allFields().next().get(p.get(1)));
	}

	@Test
	public void testUnknownBasicString() throws Exception {
		OGFile sg = OGFile.open(tmpFile("unknown"), Mode.Create, Mode.Write);
		sg.BasicStrings.make().setBasicString("basic");
		sg.close();

		empty.OGFile sg1 = empty.OGFile.open(sg.currentPath(), Mode.ReadOnly);

		Pool<?> p = sg1.pool("BasicString");
		Assert.assertEquals(1, p.size());
		Assert.assertNotNull(p.get(1));
		Assert.assertNotNull(p.allFields().next());
		Assert.assertEquals("basic", p.allFields().next().get(p.get(1)));
	}

	@Test
	public void testUnknownBasicBool() throws Exception {
		OGFile sg = OGFile.open(tmpFile("unknown"), Mode.Create, Mode.Write);
		sg.BasicBools.make().setBasicBool(false);
		sg.close();

		empty.OGFile sg1 = empty.OGFile.open(sg.currentPath(), Mode.ReadOnly);

		Pool<?> p = sg1.pool("BasicBool");
		Assert.assertEquals(1, p.size());
		Assert.assertNotNull(p.get(1));
		Assert.assertNotNull(p.allFields().next());
		Assert.assertEquals(false, p.allFields().next().get(p.get(1)));
	}

	@Test
	public void testUnknownBasicContainerTypes() throws Exception {
		OGFile sg = OGFile.open(tmpFile("unknown"), Mode.Create, Mode.Write);
		BasicContainerTypes bct = sg.BasicContainerTypess.make();
		bct.setA(array(1));
		bct.setL(list(1));
		bct.setS(set(1));
		bct.setM(put(map(), 1, 1));
		sg.close();

		empty.OGFile sg1 = empty.OGFile.open(sg.currentPath(), Mode.ReadOnly);

		Pool<?> p = sg1.pool("BasicContainerTypes");
		Assert.assertEquals(1, p.size());
		Assert.assertNotNull(p.get(1));
		for (FieldDeclaration<?> f : p.allFields()) {
			// Maps and SingleArgumentTypes must be handled seperately
			if (f.type() instanceof SingleArgumentType<?, ?>) {
				Assert.assertTrue(((Collection<?>) f.get(p.get(1))).contains(1));
				Assert.assertEquals(1, (((Collection<?>) f.get(p.get(1))).size()));
			} else {
				Assert.assertTrue(((Map<?, ?>) f.get(p.get(1))).containsKey(1));
				Assert.assertTrue(((Map<?, ?>) f.get(p.get(1))).containsValue(1));
				Assert.assertEquals(1, (((Map<?, ?>) f.get(p.get(1))).size()));
			}
		}
	}

}
