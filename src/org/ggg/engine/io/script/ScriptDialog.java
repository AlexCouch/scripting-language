package org.ggg.engine.io.script;

import org.ggg.engine.Engine;
import org.ggg.engine.consts.EnumEngineState;
import org.ggg.engine.consts.EnumLoggerTypes;
import org.ggg.engine.consts.EnumNodeTypes;
import org.ggg.engine.exceptions.NullScriptLoaderException;
import org.ggg.engine.exceptions.NullScriptNodeException;
import org.ggg.engine.io.script.node.Node;
import org.ggg.engine.io.script.node.ScriptEndNode;
import org.ggg.engine.io.script.node.ScriptJumpToNode;
import org.ggg.engine.io.script.node.ScriptStartNode;

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
        Pattern startpat = Pattern.compile(EnumNodeTypes.START.getName());
        Pattern jumptopat = Pattern.compile(EnumNodeTypes.JUMPTO.getName());
        Pattern endpat = Pattern.compile(EnumNodeTypes.END.getName());
        try(Scanner scanner = new Scanner(loader.getScriptFile())) {
            String line = scanner.nextLine();
            if (startpat.matcher(line).matches()) {
                while(scanner.hasNextLine()){
                    Node node = this.loader.getNode(line);
                    if (Engine.stateOfEngine == EnumEngineState.DEBUGGER_ON) {
                        Engine.LOGGER.log("Reading dialog from script: " + loader.getScriptFile().getAbsolutePath(), EnumLoggerTypes.DEBUG);
                    }
                    Pattern pat = Pattern.compile("^[A-Za-z,;'\"\\s]+[.?!]$");
                    Matcher mat = pat.matcher(line);
                    if (mat.matches()) {
                        if (result == null) {
                            result = mat.group() + "\n";
                        }
                        else {
                            result += mat.group() + "\n";
                        }
                    }else if (jumptopat.matcher(line).matches()) {
                        String l = scanner.next(node.getName());
                        String fileName = l.substring(node.getName().length());
                        Pattern p = Pattern.compile("[a-zA-Z]");
                        if (fileName.contains("'" + p.pattern() + ".gg'")) {
                            File file = new File(p.pattern() + ".gg");
                            if (file.exists()) {
                                ScriptLoader newLoader = new ScriptLoader(file.getName());
                                newLoader.loadScript();
                                ScriptDialog newDialog = new ScriptDialog(newLoader);
                                newDialog.readDialog();
                            }
                        }
                    }else if (endpat.matcher(line).matches()) {
                        scanner.close();
                        return result;
                    }
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
