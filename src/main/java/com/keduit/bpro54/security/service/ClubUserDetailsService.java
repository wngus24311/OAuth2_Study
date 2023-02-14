package com.keduit.bpro54.security.service;

import com.keduit.bpro54.entity.ClubMember;
import com.keduit.bpro54.repository.ClubMemberRepository;
import com.keduit.bpro54.security.dto.ClubAuthMemberDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class ClubUserDetailsService implements UserDetailsService {

    private final ClubMemberRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("ClubUserDetailsService loadUserByUsername ======> " + username);

        Optional<ClubMember> result = repository.findByEmail(username, false);

        if (result.isEmpty()) {
            throw new UsernameNotFoundException(username + "을 찾을 수 없습니다.");
        }

        ClubMember clubMember = result.get();
        log.info("clubMember ========> " + clubMember);
        ClubAuthMemberDTO clubMemberDTO = new ClubAuthMemberDTO(
                clubMember.getEmail(),
                clubMember.getPassword(),
                clubMember.isFromSocial(),
                clubMember.getRoleSet().stream().map(
                        role -> new SimpleGrantedAuthority("ROLE_" + role.name())).collect(Collectors.toSet())
        );
        clubMemberDTO.setName(clubMember.getName());
        clubMemberDTO.setEmail(clubMember.getEmail());
        clubMemberDTO.setFromSocial(clubMember.isFromSocial());
        log.info(clubMemberDTO);
        return clubMemberDTO;
    }
}
