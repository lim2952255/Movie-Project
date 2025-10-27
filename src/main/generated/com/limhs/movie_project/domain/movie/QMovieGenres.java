package com.limhs.movie_project.domain.movie;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMovieGenres is a Querydsl query type for MovieGenres
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMovieGenres extends EntityPathBase<MovieGenres> {

    private static final long serialVersionUID = -2128539780L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMovieGenres movieGenres = new QMovieGenres("movieGenres");

    public final com.limhs.movie_project.domain.mappedSuperClass.QBaseEntity _super = new com.limhs.movie_project.domain.mappedSuperClass.QBaseEntity(this);

    public final com.limhs.movie_project.domain.genre.QGenre genre;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final QMovie movie;

    public QMovieGenres(String variable) {
        this(MovieGenres.class, forVariable(variable), INITS);
    }

    public QMovieGenres(Path<? extends MovieGenres> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMovieGenres(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMovieGenres(PathMetadata metadata, PathInits inits) {
        this(MovieGenres.class, metadata, inits);
    }

    public QMovieGenres(Class<? extends MovieGenres> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.genre = inits.isInitialized("genre") ? new com.limhs.movie_project.domain.genre.QGenre(forProperty("genre")) : null;
        this.movie = inits.isInitialized("movie") ? new QMovie(forProperty("movie")) : null;
    }

}

