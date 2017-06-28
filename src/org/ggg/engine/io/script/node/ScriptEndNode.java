package org.ggg.engine.io.script.node;

import org.ggg.engine.consts.EnumNodeTypes;
import org.ggg.engine.io.script.ScriptLoader;

public class ScriptEndNode extends Node{
    public ScriptEndNode(ScriptLoader loader) {
        super(EnumNodeTypes.END, loader);
    }

}
