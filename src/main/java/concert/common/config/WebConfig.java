package concert.common.config;

import concert.api.interceptor.AuthUserIdArgumentResolver;
import concert.api.interceptor.WaitingTokenInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final WaitingTokenInterceptor waitingTokenInterceptor;
    private final AuthUserIdArgumentResolver authUserIdArgumentResolver;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(waitingTokenInterceptor)
                .addPathPatterns("/concerts/*")
                .excludePathPatterns("/concerts/tokens/*");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authUserIdArgumentResolver);
    }
}
