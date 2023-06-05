package com.camping101.beta.db.entity.member;

import com.camping101.beta.db.entity.BaseEntity;
import com.camping101.beta.db.entity.member.status.MemberStatus;
import com.camping101.beta.db.entity.member.type.MemberType;
import com.camping101.beta.db.entity.member.type.SignUpType;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.AuditOverride;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@AuditOverride(forClass = BaseEntity.class)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;
    @Column(nullable = false, unique = true)
    private String email;
    private String password;
    private String phoneNumber;
    private String profileImagePath;
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SignUpType signUpType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberType memberType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberStatus memberStatus;

    private LocalDateTime deletedAt;

    public void activateMember() {
        this.memberStatus = MemberStatus.IN_USE;
    }

}
