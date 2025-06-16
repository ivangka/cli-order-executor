package ivangka.cliorderexecutor.config;

import com.bybit.api.client.config.BybitApiConfig;
import com.bybit.api.client.restApi.BybitApiMarketRestClient;
import com.bybit.api.client.restApi.BybitApiPositionRestClient;
import com.bybit.api.client.restApi.BybitApiTradeRestClient;
import com.bybit.api.client.service.BybitApiClientFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@PropertySource("classpath:application.properties")
@Configuration
public class ApiConfig {

    @Value("${api.bybit.key}")
    private String apiKey;

    @Value("${api.bybit.secret}")
    private String apiSecret;

    @Bean
    public BybitApiMarketRestClient bybitApiMarketRestClient() {
        return BybitApiClientFactory.newInstance().newMarketDataRestClient();
    }

    @Bean
    public BybitApiTradeRestClient bybitApiTradeRestClient() { // MAINNET_DOMAIN TESTNET_DOMAIN
        return BybitApiClientFactory.newInstance(apiKey, apiSecret, BybitApiConfig.MAINNET_DOMAIN).newTradeRestClient();
    }

    @Bean
    public BybitApiPositionRestClient bybitApiPositionRestClient() {
        return BybitApiClientFactory.newInstance(apiKey, apiSecret, BybitApiConfig.MAINNET_DOMAIN)
                .newPositionRestClient();
    }

}
