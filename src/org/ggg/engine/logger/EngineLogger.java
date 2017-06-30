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

    public <T> void log(T message, EnumLoggerTypes type){
        if(logger.isEmpty()) return;
        if(type == EnumLoggerTypes.NONE) {
            System.out.println(logger + getMessageType(message));
        }else if(type == EnumLoggerTypes.SYSOUT){
            System.out.println(logger.concat("/SYSOUT] ") + getMessageType(message));
        }else if(type == EnumLoggerTypes.DEBUG){
            System.out.println(logger.concat("/DEBUG] ") + getMessageType(message));
        }else if(type == EnumLoggerTypes.ERROR){
            System.err.println(logger.concat("/ERROR] ") + getMessageType(message));
        }
    }

    private <T> String getMessageType(T message){
        return message instanceof Integer ? String.valueOf(message) :
                ((message instanceof Boolean ? String.valueOf(message) :
                        (message instanceof Float || message instanceof Double ? String.valueOf(message) :
                                message.toString())));
    }
}
