package common;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.Ignore;

import ogss.common.java.api.Access;
import ogss.common.java.api.FieldDeclaration;
import ogss.common.java.api.GeneralAccess;
import ogss.common.java.api.OGSSException;
import ogss.common.java.api.FieldType;
import ogss.common.java.internal.Obj;
import ogss.common.java.internal.State;
import ogss.common.java.internal.fieldDeclarations.AutoField;
import ogss.common.java.internal.fieldDeclarations.InterfaceField;
import ogss.common.java.internal.fieldTypes.SingleArgumentType;

/**
 * Some test code commonly used by all tests.
 * 
 * @author Timm Felden
 */
@Ignore
abstract public class CommonTest {

    /**
     * This constant is used to guide reflective init
     */
    private static final int reflectiveInitSize = 10;

    public CommonTest() {
        super();
    }

    protected static Path createFile(String packagePath, String name) throws Exception {
        File dir = new File("src/test/resources/serializedTestfiles/" + packagePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File("src/test/resources/serializedTestfiles/" + packagePath + name + ".sg");
        if (file.exists()) {
            file.delete();
        }
        return file.toPath();
    }

    protected static Path tmpFile(String string) throws Exception {
        File r = File.createTempFile(string, ".sg");
        // r.deleteOnExit();
        return r.toPath();
    }

    protected final static String sha256(String name) throws Exception {
        return sha256(new File("src/test/resources/" + name).toPath());
    }

    protected final static String sha256(Path path) throws Exception {
        byte[] bytes = Files.readAllBytes(path);
        StringBuilder sb = new StringBuilder();
        for (byte b : MessageDigest.getInstance("SHA-256").digest(bytes))
            sb.append(String.format("%02X", b));
        return sb.toString();
    }

    protected static void reflectiveInit(State sf) {
        // create instances
        for (Access<?> t : sf.allTypes()) {
            try {
                for (int i = reflectiveInitSize; i != 0; i--)
                    t.make();
            } catch (OGSSException e) {
                // the type can not have more instances
            }
        }

        // set fields
        for (Access<?> t : sf.allTypes()) {
            for (Obj o : t) {
                Iterator<? extends FieldDeclaration<?>> it = t.fields();
                while (it.hasNext()) {
                    final FieldDeclaration<?> f = it.next();
                    if (!(f instanceof AutoField) && !(f instanceof InterfaceField))
                        set(sf, o, f);
                }
            }
        }
    }

    private static <T, Ref extends Obj> void set(State sf, Ref o, FieldDeclaration<T> f) {
        f.set(o, value(sf, f.type()));
    }

    /**
     * unchecked, because the insane amount of casts is necessary to reflect the implicit value based type system
     */
    @SuppressWarnings("unchecked")
    private static <T> T value(State sf, FieldType<T> type) {
        if (type instanceof GeneralAccess<?>) {
            // get a random object
            Iterator<T> is = (Iterator<T>) ((GeneralAccess<?>) type).iterator();
            for (int i = ThreadLocalRandom.current().nextInt(reflectiveInitSize) % 200; i != 0; i--)
                is.next();
            return is.next();
        }

        switch (type.typeID) {
        case 5:
            // random type
            Iterator<? extends Access<? extends Obj>> ts = sf.allTypes().iterator();
            Access<? extends Obj> t = ts.next();
            for (int i = ThreadLocalRandom.current().nextInt(200); i != 0 && ts.hasNext(); i--)
                t = ts.next();

            // random object
            Iterator<? extends Obj> is = t.iterator();
            for (int i = ThreadLocalRandom.current().nextInt(Math.min(200, reflectiveInitSize)); i != 0; i--)
                is.next();
            return (T) is.next();

        case 6:
            return (T) (Boolean) ThreadLocalRandom.current().nextBoolean();
        case 7:
            return (T) (Byte) (byte) ThreadLocalRandom.current().nextInt(reflectiveInitSize);
        case 8:
            return (T) (Short) (short) ThreadLocalRandom.current().nextInt(reflectiveInitSize);
        case 9:
            return (T) (Integer) ThreadLocalRandom.current().nextInt(reflectiveInitSize);
        case 10:
        case 11:
            return (T) (Long) (ThreadLocalRandom.current().nextLong() % reflectiveInitSize);
        case 12:
            return (T) (Float) ThreadLocalRandom.current().nextFloat();
        case 13:
            return (T) (Double) ThreadLocalRandom.current().nextDouble();
        case 14:
            return (T) "☢☢☢";

        case 17: {
            SingleArgumentType<?, ?> cla = (SingleArgumentType<?, ?>) type;
            int length = (int) Math.sqrt(reflectiveInitSize);
            ArrayList<Object> rval = new ArrayList<>(length);
            while (0 != length--)
                rval.add(value(sf, cla.base));
            return (T) rval;
        }
        case 18: {
            SingleArgumentType<?, ?> cla = (SingleArgumentType<?, ?>) type;
            int length = (int) Math.sqrt(reflectiveInitSize);
            LinkedList<Object> rval = new LinkedList<>();
            while (0 != length--)
                rval.add(value(sf, cla.base));
            return (T) rval;
        }
        case 19: {
            SingleArgumentType<?, ?> cla = (SingleArgumentType<?, ?>) type;
            int length = (int) Math.sqrt(reflectiveInitSize);
            HashSet<Object> rval = new HashSet<>();
            while (0 != length--)
                rval.add(value(sf, cla.base));
            return (T) rval;
        }
        case 20:
            return (T) new HashMap<Object, Object>();
        default:
            throw new IllegalStateException();
        }
    }

    protected static <T, U> FieldDeclaration<T> cast(FieldDeclaration<U> arg) {
        return (FieldDeclaration<T>) arg;
    }

    protected static <T> ArrayList<T> array(T... ts) {
        ArrayList<T> rval = new ArrayList<>();
        // workaround for a javac bug
        if (null == ts)
            rval.add(null);
        else
            for (T t : ts)
                rval.add(t);
        return rval;
    }

    protected static <T> LinkedList<T> list(T... ts) {
        LinkedList<T> rval = new LinkedList<>();
        // workaround for a javac bug
        if (null == ts)
            rval.add(null);
        else
            for (T t : ts)
                rval.add(t);
        return rval;
    }

    protected static <T> HashSet<T> set(T... ts) {
        HashSet<T> rval = new HashSet<>();
        // workaround for a javac bug
        if (null == ts)
            rval.add(null);
        else
            for (T t : ts)
                rval.add(t);
        return rval;
    }

    protected static <K, V> HashMap<K, V> map() {
        return new HashMap<>();
    }

    protected static <K, V> HashMap<K, V> put(HashMap<K, V> m, K key, V value) {
        m.put(key, value);
        return m;
    }
}
