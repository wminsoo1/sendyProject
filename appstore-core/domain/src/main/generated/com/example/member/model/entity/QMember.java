package com.example.member.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = -369527203L;

    public static final QMember member = new QMember("member1");

    public final com.example.global.QBaseEntity _super = new com.example.global.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath createdBy = _super.createdBy;

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath memberName = createString("memberName");

    //inherited
    public final StringPath modifiedBy = _super.modifiedBy;

    public final StringPath password = createString("password");

    public final NumberPath<Long> provideId = createNumber("provideId", Long.class);

    public final EnumPath<com.example.global.oauth2.OAuthProvider> provider = createEnum("provider", com.example.global.oauth2.OAuthProvider.class);

    public final EnumPath<com.example.global.Roles> roles = createEnum("roles", com.example.global.Roles.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QMember(String variable) {
        super(Member.class, forVariable(variable));
    }

    public QMember(Path<? extends Member> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMember(PathMetadata metadata) {
        super(Member.class, metadata);
    }

}

