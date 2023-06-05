package com.camping101.beta.global.security.authentication;

import com.camping101.beta.db.entity.member.Member;
import com.camping101.beta.db.entity.member.status.MemberStatus;
import com.camping101.beta.db.entity.member.type.MemberType;
import com.camping101.beta.db.entity.member.type.SignUpType;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@RequiredArgsConstructor
public class MemberDetails implements UserDetails {

    private final Member member;

    public Long getMemberId() {
        return member.getMemberId();
    }

    public String getEmail() {
        return getUsername();
    }

    public MemberType getMemberType() {
        return getAuthorities().stream()
            .map(GrantedAuthority::getAuthority).map(MemberType::valueOf).findFirst().get();
    }

    public SignUpType getSignUpType() {
        return member.getSignUpType();
    }

    @Override
    public String getUsername() {
        return member.getEmail();
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    public String getProfileImagePath(){
        return member.getProfileImagePath();
    }

    public String getNickname(){
        return member.getNickname();
    }

    public String getPhoneNumber(){
        return member.getPhoneNumber();
    }

    public Member getMember(){
        return member;
    }

    public void setPassword(String encodedPassword){
        this.member.setPassword(encodedPassword);
    }

    public void setNickname(String nickName) {
        this.member.setNickname(nickName);
    }

    public void setPhoneNumber(String phoneNumber) {
        this.member.setPhoneNumber(phoneNumber);
    }

    public void setProfileImagePath(String profileImagePath) {
        this.member.setProfileImagePath(profileImagePath);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> member.getMemberType().name());
    }

    @Override
    public boolean isAccountNonExpired() {
        return !MemberStatus.WITHDRAW.equals(member.getMemberStatus());
    }

    @Override
    public boolean isAccountNonLocked() {
        return !MemberStatus.STOPPED.equals(member.getMemberStatus()) && isAccountNonExpired();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return MemberStatus.IN_USE.equals(member.getMemberStatus());
    }

}
