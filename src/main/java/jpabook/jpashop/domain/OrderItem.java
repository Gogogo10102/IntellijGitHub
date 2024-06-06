package jpabook.jpashop.domain;

import jakarta.persistence.*;
import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;
import org.aspectj.weaver.ast.Or;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter
@Setter
public class OrderItem {
    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "order_id") //연관관계 주인
    private Order order;

    private int orderPrice;
    private int count;


    //--생성 메서드--//
    public static OrderItem createOrderItem(Item item, int orderPrice, int count){
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);
        //orderitem을 만들고 재고수량을 감소시킨뒤에 return해준다

        item.removeStock(count);
        return orderItem;
    }

    //--비즈니스 로직--//
    public void cancel() {
        getItem().addStock(count);
    }

    //--조회 로직--//
    public int getTotalPrice() {
        return getOrderPrice()*getCount();
    }
}
