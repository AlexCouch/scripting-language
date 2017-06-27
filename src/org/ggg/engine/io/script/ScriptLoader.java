package org.ggg.engine.io.script;

import org.ggg.engine.Engine;
import org.ggg.engine.consts.EnumEngineState;
import org.ggg.engine.consts.EnumLoggerTypes;
import org.ggg.engine.io.resloc.ResourceLocation;

import java.io.*;

public class ScriptLoader {
    private ResourceLocation resloc;
    private File file;
    private FileReader fr;
    private BufferedReader br;

    public ScriptLoader(String path) throws IOException{
        resloc = new ResourceLocation("resources/scripts/" + path + ".gg");
        if(Engine.stateOfEngine == EnumEngineState.DEBUGGER_ON) {
            Engine.LOGGER.log("Loading scripting: " + resloc.getFile().getAbsolutePath(), EnumLoggerTypes.DEBUG);
        }
        loadScript();
    }

    public File getScriptFile(){
        if(file == null){
            throw new RuntimeException("You cannot use a file that doesn't exist!");
        }else{
            return this.file;
        }
    }

    public FileReader getFileReader(){
        if(fr == null){
            throw new RuntimeException("Cannot load a file reader while it is null!");
        }else{
            return fr;
        }
    }

    public BufferedReader getBufferedReader(){
        if(br == null) {
            throw new RuntimeException("Cannot load a buffered reader while it is null!");
        }else{
            return br;
        }
    }

    public void loadScript() throws IOException{
        file = resloc.getFile();
        fr = new FileReader(file);
        br = new BufferedReader(fr);
        if(Engine.stateOfEngine == EnumEngineState.DEBUGGER_ON) {
            Engine.LOGGER.log("\tScript Loaded!", EnumLoggerTypes.DEBUG);
        }
    }
}
