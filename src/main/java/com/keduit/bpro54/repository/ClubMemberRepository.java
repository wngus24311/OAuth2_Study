package com.keduit.bpro54.repository;

import com.keduit.bpro54.entity.ClubMember;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.Entity;
import java.util.Optional;

public interface ClubMemberRepository extends JpaRepository<ClubMember, String> {
	@EntityGraph(attributePaths = {"roleSet"}, type = EntityGraph.EntityGraphType.LOAD)
	@Query("select m from ClubMember m where m.fromSocial = :social and m.email = :email")
	public Optional<ClubMember> findByEmail(String email, Boolean social);
}
