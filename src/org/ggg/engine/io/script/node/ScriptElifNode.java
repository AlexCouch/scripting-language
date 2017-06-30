package org.ggg.engine.io.script.node;

import org.ggg.engine.Engine;
import org.ggg.engine.consts.EnumNodes;
import org.ggg.engine.io.script.ScriptLoader;

import java.util.ArrayList;

public class ScriptElifNode extends Node{

    public static final ScriptElifNode INSTANCE = new ScriptElifNode(Engine.getScriptLoader());

    public ScriptElifNode(ScriptLoader loader) {
        super(EnumNodes.ELIF, loader);
    }

    @Override
    public boolean perform(String... params) {
        ScriptIfNode.INSTANCE.perform(params);
        return true;
    }
}
