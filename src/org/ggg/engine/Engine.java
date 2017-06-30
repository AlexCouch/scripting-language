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

    private static ScriptLoader gameScriptLoader;

    public void start(EnumEngineState state){
        stateOfEngine = state;
        if(state == EnumEngineState.DEBUGGER_ON) {
            LOGGER.log("Engine initialization starting...", EnumLoggerTypes.SYSOUT);
        }
        try {
            gameScriptLoader = new ScriptLoader("start");
            gameScriptLoader.loadScript();
            ScriptDialog dialog = new ScriptDialog(gameScriptLoader);
            LOGGER.log(dialog.readDialog(), EnumLoggerTypes.SYSOUT);
        }catch(IOException e){
            LOGGER.log(e.getMessage(), EnumLoggerTypes.ERROR);
        }
    }

    public static ScriptLoader getScriptLoader(){
        return gameScriptLoader;
    }
}
