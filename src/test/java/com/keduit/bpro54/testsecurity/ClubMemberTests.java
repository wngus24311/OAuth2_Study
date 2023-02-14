package com.keduit.bpro54.testsecurity;

import com.keduit.bpro54.entity.ClubMember;
import com.keduit.bpro54.entity.ClubMemberRole;
import com.keduit.bpro54.repository.ClubMemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class ClubMemberTests {
	@Autowired
	private ClubMemberRepository repository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Test
	public void insertDummies() {
		IntStream.rangeClosed(1, 100).forEach(i -> {
			ClubMember clubMember = ClubMember.builder()
					.email("user" + i + "@abc.com")
					.name("사용자" + i)
					.fromSocial(false)
					.password(passwordEncoder.encode("1111"))
					.build();
			clubMember.addMemberRole(ClubMemberRole.USER);
			
			if (i > 80) {
				clubMember.addMemberRole(ClubMemberRole.MANAGER);
			} else if (i > 90) {
				clubMember.addMemberRole(ClubMemberRole.ADMIN);
			}
			repository.save(clubMember);
		});
	}
	
	@Test
	public void testRead() {
		Optional<ClubMember> result = repository.findByEmail("user10@abc.com", false);
		ClubMember clubMember = result.get();
		
		System.out.println("result = " + result);
		System.out.println("clubMember = " + clubMember);
	}
}
