package org.ggg.engine.io.script;

import org.ggg.engine.Engine;
import org.ggg.engine.consts.EnumEngineState;
import org.ggg.engine.consts.EnumLoggerTypes;
import org.ggg.engine.consts.EnumNodes;
import org.ggg.engine.exceptions.NullScriptLoaderException;
import org.ggg.engine.io.resloc.ResourceLocation;
import org.ggg.engine.io.script.node.Node;
import org.ggg.engine.io.script.node.ScriptElifNode;
import org.ggg.engine.io.script.node.ScriptIfNode;
import org.ggg.engine.io.script.node.ScriptWaitNode;

import java.io.BufferedReader;
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
        Set<String> inputValues = new HashSet<>();
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
                    String[] strs = new String[]{arg};
                    if(ScriptWaitNode.INSTANCE.perform(strs)){
                        for(Map<String, String> map : ScriptWaitNode.INSTANCE.inputList){
                            for(String str : map.keySet()){
                                inputValues.add(str);
                            }
                        }
                    }
                }else if(ifpat.matcher(line).matches()) {
                    String[] splitStr = line.split("[|]{2}");
                    for(String s : splitStr) {
                        String str1, str2;
                        if(s.contains(":if[")){
                            str1 = s.substring(s.indexOf("["));
                            if (ScriptIfNode.INSTANCE.perform(str1)) {
                                doesPriorIfExist = true;
                            }
                        }else if(s.contains("]:")){
                            str2 = s.substring(s.indexOf("]"));
                            if (ScriptIfNode.INSTANCE.perform(str2)) {
                                doesPriorIfExist = true;
                            }
                        }
                    }
                }else if(elifpat.matcher(line).matches()){
                    if(doesPriorIfExist) {
                        String[] splitstr = line.split("[|]{2}");
                        for(String s : splitstr){
                            String str1 = null, str2 = null;
                            if(s.contains(":elif[")){
                                str1 = splitstr[0].substring(s.indexOf("["));
                            }else if(s.contains("]:")){
                                str2 = s.substring(s.indexOf("]"));
                            }
                            ScriptElifNode.INSTANCE.perform(str1, str2);
                        }
                    }else{
                        throw new RuntimeException("'elif' without 'if'!", new Throwable(String.valueOf(loader.getLineNum(line))));
                    }
                }else if (endpat.matcher(line).matches()) {
                    scanner.close();
                }else{
                    scanner.close();
                    throw new RuntimeException(line + " is not recognizable!");
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
