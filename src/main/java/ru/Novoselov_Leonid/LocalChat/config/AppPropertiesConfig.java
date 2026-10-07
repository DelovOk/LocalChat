package ru.Novoselov_Leonid.LocalChat.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppPropertiesConfig {
    @Value("${app.name:ChatApp}")
    private String appName;

    @Value("${app.version:1.0}")
    private String appVersion;

    public String getAppName() {
        return appName;
    }

    public String getAppVersion() {
        return appVersion;
    }
}
