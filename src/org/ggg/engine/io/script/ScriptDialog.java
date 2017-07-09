package org.ggg.engine.io.script;

import org.ggg.engine.Engine;
import org.ggg.engine.consts.EnumEngineState;
import org.ggg.engine.exceptions.NullScriptLoaderException;
import org.ggg.engine.io.script.node.*;
import org.ggg.engine.io.script.node.logic.CompareIfInput;
import org.ggg.engine.io.script.node.logic.consts.LoaderStorage;
import org.ggg.engine.io.script.node.logic.consts.VariableStorage;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.ggg.engine.consts.EnumLoggerTypes.*;

/**
 * This is the class that reads and processes the dialog from the scripts.
 * @author Alex Couch
 * @since 0.1.0
 */
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

    /**
     * This gets the total number of lines in the script. This is useful for when you need to do some maths, but not
     * really useful right now, but it is made for future use, just in case we need it.
     * @return the total number of lines in the script
     */
    public long getTotalLines(){
        try(Stream<String> lines = Files.lines(this.getScriptLoader().getScriptFile().toPath(), Charset.defaultCharset())){
            return lines.count();
        }catch(IOException e){
            Engine.LOGGER.log(e.getMessage(), ERROR);
        }
        return 0;
    }

    /**
     * Reads and processes the script by passing each line of text into a regex pattern. Right now, there are start, jumpto,
     * wait, if, elif, and end patterns. There are also patterns for checking if it is plain text as well. If the line is
     * plain text in between a <em>:start</em> and an <em>:end</em> nodes, then it will print out to the console. If it
     * matches against the <em>:jumpto</em> node pattern, then it will jump to a new script that is passed into the pattern.
     * The <em>:wait</em> node is used to retrieve input from the user in some way. The <em>:if[...]</em> node is to
     * compare conditions, which an implementation that is currently be worked in variables that are created by the
     * <em>:wait</em> command and can be used to compare against certain values. The <em>:elif[...]</em> command is the
     * same as the <em>:if[...]</em> but only if there is an <em>:if[...]</em> node before it. Soon there will be a node
     * that closes the <em>if branches</em> as to not confuse <em>elif's</em> that have nothing to do with another <em>if</em>
     * block. Lastly, the <em>:end</em> node, ends a sections of the script. A future implementation of the <em>:start and :end</em>
     * nodes will make it possible to create different dialogs from different <em>characters</em> like a conversation.
     */
    public void readDialog(){
        Pattern startpat = Pattern.compile("(:start)");
        Pattern jumptopat = Pattern.compile("(:jumpto)\\s\'[a-zA-Z_]+([0-9]*?)\'");
        Pattern waitpat = Pattern.compile("^(:wait)(\\[([a-zA-Z0-9]+)\\]$)");
        Pattern ifpat = Pattern.compile("(:if)\\[((\\s)?([a-zA-Z0-9]+)(!)?=([a-zA-Z0-9]+)(\\s)?([&]{2})?([|]{2})?(\\s)?)+\\]:");
        Pattern elifpat = Pattern.compile("^(:elif)(\\[([a-zA-Z]+)=([a-zA-Z]+)([\\s]*)\\|\\|([\\s]*)([a-zA-Z]+)=([a-zA-Z]+)([\\s]*)\\]:)");
        Pattern endpat = Pattern.compile("(:end)");
        Pattern returnpat = Pattern.compile("^(:return)\\[(['])(([a-zA-Z]+)([a-zA-Z0-9_]*))(['])\\]$");
        boolean doesPriorIfExist = false;
        boolean didPriorIfComplete = false;
        String line;
        try(Scanner scanner = new Scanner(loader.getScriptFile())) {
            line = scanner.nextLine();
            if (startpat.matcher(line).matches()) {
                if(Engine.stateOfEngine == EnumEngineState.DEBUGGER_ON) {
                    Engine.LOGGER.log("Reading from: " + this.loader.getScriptFile().getName() + "\n", DEBUG);
                }
            }
            while(scanner.hasNextLine()) {
                line = scanner.nextLine();
                if (Engine.stateOfEngine == EnumEngineState.DEBUGGER_ON) {
                    Engine.LOGGER.log("Reading dialog from script: " + loader.getScriptFile().getAbsolutePath(), DEBUG);
                }
                Pattern pat = Pattern.compile("^(([\"]*)([A-Za-z,;'\"\\s]+([:.?!]*))([\"]*))$");
                Matcher mat = pat.matcher(line);
                if (mat.matches()) {
                    Engine.LOGGER.log(line, SYSOUT);
                } else if (jumptopat.matcher(line).matches()) {
                    if(ScriptJumpToNode.INSTANCE.perform(line)){
                        LoaderStorage.setFile(this.loader.getScriptFile().getName(), this.loader.getLineNum(line));
                    }else{
                        throw new NoSuchFileException("Was not able to load file at: '" + ScriptJumpToNode.INSTANCE.getFileToJumpTo().getName() + "'!");
                    }
                }else if(waitpat.matcher(line).matches()){
                    int beginIndex = line.indexOf("[");
                    int endIndex = line.lastIndexOf("]");
                    String arg = line.substring(beginIndex+1, endIndex);
                    if(ScriptWaitNode.INSTANCE.isInt){
                        ScriptWaitNode.INSTANCE.perform(arg);
                    }else if(ScriptWaitNode.INSTANCE.perform(arg)){
                        for(String s : ScriptWaitNode.INSTANCE.inputMap.values()){
                            VariableStorage.setVars(arg, s);
                        }
                    }
                }else if(ifpat.matcher(line).matches()) {
//                    List<String> list = StringUtils.complexSplit(line, "[&]{2}", "[|]{2}");
                    String[] array = (checkForCompat(line).isEmpty() ? null : (String[])checkForCompat(line).toArray());
                    if(ScriptIfNode.INSTANCE.perform(array)){
                        doesPriorIfExist = true;
                        didPriorIfComplete = true;
                    }else{
                        doesPriorIfExist = false;
                        didPriorIfComplete = false;
                    }
                }else if(elifpat.matcher(line).matches()){
                    if(doesPriorIfExist) {
                    	if(!didPriorIfComplete) {
                    		String[] splitstr = line.split("[|]{2}");
                    		String s1 = splitstr[0];
                    		String s2 = splitstr[1];
                    		String s3 = s1.substring(s1.indexOf("[")+1, s1.length());
                    		String s4 = s2.substring(0, s2.indexOf("]"));
                            String[] splitStr2;
                            String[] splitStr3;
                    		if(s3.contains("==")){
                    		    throw new IllegalArgumentException("'==' is not recognized by the interpreter!");
                            }else if(s3.matches("([a-zA-Z]+([=]{1})([a-zA-Z]+))")){
                                splitStr2 = s3.split("[=]");
                                splitStr3 = s4.split("[=]");
                            }else{
                                splitStr2 = s3.split("[=]");
                                splitStr3 = s4.split("[=]");
                            }
                    		String s5 = splitStr2[0];
                    		String s6 = splitStr2[1];
                    		String s7 = splitStr3[0];
                    		String s8 = splitStr3[1];
                    		for(String key: VariableStorage.getKeys()) {
                            	if(s5.trim().equals(key)) {
                            		if(ScriptElifNode.INSTANCE.perform(s5, s6)) {
                            			doesPriorIfExist = true;
                            			didPriorIfComplete = true;
                            		}else {
                                        doesPriorIfExist = true;
                                        didPriorIfComplete = false;
                                    }
                            	}
                            	else if(s7.trim().equals(key)) {
                            		if(ScriptElifNode.INSTANCE.perform(s7, s8)) {
                                		doesPriorIfExist = true;
                                		didPriorIfComplete = true;
                            		}else {
                                        doesPriorIfExist = true;
                                        didPriorIfComplete = false;
                                    }
                            	}else {
                            		throw new IllegalArgumentException("'elif' command uses unknown key: " + (key != null ? s5.trim() : s7.trim()), new Throwable(line + " uses an unknown key"));
                            	}
                            }
                    	}
                    	else {
                    		scanner.nextLine();
                    	}
                    }else{
                        throw new RuntimeException("'elif' without 'if'!", new Throwable(String.valueOf(loader.getLineNum(line))));
                    }
                }else if(returnpat.matcher(line).matches()){
                    Set<String> set = LoaderStorage.getKeys();
                    String filename = line.substring(line.indexOf("'"), line.lastIndexOf("'"));
                    for(String s : set){
                        if(s.equals(filename)){
                            ScriptReturnNode.INSTANCE.perform(filename);
                        }
                    }
                }else if (endpat.matcher(line).matches()) {
                    scanner.close();
                    break;
                }else{
                    scanner.close();
                    throw new IllegalArgumentException(line + " is not recognizable!");
                }
            }
        }catch(IOException e){
            Engine.LOGGER.log(e.getMessage(), ERROR);
        }
    }

    /**
     * This method returns the text as a string at the line in the specified file.
     * @param i the line to find
     * @param file the file to search
     * @return the line of text at the specified line
     */
    public String getDialogAtLine(int i, File file) {
        try(Stream<String> lines = Files.lines(Paths.get(file.getAbsolutePath()))){
            if(Engine.stateOfEngine == EnumEngineState.DEBUGGER_ON) {
                Engine.LOGGER.log("Reading line from script: " + loader.getScriptFile().getAbsolutePath(), DEBUG);
            }
            return lines.skip(i).findFirst().orElse(null);
        }catch(IOException e){
            Engine.LOGGER.log(e.getMessage(), ERROR);
        }
        return null;
    }

    private List<String> checkForCompat(String line){
        String[] res = line.split("\\s");
        String regex = "([a-zA-Z0-9]+)=([a-zA-Z0-9]+)";
        List<String> list = new ArrayList<>();
        for(int i = 0; i < res.length; i++) {
            if(i*3 >= res.length) break;
            String var = res[i*3];
            list.add(var);
            String op = res[(1+i)*3];
            list.add(op);
            String val = res[(2+i)*3];
            list.add(val);
            if(var.matches(regex) && op.matches("\\|\\||&&") && val.matches(regex)) {
                return list;
            }
        }
        return list;
    }
}
