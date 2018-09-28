# org.eclipse.sirius.workflow.model

This plug-in defines the VSM metamodel extension which allows specifiers to define/configure _Workflows_, i.e. organized groups of actions that can be performed globally on a modeling project. Individual actions are called _Activities_, and are organized in _Sections_ which are themselves grouped in _Pages_. These elements could be presented in different ways in the UI, but as of Sirius 6.1 the only presentation is via an additional tab in the _Session Editor_ which uses an embedded web browser to render the UI.

This plug-in only contains the metamodel definition (`model/workflow.ecore`) and the corresponding generated code (`src-gen`). The metamodel's implementation contains no manual customization (except for _Source > Cleanup_): it is the pristine result of the EMF generation. This should stay true in the future.

Related plug-ins:
* `org.eclipse.sirius.workflow.edit`: _EMF Edit_ support for this metamodel.
* `org.eclipse.sirius.editor.workflow`: the VSM editor contribution to author Workflow definitions.
* `org.eclipse.sirius.server.backend`: this is where the code that interprets the workflow definitions at runtime is currently hosted. If will be moved in its own plug-in at some later point.
* `org.eclipse.sirius.server.frontend.workflow`: the web-based frontend, based on the [sirius-components](https://github.com/eclipse/sirius-components) "framework" and its contribution/integration in the server.
* `org.eclipse.sirius.workflow.ui.page`: implements the additional tab in the _Session Editor_ which displays the workflow (along with some additional, general information about the session).
