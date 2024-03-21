package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {

    // 테스트 요구사항
    // 회원 가입에 성공해야 한다
    // 회원 가입 시, 같은 이름이 있으면 예외가 발생해야 한다

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    @Test
    public void 회원가입() throws Exception {
        // given
        Member member = new Member();
        member.setName("Hyewon");

        // when
        Long joinMemberId = memberService.join(member);

        // then
        Assert.assertEquals(member, memberRepository.findOne(joinMemberId));
    }

    @Test(expected = IllegalStateException.class)
    public void 중복_회원_예외() throws Exception {
        // given
        Member member1 = new Member();
        member1.setName("Hyewon");

        Member member2 = new Member();
        member2.setName("Hyewon");

        // when
        memberService.join(member1);
        memberService.join(member2);    // 예외 발생 지점

        // then
        fail("예외가 발생해야 한다.");
    }
}