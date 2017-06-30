package org.ggg.engine.io.script;

import org.ggg.engine.Engine;
import org.ggg.engine.consts.EnumEngineState;
import org.ggg.engine.consts.EnumLoggerTypes;
import org.ggg.engine.consts.EnumNodes;
import org.ggg.engine.exceptions.NullScriptLoaderException;
import org.ggg.engine.io.resloc.ResourceLocation;
import org.ggg.engine.io.script.node.ScriptWaitNode;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
        String result = null;
        Pattern startpat = Pattern.compile("(:start)");
        Pattern jumptopat = Pattern.compile("(:jumpto)\\s\'[a-zA-Z_]+([0-9]*?)\'");
        Pattern waitpat = Pattern.compile("^(:wait)(\\[([a-zA-Z]+)\\]$)");
        Pattern ifpat = Pattern.compile("^(:if)(\\[([a-zA-Z|]+)\\]$)");
        Pattern elifpat = Pattern.compile("^(:if)(\\[([a-zA-Z]+)\\]$)");
        Pattern endpat = Pattern.compile("(:end)");
        String line;
        try(Scanner scanner = new Scanner(loader.getScriptFile())) {
            line = scanner.nextLine();
            if (startpat.matcher(line).matches()) {
                if(Engine.stateOfEngine == EnumEngineState.DEBUGGER_ON) {
                    Engine.LOGGER.log("Reading from: " + this.loader.getScriptFile().getName() + "\n", EnumLoggerTypes.DEBUG);
                }
                result = null;
            }
            while(scanner.hasNextLine()) {
                line = scanner.nextLine();
                if (Engine.stateOfEngine == EnumEngineState.DEBUGGER_ON) {
                    Engine.LOGGER.log("Reading dialog from script: " + loader.getScriptFile().getAbsolutePath(), EnumLoggerTypes.DEBUG);
                }
                Pattern pat = Pattern.compile("^[A-Za-z,;'\"\\s]+([.?!]*)$");
                Matcher mat = pat.matcher(line);
                if (mat.matches()) {
                    if (result == null) {
                        result = mat.group() + "\n";
                    } else {
                        result += mat.group() + "\n";
                    }
                } else if (jumptopat.matcher(line).matches()) {
                    String nodeName = EnumNodes.JUMPTO.getName();
                    String fileName = line.substring(nodeName.length()+3, line.length()-1);
                    if (fileName.contains(fileName)) {
                        File file = new File("resources/scripts/" + fileName + ".gg");
                        ResourceLocation resloc = new ResourceLocation(file.getPath());
                        if (resloc.getFile().exists()) {
                            ScriptLoader newLoader = new ScriptLoader(fileName);
                            newLoader.loadScript();
                            ScriptDialog newDialog = new ScriptDialog(newLoader);
                            result += newDialog.readDialog();
                            if(Engine.stateOfEngine == EnumEngineState.DEBUGGER_ON) {
                                Engine.LOGGER.log("=========================", EnumLoggerTypes.DEBUG);
                            }
                        }
                    }
                }else if(waitpat.matcher(line).matches()){
                    String input = line.substring(EnumNodes.WAIT.getName().length()+1, line.length()-1);
                    String[] args = new String[]{input};
                    ScriptWaitNode.INSTANCE.perform(args);
                }else if(ifpat.matcher(line).matches()){
                    String[] splitStr = line.split("|");
                }
                else if (endpat.matcher(line).matches()) {
                    scanner.close();
                    return result;
                }
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
