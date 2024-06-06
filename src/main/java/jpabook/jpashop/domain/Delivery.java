package jpabook.jpashop.domain;

import jakarta.persistence.*;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.DeliveryStatus;
import lombok.Getter;
import lombok.Setter;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter @Setter

public class Delivery {
    @Id @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery", fetch = LAZY)
    private Order order;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    //type ordinary일떄 상태값이 0, 1, 2로 표현되는데 중간에 다른 상태값이 오면 원래값이 뒤로 밀려 장애가 발생할 수 있어 사용하지 않는다.
    private DeliveryStatus status; //READY, COMP

}