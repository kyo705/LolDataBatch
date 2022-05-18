package com.SpringBatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.SpringBatch.Entity.Member;
import com.SpringBatch.Entity.MemberCompKey;

public interface MemberRepository extends JpaRepository<Member, MemberCompKey> {

}
