package org.ggg.engine.io.script.node;

import org.ggg.engine.Engine;
import org.ggg.engine.consts.EnumNodes;
import org.ggg.engine.io.script.ScriptLoader;

public class ScriptJumpToNode extends Node{

    public static final ScriptJumpToNode INSTANCE = new ScriptJumpToNode(Engine.getScriptLoader());

    public ScriptJumpToNode(ScriptLoader loader) {
        super(EnumNodes.JUMPTO, loader);
    }

    @Override
    public <T> boolean perform(T[] params) {
        return false;
    }
}
