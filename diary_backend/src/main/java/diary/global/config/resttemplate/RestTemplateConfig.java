package diary.global.config.resttemplate;

import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.core5.util.TimeValue;
import org.springframework.boot.http.client.HttpComponentsClientHttpRequestFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;


import java.util.concurrent.TimeUnit;

@Configuration
public class RestTemplateConfig {


    @Bean
    RestTemplate restTemplate() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setHttpClient(httpClient());
        return new RestTemplate(factory);
    }

    HttpClient httpClient() {
        return HttpClientBuilder.create()
                .setConnectionManager(createHttpConnectionManager())
                .evictIdleConnections(TimeValue.of(10, TimeUnit.SECONDS)) //서버에서 keepalive시간동안 미 사용한 커넥션을 죽이는 등의 케이스 방어로 idle커넥션을 주기적으로 지움
                .build();

    }


    HttpClientConnectionManager createHttpConnectionManager() {
        return PoolingHttpClientConnectionManagerBuilder.create()
                .setDefaultConnectionConfig(ConnectionConfig.custom()
                        .setSocketTimeout(5000, TimeUnit.MILLISECONDS)  // 읽기 시간 타임아웃
                        .setConnectTimeout(3000, TimeUnit.MILLISECONDS)
                        .build())
                .setMaxConnTotal(100)
                .setMaxConnPerRoute(10)
                .build();

    }


}
