package cai.peter.vision.rest.dto;

import java.io.Serializable;
import lombok.Data;

@SuppressWarnings("serial")
@Data
public class ServerInfo implements Serializable {

	private String announcement;
	private String version;
	private String gitCommit;
	private boolean allowRegistrations;
	private String googleAnalyticsCode;
	private boolean smtpEnabled;

}
