package spring;

import com.mgs.spring.AppConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import spring.testMixers.WithEventsManager;

@Configuration
@Import({
        AppConfig.class
})
public class TestContext {
    @Bean
    public WithEventsManager withEventsManager() {
        return new WithEventsManager();
    }
}
