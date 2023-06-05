package com.camping101.beta.web.domain.camp.service;

import static com.camping101.beta.db.entity.camp.QCamp.camp;
import static com.camping101.beta.db.entity.campLog.QCampLog.campLog;
import static com.camping101.beta.db.entity.member.QMember.member;
import static com.camping101.beta.db.entity.site.QSite.site;

import com.camping101.beta.db.entity.camp.Camp;
import com.camping101.beta.db.entity.site.Site;
import com.camping101.beta.web.domain.camp.dto.campdetaildto.CampLogInCamp;
import com.camping101.beta.web.domain.camp.dto.campdetaildto.FindCampDetailsRs;
import com.camping101.beta.web.domain.camp.dto.campdetaildto.QCampLogInCamp;
import com.camping101.beta.web.domain.camp.dto.campdetaildto.QSiteInCamp;
import com.camping101.beta.web.domain.camp.dto.campdetaildto.SiteInCamp;
import com.camping101.beta.web.domain.camp.repository.CampRepository;
import com.camping101.beta.web.domain.site.repository.SiteRepository;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class FindCampQueryService {

    private final EntityManager em;
    private final CampRepository campRepository;
    private final SiteRepository siteRepository;
    private final JPAQueryFactory queryFactory;

    public FindCampQueryService(EntityManager em, CampRepository campRepository,
        SiteRepository siteRepository) {
        this.em = em;
        this.campRepository = campRepository;
        this.siteRepository = siteRepository;
        this.queryFactory = new JPAQueryFactory(em);
    }

    public FindCampDetailsRs findCampAndSiteAndCampLog(Long campId,
        Pageable campLogPageable) {

        FindCampDetailsRs rs = findCampDetails(campId);

        List<Long> siteIds = findSiteIds(campId);

        List<SiteInCamp> sites = findSiteAndReservation(siteIds);

        // 트러블슈팅 : 해당 캠프에 해당하는 모든 캠프로그들을 가져와야함.
        // 이때 캠프로그는 사이트에 속해있음.
        // 결국 해당캠프에 속한 사이트들에 속한 캠프로그들을 가져와야 하는데....
        // 캠프로그 아이디를 모두 찾아두고 fetch 조인을 한 후에 for문 2번 돌리면서 구하기
        // vs 캠프로그 아이디를 매개변수로 querydsl에 넣어서 쿼리 N 번(사이트 갯수만큼) 날리기.

        for (Long siteId : siteIds) {
            List<CampLogInCamp> campLogs = findCampLog(siteId, campLogPageable);
            rs.addCampLogInCamp(campLogs);
        }

        rs.addSiteInCamp(sites);

        return rs;

    }

    private FindCampDetailsRs findCampDetails(Long campId) {

        return em.createQuery(
                "select new com.camping101.beta.web.domain.camp.dto.campdetaildto.FindCampDetailsRs("
                    + "c.campId,"
                    + "c.name,"
                    + "c.intro,"
                    + "c.manageStatus,"
                    + "c.location,"
                    + "c.tel,"
                    + "c.oneLineReserveYn,"
                    + "c.openSeason,"
                    + "c.openDateOfWeek,"
                    + "c.facilityCnt,"
                    + "c.facility,"
                    + "c.leisure,"
                    + "c.animalCapable,"
                    + "c.equipmentTools,"
                    + "c.firstImage,"
                    + "c.homepage,"
                    + "c.businessNo) from Camp c where c.campId =: campId", FindCampDetailsRs.class)
            .setParameter("campId", campId)
            .getSingleResult();
    }

    private List<SiteInCamp> findSiteAndReservation(List<Long> siteIds) {

        LocalDate fromDate = LocalDate.from(LocalDate.now().atStartOfDay());
        LocalDate toDate = LocalDate.from(LocalDate.now().plusMonths(2).atStartOfDay());

        Predicate predicate = (site.reservationList.any().startDate.between(fromDate, toDate))
            .and(site.reservationList.any().endDate.between(fromDate, toDate));

        return queryFactory.select(new QSiteInCamp(site)).from(site)
            .where(site.siteId.in(siteIds), predicate).fetch();

    }

    private List<Long> findSiteIds(Long campId) {

        Camp findCamp = campRepository.findById(campId).orElseThrow(() -> {
            throw new RuntimeException("캠핑장이 존재하지 않습니다");
        });

        List<Site> sites = siteRepository.findByCamp(findCamp);

        List<Long> siteIds = new ArrayList<>();

        for (Site site : sites) {
            siteIds.add(site.getSiteId());
        }

        return siteIds;
    }

    // V3
    private List<CampLogInCamp> findCampLog(Long siteId, Pageable campLogPageable) {

// fetch 조인은 on 절의 한계로 인해 사용하지 않기로 함 => 이거쿼리 이렇게 하면 안될꺼 같은데 나중에 수정하자.일단하기
//        return em.createQuery("select c from CampLog c join fetch c.member m "
//            + "join fetch c.site s", CampLogInCamp.class).getResultList();

        // querydsl 로 사이트 갯수만큼 쿼리 날리기
        return queryFactory.select(new QCampLogInCamp(
                member.memberId, site.siteId, campLog.campLogId, campLog.title,
                campLog.description, campLog.visitedAt, campLog.image, campLog.createdAt,
                campLog.updatedAt
            )).from(campLog)
            .innerJoin(campLog.member, member)
            .innerJoin(campLog.site, site).on(site.siteId.eq(siteId))
            .offset(campLogPageable.getOffset())
            .limit(campLogPageable.getPageSize())
            .fetch();
    }


}
