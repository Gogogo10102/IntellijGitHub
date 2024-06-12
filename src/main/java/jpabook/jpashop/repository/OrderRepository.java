package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public void save(Order order){
        em.persist(order);
    }
    public Order findOne(Long id){
        return em.find(Order.class,id);
    }

    public List<Order> findAll(OrderSearch orderSearCh){
        return em.createQuery("select o from Order o join o.member m" , Order.class) //status, name을 모두 가져온다고 하면 동적쿼리가 되는데 + "where o.status = :status" + "and m.name like :name"
                .setParameter("status", orderSearCh.getOrderStatus()) //parameter 바인딩
                .setParameter("name", orderSearCh.getMemberName())
                .setMaxResults(1000) //최대 1000건(최대 1000개까지 조회 가능) paging
                .getResultList();
    }
    //JPA Criteria(JPA 표준스펙) -> jpql을 자바 코드로 작성할 수 있도록 도와주어 동적쿼리 작성에 도움을 준다. 하지만 유지보수성이 떨어지며 가독성 또한 떨어지게된다.
    //QueryDSL
    //QueryDSL + springJPA로 project를 하면 좋다.


    public List<Order> findAllByCriteria(OrderSearch orderSearch) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        Root<Order> o = cq.from(Order.class);
        Join<Order, Member> m = o.join("member", JoinType.INNER); //회원과 조인
        List<Predicate> criteria = new ArrayList<>();
//주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            Predicate status = cb.equal(o.get("status"),
                    orderSearch.getOrderStatus());
            criteria.add(status);
        }
//회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            Predicate name =
                    cb.like(m.<String>get("name"), "%" + orderSearch.getMemberName()
                            + "%");
            criteria.add(name);
        }
        cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
        TypedQuery<Order> query = em.createQuery(cq).setMaxResults(1000); //최대 1000 건
        return query.getResultList();
    }
}
