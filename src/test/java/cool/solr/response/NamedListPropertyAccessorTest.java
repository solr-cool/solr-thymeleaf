package cool.solr.response;

import java.util.HashMap;
import java.util.Map;

import ognl.OgnlContext;
import org.apache.solr.common.util.NamedList;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test for {@link NamedListPropertyAccessor}.
 */
public class NamedListPropertyAccessorTest {

    /**
     * Test context.
     */
    private Map<String, String> context;

   /**
    * Test list.
    */
   private NamedList<String> namedList;

   /**
    * Test OGNL context.
    */
   private OgnlContext ognlContext;

   /**
    * Property accessor under test.
    */
   private NamedListPropertyAccessor accessor;

   @Before
   public void setUp() {
      context = new HashMap<>();

      namedList = new NamedList();
      namedList.add("key", "value");

      ognlContext = new OgnlContext();

      accessor = new NamedListPropertyAccessor();
   }

   /**
    * Test for {@link NamedListPropertyAccessor#getProperty(Map, Object, Object)}.
    */
   @Test
   public void testGetProperty() throws Exception {
      assertEquals("value", accessor.getProperty(context, namedList, "key"));
   }

   /**
    * Test for {@link NamedListPropertyAccessor#setProperty(Map, Object, Object, Object)}.
    */
   @Test
   public void testSetProperty() throws Exception {
      // Overwrite value of existing key.
      accessor.setProperty(context, namedList, "key", "newValue");

      assertEquals("newValue", namedList.get("key"));

      // Add new key / value.
      accessor.setProperty(context, namedList, "key2", "value2");

      assertEquals("value2", namedList.get("key2"));
   }

    /**
     * Test for {@link NamedListPropertyAccessor#getSourceAccessor(OgnlContext, Object, Object)}.
     */
    @Test
    public void testGetSourceAccessor() {
        assertEquals(".get(key)", accessor.getSourceAccessor(ognlContext, namedList, "key"));
    }

    /**
     * Test for {@link NamedListPropertyAccessor#getSourceSetter(OgnlContext, Object, Object)}.
     */
    @Test
    public void testGetSourceSetter() {
        // Overwrite value of existing key.
        assertEquals(".setVal(0, $3)", accessor.getSourceSetter(ognlContext, namedList, "key"));

        // Add new key / value.
        assertEquals(".add(key2, $3)", accessor.getSourceSetter(ognlContext, namedList, "key2"));
    }
}