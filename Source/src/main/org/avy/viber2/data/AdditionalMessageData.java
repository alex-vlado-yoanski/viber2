package org.avy.viber2.data;

import org.avy.viber2.tables.mapping.AdditionalData;

public class AdditionalMessageData extends AdditionalData {

    private String fileContent;

    public String getFileContent() {
	return fileContent;
    }

    public void setFileContent(String fileContent) {
	this.fileContent = fileContent;
    }

}
