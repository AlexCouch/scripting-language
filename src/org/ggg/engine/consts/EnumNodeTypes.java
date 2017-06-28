package org.ggg.engine.consts;

import org.ggg.engine.io.script.node.Node;

public enum EnumNodeTypes {
    START("start"),
    JUMPTO("jumpto"),
    END("end");

    String name;

    EnumNodeTypes(String name){
        this.name = name;
    }

    public String getName(){
        return ":" + this.name;
    }


    @Override
    public String toString() {
        return name;
    }
}
