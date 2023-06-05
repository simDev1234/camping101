package com.camping101.beta.db.entity.campLog;

import com.camping101.beta.db.entity.bookMark.BookMark;
import com.camping101.beta.db.entity.comment.Comment;
import com.camping101.beta.db.entity.member.Member;
import com.camping101.beta.db.entity.site.Site;
import com.camping101.beta.web.domain.campLog.dto.CampLogCreateRequest;
import com.camping101.beta.web.domain.campLog.dto.CampLogUpdateRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(value = {AuditingEntityListener.class})
public class CampLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "camp_log_id")
    private Long campLogId;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "site_id")
    private Site site;

    private String recTags;
    private LocalDateTime visitedAt;
    private String visitedWith;
    private String title;
    private String description; // 최대 200자
    private String image;
    private String image1;
    private String image2;
    private String image3;
    private String image4;
    private String image5;
    private long likes = 0;
    private long view = 0;

    @OneToMany(mappedBy = "campLog")
    private List<BookMark> bookMarks = new ArrayList<>();

    @OneToMany(mappedBy = "campLog")
    private List<Comment> comments = new ArrayList<>();

    @CreatedDate
    @Column(updatable = false, insertable = true)
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public void addBookMark(BookMark bookMark) {
        this.bookMarks.add(bookMark);
        if (bookMark.getCampLog() != this) {
            bookMark.changeCampLog(this);
        }
    }

    public static CampLog from(CampLogCreateRequest request) {
        return CampLog.builder()
            .visitedAt(request.getVisitedAt())
            .visitedWith(request.getVisitedWith())
            .title(request.getTitle())
            .description(request.getDescription())
            .build();
    }

    public void changeImagePaths(List<String> imagePaths) {
        this.image = imagePaths.get(0);
        this.image1 = imagePaths.get(1);
        this.image2 = imagePaths.get(2);
        this.image3 = imagePaths.get(3);
        this.image4 = imagePaths.get(4);
        this.image5 = imagePaths.get(5);
    }

    public void changeRecTags(String recTags) {
        this.recTags = recTags;
    }

    public void changeMember(Member member) {
        this.member = member;
    }

    public void changeSite(Site site) {
        this.site = site;
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
        if (comment.getCampLog() != this) {
            comment.changeCampLog(this);
        }
    }

    public void increaseViewCount() {
        this.view = this.view + 1;
    }

    public void updateCampLog(CampLogUpdateRequest request) {
        this.visitedAt = request.getVisitedAt();
        this.visitedWith = request.getVisitedWith();
        this.title = request.getTitle();
        this.description = request.getDescription();
    }

    public void increaseLikesCount() {
        this.likes = this.likes + 1;
    }

    public void decreaseLikesCount() {
        this.likes = this.likes - 1;
    }
}
