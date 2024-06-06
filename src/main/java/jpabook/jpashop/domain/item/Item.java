package jpabook.jpashop.domain.item;

import jakarta.persistence.*;
import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
//name이 Book이면 어떤식으로 할거다

public abstract class Item {
    @Id
    @GeneratedValue
    @Column(name = "item")
    private Long id;
    //상속관계 mapping

    private String name;

    private int price;

    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<Category>();

    //data를 직접 다루는 로직(method)은 data를 가지고 있는 곳에 만들어주면 좋다(응집도를 높여 객체지향적 설계가 가능하다)


    //-- stock(재고) 증가 --//
    public void addStock(int quantity){
        this.stockQuantity += quantity;
    }

    //-- stock 감소 --//
    public void removeStock(int quantity){
        int restStock = this.stockQuantity - quantity;
        if(restStock < 0 ){
            throw new NotEnoughStockException("need more stock"); //Exception을 만들어 내보낸다
        }
        this.stockQuantity = restStock;
    }







}

