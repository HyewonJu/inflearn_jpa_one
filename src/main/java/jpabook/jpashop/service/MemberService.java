package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor // final 필드 초기화 생성자
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * 회원가입
     * @param member 가입 회원 정보
     * @return 가입 회원 식별자
     */
    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member); // 중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    /**
     * 중복 회원 검증
     * @param member 신규 가입하려는 회원 정보
     */
    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());

        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    /**
     * 전체 회원 조회
     * @return 전체 회원 리스트
     */
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    /**
     * 특정 회원 조회
     * @param memberId 회원 식별자
     * @return 조회한 회원
     */
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }

}
