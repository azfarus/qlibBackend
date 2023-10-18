package com.example.qlibbackend.fines;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FineRepository extends JpaRepository<Fine , Long> {
}
