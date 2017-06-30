package org.ggg.engine.io.script.node;

import org.ggg.engine.Engine;
import org.ggg.engine.consts.EnumNodes;
import org.ggg.engine.io.script.ScriptLoader;

public class ScriptEndNode extends Node{

    public static final ScriptEndNode INSTANCE = new ScriptEndNode(Engine.getScriptLoader());

    public ScriptEndNode(ScriptLoader loader) {
        super(EnumNodes.END, loader);
    }

    @Override
    public boolean perform(String... params) {
        return true;
    }
}
