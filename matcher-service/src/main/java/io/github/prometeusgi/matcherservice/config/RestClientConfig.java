package io.github.prometeusgi.matcherservice.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.net.http.HttpClient;

@Configuration
@EnableConfigurationProperties(AiServiceProperties.class)
public class RestClientConfig {

    @Bean
    public HttpClient aiHttpClient(AiServiceProperties props) {
        return HttpClient.newBuilder()
                .connectTimeout(props.connectTimeout())
                .build();
    }

    @Bean
    public RestClient aiRestClient(RestClient.Builder builder, HttpClient aiHttpClient, AiServiceProperties props) {
        JdkClientHttpRequestFactory factory = new JdkClientHttpRequestFactory(aiHttpClient);
        factory.setReadTimeout(props.readTimeout());

        return builder
                .baseUrl(props.baseUrl())
                .requestFactory(factory)
                .build();
    }
}
