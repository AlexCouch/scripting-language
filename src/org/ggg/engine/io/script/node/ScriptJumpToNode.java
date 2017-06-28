package org.ggg.engine.io.script.node;

import org.ggg.engine.consts.EnumNodeTypes;
import org.ggg.engine.io.script.ScriptLoader;

public class ScriptJumpToNode extends Node{
    public ScriptJumpToNode(ScriptLoader loader) {
        super(EnumNodeTypes.JUMPTO, loader);
    }
}
