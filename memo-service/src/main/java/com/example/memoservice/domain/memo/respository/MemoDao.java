package com.example.memoservice.domain.memo.respository;

import com.example.memoservice.domain.memo.model.MemoStatus;
import com.example.memoservice.domain.memo.model.QMemo;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class MemoDao {

    private final JPAQueryFactory queryFactory;

    private final MemoRepository memoRepository;

    QMemo qMemo = QMemo.memo;


    public List<MemoListDto> getMemosByBabies(List<String> babyIds) {


        return queryFactory.select(
                        Projections.fields(MemoListDto.class,
                                qMemo.memoId
                        ))
                .from(qMemo)
                .where(qMemo.targetBabies.any().in(babyIds))
                .fetch();
    }


    public List<MemoListDto> getMemoListByFilter(String babyIdsString,
                                                 String targetMembersString,
                                                 String targetAudiencesString,
                                                 MemoStatus memoStatus) {
        BooleanBuilder builder = new BooleanBuilder();

        if (StringUtils.hasText(babyIdsString)) {
            List<String> babyIds = List.of(babyIdsString.split(","));
            builder.and(qMemo.targetBabies.any().in(babyIds));
        }
        if (StringUtils.hasText(targetMembersString)) {
            List<String> targetMembers = List.of(targetMembersString.split(","));
            builder.and(qMemo.targetMembers.any().in(targetMembers));
        }
        if (StringUtils.hasText(targetAudiencesString)) {
//            List<TargetAudience> targetAudiences = Arrays.stream(targetAudiencesString.split(","))
//                    .map(TargetAudience::valueOf)
//                    .toList();
            List<String> targetAudiences = List.of(targetAudiencesString.split(","));
            builder.and(qMemo.targetAudiences.any().in(targetAudiences));
        }
        if (memoStatus != null) {
            builder.and(qMemo.memoStatus.eq(memoStatus));
        }
        QMemo qMemo = QMemo.memo;


        var list = queryFactory.select(
                        Projections.fields(MemoListDto.class,
                                qMemo.memoId
                        ))
                .from(qMemo)
                .where(builder)
                .fetch();

        return list.stream()
                .map(m -> {
                    return memoRepository.findById(m.getMemoId()).orElse(null);
                })
                .filter(Objects::nonNull)
                .map(MemoListDto::of)
                .toList();
    }

}
