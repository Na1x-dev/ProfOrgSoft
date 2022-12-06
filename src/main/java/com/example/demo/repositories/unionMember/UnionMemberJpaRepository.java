package com.example.demo.repositories.unionMember;

import com.example.demo.models.PhoneNumber;
import com.example.demo.models.UnionMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UnionMemberJpaRepository extends JpaRepository<UnionMember, Long> {
    UnionMember getByUnionMemberId(Long unionMemberId);

    UnionMember getByName(String name);

    List<UnionMember> getByPositionPositionId(Long positionId);

    List<UnionMember> getBySurname(String surname);

    List<UnionMember> readByPositionPositionTitle(String positionTitle);
}