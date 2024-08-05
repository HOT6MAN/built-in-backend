package com.example.hotsix.model;

import com.example.hotsix.dto.member.MemberDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Builder
@Table(name = "member")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"memberImage"})
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name="name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "profile_url")
    private String profileUrl;

    @Column(name = "phone")
    private String phone;

    @Column(name = "address")
    private String address;

    @Column(name = "role", nullable = false)
    private String role;

    @Column(name = "lgn_mtd", nullable = false)
    private String lgnMtd;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private MemberImage memberImage;

    @OneToMany(mappedBy = "member")
    private List<MemberTeam> memberTeams = new ArrayList<>();

    public void setMemberImage(MemberImage memberImage) {
        this.memberImage = memberImage;
        memberImage.setMember(this);
    }

    public MemberDto toDto() {
        return MemberDto.builder()
                .id(this.id)
                .address(this.address)
                .role(this.role)
                .phone(this.phone)
                .email(this.email)
                .lgnMtd(this.lgnMtd)
                .nickname(this.nickname)
                .profileUrl(this.profileUrl)
                .name(this.name)
                .build();
    }

    public MemberDto toDtoForMemberTeam(){
        MemberDto memberDto = MemberDto.builder()
                .id(this.id)
                .address(this.address)
                .role(this.role)
                .phone(this.phone)
                .email(this.email)
                .lgnMtd(this.lgnMtd)
                .nickname(this.nickname)
                .profileUrl(this.profileUrl)
                .name(this.name)
                .build();
        return memberDto;
    }



    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", nickname='" + nickname + '\'' +
                ", profileUrl='" + profileUrl + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", role='" + role + '\'' +
                ", lgnMtd='" + lgnMtd + '\'' +
                ", memberImage=" + memberImage +
                '}';
    }
}
