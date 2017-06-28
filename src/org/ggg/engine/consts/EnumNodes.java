package org.ggg.engine.consts;

import org.ggg.engine.Engine;
import org.ggg.engine.io.script.node.Node;
import org.ggg.engine.io.script.node.ScriptEndNode;
import org.ggg.engine.io.script.node.ScriptJumpToNode;
import org.ggg.engine.io.script.node.ScriptStartNode;

public enum EnumNodes {
    START("start", ScriptStartNode.INSTANCE),
    JUMPTO("jumpto", ScriptJumpToNode.INSTANCE),
    END("end", ScriptEndNode.INSTANCE);

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
