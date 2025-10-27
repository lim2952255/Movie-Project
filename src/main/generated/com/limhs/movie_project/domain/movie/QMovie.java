package com.limhs.movie_project.domain.movie;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMovie is a Querydsl query type for Movie
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMovie extends EntityPathBase<Movie> {

    private static final long serialVersionUID = 1899427084L;

    public static final QMovie movie = new QMovie("movie");

    public final com.limhs.movie_project.domain.mappedSuperClass.QBaseEntity _super = new com.limhs.movie_project.domain.mappedSuperClass.QBaseEntity(this);

    public final BooleanPath adult = createBoolean("adult");

    public final ListPath<com.limhs.movie_project.domain.favorite.Favorite, com.limhs.movie_project.domain.favorite.QFavorite> favorites = this.<com.limhs.movie_project.domain.favorite.Favorite, com.limhs.movie_project.domain.favorite.QFavorite>createList("favorites", com.limhs.movie_project.domain.favorite.Favorite.class, com.limhs.movie_project.domain.favorite.QFavorite.class, PathInits.DIRECT2);

    public final ListPath<Integer, NumberPath<Integer>> genreIds = this.<Integer, NumberPath<Integer>>createList("genreIds", Integer.class, NumberPath.class, PathInits.DIRECT2);

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final BooleanPath isPlaying = createBoolean("isPlaying");

    public final BooleanPath isPopular = createBoolean("isPopular");

    public final ListPath<MovieGenres, QMovieGenres> movieGenres = this.<MovieGenres, QMovieGenres>createList("movieGenres", MovieGenres.class, QMovieGenres.class, PathInits.DIRECT2);

    public final NumberPath<Integer> movieId = createNumber("movieId", Integer.class);

    public final StringPath overview = createString("overview");

    public final NumberPath<Double> popularity = createNumber("popularity", Double.class);

    public final StringPath posterPath = createString("posterPath");

    public final ListPath<com.limhs.movie_project.domain.post.Post, com.limhs.movie_project.domain.post.QPost> posts = this.<com.limhs.movie_project.domain.post.Post, com.limhs.movie_project.domain.post.QPost>createList("posts", com.limhs.movie_project.domain.post.Post.class, com.limhs.movie_project.domain.post.QPost.class, PathInits.DIRECT2);

    public final StringPath releaseDate = createString("releaseDate");

    public final StringPath title = createString("title");

    public final NumberPath<Long> version = createNumber("version", Long.class);

    public QMovie(String variable) {
        super(Movie.class, forVariable(variable));
    }

    public QMovie(Path<? extends Movie> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMovie(PathMetadata metadata) {
        super(Movie.class, metadata);
    }

}

