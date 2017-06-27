package org.ggg.engine.io.resloc;

import org.ggg.engine.Engine;
import org.ggg.engine.consts.EnumLoggerTypes;

import java.io.File;
import java.io.IOException;

public class ResourceLocation {

    private File file;
    private String absolutePath;

    public ResourceLocation(String path){
        try {
            this.setResloc(path);
        }catch(IOException e){
            Engine.LOGGER.log(e.getMessage(), EnumLoggerTypes.ERROR);
        }
    }

    public String getAbsolutePath(){
        return absolutePath;
    }

    public File getFile(){
        return file;
    }

    private void setResloc(String path) throws IOException{
        File file = new File(path);
        if(!file.exists()){
            throw new IOException("The resource location: " + file.getAbsolutePath() + " does not exist!");
        }
        this.file = file;
        absolutePath = file.getAbsolutePath();
    }
}
