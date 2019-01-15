## Gestiona Project

Este proyecto se engloba en el marco de una arquitectura de microservicios sobre un bus ServiceMix, y consiste en la implementación de un web-service securizado por certificado de aplicación, que levanta un end-point para recibir solicitudes, enruta la petición al servicio de acceso al datasource, formatea la respuesta, y devuelve el resultado.

## Bibliography

- [ServiceMix](http://servicemix.apache.org/)
- [Karaf DataSources (JDBC)](https://svn.apache.org/repos/asf/karaf/site/production/manual/latest-3.0.x/jdbc.html)
- [Camel components](https://github.com/apache/camel/blob/master/components/readme.adoc#components)
    - [camel-jdbc](https://github.com/apache/camel/blob/master/components/camel-jdbc/src/main/docs/jdbc-component.adoc)
    - [camel-cxf](https://github.com/apache/camel/blob/master/components/camel-cxf/src/main/docs/cxf-component.adoc)
- [Apache CXF](http://cxf.apache.org/)
    - [CXF Ejemplos](http://cxf.apache.org/docs/sample-projects.html)
    - [Developing a Service using JAX-WS](http://cxf.apache.org/docs/developing-a-service.html)

