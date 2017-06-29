package org.ggg.engine.io.script.node;

import org.ggg.engine.Engine;
import org.ggg.engine.consts.EnumLoggerTypes;
import org.ggg.engine.consts.EnumNodeTypes;
import org.ggg.engine.io.script.ScriptLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.stream.Stream;

public abstract class Node {

    private static ScriptLoader scriptLoader;
    private static EnumNodeTypes node;

    public Node(EnumNodeTypes nodeType, ScriptLoader loader){
        scriptLoader = loader;
        node = nodeType;
    }

    public long getLineNumber(EnumNodeTypes node){
        try(Scanner scanner = new Scanner(scriptLoader.getScriptFile()); Stream<String> stream = Files.lines(Paths.get(scriptLoader.getScriptFile().getAbsolutePath()))){
            String line;
            while((line = scanner.nextLine()) != null){
               if(line.startsWith(node.getName())){
                   return stream.count();
               }
            }
        }catch(IOException e){
            Engine.LOGGER.log(e.getMessage(), EnumLoggerTypes.ERROR);
        }
        return 0;
    }

    public String getName(){
        return node.getName();
    }

    public Node getNode(String name){
        if(getName().equals(name)){
            return this;
        }else{
            throw new RuntimeException("No such node by name of " + name);
        }
    }
}
