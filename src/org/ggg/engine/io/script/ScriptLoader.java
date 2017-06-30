package org.ggg.engine.io.script;

import org.ggg.engine.Engine;
import org.ggg.engine.consts.EnumEngineState;
import org.ggg.engine.consts.EnumLoggerTypes;
import org.ggg.engine.consts.EnumNodes;
import org.ggg.engine.io.resloc.ResourceLocation;
import org.ggg.engine.io.script.node.Node;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class ScriptLoader {
    private ResourceLocation resloc;
    private File file;
    private FileReader fr;
    private BufferedReader br;
    private Map<String, Node> nodes;

    public ScriptLoader(String path) throws IOException{
        resloc = new ResourceLocation("resources/scripts/" + path + ".gg");
        if(Engine.stateOfEngine == EnumEngineState.DEBUGGER_ON) {
            Engine.LOGGER.log("Loading scripting: " + resloc.getFile().getAbsolutePath(), EnumLoggerTypes.DEBUG);
        }
    }

    public long getLineNum(String startsWith){
        try(Scanner scanner = new Scanner(file); Stream<String> stream = Files.lines(Paths.get(this.file.getAbsolutePath()))){
            while(scanner.hasNext()){
                String line;
                while((line = scanner.nextLine()) != null) {
                    if (line.startsWith(startsWith)) {
                        return stream.count();
                    }
                }
            }
        } catch (IOException e) {
            Engine.LOGGER.log(e.getMessage(), EnumLoggerTypes.ERROR);
        }
        return 0;
    }

    public void setNodes(){
        for(EnumNodes node : EnumNodes.values()) {
            nodes.put(node.getName(), node.getNode());
        }
    }

    public Node getNode(String nodeName){
        for(Node n : nodes.values()){
            if(n.getName().equals(nodeName)) {
                return n;
            }
        }
        return null;
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
        nodes = new HashMap<>();
        this.setNodes();
        if(Engine.stateOfEngine == EnumEngineState.DEBUGGER_ON) {
            Engine.LOGGER.log("\tScript Loaded!", EnumLoggerTypes.DEBUG);
        }
    }
}
