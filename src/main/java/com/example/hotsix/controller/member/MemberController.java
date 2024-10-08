package com.example.hotsix.controller.member;

import com.example.hotsix.dto.member.MemberDto;
import com.example.hotsix.dto.team.TeamDto;
import com.example.hotsix.model.MemberImage;
import com.example.hotsix.model.Member;
import com.example.hotsix.service.member.MemberImageService;
import com.example.hotsix.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
@Slf4j
public class MemberController {
    private final MemberService memberService;
    private final MemberImageService memberImageService;

    /**
     *
     * @param memberId
     * @return
     * 멤버 모든 정보 가져오기.
     */
    @GetMapping("/{memberId}")
    public MemberDto findMemberProfileByMemberId(@PathVariable("memberId")Long memberId){
//        return memberService.findMemberProfileByMemberId(memberId);
        Member member = memberService.findMemberProfileByMemberId(memberId);
        MemberDto dto = member.toDto();
        dto.setRegDTTM(member.getCreatedDate());
        return dto;
    }

    @GetMapping("/image/{memberId}")
    public ResponseEntity<Resource> findMemberProfileImageByMemberId(@PathVariable("memberId")Long memberId){
        log.info("find Member Profile Image By Member Id Controller 도착");
        Member member = memberService.findMemberProfileByMemberId(memberId);
        log.info(" MemberId로부터 찾은 member = {}", member);
        String contentType = memberService.getContentType(member);
        log.info(" member의 프로필 이미지 content type = {}", contentType);
        Resource resource = memberService.findMemberProfileImageByMemberId(memberId);
        if(resource!=null) {
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        }
        Resource defaultResource = memberService.getImagePath();
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("image/png"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + defaultResource.getFilename() + "\"")
                .body(defaultResource);
    }

    /**
     *
     * @param memberId
     * @return
     * 회원 탈퇴 메서드
     * 멤버 컬럼의 DEL_YN를 true로 바꾼다.
     */
    @DeleteMapping("/{memberId}")
    public String deleteMemberByMemberId(@PathVariable("memberId")Long memberId){
        boolean flag = memberService.deleteMemberByMebmerId(memberId);
        if(flag) return "delete success";
        else return "delete error";
    }


    /**
     *
     * @param memberId
     * @param member
     * @return
     * 회원 정보 변경 메서드
     * 멤버 컬럼의 값을 변경한다.
     */
    @PutMapping("/{memberId}")
    public String updateMemberProfileByMemberId(@PathVariable("memberId")Long memberId, @RequestBody MemberDto member){
        System.out.println("member = "+member);
        log.info("user name = ",member.getName());
        boolean flag = memberService.updateMemberProfileByMemberId(member);
        if(flag) return "update success";
        else return "update error";
    }


    /**
     *
     * @param memberId
     * @param file
     * @return
     *
     * 프로필 이미지 변경 메서드
     * 멤버의 프로필 이미지를 변경한다.
     */
    @PutMapping("/image/{memberId}")
    public String updateMemberProfileImageByMemberId(@PathVariable("memberId") Long memberId,
                                                     @RequestParam("image") MultipartFile file){
        Map<String, Object> map = new HashMap<>();
        try{
            if(file!=null){
                String today = new SimpleDateFormat("yyMMdd").format(new Date());
                String uploadDir ="/spring/image";
                String imgDirPath =uploadDir+ File.separator+today;
                File folder =new File(imgDirPath);
                if(!folder.exists()){
                    folder.mkdirs();
                }
                MemberImage image =  memberImageService.findMemberImageByMemberId(memberId);
                Member member = memberService.findById(memberId);
                if(image == null){
                    image = new MemberImage();
                }
                image.setMember(member);
                member.setMemberImage(image);
                String originName = file.getOriginalFilename();
                if(!originName.isEmpty()){
                    String fixedFileName = UUID.randomUUID()
                            .toString()+
                            originName.substring(originName.lastIndexOf("."));
                    image.setOriginName(originName);
                    image.setFixedName(fixedFileName);
                    image.setSaveFolder(today);
                    file.transferTo(new File(folder, fixedFileName));
                }
                image.setType("profile");
                memberService.updateMemberProfileImageByMemberId(member);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
