package org.ggg.engine.io.script.node;

import org.ggg.engine.Engine;
import org.ggg.engine.consts.EnumNodes;
import org.ggg.engine.io.script.ScriptLoader;

import java.util.ArrayList;

public class ScriptElifNode extends Node{

    public static final Node INSTANCE = new ScriptElifNode(Engine.getScriptLoader());

    public ScriptElifNode(ScriptLoader loader) {
        super(EnumNodes.ELIF, loader);
    }

    @Override
    public <T> boolean perform(T[] params) {
        if(params.length > 0) {
            if (params[0] instanceof ScriptIfNode) {
                ArrayList<T> list = new ArrayList<>();
                for(T t : params){
                    list.add(t);
                }

                list.remove(0);
                T[] tArray = list.toArray(params);
                ScriptIfNode.INSTANCE.perform(tArray);
                return true;
            }
        }
        return false;
    }
}
