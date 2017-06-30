package org.ggg.engine.io.script.node;

import org.ggg.engine.Engine;
import org.ggg.engine.consts.EnumLoggerTypes;
import org.ggg.engine.consts.EnumNodes;
import org.ggg.engine.io.script.ScriptLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.stream.Stream;

/**
 * The node class is the <em>template</em> for any node that is being implemented by the program. It's constructor takes in
 * a node type, to signify the node that is being implemented so that it can be used and manipulated through it's
 * respective enum constant. The constructor also takes in the script loader as a parameter. This is because
 * we need to be able to find where at in the script the node is so that we can then do something with it, like print it
 * to the screen upon crash/error. The methods are then {@link Node#getLineNumber(EnumNodes)}, {@link Node#getName()},
 * {@link Node#getNode(String)} which can then be used by the programmer at will.
 */
public abstract class Node {

    private static ScriptLoader scriptLoader;
    private static EnumNodes node;

    public Node(EnumNodes nodeType, ScriptLoader loader){
        scriptLoader = loader;
        node = nodeType;
    }

    /**
     * This method is used to return the line number in which a node is currently present. Right now, as it stands,
     * it only returns the first node that it finds.
     * @param node the node to find
     * @return the line number
     */
    public long getLineNumber(EnumNodes node){
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

    /**
     * This is a method that all nodes must implement in order to function as a node. {@link ScriptWaitNode} implements
     * this to wait for input. {@link ScriptIfNode} implements this to compare inputs to values. This method takes in
     * a dynamic amount of strings, which can then be processed. For example, the {@link ScriptIfNode} uses a dynamic
     * amount of parameters: <em>the if statement being processed by the program</em> which is seen as <pre><code>
     *     :if[input=value||input2=value2&&input3=value3]:
     * </code></pre> as the format.
     * @param params the values needing to process the command
     * @return whether the command was performed successfully or not
     */
    public abstract boolean perform(String... params);
}
