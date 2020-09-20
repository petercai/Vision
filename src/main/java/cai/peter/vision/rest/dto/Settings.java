package cai.peter.vision.rest.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@SuppressWarnings("serial")
@Data
public class Settings implements Serializable {

	
	private String language;

	
	private String readingMode;

	
	private String readingOrder;

	
	private String viewMode;

	
	private boolean showRead;

	
	private boolean scrollMarks;

	
	private String theme;

	
	private String customCss;

	
	private int scrollSpeed;

	private boolean email;
	private boolean gmail;
	private boolean facebook;
	private boolean twitter;
	private boolean googleplus;
	private boolean tumblr;
	private boolean pocket;
	private boolean instapaper;
	private boolean buffer;
	private boolean readability;

}
