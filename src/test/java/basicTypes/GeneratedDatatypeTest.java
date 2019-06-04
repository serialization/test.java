package basicTypes;

import org.junit.Assert;
import org.junit.Test;

import basicTypes.OGFile;

import ogss.common.java.api.Mode;
import ogss.common.java.api.OGSSException;

/**
 * These tests ensure, that the ranges of the generated numeric data types match
 * the ranges of the specified ones.
 * 
 * @author Sarah Sophie Stieß
 */
@SuppressWarnings("static-method")
public class GeneratedDatatypeTest extends common.CommonTest {

	/**
	 * This test ensures, that the positive range of the generated numeric types is
	 * not greater than it is supposed to be.
	 */
	@Test
	public void test_Overflow() throws Exception {
		OGFile sf = OGFile.open(tmpFile("gentype"), Mode.Create, Mode.Write);

		// create objects
		basicTypes.BasicInt8 int8 = sf.BasicInt8s.make();
		basicTypes.BasicInt16 int16 = sf.BasicInt16s.make();
		basicTypes.BasicInt32 int32 = sf.BasicInt32s.make();
		basicTypes.BasicInt64I int64I = sf.BasicInt64Is.make();
		basicTypes.BasicInt64V int64V = sf.BasicInt64Vs.make();
		basicTypes.BasicFloat32 float32 = sf.BasicFloat32s.make();
		basicTypes.BasicFloat64 float64 = sf.BasicFloat64s.make();

		// set fields
		int8.setBasicInt(Byte.MAX_VALUE);
		int16.setBasicInt(Short.MAX_VALUE);
		int32.setBasicInt(Integer.MAX_VALUE);
		int64I.setBasicInt(Long.MAX_VALUE);
		int64V.setBasicInt(Long.MAX_VALUE);
		float32.setBasicFloat(Float.MAX_VALUE);
		float64.setBasicFloat(Double.MAX_VALUE);

		sf.close();

		{ // read back and assert correctness
			OGFile sf2 = OGFile.open(sf.currentPath(), Mode.Read, Mode.ReadOnly);
			// check count per Type
			Assert.assertEquals(1, sf2.BasicInt8s.staticSize());
			Assert.assertEquals(1, sf2.BasicInt16s.staticSize());
			Assert.assertEquals(1, sf2.BasicInt32s.staticSize());
			Assert.assertEquals(1, sf2.BasicInt64Is.staticSize());
			Assert.assertEquals(1, sf2.BasicInt64Vs.staticSize());
			Assert.assertEquals(1, sf2.BasicFloat32s.staticSize());
			Assert.assertEquals(1, sf2.BasicFloat64s.staticSize());
			// create objects from file
			basicTypes.BasicInt8 int8_2 = sf2.BasicInt8s.get(int8.ID());
			basicTypes.BasicInt16 int16_2 = sf2.BasicInt16s.get(int16.ID());
			basicTypes.BasicInt32 int32_2 = sf2.BasicInt32s.get(int32.ID());
			basicTypes.BasicInt64I int64I_2 = sf2.BasicInt64Is.get(int64I.ID());
			basicTypes.BasicInt64V int64V_2 = sf2.BasicInt64Vs.get(int64V.ID());
			basicTypes.BasicFloat32 float32_2 = sf2.BasicFloat32s.get(float32.ID());
			basicTypes.BasicFloat64 float64_2 = sf2.BasicFloat64s.get(float64.ID());
			// assert fields
			Assert.assertTrue((byte) (int8_2.getBasicInt() + 1) == Byte.MIN_VALUE);
			Assert.assertTrue((short) (int16_2.getBasicInt() + 1) == Short.MIN_VALUE);
			Assert.assertTrue(int32_2.getBasicInt() + 1 == Integer.MIN_VALUE);
			Assert.assertTrue(int64I_2.getBasicInt() + 1 == Long.MIN_VALUE);
			Assert.assertTrue(int64V_2.getBasicInt() + 1 == Long.MIN_VALUE);
			Assert.assertTrue(float32_2.getBasicFloat() * 2 == Float.POSITIVE_INFINITY);
			Assert.assertTrue(float64_2.getBasicFloat() * 2 == Double.POSITIVE_INFINITY);
		}
	}

	/**
	 * This test ensures, that the negative range of the generated numeric types is
	 * not greater than it is supposed to be.
	 */

	@Test
	public void test_Underflow() throws Exception {
		OGFile sf = OGFile.open(tmpFile("gentype"), Mode.Create, Mode.Write);

		// create objects
		basicTypes.BasicInt8 int8 = sf.BasicInt8s.make();
		basicTypes.BasicInt16 int16 = sf.BasicInt16s.make();
		basicTypes.BasicInt32 int32 = sf.BasicInt32s.make();
		basicTypes.BasicInt64I int64I = sf.BasicInt64Is.make();
		basicTypes.BasicInt64V int64V = sf.BasicInt64Vs.make();
		basicTypes.BasicFloat32 float32 = sf.BasicFloat32s.make();
		basicTypes.BasicFloat64 float64 = sf.BasicFloat64s.make();

		// set fields
		int8.setBasicInt(Byte.MIN_VALUE);
		int16.setBasicInt(Short.MIN_VALUE);
		int32.setBasicInt(Integer.MIN_VALUE);
		int64I.setBasicInt(Long.MIN_VALUE);
		int64V.setBasicInt(Long.MIN_VALUE);
		float32.setBasicFloat(Float.MIN_VALUE);
		float64.setBasicFloat(Double.MIN_VALUE);

		sf.close();

		{ // read back and assert correctness
			OGFile sf2 = OGFile.open(sf.currentPath(), Mode.Read, Mode.ReadOnly);
			// check count per Type
			Assert.assertEquals(1, sf2.BasicInt8s.staticSize());
			Assert.assertEquals(1, sf2.BasicInt16s.staticSize());
			Assert.assertEquals(1, sf2.BasicInt32s.staticSize());
			Assert.assertEquals(1, sf2.BasicInt64Is.staticSize());
			Assert.assertEquals(1, sf2.BasicInt64Vs.staticSize());
			Assert.assertEquals(1, sf2.BasicFloat32s.staticSize());
			Assert.assertEquals(1, sf2.BasicFloat64s.staticSize());
			// create objects from file
			basicTypes.BasicInt8 int8_2 = sf2.BasicInt8s.get(int8.ID());
			basicTypes.BasicInt16 int16_2 = sf2.BasicInt16s.get(int16.ID());
			basicTypes.BasicInt32 int32_2 = sf2.BasicInt32s.get(int32.ID());
			basicTypes.BasicInt64I int64I_2 = sf2.BasicInt64Is.get(int64I.ID());
			basicTypes.BasicInt64V int64V_2 = sf2.BasicInt64Vs.get(int64V.ID());
			basicTypes.BasicFloat32 float32_2 = sf2.BasicFloat32s.get(float32.ID());
			basicTypes.BasicFloat64 float64_2 = sf2.BasicFloat64s.get(float64.ID());
			// assert fields
			Assert.assertTrue((byte) (int8_2.getBasicInt() - 1) == Byte.MAX_VALUE);
			Assert.assertTrue((short) (int16_2.getBasicInt() - 1) == Short.MAX_VALUE);
			Assert.assertTrue(int32_2.getBasicInt() - 1 == Integer.MAX_VALUE);
			Assert.assertTrue(int64I_2.getBasicInt() - 1 == Long.MAX_VALUE);
			Assert.assertTrue(int64V_2.getBasicInt() - 1 == Long.MAX_VALUE);
			Assert.assertTrue(float32_2.getBasicFloat() / 2 == (float) 0);
			Assert.assertTrue(float64_2.getBasicFloat() / 2 == (double) 0);
		}
	}

	/**
	 * This test ensures, that the positive range of the generated numeric types is
	 * as great as it is supposed to be.
	 */
	@Test
	public void test_HoldMax() throws Exception {
		OGFile sf = OGFile.open(tmpFile("gentype"), Mode.Create, Mode.Write);

		// create objects
		basicTypes.BasicInt8 int8 = sf.BasicInt8s.make();
		basicTypes.BasicInt16 int16 = sf.BasicInt16s.make();
		basicTypes.BasicInt32 int32 = sf.BasicInt32s.make();
		basicTypes.BasicInt64I int64I = sf.BasicInt64Is.make();
		basicTypes.BasicInt64V int64V = sf.BasicInt64Vs.make();
		basicTypes.BasicFloat32 float32 = sf.BasicFloat32s.make();
		basicTypes.BasicFloat64 float64 = sf.BasicFloat64s.make();

		// set fields
		int8.setBasicInt(Byte.MAX_VALUE);
		int16.setBasicInt(Short.MAX_VALUE);
		int32.setBasicInt(Integer.MAX_VALUE);
		int64I.setBasicInt(Long.MAX_VALUE);
		int64V.setBasicInt(Long.MAX_VALUE);
		float32.setBasicFloat(Float.MAX_VALUE);
		float64.setBasicFloat(Double.MAX_VALUE);

		sf.close();

		{ // read back and assert correctness
			OGFile sf2 = OGFile.open(sf.currentPath(), Mode.Read, Mode.ReadOnly);
			// check count per Type
			Assert.assertEquals(1, sf2.BasicInt8s.staticSize());
			Assert.assertEquals(1, sf2.BasicInt16s.staticSize());
			Assert.assertEquals(1, sf2.BasicInt32s.staticSize());
			Assert.assertEquals(1, sf2.BasicInt64Is.staticSize());
			Assert.assertEquals(1, sf2.BasicInt64Vs.staticSize());
			Assert.assertEquals(1, sf2.BasicFloat32s.staticSize());
			Assert.assertEquals(1, sf2.BasicFloat64s.staticSize());
			// create objects from file
			basicTypes.BasicInt8 int8_2 = sf2.BasicInt8s.get(int8.ID());
			basicTypes.BasicInt16 int16_2 = sf2.BasicInt16s.get(int16.ID());
			basicTypes.BasicInt32 int32_2 = sf2.BasicInt32s.get(int32.ID());
			basicTypes.BasicInt64I int64I_2 = sf2.BasicInt64Is.get(int64I.ID());
			basicTypes.BasicInt64V int64V_2 = sf2.BasicInt64Vs.get(int64V.ID());
			basicTypes.BasicFloat32 float32_2 = sf2.BasicFloat32s.get(float32.ID());
			basicTypes.BasicFloat64 float64_2 = sf2.BasicFloat64s.get(float64.ID());
			// assert fields
			Assert.assertTrue(int8_2.getBasicInt() == Byte.MAX_VALUE);
			Assert.assertTrue(int16_2.getBasicInt() == Short.MAX_VALUE);
			Assert.assertTrue(int32_2.getBasicInt() == Integer.MAX_VALUE);
			Assert.assertTrue(int64I_2.getBasicInt() == Long.MAX_VALUE);
			Assert.assertTrue(int64V_2.getBasicInt() == Long.MAX_VALUE);
			Assert.assertTrue(float32_2.getBasicFloat() == Float.MAX_VALUE);
			Assert.assertTrue(float64_2.getBasicFloat() == Double.MAX_VALUE);
		}
	}

	/**
	 * This test ensures, that the negative range of the generated numeric types is
	 * as great as it is supposed to be.
	 */
	@Test
	public void test_HoldMin() throws Exception {
		OGFile sf = OGFile.open(tmpFile("gentype"), Mode.Create, Mode.Write);

		// create objects
		basicTypes.BasicInt8 int8 = sf.BasicInt8s.make();
		basicTypes.BasicInt16 int16 = sf.BasicInt16s.make();
		basicTypes.BasicInt32 int32 = sf.BasicInt32s.make();
		basicTypes.BasicInt64I int64I = sf.BasicInt64Is.make();
		basicTypes.BasicInt64V int64V = sf.BasicInt64Vs.make();
		basicTypes.BasicFloat32 float32 = sf.BasicFloat32s.make();
		basicTypes.BasicFloat64 float64 = sf.BasicFloat64s.make();

		// set fields
		int8.setBasicInt(Byte.MIN_VALUE);
		int16.setBasicInt(Short.MIN_VALUE);
		int32.setBasicInt(Integer.MIN_VALUE);
		int64I.setBasicInt(Long.MIN_VALUE);
		int64V.setBasicInt(Long.MIN_VALUE);
		float32.setBasicFloat(Float.MIN_VALUE);
		float64.setBasicFloat(Double.MIN_VALUE);

		sf.close();

		{ // read back and assert correctness
			OGFile sf2 = OGFile.open(sf.currentPath(), Mode.Read, Mode.ReadOnly);
			// check count per Type
			Assert.assertEquals(1, sf2.BasicInt8s.staticSize());
			Assert.assertEquals(1, sf2.BasicInt16s.staticSize());
			Assert.assertEquals(1, sf2.BasicInt32s.staticSize());
			Assert.assertEquals(1, sf2.BasicInt64Is.staticSize());
			Assert.assertEquals(1, sf2.BasicInt64Vs.staticSize());
			Assert.assertEquals(1, sf2.BasicFloat32s.staticSize());
			Assert.assertEquals(1, sf2.BasicFloat64s.staticSize());
			// create objects from file
			basicTypes.BasicInt8 int8_2 = sf2.BasicInt8s.get(int8.ID());
			basicTypes.BasicInt16 int16_2 = sf2.BasicInt16s.get(int16.ID());
			basicTypes.BasicInt32 int32_2 = sf2.BasicInt32s.get(int32.ID());
			basicTypes.BasicInt64I int64I_2 = sf2.BasicInt64Is.get(int64I.ID());
			basicTypes.BasicInt64V int64V_2 = sf2.BasicInt64Vs.get(int64V.ID());
			basicTypes.BasicFloat32 float32_2 = sf2.BasicFloat32s.get(float32.ID());
			basicTypes.BasicFloat64 float64_2 = sf2.BasicFloat64s.get(float64.ID());
			// assert fields
			Assert.assertTrue(int8_2.getBasicInt() == Byte.MIN_VALUE);
			Assert.assertTrue(int16_2.getBasicInt() == Short.MIN_VALUE);
			Assert.assertTrue(int32_2.getBasicInt() == Integer.MIN_VALUE);
			Assert.assertTrue(int64I_2.getBasicInt() == Long.MIN_VALUE);
			Assert.assertTrue(int64V_2.getBasicInt() == Long.MIN_VALUE);
			Assert.assertTrue(float32_2.getBasicFloat() == Float.MIN_VALUE);
			Assert.assertTrue(float64_2.getBasicFloat() == Double.MIN_VALUE);
		}
	}
}
