# org.eclipse.sirius.editor.workflow

This plug-in contains the VSM editor support for creating/editing _Workflow_ elements.

* `src-gen` contains the pristine result (except for _Source > Cleanup_ post-processing) of the code generation's result. At this point, no manual customization exists, even n the "user code sections".
* `src` defines the _Workflow_ creation menu in the VSM editor, contributed via `org.eclipse.sirius.editor.menuBuilder`.
* In the `plugin.xml` file, the only changes for the generated property sections definitions are tweaks to the ordering (`afterSection`).
