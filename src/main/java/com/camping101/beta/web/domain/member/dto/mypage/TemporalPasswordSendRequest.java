package com.camping101.beta.web.domain.member.dto.mypage;

import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TemporalPasswordSendRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private String email;

}
