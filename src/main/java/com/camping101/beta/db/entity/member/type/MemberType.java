package com.camping101.beta.db.entity.member.type;

import com.camping101.beta.web.domain.member.exception.ErrorCode;
import com.camping101.beta.web.domain.member.exception.MemberException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import org.springframework.security.core.GrantedAuthority;

public enum MemberType implements GrantedAuthority {

    CUSTOMER, OWNER, ADMIN;

    public static MemberType of(List<GrantedAuthority> roles) throws MemberException {

        Optional<String> optionalMemberTypeString = roles.stream()
            .map(GrantedAuthority::getAuthority).findFirst();

        if (optionalMemberTypeString.isEmpty()) {
            throw new MemberException(ErrorCode.MEMBER_SIGN_IN_ERROR);
        }

        return MemberType.valueOf(optionalMemberTypeString.get().toUpperCase(Locale.ROOT));
    }

    @Override
    public String getAuthority() {
        return this.name();
    }
}
