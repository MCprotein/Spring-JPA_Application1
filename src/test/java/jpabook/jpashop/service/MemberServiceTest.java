package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired EntityManager em;

    @Test
//    @Rollback(value = false)
//    테스트에서 @Transactional은 기본적으로 롤백이기 때문에 쿼리에서 insert를 안한다.
    public void 회원가입 () throws Exception {
        //given
        Member member = new Member();
        member.setName("kim");


        //when
        Long saveId = memberService.join(member);

        //then
        em.flush(); // flush: 영속성 데이터를 데이터베이스에 반영
        assertEquals(member, memberRepository.findOne(saveId));
    }

    @Test
    public void 중복_회원_예외() throws Exception {
        //given
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");
        //when
        memberService.join(member1);
            //then
//        assertThrows(IllegalStateException.class, ()
//                -> memberService.join(member2));
//        Junit5는 expected 지원안함
        assertAll(
                () -> assertThrows(
                        IllegalStateException.class,
                        () -> memberService.join(member2)
                )
        );
    }
}