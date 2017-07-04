package org.ggg.engine.io.script.node;

import org.ggg.engine.Engine;
import org.ggg.engine.consts.EnumNodes;
import org.ggg.engine.io.script.ScriptLoader;

public class ScriptReturnNode extends Node {
	public static final ScriptReturnNode INSTANCE = new ScriptReturnNode(Engine.getScriptLoader());
	private static boolean isReturn = false;
	
	public ScriptReturnNode(ScriptLoader loader) {
		super(EnumNodes.RETURN, loader);
	}
	
	@Override
	public boolean perform(String...params) {
		return true;
	}
	
	public boolean getIsReturn() {
		return isReturn;
	}
	
	public void setIsReturnTrue() {
		isReturn = true;
	}
	
	public void setIsReturnFalse() {
		isReturn = false;
	}
}
