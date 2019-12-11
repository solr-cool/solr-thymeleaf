package com.s24.search.solr.response;

import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.request.SolrQueryRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

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
    public void testGetContentTypeWithNullRequest() {
        new ThymeleafResponseWriter().getContentType(null, null);
    }

    @Test
    public void testGetContentType() {
        assertEquals(
            "text/html;charset=UTF-8",
            writer.getContentType(request, null)
        );
    }

    @Test(expected = NullPointerException.class)
    public void testInitWithNullValue() {
        writer.init(null);
    }
}
