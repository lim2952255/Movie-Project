package com.limhs.movie_project.domain.converter;

import jakarta.persistence.AttributeConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MovieUrlConverter implements AttributeConverter<String,String> {

    @Value("${tmdb.image.base.url}")
    private String tmdbImageBaseUrl;

    @Override
    public String convertToDatabaseColumn(String attribute) {
        return attribute.replace(tmdbImageBaseUrl, "");
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        return tmdbImageBaseUrl + dbData;
    }
}
