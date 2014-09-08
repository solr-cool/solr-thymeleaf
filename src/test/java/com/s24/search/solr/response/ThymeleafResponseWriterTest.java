package com.s24.search.solr.response;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.request.SolrQueryRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ThymeleafResponseWriterTest {

   @Mock
   private SolrQueryRequest request;

   private SolrParams params;

   private ThymeleafResponseWriter writer;

   @Before
   public void setUp() {
      params = new ModifiableSolrParams();
      when(request.getParams()).thenReturn(params);
      writer = new ThymeleafResponseWriter();
   }

   @Test(expected = NullPointerException.class)
   public void testGetContentTypeWithNullRequest() throws Exception {
      new ThymeleafResponseWriter().getContentType(null, null);
   }

   @Test
   public void testGetContentType() throws Exception {
      assertEquals("text/html;charset=UTF-8",
            writer.getContentType(request, null));
   }

   @Test(expected = NullPointerException.class)
   public void testInitWithNullValue() throws Exception {
      writer.init(null);
   }
}
