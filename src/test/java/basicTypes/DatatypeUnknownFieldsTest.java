package basicTypes;

import org.junit.Assert;
import org.junit.Test;

import basicTypes.OGFile;
import ogss.common.java.api.Access;
import ogss.common.java.api.FieldDeclaration;
import ogss.common.java.api.Mode;
import ogss.common.java.api.OGSSException;
import ogss.common.java.internal.Obj;
import ogss.common.java.internal.Pool;
import ogss.common.java.internal.fieldTypes.F32;
import ogss.common.java.internal.fieldTypes.F64;
import ogss.common.java.internal.fieldTypes.I16;
import ogss.common.java.internal.fieldTypes.I32;
import ogss.common.java.internal.fieldTypes.I64;
import ogss.common.java.internal.fieldTypes.I8;
import ogss.common.java.internal.fieldTypes.IntegerType;
import ogss.common.java.internal.fieldTypes.V64;

/**
 * These tests ensure, that the ranges of unknown fields of numeric data types match
 * the ranges of the specified ones.
 * 
 * @author Sarah Sophie Stieß
 */
@SuppressWarnings("static-method")
public class DatatypeUnknownFieldsTest extends common.CommonTest {

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
            empty.OGFile sf2 = empty.OGFile.open(sf.currentPath(), Mode.Read, Mode.ReadOnly);

            int types = 0;
            for (Access<? extends Obj> t : sf2.allTypes()) {
                if (1 == ((Pool<Obj>) t).staticSize()) {
                    types++;
                    Obj o = t.get(1);

                    Assert.assertNotNull(o);
                    Assert.assertTrue(t.fields().hasNext());
                    FieldDeclaration<?> f = t.fields().next();
                    if (f.type() instanceof I8) {
                        FieldDeclaration<java.lang.Byte> typedF = (FieldDeclaration<java.lang.Byte>) f;
                        Assert.assertTrue(typedF.get(o) + 1 == Byte.MIN_VALUE);
                    } else if (f.type() instanceof I16) {
                        FieldDeclaration<java.lang.Short> typedF = (FieldDeclaration<java.lang.Short>) f;
                        Assert.assertTrue(typedF.get(o) + 1 == Short.MIN_VALUE);
                    } else if (f.type() instanceof I32) {
                        FieldDeclaration<java.lang.Integer> typedF = (FieldDeclaration<java.lang.Integer>) f;
                        Assert.assertTrue(typedF.get(o) + 1 == Integer.MIN_VALUE);
                    } else if (f.type() instanceof I64) {
                        FieldDeclaration<java.lang.Long> typedF = (FieldDeclaration<java.lang.Long>) f;
                        Assert.assertTrue(typedF.get(o) + 1 == Long.MIN_VALUE);
                    } else if (f.type() instanceof V64) {
                        FieldDeclaration<java.lang.Long> typedF = (FieldDeclaration<java.lang.Long>) f;
                        Assert.assertTrue(typedF.get(o) + 1 == Long.MIN_VALUE);
                    } else if (f.type() instanceof F32) {
                        FieldDeclaration<java.lang.Float> typedF = (FieldDeclaration<java.lang.Float>) f;
                        Assert.assertTrue(typedF.get(o) * 2 == Float.POSITIVE_INFINITY);
                    } else if (f.type() instanceof F64) {
                        FieldDeclaration<java.lang.Double> typedF = (FieldDeclaration<java.lang.Double>) f;
                        Assert.assertTrue(typedF.get(o) * 2 == Double.POSITIVE_INFINITY);
                    } else {
                        Assert.fail("Either there are some instances that should not be there, or a LazyField has the wrong FieldType");
                    }
                }
            }
            // ensure that all types supposed to have instances had some.
            Assert.assertEquals(7, types);
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
            empty.OGFile sf2 = empty.OGFile.open(sf.currentPath(), Mode.Read, Mode.ReadOnly);

            int types = 0;
            for (Access<? extends Obj> t : sf2.allTypes()) {
                if (1 == ((Pool<Obj>) t).staticSize()) {
                    types++;
                    Obj o = t.get(1);
                    Assert.assertNotNull(o);
                    Assert.assertTrue(t.fields().hasNext());
                    FieldDeclaration<?> f = t.fields().next();
                    if (f.type() instanceof I8) {
                        FieldDeclaration<java.lang.Byte> typedF = (FieldDeclaration<java.lang.Byte>) f;
                        Assert.assertTrue(typedF.get(o) - 1 == Byte.MAX_VALUE);
                    } else if (f.type() instanceof I16) {
                        FieldDeclaration<java.lang.Short> typedF = (FieldDeclaration<java.lang.Short>) f;
                        Assert.assertTrue(typedF.get(o) - 1 == Short.MAX_VALUE);
                    } else if (f.type() instanceof I32) {
                        FieldDeclaration<java.lang.Integer> typedF = (FieldDeclaration<java.lang.Integer>) f;
                        Assert.assertTrue(typedF.get(o) - 1 == Integer.MAX_VALUE);
                    } else if (f.type() instanceof I64) {
                        FieldDeclaration<java.lang.Long> typedF = (FieldDeclaration<java.lang.Long>) f;
                        Assert.assertTrue(typedF.get(o) - 1 == Long.MAX_VALUE);
                    } else if (f.type() instanceof V64) {
                        FieldDeclaration<java.lang.Long> typedF = (FieldDeclaration<java.lang.Long>) f;
                        Assert.assertTrue(typedF.get(o) - 1 == Long.MAX_VALUE);
                    } else if (f.type() instanceof F32) {
                        FieldDeclaration<java.lang.Float> typedF = (FieldDeclaration<java.lang.Float>) f;
                        Assert.assertTrue(typedF.get(o) / 2 == (float) 0);
                    } else if (f.type() instanceof F64) {
                        FieldDeclaration<java.lang.Double> typedF = (FieldDeclaration<java.lang.Double>) f;
                        Assert.assertTrue(typedF.get(o) / 2 == (double) 0);
                    } else {
                        Assert.fail("Either there are some instances that should not be there, or a LazyField has the wrong FieldType");
                    }
                }
            }
            // ensure that all types supposed to have instances had some.
            Assert.assertEquals(7, types);
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
            empty.OGFile sf2 = empty.OGFile.open(sf.currentPath(), Mode.Read, Mode.ReadOnly);

            int types = 0;
            for (Access<? extends Obj> t : sf2.allTypes()) {
                if (1 == ((Pool<Obj>) t).staticSize()) {
                    types++;
                    Obj o = t.get(1);
                    Assert.assertNotNull(o);
                    Assert.assertTrue(t.fields().hasNext());
                    FieldDeclaration<?> f = t.fields().next();
                    if (f.type() instanceof I8) {
                        FieldDeclaration<java.lang.Byte> typedF = (FieldDeclaration<java.lang.Byte>) f;
                        Assert.assertTrue(typedF.get(o) == Byte.MAX_VALUE);
                    } else if (f.type() instanceof I16) {
                        FieldDeclaration<java.lang.Short> typedF = (FieldDeclaration<java.lang.Short>) f;
                        Assert.assertTrue(typedF.get(o) == Short.MAX_VALUE);
                    } else if (f.type() instanceof I32) {
                        FieldDeclaration<java.lang.Integer> typedF = (FieldDeclaration<java.lang.Integer>) f;
                        Assert.assertTrue(typedF.get(o) == Integer.MAX_VALUE);
                    } else if (f.type() instanceof I64) {
                        FieldDeclaration<java.lang.Long> typedF = (FieldDeclaration<java.lang.Long>) f;
                        Assert.assertTrue(typedF.get(o) == Long.MAX_VALUE);
                    } else if (f.type() instanceof V64) {
                        FieldDeclaration<java.lang.Long> typedF = (FieldDeclaration<java.lang.Long>) f;
                        Assert.assertTrue(typedF.get(o) == Long.MAX_VALUE);
                    } else if (f.type() instanceof F32) {
                        FieldDeclaration<java.lang.Float> typedF = (FieldDeclaration<java.lang.Float>) f;
                        Assert.assertTrue(typedF.get(o) == Float.MAX_VALUE);
                    } else if (f.type() instanceof F64) {
                        FieldDeclaration<java.lang.Double> typedF = (FieldDeclaration<java.lang.Double>) f;
                        Assert.assertTrue(typedF.get(o) == Double.MAX_VALUE);
                    } else {
                        Assert.fail("Either there are some instances that should not be there, or a LazyField has the wrong FieldType");
                    }
                }
            }
            // ensure that all types supposed to have instances had some.
            Assert.assertEquals(7, types);
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
            empty.OGFile sf2 = empty.OGFile.open(sf.currentPath(), Mode.Read, Mode.ReadOnly);

            int types = 0;
            for (Access<? extends Obj> t : sf2.allTypes()) {
                if (1 == ((Pool<Obj>) t).staticSize()) {
                    types++;
                    Obj o = t.get(1);
                    Assert.assertNotNull(o);
                    Assert.assertTrue(t.fields().hasNext());
                    FieldDeclaration<?> f = t.fields().next();
                    if (f.type() instanceof I8) {
                        FieldDeclaration<java.lang.Byte> typedF = (FieldDeclaration<java.lang.Byte>) f;
                        Assert.assertTrue(typedF.get(o) == Byte.MIN_VALUE);
                    } else if (f.type() instanceof I16) {
                        FieldDeclaration<java.lang.Short> typedF = (FieldDeclaration<java.lang.Short>) f;
                        Assert.assertTrue(typedF.get(o) == Short.MIN_VALUE);
                    } else if (f.type() instanceof I32) {
                        FieldDeclaration<java.lang.Integer> typedF = (FieldDeclaration<java.lang.Integer>) f;
                        Assert.assertTrue(typedF.get(o) == Integer.MIN_VALUE);
                    } else if (f.type() instanceof I64) {
                        FieldDeclaration<java.lang.Long> typedF = (FieldDeclaration<java.lang.Long>) f;
                        Assert.assertTrue(typedF.get(o) == Long.MIN_VALUE);
                    } else if (f.type() instanceof V64) {
                        FieldDeclaration<java.lang.Long> typedF = (FieldDeclaration<java.lang.Long>) f;
                        Assert.assertTrue(typedF.get(o) == Long.MIN_VALUE);
                    } else if (f.type() instanceof F32) {
                        FieldDeclaration<java.lang.Float> typedF = (FieldDeclaration<java.lang.Float>) f;
                        Assert.assertTrue(typedF.get(o) == Float.MIN_VALUE);
                    } else if (f.type() instanceof F64) {
                        FieldDeclaration<java.lang.Double> typedF = (FieldDeclaration<java.lang.Double>) f;
                        Assert.assertTrue(typedF.get(o) == Double.MIN_VALUE);
                    } else {
                        Assert.fail("Either there are some instances that should not be there, or a LazyField has the wrong FieldType");
                    }
                }
            }
            // ensure that all types supposed to have instances had some.
            Assert.assertEquals(7, types);
        }
    }
}
