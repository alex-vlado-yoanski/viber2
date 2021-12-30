package org.avy.viber2.data;

import com.google.gson.*;

public interface IDataHandler<CLASS> extends JsonSerializer<CLASS>, JsonDeserializer<CLASS> {
    
    public String process(String request);
    
}
