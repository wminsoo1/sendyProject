package com.example.wallet.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QWallet is a Querydsl query type for Wallet
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QWallet extends EntityPathBase<Wallet> {

    private static final long serialVersionUID = -5397605L;

    public static final QWallet wallet = new QWallet("wallet");

    public final com.example.global.QBaseEntity _super = new com.example.global.QBaseEntity(this);

    public final NumberPath<java.math.BigDecimal> balance = createNumber("balance", java.math.BigDecimal.class);

    public final EnumPath<com.example.wallet.model.CardCompany> cardCompany = createEnum("cardCompany", com.example.wallet.model.CardCompany.class);

    public final StringPath chargeIdempotencyKey = createString("chargeIdempotencyKey");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath createdBy = _super.createdBy;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    //inherited
    public final StringPath modifiedBy = _super.modifiedBy;

    public final StringPath paymentIdempotencyKey = createString("paymentIdempotencyKey");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QWallet(String variable) {
        super(Wallet.class, forVariable(variable));
    }

    public QWallet(Path<? extends Wallet> path) {
        super(path.getType(), path.getMetadata());
    }

    public QWallet(PathMetadata metadata) {
        super(Wallet.class, metadata);
    }

}

