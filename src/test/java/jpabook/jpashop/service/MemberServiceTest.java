package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
//기본 import문을 삭제하고 assertEqual()를 치고 import를 사용하면 알맞게 import된다.


// @RunWith(SpringRunner.class) ->Junit5에서는 springBootTest에 추가되어있다.
@SpringBootTest
@Transactional //rollback을 위해
//spring과 integration해서 test 가능하다
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    //testcase로 참조할게 없기때문에 간단하게 Field injection
    @Autowired EntityManager em;
    //EntityManager를 받고 em.flush()를 호출 -> 영속성 Context에 meber 객체가 들어가고 query로 db에 반영이된다 + transaction은 rollback
    //영속성 Context에 있는 query를 날리고 그 다음에 test가 끝날때 rollback을 진행(db에 데이터가 남으면 안되기 때문에 rollback을 진행해줘야한다)

    @Test
//    @Rollback(false)
    public void 회원가입() throws Exception{
        //given
        Member member = new Member();
        member.setName("kim");

        //when
        Long saveId = memberService.join(member);
        //join시에 member객체의 save안에 persist하지만 db에는 insert문이 나가지 않는다(Generate value 전략에서는)
        //db transcation이 commit될때 플러시가 되면서 db insert query가 나가게된다.
        //jpa 영속성 context에 있는 멤버 객체가  insert를 만들면서 나간다.
        //spring에서 transcation은 commit을 하지 않고 rollback을 한다 -> insert가 안보임 -> @rollback(false) 지정하면 insert문 확인 가능

//        insert
//                into
//        member
//                (city, street, zipcode, name, member_id)
//        values
//                (?, ?, ?, ?, ?)

        //then
        em.flush();
        assertEquals(member, memberRepository.findOne(saveId)); //member와 저장된 Id로 찾은 member가 같은지
    }

    @Test //(expected = IllegalStateException.clas다) 중간에 exception이 발생하였을때 IllegalStateException이면 return -> junit5는 expected를 지원하지 않는다 -> assertThrows로 해결
    public void 중복_회원_예외() throws Exception{
        //given
        Member member1 = new Member();
        member1.setName("kim1");

        Member member2 = new Member();
        member2.setName("kim1"); //이름이 같은 member
        //when
        memberService.join(member1);
        //then
        assertThrows(IllegalStateException.class, ()->memberService.join(member2));
        //member2를 join했을때 IllegalStateException이 발생하면 된다.

    }

}