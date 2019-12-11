package com.s24.search.solr.response;

import java.io.Writer;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;

import nz.net.ultraq.thymeleaf.LayoutDialect;
import ognl.OgnlRuntime;
import org.apache.solr.client.solrj.SolrResponse;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.core.SolrCore;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.BinaryResponseWriter;
import org.apache.solr.response.QueryResponseWriter;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.util.plugin.SolrCoreAware;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.exceptions.TemplateProcessingException;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A response writer utilizing the Thymeleaf template engine.
 *
 * @author shopping24 GmbH, Torsten Bøgh Köster (@tboeghk)
 */
public class ThymeleafResponseWriter
    implements QueryResponseWriter, SolrCoreAware {

    static {
        try {
            OgnlRuntime.setPropertyAccessor(NamedList.class, new NamedListPropertyAccessor());
        } catch (final Exception e) {
            // We will not ignore this: there's a problem creating an instance of the new property accessor!
            throw new TemplateProcessingException("Exception while configuring OGNL named list property accessor", e);
        }
    }

    private TemplateEngine templateEngine;

    private LuceneTemplateResolver templateResolver;

    private LayoutDialect layoutDialect;

    private Locale locale = Locale.getDefault();

    private SolrParams configuration;

    /**
     * This get's called as soon the core is ready, which is after the
     * {@linkplain #init(NamedList)} method below.
     */
    @Override
    public void inform(SolrCore core) {
        templateResolver.setResourceLoader(core.getResourceLoader());

        // Clear all cached templates. There could be a new one and should be loaded on collection reload.
        if (templateEngine != null) {
            templateEngine.clearTemplateCache();
        }
    }

    @Override
    public void write(Writer writer, SolrQueryRequest request, SolrQueryResponse response) {
        checkNotNull(writer);
        checkNotNull(request);
        checkNotNull(response);

        // get template name from request params
        String templateName = request.getParams().get("tl.template");
        checkNotNull(templateName, "No tl.template given");

        // When changing the templates we want a cleared cache
        if (request.getParams().getBool("tl.clearCache", false)) {
            templateEngine.clearTemplateCache();
        }

        // Prefill context with some defaults
        HttpServletRequest httpServletRequest = (HttpServletRequest) request.getContext().get("httpRequest");
        WebContext context = new WebContext(
            httpServletRequest,
            new FakeServletResponse(),
            httpServletRequest.getServletContext(),
            locale
        );
        context.setVariable("request", request);
        context.setVariable("params", toMap(request.getParams().toNamedList()));

        // add core properties
        Properties coreProperties = request.getCore().getResourceLoader().getCoreProperties();
        if (coreProperties != null) {
            for (Entry<Object, Object> p : coreProperties.entrySet()) {
                context.setVariable(p.getKey().toString().replace('.', '_'), p.getValue());
            }
        }

        SolrResponse rsp = new QueryResponse();
        NamedList<Object> parsedResponse = BinaryResponseWriter.getParsedResponse(request, response);
        rsp.setResponse(parsedResponse);
        context.setVariable("response", rsp);

        getEngine().process(templateName, context, writer);
    }

    private static Map<String, String> toMap(NamedList<Object> params) {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < params.size(); i++) {
            map.put(params.getName(i), params.getVal(i).toString());
        }
        return map;
    }

    protected TemplateEngine getEngine() {
        // create engine
        if (templateEngine == null) {
            // use solr core here
            templateResolver.setPrefix(configuration.get("tl.prefix", "templates/"));

            TemplateEngine te = new TemplateEngine();
            te.setTemplateResolver(templateResolver);
            te.addDialect(layoutDialect);

            // check race condition, avoid synchronized block
            templateEngine = te;
        }

        return templateEngine;
    }

    @Override
    public String getContentType(SolrQueryRequest request, SolrQueryResponse response) {
        checkNotNull(request);
        return request.getParams().get("tl.contentType", "text/html;charset=UTF-8");
    }

    /**
     * Configures engine and template resolving.
     */
    @Override
    public void init(@SuppressWarnings("rawtypes") NamedList args) {
        checkNotNull(args);
        configuration = args.toSolrParams();

        // configure template resolver
        templateResolver = new LuceneTemplateResolver();
        templateResolver.setCharacterEncoding("utf-8");
        templateResolver.setTemplateMode(configuration.get("tl.templateMode", "XHTML"));
        templateResolver.setSuffix(configuration.get("tl.suffix", ".html"));

        if (configuration.get("tl.cacheTtlMs") != null) {
            templateResolver.setCacheTTLMs(Long.parseLong(configuration.get("tl.cacheTTLMs")));
        } else {
            templateResolver.setCacheTTLMs(TimeUnit.HOURS.toMillis(1L));
        }

        if (configuration.get("tl.locale") != null) {
            locale = Locale.forLanguageTag(configuration.get("tl.locale"));
        }

        layoutDialect = new LayoutDialect();
    }
}
