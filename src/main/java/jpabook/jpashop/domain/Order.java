package jpabook.jpashop.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY) //order 조회시에 member를 join해서 같이 가져온다(em.find시에) EAGER는 예측이 어렵고 추적이 어렵다(JPQL을 실행할 때 N+1 문제가 자주 발생한다) -> 지연 로딩으로 설정해야한다.
    //@X to One은 모두 EAGER로 패치가 되어있다
    //OnetoMany는 LAZY로 패치 되어있다.
    @JoinColumn(name = "member_id") //연관관계 주인
    //order 테이블에 있는 member 필드에 의해서 mapping 되었
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL) //orderItems에 data를 넣어두고 order를 저장하면 orderitems도 같이 저장이 된다.
    //persist(orderItemA)
    //persist(orderItemB)
    //persist(orderItemC)
    //persist(order)로 저장해야하는데 위의 orderItem들을 생략할 수 있게 된다
    //Cascade를 전파한다
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL) //order 저장할대 delivery도 같이 저장된다.
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    //order_date DBA에서는 underscore를 많이 쓰므로 자바의 camel case를 underscore로 바꿔준다 + (.) -> (_) + 대문자 -> 소문자
    private LocalDateTime orderDate; //Hidernate가 Annotation을 자동 지원해준다(java8~)

    @Enumerated(EnumType.STRING)
    private OrderStatus status; //주문상태 [ORDER, CANCEL]

    //--연관관계 메서드--//
    public void setMember(Member member){
        this.member = member;
        member.getOrders().add(this);
    } //값을 받아와 자신과 반대편의 값 모두에 집어 넣어야한다

    public void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    //--생성 메서드--//
    //생성에 필요한 기능을 모두 여기에 넣는다
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems){
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for (OrderItem orderItem : orderItems){
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    //--주문 취소--//
    public void cancel(){
        if(delivery.getStatus() == DeliveryStatus.COMP){ //COMP(배송완료)이면..
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }

        this.setStatus(OrderStatus.CANCEL);
        for (OrderItem orderItem : orderItems){ //this가 생략되어있는 상태이지만 강조할때는 this를 써주는것도 좋다
            orderItem.cancel();
        }
    }


    //--조회 로직--//

    //-- 전체 주문 가격 조회--//
    public int getTotalPrice(){
        int totalPrice = orderItems.stream().mapToInt(OrderItem::getTotalPrice).sum();
//         int totalPrice = 0;
//                for(OrderItem orderItem : orderItems){
//                    totalPrice += orderItem.getTotalPrice(); //
//                }

        //stream을 이용해서 간단하게 줄일 수 있다
        return totalPrice;
    }







}
