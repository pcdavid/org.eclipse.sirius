# org.eclipse.sirius.workflow.edit

This plug-in contains the _EMF Edit_ support (item providers) for elements of the _Workflow_ metamodel defined in `org.eclipse.sirius.workflow.model`.

The code follows the _generation gap_ pattern to isolate manual customizations from the default EMF-generated code.
* `src-gen`: pristine result (except for formatting) of the EMF code generation (from `org.eclipse.sirius.workflow.model/model/workflow.genmodel`).
* `src-spec`: `*Spec` classes, one for each item provider in `src-gen`, and one for the adapter factory. To customize an item provider, override the corresponding method(s) in these, **do not modify the code in src-gen**).
* `src-generator`: the custom generator that has been used to initialize the contents of `src-spec`. It is kept for reference, but unless the metamodel changes significantly it is not necessary to re-run it. For example if a single new type is added, its `ItemProviderSpec` can be created manually (in this case do not forget to update `WorkflowItemProviderAdapterFactorySpec` too!). 

The `plugin.xml` is configured so that the `WorkflowItemProviderAdapterFactorySpec` (which creates the `Spec` item providers) is registered instead of the default `WorkflowItemProviderAdapterFactory` from `src-gen`. Note that when regenerating from `workflow.genmodel`, EMF will re-create the `org.eclipse.emf.edit.itemProviderAdapterFactories` and `org.eclipse.emf.edit.childCreationExtenders` extensions. **They must be removed manually** so that only the `@generated NOT` versions which point to the `Spec` classes are configured.
