package org.ggg.engine.io.script;

import org.ggg.engine.Engine;
import org.ggg.engine.consts.EnumEngineState;
import org.ggg.engine.consts.EnumLoggerTypes;
import org.ggg.engine.exceptions.NullScriptLoaderException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class ScriptDialog {
    private ScriptLoader loader;

    public ScriptDialog(ScriptLoader loader){
        this.loader = loader;
    }

    public ScriptLoader getScriptLoader(){
        if(loader == null){
            throw new NullScriptLoaderException("There is no script loader initialized!");
        }else{
            return loader;
        }
    }

    public long getTotalLines(){
        try(Stream<String> lines = Files.lines(this.getScriptLoader().getScriptFile().toPath(), Charset.defaultCharset())){
            return lines.count();
        }catch(IOException e){
            Engine.LOGGER.log(e.getMessage(), EnumLoggerTypes.ERROR);
        }
        return 0;
    }

    public String readDialog(){
        try(Scanner scanner = new Scanner(loader.getScriptFile())) {
            if(Engine.stateOfEngine == EnumEngineState.DEBUGGER_ON) {
                Engine.LOGGER.log("Reading dialog from script: " + loader.getScriptFile().getAbsolutePath(), EnumLoggerTypes.DEBUG);
            }
            while(scanner.hasNext()){
                return loader.getBufferedReader().readLine();
            }
        }catch(IOException e){
            Engine.LOGGER.log(e.getMessage(), EnumLoggerTypes.ERROR);
        }
        return null;
    }

    public String getDialogAtLine(int i, File file) {
        try(Stream<String> lines = Files.lines(Paths.get(file.getAbsolutePath()))){
            if(Engine.stateOfEngine == EnumEngineState.DEBUGGER_ON) {
                Engine.LOGGER.log("Reading line from script: " + loader.getScriptFile().getAbsolutePath(), EnumLoggerTypes.DEBUG);
            }
            return lines.skip(i).findFirst().orElse(null);
        }catch(IOException e){
            Engine.LOGGER.log(e.getMessage(), EnumLoggerTypes.ERROR);
        }
        return null;
    }
}
