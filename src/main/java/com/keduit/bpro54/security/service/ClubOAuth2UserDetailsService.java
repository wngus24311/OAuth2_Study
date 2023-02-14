package com.keduit.bpro54.security.service;

import com.keduit.bpro54.entity.ClubMember;
import com.keduit.bpro54.entity.ClubMemberRole;
import com.keduit.bpro54.repository.ClubMemberRepository;
import com.keduit.bpro54.security.dto.ClubAuthMemberDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class ClubOAuth2UserDetailsService extends DefaultOAuth2UserService {

    private final ClubMemberRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("ClubOAuth2UserDetailsService.loadUser.userRequest ========> " + userRequest);

        String clientName = userRequest.getClientRegistration().getClientName();
        log.info("clientName ===========> " + clientName);
        log.info("clientID =========> " + userRequest.getClientRegistration().getClientId());
        log.info("clientID =========> " + userRequest.getAdditionalParameters());

        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info(oAuth2User);
        Map<String, Object> paramMap = oAuth2User.getAttributes();
        String email = null;
        String name = null;

        switch (clientName) {
            case "kakao" :
                email = getKakaoEmail(paramMap);
                break;
            case "google" :
            default:
                break;
        }

        oAuth2User.getAttributes().forEach((k, v) -> log.info(k + " : " + v));

        ClubMember member = saveSocialMember(email, clientName);

        ClubAuthMemberDTO clubAuthMemberDTO = new ClubAuthMemberDTO(
                member.getEmail(),
                member.getPassword(),
                true,
                member.getRoleSet().stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                        .collect(Collectors.toList()),
                oAuth2User.getAttributes());

        clubAuthMemberDTO.setName(member.getName());

        return clubAuthMemberDTO;
    }

    private ClubMember saveSocialMember(String email, String clientName) {
        Optional<ClubMember> result = repository.findByEmail(email, true);

        if (result.isPresent()) {
            return result.get();
        }

        ClubMember clubMember = ClubMember.builder()
                .email(email)
                .name(clientName)
                .password(passwordEncoder.encode("1111"))
                .fromSocial(true)
                .build();
        clubMember.addMemberRole(ClubMemberRole.USER);
        repository.save(clubMember);

        return clubMember;
    }

    public String getKakaoEmail(Map<String, Object> map) {
        log.info("======== kakao ========");
        Object value = map.get("kakao_account");

        log.info("kakao_account : " + value);

        LinkedHashMap accountMap = (LinkedHashMap) value;

        String email = (String) accountMap.get("email");

        log.info("email =========> " + email);

        return email;
    }
}
