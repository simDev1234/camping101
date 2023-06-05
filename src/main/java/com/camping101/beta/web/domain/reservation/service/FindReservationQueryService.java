package com.camping101.beta.web.domain.reservation.service;

import static com.camping101.beta.db.entity.reservation.QReservation.reservation;
import static java.time.LocalDateTime.now;

import com.camping101.beta.db.entity.reservation.Reservation;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class FindReservationQueryService {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    @Autowired
    public FindReservationQueryService(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    // 해당 기간에 해당하는 모든 회원의 예약 내역 가져오기
    public List<Reservation> findReservationList(Optional<Integer> month, Long memberId,
        Pageable pageable) {

        if (month.isEmpty()) {
            return queryFactory.select(reservation)
                .from(reservation)
                .where(reservation.member.memberId.eq(memberId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        }

//        LocalDateTime fromDate = LocalDateTime.from(LocalDate.now().atStartOfDay());
//        LocalDateTime toDate = LocalDateTime.from(LocalDate.now().plusMonths(2).atStartOfDay());
//
//        Predicate predicate = (site.reservationList.any().startDate.between(fromDate, toDate))
//            .and(site.reservationList.any().endDate.between(fromDate, toDate));
//
//        return queryFactory.select(new QSiteInCamp(site)).from(site)
//            .where(site.siteId.in(siteIds), predicate).fetch();

//        DateTemplate<LocalDateTime> dateFormat = Expressions.dateTemplate(
//            LocalDateTime.class, "DATE_SUB({0},{1})",
//            now(), "INTERVAL -" + month + " MONTH");
        Integer monthFilter = month.get();
        int year = now().getYear();
        LocalDate localDate = LocalDate.of(year, monthFilter, 1);

        return queryFactory.select(reservation)
            .from(reservation)
            .where(
                // 해당 기간 내의 예약들(기간 필터를 사용)
                reservation.startDate.goe(localDate)
                    .and(reservation.startDate.loe(localDate.plusMonths(1)))
                    // 해당 회원이 한 예약들
                    .and(reservation.member.memberId.eq(memberId))
            )
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();
    }

    // 예약 가능 날 확인(여기에 취소된 예약 날은 제외하고 해야하는데...)
    // 해당 값은 자동적으로 LocalDateTime.now() 보다 큰 값들이다.
    public List<Reservation> findReservationBySite(Long siteId) {

        return queryFactory.select(reservation).from(reservation)
            .where(reservation.endDate.lt(LocalDate.now()))
            .fetch();
    }

}
