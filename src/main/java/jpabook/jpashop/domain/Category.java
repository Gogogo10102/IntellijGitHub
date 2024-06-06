package jpabook.jpashop.domain;

import jakarta.persistence.*;
import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Category {
    @Id @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private  String name;

    @ManyToMany
    @JoinTable(name = "category_item",
            joinColumns = @JoinColumn(name = "category_id"), //category쪽으로 들어가는 id를 mapping
            inverseJoinColumns = @JoinColumn(name = "item_id")) //item쪽으로 들어가는 id를 mapping
    //객체는 모두 collection이 있어 다대다 관계가 가능하지만 관계형 DB collection 관계를 양쪽에 가질 수 있는게 아니므로 일대다 다대일로 풀어내는 중간 테이블이 필요하(실무에서는 거의 쓰지 못한다)
    private List<Item> items = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();
    //양방향 연관관계를 설정

    //--연관관계 메서드--//
    public void addChildCategory(Category child){
        this.child.add(child);
        child.setParent(this);
    }

}
