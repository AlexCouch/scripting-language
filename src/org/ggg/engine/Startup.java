package org.ggg.engine;

import org.ggg.engine.consts.EnumEngineState;

public class Startup {
    private static final Engine engine = new Engine();

    public static void main(String[] args){
        if(args.length == 0) {
            engine.start(EnumEngineState.DEBUGGER_OFF);
        }else{
            if(args[0].equals("-debug")){
                engine.start(EnumEngineState.DEBUGGER_ON);
            }
        }
    }
}
