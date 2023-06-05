package com.camping101.beta.web.domain.comment.repository;

import com.camping101.beta.db.entity.campLog.QCampLog;
import com.camping101.beta.db.entity.comment.Comment;
import com.camping101.beta.db.entity.comment.QComment;
import com.camping101.beta.db.entity.comment.QReComment;
import com.camping101.beta.db.entity.comment.ReComment;
import com.camping101.beta.db.entity.member.QMember;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CommentCustomRepositoryImpl implements CommentCustomRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Comment> findCommentListByCampLogId(Long campLogId, Pageable pageable) {

        QComment comment = QComment.comment;
        QCampLog campLog = QCampLog.campLog;
        QReComment reComment = QReComment.reComment;
        QMember member = QMember.member;

        List<Comment> comments = jpaQueryFactory
                .selectFrom(comment)
                .join(comment.campLog, campLog)
                .fetchJoin()
                .join(comment.member, member)
                .fetchJoin()
                .leftJoin(comment.reComments, reComment)
                .fetchJoin()
                .where(comment.campLog.campLogId.eq(campLogId))
                .orderBy(getOrderSpecifier(pageable.getSort()).stream().toArray(OrderSpecifier[]::new))
                .offset(pageable.getPageNumber() - 1)
                .limit(pageable.getPageSize())
                .fetch();

        comments.stream().forEach(x -> x.changeReComments(x.getReComments().stream().sorted(new Comparator<ReComment>() {
                    @Override
                    public int compare(ReComment r1, ReComment r2) {
                        return r1.getCreatedAt().compareTo(r2.getCreatedAt()) * -1;
                    }
                }).collect(Collectors.toList())));

        long total = jpaQueryFactory
                .select(campLog.comments.size())
                .from(campLog)
                .where(campLog.campLogId.eq(campLogId))
                .fetchOne();

        return new PageImpl<>(comments, pageable, total);
    }

    private List<OrderSpecifier> getOrderSpecifier(Sort sort) {

        List<OrderSpecifier> orders = new ArrayList<>();

        sort.stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String property = order.getProperty();
            PathBuilder orderByExpression = new PathBuilder(Comment.class, "comment");
            orders.add(new OrderSpecifier(direction, orderByExpression.get(property)));
        });

        return orders;
    }

}
