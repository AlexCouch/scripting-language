package org.ggg.engine.io.script.node;

import org.ggg.engine.Engine;
import org.ggg.engine.consts.EnumNodeTypes;
import org.ggg.engine.io.script.ScriptLoader;

public class ScriptStartNode extends Node{

    public static final Node INSTANCE = new ScriptStartNode(Engine.getScriptLoader());

    public ScriptStartNode(ScriptLoader loader) {
        super(EnumNodeTypes.START, loader);
    }
}
