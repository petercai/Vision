package com.peppermint.vision.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@SuppressWarnings("serial")
@Data
public class Entries /*implements Serializable*/ {

	@JsonProperty("name")
	private String name;
	@JsonProperty("message")
	private String message;
	@JsonProperty("errorCount")
	private int errorCount;
	@JsonProperty("feedLink")
	private String feedLink;
	@JsonProperty("timestamp")
	private long timestamp;
	@JsonProperty("hasMore")
	private boolean hasMore;
	@JsonProperty("offset")
	private int offset;
	@JsonProperty("limit")
	private int limit;
	@JsonProperty("entries")
	private List<Entry> entries = new ArrayList<>();
	@JsonProperty("ignoredReadStatus")
	private boolean ignoredReadStatus;

}
