# solr-thymeleaf

![travis ci build status](https://travis-ci.org/shopping24/solr-thymeleaf.png)

A Solr component to use the [Thymeleaf template engine](http://www.thymeleaf.org/).

## Installing the component

* Place the [`solr-thymeleaf-<VERSION>-jar-with-dependencies.jar`](https://github.com/shopping24/solr-thymeleaf/releases) in the `/lib` 
  directory of your Solr installation.
* Configure the component in your `solrconfig.xml`:

    <!-- html / thymeleaf response writer -->
    <queryResponseWriter name="html" class="com.s24.search.solr.response.ThymeleafResponseWriter" />

### Configuring template resolving

Pass the template to render in the `tl.template` request parameter. You can configure Thymeleaf template resolving:

    <queryResponseWriter name="html" class="com.s24.search.solr.response.ThymeleafResponseWriter">
         <str name="tl.templateMode">XHTML</str>
         <str name="tl.locale">de_de</str>
         <str name="tl.cacheTTLMs">3600000</str>
         <str name="tl.prefix">.html</str>
         <str name="tl.suffix">${solr.core.config}/templates/</str>
    </queryResponseWriter>

The template context is prefilled with the current `request`, the request `params` and a solr `response`.

## Building the project

This should install the current version into your local repository

    $ ./mvnw clean verify
    
## License

This project is licensed under the [Apache License, Version 2](http://www.apache.org/licenses/LICENSE-2.0.html).
