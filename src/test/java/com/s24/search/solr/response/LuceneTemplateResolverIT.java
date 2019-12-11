package com.s24.search.solr.response;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.util.ResourceLoader;
import org.apache.solr.SolrTestCaseJ4;
import org.apache.solr.core.SolrResourceLoader;
import org.junit.Test;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.ITemplateResolver;

public class LuceneTemplateResolverIT
    extends SolrTestCaseJ4 {

    @Test
    public void testTemplateResolverConfiguration04() throws Exception {

        initCore();
        ResourceLoader resourceLoader = new SolrResourceLoader(testSolrHome);

        TemplateEngine templateEngine = new TemplateEngine();
        LuceneTemplateResolver templateResolver = new LuceneTemplateResolver();
        templateResolver.setResourceLoader(resourceLoader);
        templateEngine.setTemplateResolver(templateResolver);
        templateEngine.getConfiguration();

        List<ITemplateResolver> templateResolvers = new ArrayList<>(templateEngine.getTemplateResolvers());

        assertEquals(1, templateResolvers.size());
        assertEquals("com.s24.search.solr.response.LuceneTemplateResolver", templateResolvers.get(0).getName());

    }

}