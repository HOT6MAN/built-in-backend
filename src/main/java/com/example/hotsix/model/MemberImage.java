package com.example.hotsix.model;

import jakarta.persistence.*;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name="member_image")
public class MemberImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Member member;

    private String originName;
    private String fixedName;
    private String saveFolder;
    private String type;

    @Override
    public String toString() {
        return "MemberImageDto{" +
                "id=" + id +
                ", member=" + member +
                ", originName='" + originName + '\'' +
                ", fixedName='" + fixedName + '\'' +
                ", saveFolder='" + saveFolder + '\'' +
                ", type='" + type + '\'' +
                "}\n";
    }
}
