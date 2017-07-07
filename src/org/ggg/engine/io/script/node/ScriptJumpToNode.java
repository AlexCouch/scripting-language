package org.ggg.engine.io.script.node;

import org.ggg.engine.Engine;
import org.ggg.engine.consts.EnumEngineState;
import org.ggg.engine.consts.EnumLoggerTypes;
import org.ggg.engine.consts.EnumNodes;
import org.ggg.engine.io.resloc.ResourceLocation;
import org.ggg.engine.io.script.ScriptDialog;
import org.ggg.engine.io.script.ScriptLoader;

import java.io.File;
import java.io.IOException;

public class ScriptJumpToNode extends Node{

    public static final ScriptJumpToNode INSTANCE = new ScriptJumpToNode(Engine.getScriptLoader());

    private static File fileToJumpTo = null;

    public ScriptJumpToNode(ScriptLoader loader) {
        super(EnumNodes.JUMPTO, loader);
    }

    @Override
    public boolean perform(String... params) {
        String var = params[0];
        String filename;
        if(var.contains("'")) {
            filename = var.substring(var.indexOf("'")+1, var.lastIndexOf("'"));
        }else {
            filename = var;
        }
        if (filename.contains(filename)) {
            File file = new File("resources/scripts/" + filename + ".as");
            ResourceLocation resloc = new ResourceLocation(file.getPath());
            if (resloc.getFile().exists()) {
                fileToJumpTo = resloc.getFile();
                try {
                    ScriptLoader newLoader = new ScriptLoader(filename);
                    newLoader.loadScript();
                    ScriptDialog newDialog = new ScriptDialog(newLoader);
                    newDialog.readDialog();
                    if (Engine.stateOfEngine == EnumEngineState.DEBUGGER_ON) {
                        Engine.LOGGER.log("=========================", EnumLoggerTypes.DEBUG);
                    }
                    return true;
                } catch (IOException e) {
                    Engine.LOGGER.log(e.getMessage(), EnumLoggerTypes.ERROR);
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public File getFileToJumpTo(){
        return fileToJumpTo;
    }
}
