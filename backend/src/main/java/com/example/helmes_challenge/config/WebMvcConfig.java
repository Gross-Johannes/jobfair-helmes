package com.example.helmes_challenge.config;

import jakarta.servlet.http.HttpServletRequest;
import org.jspecify.annotations.NonNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.ResourceResolver;
import org.springframework.web.servlet.resource.ResourceResolverChain;

import java.util.Arrays;
import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/")
                .resourceChain(true)
                .addResolver(new SpaIndexResolver());
    }

    /**
     * Custom resource resolver that falls back to index.html for SPA routing.
     * This allows Vue Router to handle client-side routing.
     */
    private static class SpaIndexResolver implements ResourceResolver {
        private static final List<String> STATIC_EXTENSIONS = Arrays.asList(
                ".js", ".css", ".png", ".jpg", ".jpeg", ".gif", ".svg", ".ico",
                ".woff", ".woff2", ".ttf", ".eot", ".otf", ".webmanifest"
        );

        @Override
        public Resource resolveResource(HttpServletRequest request, @NonNull String requestPath,
                                        @NonNull List<? extends Resource> locations, ResourceResolverChain chain) {
            Resource resource = chain.resolveResource(request, requestPath, locations);

            if (resource != null && resource.exists()) {
                return resource;
            }

            if (requestPath.startsWith("api/") || requestPath.startsWith("/api/")) {
                return null;
            }

            if (hasStaticExtension(requestPath)) {
                return null;
            }

            try {
                Resource indexResource = new ClassPathResource("static/index.html");
                if (indexResource.exists()) {
                    return indexResource;
                }
            } catch (Exception e) {
                // Silently ignore
            }

            return null;
        }

        @Override
        public String resolveUrlPath(@NonNull String resourcePath, @NonNull List<? extends Resource> locations,
                                     ResourceResolverChain chain) {
            return chain.resolveUrlPath(resourcePath, locations);
        }

        private boolean hasStaticExtension(String path) {
            return STATIC_EXTENSIONS.stream().anyMatch(path::endsWith);
        }
    }
}

