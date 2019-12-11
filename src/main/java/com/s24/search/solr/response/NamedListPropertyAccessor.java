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
   public Object getProperty(Map context, Object target, Object name) throws OgnlException {
       if (name instanceof String) {
           NamedList namedList = (NamedList) target;
           return namedList.get((String) name);
       }
       throw new NoSuchPropertyException(target, name);
   }

    @Override
    public void setProperty(Map context, Object target, Object name, Object value) throws OgnlException {
        if (name instanceof String) {
            NamedList namedList = (NamedList) target;
            int idx = namedList.indexOf((String) name, 0);
            if (idx >= 0) {
                // Key exists -> replace value.
                namedList.setVal(idx, value);
                return;
            } else {
                // Key does not exist -> add key / value.
                namedList.add((String) name, value);
            return;
         }
      }
        throw new NoSuchPropertyException(target, name);
   }

    @Override
    public String getSourceAccessor(OgnlContext context, Object target, Object index) {
        context.setCurrentAccessor(NamedList.class);
        context.setCurrentType(Object.class);

        return ".get(" + index + ")";
    }

    @Override
    public String getSourceSetter(OgnlContext context, Object target, Object index) {
        context.setCurrentAccessor(NamedList.class);
        context.setCurrentType(Object.class);
        NamedList namedList = (NamedList) target;
        int idx = namedList.indexOf((String) index, 0);
        if (idx >= 0) {
            // Key exists -> replace value.
            return ".setVal(" + idx + ", $3)";
        } else {
            // Key does not exist -> add key / value.
            return ".add(" + index + ", $3)";
        }
    }
}
