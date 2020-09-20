package cai.peter.vision.rest.dto;

import com.fasterxml.jackson.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@SuppressWarnings("serial")
@Data
public class Category implements Serializable {
	@JsonProperty("id")
	private String id;
	@JsonProperty("parentId")
	private String parentId;
	@JsonProperty("name")
	private String name;
	@JsonProperty("children")
	private List<Category> children = new ArrayList<>();
	@JsonProperty("feeds")
	private List<Subscription> feeds = new ArrayList<>();
	@JsonProperty("expanded")
	private boolean expanded;
	@JsonProperty("position")
	private Integer position;
}