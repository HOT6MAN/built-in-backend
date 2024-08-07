package com.example.hotsix.service.member;

import com.example.hotsix.dto.member.MemberDto;
import com.example.hotsix.enums.Process;
import com.example.hotsix.exception.BuiltInException;
import com.example.hotsix.model.Member;
import com.example.hotsix.repository.member.MemberImageRepository;
import com.example.hotsix.repository.member.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{
    private final MemberRepository memberRepository;
    private final MemberImageRepository memberImageRepository;

    @Override
    public Member findById(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new BuiltInException(Process.USER_NOT_FOUND));
    }

    @Override
    public Member findMemberProfileByMemberId(Long id) {
        return memberRepository.findMemberProfileByMemberId(id);
    }
    @Override
    public Resource findMemberProfileImageByMemberId(Long id) {
        try{
            Member member = memberRepository.findMemberProfileByMemberId(id);
            Path path = getImagePath(member);
            Resource resource = new UrlResource(path.toUri());
            return resource;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public Path getImagePath(Member member){
        if(member.getMemberImage() == null){
            return Paths.get("C:/upload/img/default-profile.png");
        }
        String UUID = member.getMemberImage().getFixedName();
        String saveFolder = member.getMemberImage().getSaveFolder();
        String basicPath = "C:/upload/img"+ File.separator+saveFolder+File.separator+UUID;
        return Paths.get(basicPath);
    }
    @Override
    public Resource getImagePath(){
        try{
            String basicPath = "C:/upload/img/default-profile.png";
            Path path = Paths.get(basicPath);
            Resource resource = new UrlResource(path.toUri());
            return resource;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public String getContentType(Member member){
        try{
            Path path = getImagePath(member);
            Resource resource = new UrlResource(path.toUri());
            if(resource.exists() || resource.isReadable()){
                String contentType = Files.probeContentType(path);
                if(contentType == null){
                    return "application/octet-stream";
                }
                System.out.println("path = "+path.toString());
                System.out.println("content Type = "+contentType);
                return contentType;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    @Transactional
    public boolean deleteMemberByMebmerId(Long id) {
        try{
            memberRepository.deleteMemberByMemberId(id);
            return true;
        }
        catch(Exception e) {
            return false;
        }
    }

    @Override
    @Transactional
    public boolean updateMemberProfileByMemberId(Member dto) {
        try{
            memberRepository.updateMemberProfileByMemberId(dto);
            return true;
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    @Transactional
    public boolean updateMemberProfileImageByMemberId(Member dto) {
        try{
            Member member = memberRepository.findMemberProfileByMemberId(dto.getId());
            member.setMemberImage(dto.getMemberImage());
            memberRepository.flush();
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
