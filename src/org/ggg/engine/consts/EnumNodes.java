package org.ggg.engine.consts;

import org.ggg.engine.Engine;
import org.ggg.engine.io.script.node.Node;
import org.ggg.engine.io.script.node.ScriptStartNode;

/**
 * Created by Alex on 6/27/2017.
 */
public enum EnumNodes {
    START("start", new ScriptStartNode(Engine.getScriptLoader())),
    JUMPTO("start", new ScriptStartNode(Engine.getScriptLoader())),
    END("start", new ScriptStartNode(Engine.getScriptLoader()));

    String name;
    Node node;

    EnumNodes(String name, Node node){
        this.name = name;
        this.node = node;
    }

    public String getName(){
        return this.name;
    }

    public Node getNode(){
        return this.node;
    }
}
