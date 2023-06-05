package com.camping101.beta.db.entity.bookMark;

import com.camping101.beta.db.entity.campLog.CampLog;
import com.camping101.beta.db.entity.member.Member;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
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
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(value = {AuditingEntityListener.class})
public class BookMark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_mark_id")
    private Long bookMarkId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "camp_log_id")
    private CampLog campLog;

    public void changeCampLog(CampLog campLog) {
        this.campLog = campLog;
        if (!campLog.getBookMarks().contains(this)) {
            campLog.addBookMark(this);
        }
    }

    public void changeMember(Member member) {
        this.member = member;
    }


}
