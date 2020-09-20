package cai.peter.vision.rest.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@SuppressWarnings("serial")
@Data
public class FeedInfo implements Serializable {

	private String url;
	private String title;

}
