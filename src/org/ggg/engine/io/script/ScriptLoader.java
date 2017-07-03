
package org.ggg.engine.io.script;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.HashMap;
import java.util.Map;

import org.ggg.engine.Engine;
import org.ggg.engine.consts.EnumEngineState;
import org.ggg.engine.consts.EnumLoggerTypes;
import org.ggg.engine.consts.EnumNodes;
import org.ggg.engine.io.resloc.ResourceLocation;
import org.ggg.engine.io.script.node.Node;

/**
 * This class is super duper important. It takes in a path string so that you can point it at the right {@link ResourceLocation}.
 * Then the ResourceLocation class checks if the path specified has a file that exists. If not, then it will throw an error.
 * This class has various important and useful methods such as {@link ScriptLoader#getLineNum(String)} which spits out the
 * line number in which a string is founded on first. Secondly, {@link ScriptLoader#setNodes()} which sets all the nodes
 * that are added to the {@link EnumNodes} enum class. This class has a map that has a String for a node name, and a node
 * that is corresponded with the name. You can then use {@link #getNode(String)} to get the node that matches with a String name.
 * {@link #getScriptFile()} returns the file that is being loaded. {@link #getFileReader()} returns the FileReader object
 * that is reading the file. {@link #getBufferedReader()} returns the BufferedReader object. Lastly, {@link #loadScript()}
 * is like the <em>Engine</em>'s {@link Engine#start(EnumEngineState)} method, in which it initializes everything in this
 * class. It is used during the start of a new script loader. I am hesitating on having it be set in the constructor or
 * be called outside the class.
 * @see EnumNodes
 * @see Engine
 * @see ResourceLocation
 * @author Alex Couch
 * @since 0.1.0
 */
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

	public long getLineNum(String line) {
    	try {
    		FileReader fr = new FileReader(resloc.getFile().getAbsolutePath());
    		LineNumberReader lnr = new LineNumberReader(fr);
    		lnr.setLineNumber(0);
    		String str = null;
    		while((str = lnr.readLine()) != null) {
    			if(str.equals(line)) {
    				break;
    			}
    		}
    		lnr.close();
    		return lnr.getLineNumber();
    	}
    	catch (FileNotFoundException e) {
    		Engine.LOGGER.log(e.getMessage(), EnumLoggerTypes.ERROR);
    	}
    	catch (IOException e) {
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
