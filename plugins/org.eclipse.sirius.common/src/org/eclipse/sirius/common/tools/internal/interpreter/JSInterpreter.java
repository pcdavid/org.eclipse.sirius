package org.eclipse.sirius.common.tools.internal.interpreter;

import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleBindings;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.sirius.common.tools.api.interpreter.EvaluationException;
import org.eclipse.sirius.common.tools.api.interpreter.IInterpreter;
import org.eclipse.sirius.common.tools.api.interpreter.IInterpreterProvider;

import com.google.common.collect.Maps;

// CHECKSTYLE:OFF
public class JSInterpreter extends AbstractInterpreter implements org.eclipse.sirius.common.tools.api.interpreter.IInterpreter, IInterpreterProvider {
    private final ScriptEngine js;

    private final SimpleBindings variables = new SimpleBindings();

    public JSInterpreter() {
        this.js = new ScriptEngineManager().getEngineByName("JavaScript");
    }

    @Override
    public IInterpreter createInterpreter() {
        return new JSInterpreter();
    }

    @Override
    public void setVariable(String name, Object value) {
        variables.put(name, value);
    }

    @Override
    public Object getVariable(String name) {
        return variables.get(name);
    }

    @Override
    public void clearVariables() {
        variables.clear();
    }

    public Map<String, Object> getVariables() {
        return Maps.newHashMap(variables);
    }

    @Override
    public synchronized Object evaluate(EObject target, String expression) throws EvaluationException {
        Object oldSelf = variables.get("self");
        try {
            variables.put("self", target);
            return js.eval(expression.substring(getPrefix().length()), variables);
        } catch (ScriptException e) {
            e.printStackTrace();
            return null;
        } finally {
            variables.put("self", oldSelf);
        }
    }

    @Override
    public String getPrefix() {
        return "js:";
    }
}
