package org.ggg.engine.logger;

import org.ggg.engine.consts.EnumLoggerTypes;

import org.ggg.engine.Engine;

/**
 * A custom logger. Call the logger in the {@link Engine} class and call the method {@link #log(Object, EnumLoggerTypes)}
 * and pass in an object to be printed along with a logger type. The logger type can be selected from the class
 * {@link EnumLoggerTypes} which there are <em>NONE</em>, which is just for general printing out; <em>SYSOUT</em>, which
 * is for system output. Pretty much the same as the <em>NONE</em>. <em>NONE</em> may become deprecated in the future.
 * There is also <em>DEBUG</em>, which is for debugging and should only be used when adding a debug log into the code.
 * There is also, lastly, <em>ERROR</em> which is for printing an error to the console, usually for crash reports.
 *
 * @see EnumLoggerTypes
 * @see Engine
 * @author Alex Couch
 * @since 0.1.0
 */
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
