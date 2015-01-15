package org.thymeleaf.standard.expression;

import static org.junit.Assert.assertEquals;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.junit.Before;
import org.junit.Test;
import org.thymeleaf.Configuration;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.ProcessingContext;

public class SolrExpressionTest {

   Context context = null;

   @Before
   public void setUp() {
      NamedList<Object> solrHeader = new NamedList<Object>();
      solrHeader.add("QTime", new Integer(667));

      NamedList<Object> solrResponse = new NamedList<Object>();
      solrResponse.add("responseHeader", solrHeader);

      QueryResponse response = new QueryResponse();
      response.setResponse(solrResponse);

      ModifiableSolrParams params = new ModifiableSolrParams();
      params.add("rows", "42");

      context = new Context();
      context.setVariable("response", response);
      context.setVariable("params", SolrParams.toMap(params.toNamedList()));
      context.setVariable("solr_core_name", "some-core");
   }

   @Test
   public void testGetQueryTime() throws Exception {
      Expression qTime = Expression.parse("${response.QTime}");
      assertEquals(667,Expression.execute(new Configuration(), new ProcessingContext(context), qTime,
            new OgnlVariableExpressionEvaluator(), StandardExpressionExecutionContext.NORMAL));
   }

   @Test
   public void testGetRowsFromRequestParams() throws Exception {
      Expression qTime = Expression.parse("${params.rows}");
      assertEquals("42", Expression.execute(new Configuration(), new ProcessingContext(context), qTime,
            new OgnlVariableExpressionEvaluator(), StandardExpressionExecutionContext.NORMAL));

   }

   @Test
   public void testVariables() throws Exception {
      Expression corename = Expression.parse("${solr_core_name}");
      assertEquals("some-core", Expression.execute(new Configuration(), new ProcessingContext(context), corename,
            new OgnlVariableExpressionEvaluator(), StandardExpressionExecutionContext.NORMAL));
   }

}
