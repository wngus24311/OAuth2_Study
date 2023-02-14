package com.keduit.bpro54.entity;

import lombok.*;
import org.springframework.context.annotation.Bean;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ClubMember {
	@Id
	private String email;
	
	private String password;
	
	private String name;
	
	private boolean fromSocial;
	
	@ElementCollection(fetch = FetchType.LAZY)
	@Builder.Default
	private Set<ClubMemberRole> roleSet = new HashSet<>();
	
	public void addMemberRole(ClubMemberRole user) {
		roleSet.add(user);
	}
	
	public void changePassword(String password) {
		this.password = password;
	}
	
	public void changeName(String name) {
		this.name = name;
	}
}
