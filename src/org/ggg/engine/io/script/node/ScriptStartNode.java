package org.ggg.engine.io.script.node;

import org.ggg.engine.Engine;
import org.ggg.engine.consts.EnumNodes;
import org.ggg.engine.io.script.ScriptLoader;

public class ScriptStartNode extends Node{

    public static final ScriptStartNode INSTANCE = new ScriptStartNode(Engine.getScriptLoader());

    public ScriptStartNode(ScriptLoader loader) {
        super(EnumNodes.START, loader);
    }

    @Override
    public <T> boolean perform(T[] params) {
        return false;
    }
}
