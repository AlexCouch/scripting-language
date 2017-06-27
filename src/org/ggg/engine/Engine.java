package org.ggg.engine;

import org.ggg.engine.consts.EnumEngineState;
import org.ggg.engine.consts.EnumLoggerTypes;
import org.ggg.engine.io.script.ScriptDialog;
import org.ggg.engine.io.script.ScriptLoader;
import org.ggg.engine.logger.EngineLogger;

import java.io.IOException;

public class Engine {
    public static final EngineLogger LOGGER = new EngineLogger("engine");
    public static EnumEngineState stateOfEngine;

    public void start(EnumEngineState state){
        stateOfEngine = state;
        if(state == EnumEngineState.DEBUGGER_ON) {
            LOGGER.log("Engine initialization starting...", EnumLoggerTypes.SYSOUT);
        }
        try {
            ScriptLoader loader = new ScriptLoader("test");
            ScriptDialog dialog = new ScriptDialog(loader);
            for(int i = 0; i < dialog.getTotalLines(); i++) {
                LOGGER.log(dialog.getDialogAtLine(i, loader.getScriptFile()), EnumLoggerTypes.SYSOUT);
            }
        }catch(IOException e){
            LOGGER.log(e.getMessage(), EnumLoggerTypes.ERROR);
        }
    }
}
