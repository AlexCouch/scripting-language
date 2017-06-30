package org.ggg.engine.io.script.node;

import org.ggg.engine.Engine;
import org.ggg.engine.consts.EnumNodes;
import org.ggg.engine.io.script.ScriptLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ScriptIfNode extends Node{

    public static final ScriptIfNode INSTANCE = new ScriptIfNode(Engine.getScriptLoader());

    public ScriptIfNode(ScriptLoader loader) {
        super(EnumNodes.IF, loader);
    }

    @Override
    public <T> boolean perform(T[]params) {
        String value = null;
        List<List> list = new ArrayList<>();
        if(params[0] instanceof List){
            List l = (List)params[0];
            list.add(l);
            if(params[1] instanceof Set) {
                Set set = (Set) params[1];
                for (Object o : set) {
                    if (o instanceof String) {
                        value = (String)o;
                    }
                }
            }

            List condlist = list.get(0);
            if(value != null) {
                for(Object obj : condlist) {
                    if(obj instanceof String) {
                        String str = (String)obj;
                        if (Boolean.parseBoolean(str)) {
                            String[] s = str.split("=");
                            String right = s[1];
                            if (right.equals(value)) {
                                return true;
                            } else {
                                return false;
                            }
                        } else {
                            return false;
                        }
                    }
                }
            }
        }
        return false;
    }
}
