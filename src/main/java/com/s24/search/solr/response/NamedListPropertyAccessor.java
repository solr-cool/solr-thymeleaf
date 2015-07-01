package com.s24.search.solr.response;

import java.util.Map;

import ognl.NoSuchPropertyException;
import ognl.OgnlContext;
import ognl.OgnlException;
import ognl.PropertyAccessor;

import org.apache.solr.common.util.NamedList;

/**
 * {@link PropertyAccessor} for {@link NamedList}.
 */
@SuppressWarnings("unchecked")
public class NamedListPropertyAccessor implements PropertyAccessor {
   @Override
   public Object getProperty(Map context, Object list, Object name) throws OgnlException {
      if (name instanceof String) {
         NamedList namedList = (NamedList) list;
         return namedList.get((String) name);
      }

      throw new NoSuchPropertyException(list, name);
   }

   @Override
   public void setProperty(Map context, Object list, Object name, Object value) throws OgnlException {
      if (name instanceof String) {
         NamedList namedList = (NamedList) list;
         int idx = namedList.indexOf((String) name, 0);
         if (idx >= 0) {
            // Key exists -> replace value.
            namedList.setVal(idx, value);
         } else {
            // Key does not exist -> add key / value.
            namedList.add((String) name, value);
         }
      }

      throw new NoSuchPropertyException(list, name);
   }

   @Override
   public String getSourceAccessor(OgnlContext context, Object list, Object name) {
      context.setCurrentAccessor(NamedList.class);
      context.setCurrentType(Object.class);

      return ".get(" + name + ")";
   }

   @Override
   public String getSourceSetter(OgnlContext context, Object list, Object name) {
      context.setCurrentAccessor(NamedList.class);
      context.setCurrentType(Object.class);

      NamedList namedList = (NamedList) list;
      int idx = namedList.indexOf((String) name, 0);
      if (idx >= 0) {
         // Key exists -> replace value.
         return ".setVal(" + idx + ", $3)";
      } else {
         // Key does not exist -> add key / value.
         return ".add(" + name + ", $3)";
      }
   }
}
