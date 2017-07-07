package org.ggg.engine;

import org.ggg.engine.consts.EnumEngineState;
import org.ggg.engine.consts.EnumLoggerTypes;
import org.ggg.engine.io.script.ScriptDialog;
import org.ggg.engine.io.script.ScriptLoader;
import org.ggg.engine.logger.EngineLogger;

import java.io.IOException;

/**
 * The engine that is being used to parse and process the scripts. I decided to go with an engine because originally
 * this was going to be a text game engine, then it turned into a scripting language parser. I am keeping the term "engine"
 * and using it to describe the parser and processor functionality of this API. If you would like to use this in any way,
 * you can use the {@link Engine#LOGGER} to log to something. You can use {@link Engine#stateOfEngine} to check if the
 * program as an argument passed in called <em>-debug</em> which will print every step of the process out. Of course,
 * you would have to do it something like at lines <em>27-29</em>. There is also a global script loader which is used to
 * get the active script loader. I am thinking about making the script loader have a <em>close()</em> method so that
 * when one script loader has been used and it is time to load up a new script loader, then one can close while another opens
 * and we can add it to the <bold>Engine</bold>'s global script loader.
 *
 * @see EnumEngineState
 * @see EngineLogger
 * @see ScriptLoader
 * @author Alex Couch
 * @since 0.1.0
 */
public class Engine {
    public static final EngineLogger LOGGER = new EngineLogger("engine");
    public static EnumEngineState stateOfEngine;

    /**
     * There is only one script loader on at a time, hence the modifier <em>static</em>. You can call this to get the global
     * script loader
     */
    private static ScriptLoader gameScriptLoader;

    /**
     * This method is the start of the engine. This is only for when starting up the program.
     * @param state
     */
    public void start(EnumEngineState state){
        stateOfEngine = state;
        if(state == EnumEngineState.DEBUGGER_ON) {
            LOGGER.log("Welcome to the debugger version of the Aurora Scripting Language Interpreter!", EnumLoggerTypes.SYSOUT);
            LOGGER.log("Starting initialization of interpreter!", EnumLoggerTypes.SYSOUT);
        }
        try {
            gameScriptLoader = new ScriptLoader("start");
            gameScriptLoader.loadScript();
            ScriptDialog dialog = new ScriptDialog(gameScriptLoader);
            dialog.readDialog();
        }catch(IOException e){
            LOGGER.log(e.getMessage(), EnumLoggerTypes.ERROR);
        }
    }

    public static ScriptLoader getScriptLoader(){
        return gameScriptLoader;
    }
}
