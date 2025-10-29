package com.limhs.movie_project.repository.movie;

import com.limhs.movie_project.domain.movie.Movie;
import com.limhs.movie_project.domain.movie.QMovie;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class CustomMovieRepositoryImpl implements CustomMovieRepository{

    private final JPAQueryFactory jqf;
    private final QMovie movie = QMovie.movie;

    @Override
    public Page<Movie> findMoviePages(Pageable pageable, String sortParam ,String movieName, boolean isPopular, boolean isPlaying) {
        OrderSpecifier<?> sort;

        // 동적 쿼리
        BooleanBuilder builder = new BooleanBuilder();
        if(movieName != null && !movieName.isEmpty()){
            builder.and(movie.title.containsIgnoreCase(movieName));
        }

        if(isPopular){
            builder.and(movie.isPopular.isTrue());
        } else{
            builder.and(movie.isPopular.isFalse());
        }

        if (isPlaying){
            builder.and(movie.isPlaying.isTrue());
        } else{
            builder.and(movie.isPlaying.isFalse());
        }

        if(sortParam.equals("releaseDate")){
            sort = movie.releaseDate.desc();
        } else{
            sort = movie.popularity.desc();
        }

        List<Movie> movieList = jqf.selectFrom(movie)
                .where(builder)
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .orderBy(sort)
                .setHint("org.hibernate.readOnly", true) // 읽기 전용 힌트
                .setHint("org.hibernate.cacheable", true)
                .fetch();


        long total = jqf.select(movie.count())
                .from(movie)
                .where(builder)
                .setHint("org.hibernate.cacheable", true)
                .fetchOne();

        return new PageImpl<>(movieList,pageable,total);
    }

}
