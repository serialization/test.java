package a;

import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.Test;

import ogss.common.java.api.Access;
import ogss.common.java.api.FieldDeclaration;
import ogss.common.java.api.Mode;
import ogss.common.java.api.OGSSException;
import ogss.common.java.internal.Obj;
import ogss.common.java.internal.StaticFieldIterator;

/**
 * Tests with regard to getter and setter of unknown fields.
 * 
 * @author Sarah Sophie Stieß
 */
@SuppressWarnings("static-method")
public class GetSetOnLazyfieldTest extends common.CommonTest {

    /**
     * Test get/set on unknown field in unknown type
     */
    @Test
    public void test0() throws Exception {
        aWithFields.OGFile sg = aWithFields.OGFile.open(tmpFile("aaaaa"), Mode.Create, Mode.Write);

        // create objects
        aWithFields.A a0 = sg.As.make();
        aWithFields.A a1 = sg.As.make();
        // set fields
        a0.setInst(a0);
        a1.setInst(a0);
        a0.setArr(array(a0, a0));
        a1.setArr(array(a1, a1));
        sg.close();

        { // read back and assert correctness
            empty.OGFile sg2 = empty.OGFile.open(sg.currentPath(), Mode.Read, Mode.Write);
            // only one type supposed to be there, and thats A
            Assert.assertTrue(sg2.allTypes().iterator().hasNext());
            Access<? extends Obj> pool = sg2.allTypes().iterator().next();
            // Assert number of instances
            Assert.assertEquals(2, pool.size());
            // get instances
            Obj a0_2 = pool.get(a0.ID());
            Obj a1_2 = pool.get(a1.ID());

            StaticFieldIterator sfi = pool.fields();
            assertTrue(sfi.hasNext());
            while (sfi.hasNext()) {

                FieldDeclaration<?> f = sfi.next();
                if (f.name().equals("Arr")) {
                    FieldDeclaration<java.util.ArrayList<Obj>> ff = (FieldDeclaration<java.util.ArrayList<Obj>>) f;
                    // Assert size and content
                    Assert.assertEquals(2, ff.get(a0_2).size());
                    Assert.assertEquals(a0_2, ff.get(a0_2).get(0));
                    Assert.assertEquals(a0_2, ff.get(a0_2).get(1));

                    Assert.assertEquals(2, ff.get(a1_2).size());
                    Assert.assertEquals(a1_2, ff.get(a1_2).get(0));
                    Assert.assertEquals(a1_2, ff.get(a1_2).get(1));

                    // add item to first instance
                    ff.get(a0_2).add(0, a0_2);
                    ff.set(a1_2, ff.get(a0_2));
                    Assert.assertEquals(3, ff.get(a0_2).size());
                    Assert.assertEquals(3, ff.get(a1_2).size());
                } else if (f.name().equals("Inst")) {
                    FieldDeclaration<Obj> ff = (FieldDeclaration<Obj>) f;
                    Assert.assertEquals(a0_2, ff.get(a0_2));
                    Assert.assertEquals(a0_2, ff.get(a1_2));

                    ff.set(a0_2, a1_2);
                    ff.set(a1_2, a1_2);
                } else {
                    Assert.fail("unexpected fieldname.");
                }
            }
            sg2.close();
        }
        {
            // read back and assert correctness
            aWithFields.OGFile sg2 = aWithFields.OGFile.open(sg.currentPath(), Mode.Read, Mode.ReadOnly);
            // check count per Type
            Assert.assertEquals(2, sg2.As.staticSize());
            // create objects from file
            aWithFields.A a0_2 = sg2.As.get(a0.ID());
            aWithFields.A a1_2 = sg2.As.get(a1.ID());
            // assert fields
            Assert.assertEquals(3, a0_2.getArr().size());
            Assert.assertEquals(a0_2, a0_2.getArr().get(0));
            Assert.assertEquals(a0_2, a0_2.getArr().get(1));
            Assert.assertEquals(a0_2, a0_2.getArr().get(2));

            Assert.assertEquals(3, a1_2.getArr().size());
            Assert.assertEquals(a0_2, a1_2.getArr().get(0));
            Assert.assertEquals(a0_2, a1_2.getArr().get(1));
            Assert.assertEquals(a0_2, a1_2.getArr().get(2));

            Assert.assertEquals(a1_2, a0_2.getInst());
            Assert.assertEquals(a1_2, a1_2.getInst());
        }
    }

    /**
     * Test get/set on unknown fields in known type
     * 
     */
    @Test
    public void test1() throws Exception {
        aWithFields.OGFile sg = aWithFields.OGFile.open(tmpFile("aaaaa"), Mode.Create, Mode.Write);

        // create objects
        aWithFields.A a0 = sg.As.make();
        aWithFields.A a1 = sg.As.make();
        // set fields
        a0.setInst(a0);
        a1.setInst(a0);
        a0.setArr(array(a0, a0));
        a1.setArr(array(a1, a1));
        sg.close();

        // read back and assert correctness
        a.OGFile sg2 = a.OGFile.open(sg.currentPath(), Mode.Read, Mode.Write);

        // Assert number of instances
        Assert.assertEquals(2, sg2.As.size());
        // get instances
        Obj a0_2 = sg2.As.get(a0.ID());
        Obj a1_2 = sg2.As.get(a1.ID());
        Obj a2 = sg2.As.make();

        StaticFieldIterator sfi = sg2.As.fields();
        assertTrue(sfi.hasNext());
        while (sfi.hasNext()) {
            FieldDeclaration<?> f = sfi.next();
            if (f.name().equals("Arr")) {
                FieldDeclaration<java.util.ArrayList<Obj>> ff = (FieldDeclaration<java.util.ArrayList<Obj>>) f;
                // Assert size and content
                Assert.assertEquals(2, ff.get(a0_2).size());
                Assert.assertEquals(a0_2, ff.get(a0_2).get(0));
                Assert.assertEquals(a0_2, ff.get(a0_2).get(1));

                Assert.assertEquals(2, ff.get(a1_2).size());
                Assert.assertEquals(a1_2, ff.get(a1_2).get(0));
                Assert.assertEquals(a1_2, ff.get(a1_2).get(1));

                // add item to first instance
                ff.get(a0_2).clear();
                ff.get(a0_2).add(a2);
                ff.get(a0_2).add(a2);

                ff.set(a1_2, ff.get(a0_2));
                Assert.assertEquals(2, ff.get(a0_2).size());
                Assert.assertEquals(2, ff.get(a1_2).size());
            } else if (f.name().equals("Inst")) {
                FieldDeclaration<Obj> ff = (FieldDeclaration<Obj>) f;
                Assert.assertEquals(a0_2, ff.get(a0_2));
                Assert.assertEquals(a0_2, ff.get(a1_2));
                // set
                ff.set(a0_2, a2);
                ff.set(a1_2, a2);
            } else {
                Assert.fail("unexpected fieldname.");
            }
        }
        sg2.close();

        // read back and assert correctness
        aWithFields.OGFile sg3 = aWithFields.OGFile.open(sg.currentPath(), Mode.Read, Mode.ReadOnly);
        // check count per Type
        Assert.assertEquals(3, sg3.As.staticSize());
        // create objects from file
        aWithFields.A a0_3 = sg3.As.get(a0.ID());
        aWithFields.A a1_3 = sg3.As.get(a1.ID());
        aWithFields.A a2_3 = sg3.As.get(a2.ID());
        // assert fields
        Assert.assertEquals(2, a0_3.getArr().size());
        Assert.assertEquals(a2_3, a0_3.getArr().get(0));
        Assert.assertEquals(a2_3, a0_3.getArr().get(1));

        Assert.assertEquals(2, a1_3.getArr().size());
        Assert.assertEquals(a2_3, a1_3.getArr().get(0));
        Assert.assertEquals(a2_3, a1_3.getArr().get(1));

        Assert.assertEquals(a2_3, a0_3.getInst());
        Assert.assertEquals(a2_3, a1_3.getInst());
    }

    /**
     * Test get/set of unknown fields for new instance of known type
     * 
     * felder werden nicht geschrieben weil f.owner.cachedSize == 0??
     */
    @Test
    public void test2() throws Exception {
        aWithFields.OGFile sg = aWithFields.OGFile.open(tmpFile("aaaaa"), Mode.Create, Mode.Write);

        sg.close();

        // read back and assert correctness
        a.OGFile sg2 = a.OGFile.open(sg.currentPath(), Mode.Read, Mode.Write);

        // Assert number of instances
        Assert.assertEquals(0, sg2.As.size());
        // make new instances
        Obj a = sg2.As.make();

        StaticFieldIterator sfi = sg2.As.fields();
        assertTrue(sfi.hasNext());
        while (sfi.hasNext()) {
            FieldDeclaration<?> f = sfi.next();
            if (f.name().equals("Arr")) {
                FieldDeclaration<java.util.ArrayList<Obj>> ff = (FieldDeclaration<java.util.ArrayList<Obj>>) f;                
                // add items
                ff.set(a, array(a,a));
                
                Assert.assertEquals(2, ff.get(a).size());
            } else if (f.name().equals("Inst")) {
                FieldDeclaration<Obj> ff = (FieldDeclaration<Obj>) f;
                // set
                ff.set(a, a);
            } else {
                Assert.fail("unexpected fieldname.");
            }
        }
        sg2.close();

        // read back and assert correctness
        aWithFields.OGFile sg3 = aWithFields.OGFile.open(sg.currentPath(), Mode.Read, Mode.ReadOnly);
        // check count per Type
        Assert.assertEquals(1, sg3.As.staticSize());
        // create objects from file
        aWithFields.A a_3 = sg3.As.get(a.ID());
        // assert fields
        Assert.assertNotNull(a_3.getArr());
        Assert.assertEquals(2, a_3.getArr().size());
        Assert.assertEquals(a_3, a_3.getArr().get(0));
        Assert.assertEquals(a_3, a_3.getArr().get(1));

        Assert.assertNotNull(a_3.getInst());
        Assert.assertEquals(a_3, a_3.getInst());
    }
}
