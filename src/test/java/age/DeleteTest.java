package age;

import java.util.Iterator;

import org.junit.Assert;
import org.junit.Test;

import age.OGFile;
import ogss.common.java.api.Access;
import ogss.common.java.api.Mode;
import ogss.common.java.api.OGSSException;
import ogss.common.java.internal.Obj;

/**
 * Tests for the state's delete method.
 */
@SuppressWarnings("static-method")
public class DeleteTest extends common.CommonTest {

    /**
     * test that instances of unknown type may be delete as well
     */
    @Test
    public void testDeleteUnknown() throws Exception {
        try (OGFile sg = OGFile.open(tmpFile("delete"), Mode.Create, Mode.Write)) {
            sg.Ages.make().setAge(0);
            sg.Ages.make().setAge(1);

            sg.close();

            empty.OGFile sg1 = empty.OGFile.open(sg.currentPath(), Mode.Read);

            for (Access<? extends Obj> acc : sg1.allTypes()) {
                Iterator<? extends Obj> it = acc.iterator();
                while (it.hasNext()) {
                    sg1.delete(it.next());
                }
                sg1.flush();
                Assert.assertEquals(0, acc.size());
            }
        }
    }

    /**
     * test that instances of known type may be deleted
     */
    @Test
    public void testDeleteKnown() throws Exception {
        try (OGFile sg = OGFile.open(tmpFile("delete"), Mode.Create, Mode.Write)) {
            sg.Ages.make().setAge(0);
            sg.Ages.make().setAge(1);

            sg.close();

            OGFile sg1 = OGFile.open(sg.currentPath(), Mode.Read);

            for (Access<? extends Obj> acc : sg1.allTypes()) {
                Iterator<? extends Obj> it = acc.iterator();
                while (it.hasNext()) {
                    sg1.delete(it.next());
                }
                sg1.flush();
                Assert.assertEquals(0, acc.size());
            }
        }
    }

    /**
     * test that the correct instance is deleted
     */
    @Test
    public void testDelete0() throws Exception {
        try (OGFile sg = OGFile.open(tmpFile("delete"), Mode.Create, Mode.Write)) {

            Age age0 = sg.Ages.make();
            Age age1 = sg.Ages.make();

            sg.delete(age0);

            sg.flush();

            Assert.assertEquals(1, sg.Ages.size());
            for (Age age : sg.Ages) {
                Assert.assertEquals(age1, age);
            }
        }
    }

    /**
     * test that deleted instances are indeed deleted and indeed not written to file
     */
    @Test
    public void testDelete1() throws Exception {
        try (OGFile sg = OGFile.open(tmpFile("delete"), Mode.Create, Mode.Write)) {

            Age age0 = sg.Ages.make();
            Age age1 = sg.Ages.make();
            age0.setAge(0);
            age1.setAge(1);

            sg.delete(age0);

            sg.close();

            OGFile sg1 = OGFile.open(sg.currentPath(), Mode.ReadOnly);
            Assert.assertEquals(1, sg1.Ages.size());
            for (Age age : sg1.Ages) {
                Assert.assertEquals(1, age.getAge());
            }
        }
    }

    /**
     * test that deleting an already deleted instance again does not change the state
     */
    @Test
    public void testDelete2() throws Exception {
        OGFile sg = OGFile.open(tmpFile("delete"), Mode.Create, Mode.Write);

        Age age0 = sg.Ages.make();

        Assert.assertEquals(1, sg.Ages.size());

        sg.delete(age0);
        sg.flush();
        Assert.assertEquals(0, sg.Ages.size());

        sg.delete(age0);
        sg.flush();
        Assert.assertEquals(0, sg.Ages.size());
    }

    /**
     * delete an instance that does not belong to the state. documentation of delete method does not state what is
     * supposed to happen.
     */
    @Test(expected = OGSSException.class)
    public void testDelete3() throws Exception {
        OGFile sg = OGFile.open(tmpFile("delete"), Mode.Create, Mode.Write);
        OGFile sg1 = OGFile.open(tmpFile("delete"), Mode.Create, Mode.Write);

        sg1.delete(sg.Ages.make());
        // should not be able to pass over this point
        Assert.fail("illegal deletes should be checked for");
    }

    /**
     * test that deletion of null does not change the state
     */
    @Test
    public void testDelete4() throws Exception {
        OGFile sg = OGFile.open(tmpFile("delete"), Mode.Create, Mode.Write);

        sg.Ages.make();
        sg.Ages.make();
        Assert.assertEquals(2, sg.Ages.size());

        sg.delete(null);

        sg.flush();

        Assert.assertEquals(2, sg.Ages.size());
    }
}
