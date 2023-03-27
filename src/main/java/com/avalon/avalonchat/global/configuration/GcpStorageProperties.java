package com.avalon.avalonchat.global.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "spring.cloud.gcp.storage")
public class GcpStorageProperties {
	private String bucket;
	private String url;
}
