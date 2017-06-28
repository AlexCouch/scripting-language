package org.ggg.engine.io.script.node;

import org.ggg.engine.Engine;
import org.ggg.engine.consts.EnumNodeTypes;
import org.ggg.engine.io.script.ScriptLoader;

public class ScriptEndNode extends Node{

    public static final Node INSTANCE = new ScriptStartNode(Engine.getScriptLoader());

    public ScriptEndNode(ScriptLoader loader) {
        super(EnumNodeTypes.END, loader);
    }

}
