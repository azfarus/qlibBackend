package com.example.qlibbackend.members;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member , String> {
}
