package jpabook.jpashop.service;


import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.item.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ItemUpdateTest {
    @Autowired
    EntityManager em;

    @Test
    public void updateTest() throws Exception{
        Book book = em.find(Book.class, 1L);

        //TX
        book.setName("고영진");

        //변경감지 == dirty checking
        //Order의 setStatus에서 cancel 동작시에 CANCEL 상태로 바꾸는것은 있지만 DB update를 따로 날려주진 않았다. -> 값이 바뀌면 trasaction commit 시점에 바뀐곳을 찾아 DB에 update를 보내고 tx commit -> 플러시할때 Dirty Check가 일어난다
        //준영속 Entity : item(book)을 만들어 set으로 값을 설정한 뒤에 BookForm을 통해서 data를 넘겨준다. -> 이때 updateItem 부분에서 book객체를 만들지만 이는 새로운 book이 아닌, 새로운 객체이지만 id가 세팅되어 있는 -> JPA를 방문한적있는, DB에 다녀온 적 있는것을 준영속 상태 Entity라 한다.
        //영속성Context가 더이상 관리하지 않는다. -> JPA가 관리하지 않는다 -> 변경감지가 일어나지 않는다.
        //준영속 Entity는 DB에 이미 한번 저장되어서 식별자가 존재하는 상태

        //준영속 Entity를 수정하는 방법 -> 변경감지 사용
        //병합 사용 -> saveItem - ItemRepository의 save에서는 merge가 발생한다. 위의 변경감지 코드와 같은 동작을 수행한다. parameter로 들어온 값을 찾아온 값과 모두 바꾼다. -> tx commit될때 반영된다.

        //차이점 : merge는 null값도 의도와 다른게 수정할 수 있다. 예를 들어 값을 고정하고 싶어 setPrice를 하지 않으면 해당 값은 null이 되고 이 값이 DB에 올라가게된다. -> merge를 사용하지 않는것이 좋다.
        //Entity 레벨에서 수정이 일어날 수 있도록 set을 통한 값 입력은 지양하자.



        //TX commit

    }
}
