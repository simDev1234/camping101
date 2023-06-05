package com.camping101.beta.db.type;

import com.camping101.beta.db.entity.camp.Camp;
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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@EntityListeners(value = {AuditingEntityListener.class})
public class CampAuth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "camp_auth_id")
    private Long campAuthId;

    @Enumerated(EnumType.STRING)
    private CampAuthStatus campAuthStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "camp_id")
    private Camp camp;

    @CreatedDate
    @Column(updatable = false, insertable = true)
    private LocalDateTime createdAt;

    public static CampAuth createCampAuth(Camp camp) {
        return CampAuth.builder()
            .campAuthStatus(CampAuthStatus.UNAUTHORIZED)
            .camp(camp)
            .build();
    }

    public void editCampAuthStatus() {
        this.campAuthStatus = CampAuthStatus.AUTHORIZED;
    }

}
