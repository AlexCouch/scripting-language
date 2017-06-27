package org.ggg.engine.logger;

import org.ggg.engine.consts.EnumLoggerTypes;

public class EngineLogger {
    private static String logger = null;
    private static String loggerName = null;

    public EngineLogger(String name){
        setLoggerName(name);
    }

    public EngineLogger setLoggerName(String name){
        logger = "[" + name.toUpperCase();
        return this;
    }

    @Override
    public String toString() {
        return logger;
    }

    public void log(String message, EnumLoggerTypes type){
        if(logger.isEmpty()) return;
        if(type == EnumLoggerTypes.NONE) {
            System.out.println(logger + message);
        }else if(type == EnumLoggerTypes.SYSOUT){
            System.out.println(logger.concat("/SYSOUT] ") + message);
        }else if(type == EnumLoggerTypes.DEBUG){
            System.out.println(logger.concat("/DEBUG] ") + message);
        }else if(type == EnumLoggerTypes.ERROR){
            System.err.println(logger.concat("/ERROR] ") + message);
        }
    }
}
