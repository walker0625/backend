package kr.lifesemantics.canofymd.moduleapi.global.config;

import kr.lifesemantics.canofymd.moduleapi.global.config.converter.StringToEnumConverter;
import kr.lifesemantics.canofymd.moduleapi.global.interceptor.CommonInterceptor;
import kr.lifesemantics.canofymd.modulecore.enums.Category;
import kr.lifesemantics.canofymd.modulecore.enums.ParticipationCompliance;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * packageName    : kr.lifesemantics.canofymd.moduleapi.global.config.security
 * fileName       : WebConfig
 * author         : ms.jo
 * date           : 2024/04/18
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024/04/18        ms.jo       최초 생성
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WebConfig implements WebMvcConfigurer {

	ApplicationContext ac;

	CommonInterceptor commonInterceptor;

	List<Category> categories = List.of(Category.values());


	List<String> WHITE_LIST = List.of(
			"/auth/staff",
			"/auth/patient",
			"/v3/**", // v3 : SpringBoot 3(없으면 swagger 예시 api 목록 제공)
			"/swagger-ui/**"
	);

    @Override
    	public void addCorsMappings(CorsRegistry registry) {
    		registry.addMapping("/**")
    				.allowedOrigins("*")
    				.allowedMethods(
							HttpMethod.GET.name(),
    						HttpMethod.POST.name(),
							HttpMethod.PATCH.name(),
    						HttpMethod.PUT.name(),
    						HttpMethod.DELETE.name(),
    						HttpMethod.HEAD.name(),
    						HttpMethod.OPTIONS.name()
					);
    	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {

		registry.addInterceptor(commonInterceptor)
				.excludePathPatterns(
					categories.stream()
							.map(item -> createUri(item)).toList()
				)
				.excludePathPatterns(WHITE_LIST)
				.addPathPatterns(
					"/**"
				);

		/**
		 * regist interceptor about category
		 */
		categories.forEach(item -> {
			registry.addInterceptor((HandlerInterceptor) ac.getBean(item.name() + "Interceptor"))
					.addPathPatterns(createUri(item));
		});

	}

	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addConverter(new StringToEnumConverter<>(ParticipationCompliance.class));
	}

	private static String createUri(Category item) {
		return new String("/" + item.getUri() + "/**");
	}

}

