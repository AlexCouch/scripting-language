package org.ggg.engine.consts;

import org.ggg.engine.io.script.node.*;

public enum EnumNodes {
    START("start", ScriptStartNode.INSTANCE),
    JUMPTO("jumpto", ScriptJumpToNode.INSTANCE),
    WAIT("wait", ScriptWaitNode.INSTANCE),
    IF("if", ScriptIfNode.INSTANCE),
    ELIF("elif", ScriptElifNode.INSTANCE),
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

    @Override
    public String toString() {
        return this.getName();
    }
}
