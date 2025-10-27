package com.limhs.movie_project.domain.post;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPost is a Querydsl query type for Post
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPost extends EntityPathBase<Post> {

    private static final long serialVersionUID = 177796616L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPost post = new QPost("post");

    public final com.limhs.movie_project.domain.mappedSuperClass.QBaseTimeEntity _super = new com.limhs.movie_project.domain.mappedSuperClass.QBaseTimeEntity(this);

    public final ListPath<com.limhs.movie_project.domain.comment.Comment, com.limhs.movie_project.domain.comment.QComment> comments = this.<com.limhs.movie_project.domain.comment.Comment, com.limhs.movie_project.domain.comment.QComment>createList("comments", com.limhs.movie_project.domain.comment.Comment.class, com.limhs.movie_project.domain.comment.QComment.class, PathInits.DIRECT2);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdTime = _super.createdTime;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final ListPath<com.limhs.movie_project.domain.like.Like, com.limhs.movie_project.domain.like.QLike> likes = this.<com.limhs.movie_project.domain.like.Like, com.limhs.movie_project.domain.like.QLike>createList("likes", com.limhs.movie_project.domain.like.Like.class, com.limhs.movie_project.domain.like.QLike.class, PathInits.DIRECT2);

    public final com.limhs.movie_project.domain.movie.QMovie movie;

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedTime = _super.updatedTime;

    public final com.limhs.movie_project.domain.user.QUser user;

    public final NumberPath<Long> viewCount = createNumber("viewCount", Long.class);

    public QPost(String variable) {
        this(Post.class, forVariable(variable), INITS);
    }

    public QPost(Path<? extends Post> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPost(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPost(PathMetadata metadata, PathInits inits) {
        this(Post.class, metadata, inits);
    }

    public QPost(Class<? extends Post> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.movie = inits.isInitialized("movie") ? new com.limhs.movie_project.domain.movie.QMovie(forProperty("movie")) : null;
        this.user = inits.isInitialized("user") ? new com.limhs.movie_project.domain.user.QUser(forProperty("user")) : null;
    }

}

