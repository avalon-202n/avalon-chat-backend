package com.avalon.avalonchat.global.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "spring.cloud.gcp.storage")
public class GcpStorageProperties {
	private final String bucket;
	private final String url;
}
