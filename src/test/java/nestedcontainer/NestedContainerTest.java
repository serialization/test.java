package nestedcontainer;

import org.junit.Assert;
import org.junit.Test;

import ogss.common.java.api.Access;
import ogss.common.java.api.FieldDeclaration;
import ogss.common.java.api.Mode;
import ogss.common.java.internal.Obj;
import ogss.common.java.internal.StaticFieldIterator;

/**
 * Tests the file reading capabilities.
 */
@SuppressWarnings("static-method")
public class NestedContainerTest extends common.CommonTest {

    /**
     * this test asserts, that nested containers are correct, even id they are unknown.
     */
    @Test
    public void testUnknownNestedContainerCorrectness() throws Exception {
        OGFile sf = OGFile.open(tmpFile("make.sg"), Mode.Create, Mode.Write);

        // create objects
        nestedcontainer.NestedContainerString stringCont = sf.NestedContainerStrings.make();
        nestedcontainer.NestedContainerA ACont = sf.NestedContainerAs.make();
        nestedcontainer.A obj1 = sf.As.make();
        nestedcontainer.A obj0 = sf.As.make();
        nestedcontainer.NestedContainer cont = sf.NestedContainers.make();
        // set fields
        stringCont.setA(array(array("a", "aa"), array("aaa", "aaaa"), array()));
        stringCont.setL(list(list("l", "ll"), list("lll", "llll"), list()));
        stringCont.setM(put(map(), "x", put(map(), "y", "z")));

        ACont.setA(array(array(obj0, obj1), array(obj0), array()));
        ACont.setL(list(list(obj0, obj1), list(obj0), list()));
        ACont.setM(put(map(), obj0, put(map(), obj0, obj1)));

        cont.setA(array(array(0L, 1L), array(1L, 0L), array()));
        cont.setL(list(list(0L, 1L, 2L, 3L, 4L), list(5L, 6L, 7L, 8L), list()));
        cont.setM(put(map(), 0L, put(put(map(), 1L, 2L), 3L, 4L)));
        sf.close();

        // read back and assert correctness
        empty.OGFile sf2 = empty.OGFile.open(sf.currentPath(), Mode.Read, Mode.Write);
        // check count per Type
        Access<?> NCs = sf2.pool("NestedContainer");
        Access<?> NCAs = sf2.pool("NestedContainerA");
        Access<?> NCStrings = sf2.pool("NestedContainerString");
        Access<?> As = sf2.pool("A");

        Assert.assertEquals(1, NCAs.size());
        Assert.assertEquals(1, NCs.size());
        Assert.assertEquals(2, As.size());
        Assert.assertEquals(1, NCStrings.size());

        // check array
        Obj cont1 = NCs.get(1);
        Obj ACont1 = NCAs.get(1);
        Obj stringCont1 = NCStrings.get(1);

        { // Assert NestedContainer
            StaticFieldIterator sfi = NCs.fields();
            while (sfi.hasNext()) {
                FieldDeclaration<?> f = sfi.next();
                switch (f.name()) {
                case "A": // array
                    FieldDeclaration<java.util.ArrayList<java.util.ArrayList<java.lang.Long>>> fa = (FieldDeclaration<java.util.ArrayList<java.util.ArrayList<java.lang.Long>>>) f;
                    java.util.ArrayList<java.util.ArrayList<java.lang.Long>> array = fa.get(cont1);
                    Assert.assertEquals(3, array.size());
                    Assert.assertEquals(array(0L, 1L), array.get(0));
                    Assert.assertEquals(array(1L, 0L), array.get(1));
                    Assert.assertEquals(array(), array.get(2));
                    break;
                case "L": // list
                    FieldDeclaration<java.util.LinkedList<java.util.LinkedList<java.lang.Long>>> fl = (FieldDeclaration<java.util.LinkedList<java.util.LinkedList<java.lang.Long>>>) f;
                    java.util.LinkedList<java.util.LinkedList<java.lang.Long>> list = fl.get(cont1);
                    Assert.assertEquals(3, list.size());
                    Assert.assertEquals(list(0L, 1L, 2L, 3L, 4L), list.get(0));
                    Assert.assertEquals(list(5L, 6L, 7L, 8L), list.get(1));
                    Assert.assertEquals(list(), list.get(2));
                    break;
                case "M": // map
                    FieldDeclaration<java.util.HashMap<java.lang.Long, java.util.HashMap<java.lang.Long, java.lang.Long>>> fm = (FieldDeclaration<java.util.HashMap<java.lang.Long, java.util.HashMap<java.lang.Long, java.lang.Long>>>) f;
                    java.util.HashMap<java.lang.Long, java.util.HashMap<java.lang.Long, java.lang.Long>> map = fm
                            .get(cont1);
                    Assert.assertEquals(1, map.keySet().size());
                    // TODO assert more map stuff
                    break;
                default:
                    Assert.fail("unexpected field name");
                    break;
                }
            }
        }

        { // Assert NestedContainerString
            StaticFieldIterator sfi = NCStrings.fields();
            while (sfi.hasNext()) {
                FieldDeclaration<?> f = sfi.next();
                switch (f.name()) {
                case "A": // array
                    FieldDeclaration<java.util.ArrayList<java.util.ArrayList<java.lang.String>>> fa = (FieldDeclaration<java.util.ArrayList<java.util.ArrayList<java.lang.String>>>) f;
                    java.util.ArrayList<java.util.ArrayList<java.lang.String>> array = fa.get(stringCont1);
                    Assert.assertEquals(3, array.size());
                    Assert.assertEquals(array("a", "aa"), array.get(0));
                    Assert.assertEquals(array("aaa", "aaaa"), array.get(1));
                    Assert.assertEquals(array(), array.get(2));
                    break;
                case "L": // list
                    FieldDeclaration<java.util.LinkedList<java.util.LinkedList<java.lang.String>>> fl = (FieldDeclaration<java.util.LinkedList<java.util.LinkedList<java.lang.String>>>) f;
                    java.util.LinkedList<java.util.LinkedList<java.lang.String>> list = fl.get(stringCont1);
                    Assert.assertEquals(3, list.size());
                    Assert.assertEquals(list("l", "ll"), list.get(0));
                    Assert.assertEquals(list("lll", "llll"), list.get(1));
                    Assert.assertEquals(list(), list.get(2));
                    break;
                case "M": // map
                    FieldDeclaration<java.util.HashMap<java.lang.String, java.util.HashMap<java.lang.String, java.lang.String>>> fm = (FieldDeclaration<java.util.HashMap<java.lang.String, java.util.HashMap<java.lang.String, java.lang.String>>>) f;
                    java.util.HashMap<java.lang.String, java.util.HashMap<java.lang.String, java.lang.String>> map = fm
                            .get(stringCont1);
                    Assert.assertEquals(1, map.keySet().size());
                    // TODO assert more map stuff
                    break;
                default:
                    Assert.fail("unexpected field name");
                    break;
                }
            }
        }
        { // Assert NestedContainerA
            Obj a0 = As.get(obj0.ID());
            Obj a1 = As.get(obj1.ID());
            StaticFieldIterator sfi = NCAs.fields();
            while (sfi.hasNext()) {
                FieldDeclaration<?> f = sfi.next();
                switch (f.name()) {
                case "A": // array
                    FieldDeclaration<java.util.ArrayList<java.util.ArrayList<Obj>>> fa = (FieldDeclaration<java.util.ArrayList<java.util.ArrayList<Obj>>>) f;
                    java.util.ArrayList<java.util.ArrayList<Obj>> array = fa.get(ACont1);
                    Assert.assertEquals(3, array.size());
                    Assert.assertEquals(array(a0, a1), array.get(0));
                    Assert.assertEquals(array(a0), array.get(1));
                    Assert.assertEquals(array(), array.get(2));
                    break;
                case "L": // list
                    FieldDeclaration<java.util.LinkedList<java.util.LinkedList<Obj>>> fl = (FieldDeclaration<java.util.LinkedList<java.util.LinkedList<Obj>>>) f;
                    java.util.LinkedList<java.util.LinkedList<Obj>> list = fl.get(ACont1);
                    Assert.assertEquals(3, list.size());
                    Assert.assertEquals(list(a0, a1), list.get(0));
                    Assert.assertEquals(list(a0), list.get(1));
                    Assert.assertEquals(list(), list.get(2));
                    break;
                case "M": // map
                    FieldDeclaration<java.util.HashMap<Obj, java.util.HashMap<Obj, Obj>>> fm = (FieldDeclaration<java.util.HashMap<Obj, java.util.HashMap<Obj, Obj>>>) f;
                    java.util.HashMap<Obj, java.util.HashMap<Obj, Obj>> map = fm.get(ACont1);
                    Assert.assertEquals(1, map.keySet().size());
                    // TODO assert more map stuff
                    break;
                default:
                    Assert.fail("unexpected field name");
                    break;
                }
            }
        }

    }

    /**
     * this test asserts that a state that doesn't know any nested containers writes
     * them correctly anyway.
     */
    @Test
    public void testUnknownNestedContainerWrite() throws Exception {
        OGFile sf = OGFile.open(tmpFile("make.sg"), Mode.Create, Mode.Write);

        // create objects
        nestedcontainer.NestedContainerString stringCont = sf.NestedContainerStrings.make();
        nestedcontainer.NestedContainerA ACont = sf.NestedContainerAs.make();
        nestedcontainer.A obj1 = sf.As.make();
        nestedcontainer.A obj0 = sf.As.make();
        nestedcontainer.NestedContainer cont = sf.NestedContainers.make();
        // set fields
        stringCont.setA(array(array("a", "aa"), array("aaa", "aaaa"), array()));
        stringCont.setL(list(list("l", "ll"), list("lll", "llll"), list()));
        stringCont.setM(put(map(), "x", put(map(), "y", "z")));

        ACont.setA(array(array(obj0, obj1), array(obj0), array()));
        ACont.setL(list(list(obj0, obj1), list(obj0), list()));
        ACont.setM(put(map(), obj0, put(map(), obj0, obj1)));

        cont.setA(array(array(0L, 1L), array(1L, 0L), array()));
        cont.setL(list(list(0L, 1L, 2L, 3L, 4L), list(5L, 6L, 7L, 8L), list()));
        cont.setM(put(map(), 0L, put(put(map(), 1L, 2L), 3L, 4L)));
        sf.close();

        // read such that everything is unknown and write it again.
        empty.OGFile sf1 = empty.OGFile.open(sf.currentPath(), Mode.Read, Mode.Write);
        sf1.close();

        { // read back and assert correctness
            OGFile sf2 = OGFile.open(sf1.currentPath(), Mode.Read, Mode.ReadOnly);
            // check count per Type
            Assert.assertEquals(1, sf.NestedContainerAs.staticSize());
            Assert.assertEquals(1, sf.NestedContainers.staticSize());
            Assert.assertEquals(2, sf.As.staticSize());
            Assert.assertEquals(1, sf.NestedContainerStrings.staticSize());
            // create objects from file
            nestedcontainer.NestedContainerString stringCont_2 = sf2.NestedContainerStrings.get(stringCont.ID());
            nestedcontainer.NestedContainerA ACont_2 = sf2.NestedContainerAs.get(ACont.ID());
            nestedcontainer.A obj1_2 = sf2.As.get(obj1.ID());
            nestedcontainer.A obj0_2 = sf2.As.get(obj0.ID());
            nestedcontainer.NestedContainer cont_2 = sf2.NestedContainers.get(cont.ID());
            // assert fields
            Assert.assertTrue(stringCont_2.getA() != null
                    && stringCont_2.getA().equals(array(array("a", "aa"), array("aaa", "aaaa"), array())));
            Assert.assertTrue(stringCont_2.getL() != null
                    && stringCont_2.getL().equals(list(list("l", "ll"), list("lll", "llll"), list())));
            Assert.assertTrue(
                    stringCont_2.getM() != null && stringCont_2.getM().equals(put(map(), "x", put(map(), "y", "z"))));

            Assert.assertTrue(ACont_2.getA() != null
                    && ACont_2.getA().equals(array(array(obj0_2, obj1_2), array(obj0_2), array())));
            Assert.assertTrue(
                    ACont_2.getL() != null && ACont_2.getL().equals(list(list(obj0_2, obj1_2), list(obj0_2), list())));
            Assert.assertTrue(
                    ACont_2.getM() != null && ACont_2.getM().equals(put(map(), obj0_2, put(map(), obj0_2, obj1_2))));

            Assert.assertTrue(
                    cont_2.getA() != null && cont_2.getA().equals(array(array(0L, 1L), array(1L, 0L), array())));
            Assert.assertTrue(cont_2.getL() != null
                    && cont_2.getL().equals(list(list(0L, 1L, 2L, 3L, 4L), list(5L, 6L, 7L, 8L), list())));
            Assert.assertTrue(
                    cont_2.getM() != null && cont_2.getM().equals(put(map(), 0L, put(put(map(), 1L, 2L), 3L, 4L))));
        }
    }

}
