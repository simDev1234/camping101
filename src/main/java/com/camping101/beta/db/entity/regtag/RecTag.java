package com.camping101.beta.db.entity.regtag;

import com.camping101.beta.web.domain.admin.recTag.dto.AdminRecTagCreateRequest;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(value = {AuditingEntityListener.class})
public class RecTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rec_tag_id")
    private Long recTagId;
    private String name;
    private boolean useYn;

    @CreatedDate
    private LocalDateTime createdAt;

    public static RecTag from(AdminRecTagCreateRequest request) {
        return RecTag.builder()
            .name(request.getName())
            .useYn(false)
            .build();
    }

    public void setUseYn(boolean useYn) {
        this.useYn = useYn;
    }

}
