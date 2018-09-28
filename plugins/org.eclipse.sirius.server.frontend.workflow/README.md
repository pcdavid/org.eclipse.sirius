# org.eclipse.sirius.server.frontend.workflow

This plug-in contains the compiled version of the Workflow frontend web application.

The static HTML/JS/CSS files which constitute the front-end application are in `workflow-frontend`. They are compiled from the sources available in the [sirius-components](https://github.com/eclipse/sirius-components) project on GitHub.

These static resources are contributed to the Sirius server through the `org.eclipse.sirius.server.siriusServerConfigurator` extension point. They are exposed by the server at `/workflow/static` (defined in `SiriusServerWorkflowFrontendFilter.STATIC_PATH`).
