# org.eclipse.sirius.server.backend

This plug-in contains the core of the actual implementation of the _worklow_ feature:
* domain-level code that finds and interprets the _workflow_ elements (pages, sections, activities) associated to a particular Sirius session.
* web services (as `ISiriusServerService`) that exposes this code on well-defined REST end-points that are used by the frontend application. The web services only contain the "glue" code:
    * interpret the HTTP request paramters;
    * call the appropriate domain-level code;
    * expose the result as DTOs that will be converted to JSON automatically in the HTTP response.
