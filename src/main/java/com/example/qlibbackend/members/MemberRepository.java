package com.example.qlibbackend.members;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member , String> {
    Optional<Member> findMemberByUsername(String username);
}
