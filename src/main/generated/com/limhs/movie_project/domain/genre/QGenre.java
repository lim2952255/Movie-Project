package com.limhs.movie_project.domain.genre;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QGenre is a Querydsl query type for Genre
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QGenre extends EntityPathBase<Genre> {

    private static final long serialVersionUID = 1835679602L;

    public static final QGenre genre = new QGenre("genre");

    public final com.limhs.movie_project.domain.mappedSuperClass.QBaseEntity _super = new com.limhs.movie_project.domain.mappedSuperClass.QBaseEntity(this);

    public final NumberPath<Integer> genreId = createNumber("genreId", Integer.class);

    public final StringPath genreName = createString("genreName");

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final ListPath<com.limhs.movie_project.domain.movie.MovieGenres, com.limhs.movie_project.domain.movie.QMovieGenres> movieGenres = this.<com.limhs.movie_project.domain.movie.MovieGenres, com.limhs.movie_project.domain.movie.QMovieGenres>createList("movieGenres", com.limhs.movie_project.domain.movie.MovieGenres.class, com.limhs.movie_project.domain.movie.QMovieGenres.class, PathInits.DIRECT2);

    public QGenre(String variable) {
        super(Genre.class, forVariable(variable));
    }

    public QGenre(Path<? extends Genre> path) {
        super(path.getType(), path.getMetadata());
    }

    public QGenre(PathMetadata metadata) {
        super(Genre.class, metadata);
    }

}

