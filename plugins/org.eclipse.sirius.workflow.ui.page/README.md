# org.eclipse.sirius.workflow.ui.page

This plug-in contributes a tab/page to the session editor to display the _Workflow_ of the corresponding session:
* The page is _contributed_ by `org.eclipse.sirius.workflow.ui.page.WorkflowPageProvider` using the `org.eclipse.sirius.ui.editor.sessionEditorPageProvider` extension point.
* The page's _implementation_ itself is in `org.eclipse.sirius.workflow.ui.page.WorkflowPage`, and simply sets up an embedded browser to point to the web app which actually implements the workflow's interface.
* The _web app_ itself is defined in `org.eclipse.sirius.server.frontend.workflow` for the front-end, and `org.eclipse.sirius.server.backend` for the backend.
