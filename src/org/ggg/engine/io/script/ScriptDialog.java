package org.ggg.engine.io.script;

import org.ggg.engine.Engine;
import org.ggg.engine.consts.EnumEngineState;
import org.ggg.engine.consts.EnumLoggerTypes;
import org.ggg.engine.consts.EnumNodes;
import org.ggg.engine.exceptions.NullScriptLoaderException;
import org.ggg.engine.io.resloc.ResourceLocation;
import org.ggg.engine.io.script.node.ScriptElifNode;
import org.ggg.engine.io.script.node.ScriptIfNode;
import org.ggg.engine.io.script.node.ScriptWaitNode;
import org.ggg.engine.io.script.node.logic.consts.VariableStorage;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
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

    public void readDialog(){
        Pattern startpat = Pattern.compile("(:start)");
        Pattern jumptopat = Pattern.compile("(:jumpto)\\s\'[a-zA-Z_]+([0-9]*?)\'");
        Pattern waitpat = Pattern.compile("^(:wait)(\\[([a-zA-Z]+)\\]$)");
        Pattern ifpat = Pattern.compile("^(:if)(\\[([a-zA-Z]+)=([a-zA-Z]+)([\\s]*)\\|\\|([\\s]*)([a-zA-Z]+)=([a-zA-Z]+)([\\s]*)\\]:)");
        Pattern elifpat = Pattern.compile("^(:elif)(\\[([a-zA-Z]+)=([a-zA-Z]+)([\\s]*)\\|\\|([\\s]*)([a-zA-Z]+)=([a-zA-Z]+)([\\s]*)\\]:)");
        Pattern endpat = Pattern.compile("(:end)");
        boolean doesPriorIfExist = false;
        String line;
        try(Scanner scanner = new Scanner(loader.getScriptFile())) {
            line = scanner.nextLine();
            if (startpat.matcher(line).matches()) {
                if(Engine.stateOfEngine == EnumEngineState.DEBUGGER_ON) {
                    Engine.LOGGER.log("Reading from: " + this.loader.getScriptFile().getName() + "\n", EnumLoggerTypes.DEBUG);
                }
            }
            while(scanner.hasNextLine()) {
                line = scanner.nextLine();
                if (Engine.stateOfEngine == EnumEngineState.DEBUGGER_ON) {
                    Engine.LOGGER.log("Reading dialog from script: " + loader.getScriptFile().getAbsolutePath(), EnumLoggerTypes.DEBUG);
                }
                Pattern pat = Pattern.compile("^(([\"]*)([A-Za-z,;:'\"\\s]+([.?!]*))([\"]*))$");
                Matcher mat = pat.matcher(line);
                if (mat.matches()) {
                    Engine.LOGGER.log(line, EnumLoggerTypes.SYSOUT);
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
                            newDialog.readDialog();
                            if(Engine.stateOfEngine == EnumEngineState.DEBUGGER_ON) {
                                Engine.LOGGER.log("=========================", EnumLoggerTypes.DEBUG);
                            }
                        }
                    }
                }else if(waitpat.matcher(line).matches()){
                    int beginIndex = line.indexOf("[");
                    int endIndex = line.lastIndexOf("]");
                    String arg = line.substring(beginIndex+1, endIndex);
                    if(ScriptWaitNode.INSTANCE.perform(arg)){
                        for(String s : ScriptWaitNode.INSTANCE.inputMap.values()){
                            VariableStorage.setVars(arg, s);
                        }
                    }
                }else if(ifpat.matcher(line).matches()) {
                    String[] splitStr = line.split("[|]{2}");
                    String s1 = splitStr[0];
                    String s2 = splitStr[1];
                    String s3 = s1.substring(s1.indexOf("[")+1, s1.length());
                    String s4 = s2.substring(0, s2.indexOf("]"));
                    String[] splitStr2 = s3.split("[=]");
                    String[] splitStr3 = s4.split("[=]");
                    String s5 = splitStr2[0];
                    String s6 = splitStr2[1];
                    String s7 = splitStr3[0];
                    String s8 = splitStr3[1];
                    if(s5.trim().equals(VariableStorage.getVars(s6.trim())) || s7.trim().equals(VariableStorage.getVars(s8.trim()))){
                        ScriptIfNode.INSTANCE.perform(s3, s4);
                        doesPriorIfExist = true;
                    }else{
                        throw new IllegalArgumentException("'if' command uses unknown variable: " + ((s5.trim().equals(VariableStorage.getVars(s6.trim()))) ? s5.trim() : s7.trim()), new Throwable(line + " uses an unknown variable"));
                    }
                }else if(elifpat.matcher(line).matches()){
                    if(doesPriorIfExist) {
                        String[] splitstr = line.split("[|]{2}");
                        String s1 = splitstr[0];
                        String s2 = splitstr[1];
                        String s3 = s1.substring(s1.indexOf("[")+1);
                        String s4 = s2.substring(s2.indexOf("]")-1);
                        if(s3.equals(VariableStorage.getVars(s4))){
                            ScriptElifNode.INSTANCE.perform(s3, s4);
                        }
                    }else{
                        throw new RuntimeException("'elif' without 'if'!", new Throwable(String.valueOf(loader.getLineNum(line))));
                    }
                }else if (endpat.matcher(line).matches()) {
                    scanner.close();
                }else{
                    scanner.close();
                    throw new IllegalArgumentException(line + " is not recognizable!");
                }
            }
        }catch(IOException e){
            Engine.LOGGER.log(e.getMessage(), EnumLoggerTypes.ERROR);
        }
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
