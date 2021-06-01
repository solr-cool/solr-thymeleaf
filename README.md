# solr-thymeleaf

[![continuous integration](https://github.com/solr-cool/solr-thymeleaf/actions/workflows/ci.yaml/badge.svg)](https://github.com/solr-cool/solr-thymeleaf/actions/workflows/ci.yaml)
[![Maven Central](https://img.shields.io/maven-central/v/cool.solr/solr-thymeleaf)](https://search.maven.org/artifact/cool.solr/solr-thymeleaf/)

> ♻️ this is the official and maintained fork of the original [@shopping24](https://github.com/shopping24) repository maintained by [solr.cool](https://solr.cool).

A Solr component to use the [Thymeleaf template engine](http://www.thymeleaf.org/).

## Installing the component

* Place the [`solr-thymeleaf-<VERSION>-jar-with-dependencies.jar`](https://github.com/shopping24/solr-thymeleaf/releases) in the `/lib`
  directory of your Solr installation.
* Configure the component in your `solrconfig.xml`:

    <!-- html / thymeleaf response writer -->
    <queryResponseWriter name="html" class="com.s24.search.solr.response.ThymeleafResponseWriter" />

### Configuring template resolving

Pass the template to render in the `tl.template` request parameter. You can configure Thymeleaf template resolving:

    <queryResponseWriter name="html" class="cool.solr.response.ThymeleafResponseWriter">
         <str name="tl.templateMode">XHTML</str>
         <str name="tl.locale">de_de</str>
         <str name="tl.cacheTTLMs">3600000</str>
         <str name="tl.prefix">${solr.core.config}/templates/</str>
         <str name="tl.suffix">.html</str>
    </queryResponseWriter>

The template context is prefilled with the current `request`, the request `params` and a solr `response`.

## Building the project

This should install the current version into your local repository

    $ ./mvn clean verify

## License

This project is licensed under the [Apache License, Version 2](http://www.apache.org/licenses/LICENSE-2.0.html).
