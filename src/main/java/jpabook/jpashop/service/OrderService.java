package jpabook.jpashop.service;


import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    //주문
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        //배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        //주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);
        //이는 이전에 createOrderItem이라는 static method를 만들어 둔 상태로 진행하여 쉽게 만들수 있었지만 orderitem 객체를 만들어 값을 집어넣고 설계할 수도 있다
        //하지만 이는 생성 스타일의 일관성 유지 부분에 있어 유지보수가 힘들어질 수 있다. -> new와 같은 직접적으로 객체를 만드는 것을 막기 위해 해당 class의 생성자를 protected로 override 해주면 컴파일 오류가 발생하게 만들어 제약한다.
        //또는 NoArgsConstructor에서 accessLevel을 protected로 지정해주면 같은 효과를 줄 수 있다.
        //이를 통해 직접 생성이 아닌 다른 스타일의 생성이 필요함을 알려준다.

        //주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        //주문 저장
        orderRepository.save(order);

        return order.getId();

        //위의 생성시에는 jpa에 저장하지 않고 주문저장 부분에만 jpa에 저장을 하였다. 이는 order의 orderitem은 cascadeType.all 설정을 하였다 -> order를 persist를 하면 orderitem도 모두 강제로 persist해주는것이다.(delivery도 포함)
        //cascade의 범위는 다른 class가 참조하지 않는 private owner인 경우에 사용하면 도움을 받을 수 있다.
        //다른 class에도 참조되어있는 상태에서는 cascasde로 인해서 데이터 관리가 어려워질 수 있다.

        //member, item entity를 조회(이를 위해 연관관계를 넣어줌) -> 배성정보, 주문상품을 createXXX method로 생성 ->
    }

    //취소
    @Transactional
    public void cancelOrder(Long orderId){
        //주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);
        //주문 취소
        order.cancel();
        //order cancel시의 상태를 cancel로 바꿔주고 count(재고)를 원복시킨다. 이때 jpa는 해당 data의 변경사항을 dirty checking(변경 내역 감지)를 하여 자동으로 query가 생성되어 전송된다.
        //즉 order(state)와 item(stock)에서 변경이 일어났으므로 jpa를 통해 db로 query가 전송된다.
    }


    //검색
//    public List<Order> findOrders(OrderSearch orderSearch){
//        return orderSearch.findAll(orderSearch);
//    }

}

//도메인 모델 패턴 : Entity가 비즈니스 로직을 가지고 객체 지향 특성을 적극 활용하는 것 <-> 트랜잭션 스크립트 패턴
//Entity에 비즈니스 로직을 몰아 넣는 스타일


