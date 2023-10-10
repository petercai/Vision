package com.peppermint.vision.rest.dto;

import java.io.Serializable;
import lombok.Data;

@SuppressWarnings("serial")
@Data
public class FeedInfo implements Serializable {

	private String url;
	private String title;

}
