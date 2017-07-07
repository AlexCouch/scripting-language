package org.ggg.engine.io.script.node;

import org.ggg.engine.Engine;
import org.ggg.engine.consts.EnumNodes;
import org.ggg.engine.io.script.ScriptLoader;

public class ScriptReturnNode extends Node {
	public static final ScriptReturnNode INSTANCE = new ScriptReturnNode(Engine.getScriptLoader());

	public ScriptReturnNode(ScriptLoader loader) {
		super(EnumNodes.RETURN, loader);
	}
	
	@Override
	public boolean perform(String...params) {
		String arg = params[0];
		if(ScriptJumpToNode.INSTANCE.perform(arg)){
			return true;
		}else{
			return false;
		}
	}
}
