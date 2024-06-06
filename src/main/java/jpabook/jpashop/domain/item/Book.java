package jpabook.jpashop.domain.item;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@DiscriminatorValue("B")
//single table 이므로 저장할때 값을 구분할 수 있어야한다 그때 해당 값을 사용한다.
public class Book extends Item{

    private String author;
    private String isbn;

}
