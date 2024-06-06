package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
//jpa를 이용하면 PersistenceContext를 @Autowired로 대체 가능하므로 MemberService와 같은 형태로 생성자 주입이 가능하
//Component scan에 의해 자동으로 스프링빈에 관리
public class MemberRepository {
    //@PersistenceContext -> EntityManger는 표준 annotation인 persistenceContext를 사용해야하지만 spring에서는 Autowired로도 할 수 있게 지원해준다
    //
    private final EntityManager em;
    //spring이 manager를 만들서 injection해준다

    //--저장--//
    public void save(Member member){
        em.persist(member); //영속성 Context에 Member Entity를 넣고 transcation이 commit되는 시점에 db에 반영이 된
    }

    //--조회(단건조회)--//
    public Member findOne(Long id){
        return em.find(Member.class, id); //jpa가 제공하는 find
    }

    //--리스트 조회--//
    public List<Member> findAll(){
        return em.createQuery("select m from Member m", Member.class).getResultList(); //sql은 table을 대상으로 query를 하지만 creaeQuery는 entity 객체를 대상으로 쿼리
        //JPQL을 사용(SQL과 거의 유사)
    }

    public List<Member> findByName(String name){
        return em.createQuery("select m from Member m where m.name = :name", Member.class).setParameter("name", name).getResultList(); //Parameter가 바인디
    }
}
