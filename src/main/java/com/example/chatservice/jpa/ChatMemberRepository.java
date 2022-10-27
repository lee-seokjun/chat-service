package com.example.chatservice.jpa;

import org.springframework.data.repository.CrudRepository;

public interface ChatMemberRepository extends CrudRepository<ChatMember,Long> {
    Iterable<ChatMember> findByUserId(String memberId);
}
