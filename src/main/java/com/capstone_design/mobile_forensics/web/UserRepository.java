package com.capstone_design.mobile_forensics.web;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<UserData, Long> {
    @Override
    UserData save(UserData entity);

    @Override
    Optional<UserData> findById(Long userId);
}
