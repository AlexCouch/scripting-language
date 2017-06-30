package org.ggg.engine.io.script.node;

import org.ggg.engine.Engine;
import org.ggg.engine.consts.EnumNodes;
import org.ggg.engine.io.script.ScriptLoader;

import java.util.*;

public class ScriptWaitNode extends Node{

    private Map<String, String> inputMap = new HashMap<>();
    public List<Map<String, String>> inputList = new ArrayList<>();

    public static final Node INSTANCE = new ScriptWaitNode(Engine.getScriptLoader());

    public ScriptWaitNode(ScriptLoader loader) {
        super(EnumNodes.WAIT, loader);
    }

    @Override
    public <T> boolean perform(T[] params) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(params[0] instanceof String){
                    String name = (String)params[0];
                    Scanner scanner = new Scanner(System.in);
                    while(scanner.hasNextLine()){
                        inputMap.put(name, scanner.nextLine());
                        inputList.add(inputMap);
                    }
                }
            }
        }, 1000);
        return true;
    }
}
