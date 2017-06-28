package org.ggg.engine.io.script.node;

import org.ggg.engine.consts.EnumNodeTypes;
import org.ggg.engine.io.script.ScriptLoader;

public class ScriptStartNode extends Node{

    public ScriptStartNode(ScriptLoader loader) {
        super(EnumNodeTypes.START, loader);
    }
}
