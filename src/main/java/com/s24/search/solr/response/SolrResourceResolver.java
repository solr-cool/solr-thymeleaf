package com.s24.search.solr.response;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.io.InputStream;

import org.apache.solr.core.SolrResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.TemplateProcessingParameters;
import org.thymeleaf.resourceresolver.IResourceResolver;

/**
 * Template resolver using solr resource loader.
 */
class SolrResourceResolver implements IResourceResolver {
   /**
    * Logger.
    */
   private static final Logger log = LoggerFactory.getLogger(SolrResourceResolver.class);

   /**
    * Solr core.
    */
   private SolrResourceLoader loader;

   /**
    * Inject solr resource loader.
    */
   public void setLoader(SolrResourceLoader loader) {
      this.loader = checkNotNull(loader);
   }

   @Override
   public String getName() {
      return "Solr";
   }

   @Override
   public InputStream getResourceAsStream(TemplateProcessingParameters templateProcessingParameters, String resourceName) {
      try {
         return loader.openResource(resourceName);
      } catch (IOException e) {
         log.debug(
               "[THYMELEAF][{}][{}] Resource \"{}\" could not be resolved. This can be normal as " +
               "maybe this resource is not intended to be resolved by this resolver. " +
               "Exception message is provided: {}: {}",
               TemplateEngine.threadIndex(), templateProcessingParameters.getTemplateName(),
               resourceName, e.getClass().getName(), e.getMessage());
         return null;
      }
   }
}
