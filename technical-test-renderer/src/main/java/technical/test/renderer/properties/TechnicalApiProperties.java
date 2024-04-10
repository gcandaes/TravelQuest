package technical.test.renderer.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "webclient.api.technical")
@Data
public class TechnicalApiProperties {
    private String url;
    private String basePath;
    private String flightPath;

}
