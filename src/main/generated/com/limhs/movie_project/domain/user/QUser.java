package com.limhs.movie_project.domain.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = -1485489944L;

    public static final QUser user = new QUser("user");

    public final com.limhs.movie_project.domain.mappedSuperClass.QBaseEntity _super = new com.limhs.movie_project.domain.mappedSuperClass.QBaseEntity(this);

    public final ListPath<com.limhs.movie_project.domain.like.CommentLike, com.limhs.movie_project.domain.like.QCommentLike> commentLikes = this.<com.limhs.movie_project.domain.like.CommentLike, com.limhs.movie_project.domain.like.QCommentLike>createList("commentLikes", com.limhs.movie_project.domain.like.CommentLike.class, com.limhs.movie_project.domain.like.QCommentLike.class, PathInits.DIRECT2);

    public final ListPath<com.limhs.movie_project.domain.comment.Comment, com.limhs.movie_project.domain.comment.QComment> comments = this.<com.limhs.movie_project.domain.comment.Comment, com.limhs.movie_project.domain.comment.QComment>createList("comments", com.limhs.movie_project.domain.comment.Comment.class, com.limhs.movie_project.domain.comment.QComment.class, PathInits.DIRECT2);

    public final StringPath email = createString("email");

    public final ListPath<com.limhs.movie_project.domain.favorite.Favorite, com.limhs.movie_project.domain.favorite.QFavorite> favorites = this.<com.limhs.movie_project.domain.favorite.Favorite, com.limhs.movie_project.domain.favorite.QFavorite>createList("favorites", com.limhs.movie_project.domain.favorite.Favorite.class, com.limhs.movie_project.domain.favorite.QFavorite.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<com.limhs.movie_project.domain.like.Like, com.limhs.movie_project.domain.like.QLike> likes = this.<com.limhs.movie_project.domain.like.Like, com.limhs.movie_project.domain.like.QLike>createList("likes", com.limhs.movie_project.domain.like.Like.class, com.limhs.movie_project.domain.like.QLike.class, PathInits.DIRECT2);

    public final StringPath password = createString("password");

    public final ListPath<com.limhs.movie_project.domain.post.Post, com.limhs.movie_project.domain.post.QPost> posts = this.<com.limhs.movie_project.domain.post.Post, com.limhs.movie_project.domain.post.QPost>createList("posts", com.limhs.movie_project.domain.post.Post.class, com.limhs.movie_project.domain.post.QPost.class, PathInits.DIRECT2);

    public final StringPath telephone = createString("telephone");

    public final StringPath userId = createString("userId");

    public final StringPath username = createString("username");

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

