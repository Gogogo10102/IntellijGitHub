package jpabook.jpashop.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
//값 타입은 getter만 설정 -> 값은 immutable하게 설계해야한
//setter가 없어 변경이 불가하게 만들었다.
public class Address {
    private String city;
    private String street;
    private String zipcode;

    protected Address() { //기본 생성자 생성(jpa 스펙에서는 protected까지 허용해준다

    }
    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    } //이런식으로 해둬야한다.


}
