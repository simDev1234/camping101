package com.camping101.beta.db.entity.reservation;

import static com.camping101.beta.db.entity.reservation.ReservationStatus.CANCEL;

import com.camping101.beta.db.entity.member.Member;
import com.camping101.beta.db.entity.site.Site;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.lang.Nullable;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Builder
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long reservationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "site_id")
    private Site site;

    private LocalDate startDate;
    private LocalDate endDate;

    private int humanCapacity;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    private Long payment;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime cancelAt;

    private boolean campLogYn; // 캠프로그 여부
    private boolean campLogWritableYn; // 캠프로그 쓰기 권한

    public void addMember(Member member) {
        this.member = member;
    }

    public void addSite(Site site) {
        this.site = site;
    }

    public static void modifyReservationStatus(Reservation reservation) {

        reservation.status = CANCEL;
        reservation.cancelAt = LocalDateTime.now();
    }


    public void changeCampLogWritableYn(Reservation reservation) {

        this.campLogWritableYn = true;

    }

    public void addPayment(Long payment) {
        this.payment = payment;
    }
}
