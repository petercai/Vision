package com.peppermint.vision.rest.dto;

import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UnreadCount implements Serializable {
	private long subscriptionId;
	private long unreadCount;
	private Date newestItemTime;
}
