package jpabook.jpashop.controller;


import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/items/new")
    public String createForm(Model model){
        model.addAttribute("form", new BookForm());
        return "items/createItemForm";
    }

    @PostMapping("/items/new")
    public String create(BookForm form){
        Book book = new Book();
        book.setName(form.getName());
        book.setPrice(form.getPrice());
        book.setStockQuantity(form.getStockQuantity());
        book.setAuthor(form.getAuthor());
        book.setIsbn(form.getIsbn());
        //set으로 값을 초기화하는 것이 아닌 createBook이라는 기능을 만들어 Parameter를 넘겨 사용할 수 있도록 해야한다.
        //실무에서는 setter를 모두 날린다.
        itemService.saveItem(book);
        return "redirect:/";
        //'불온서적',15000,3,'최승균','1313','B',1 -> B는 class Book의 @DiscriminatorValue("B")이다
    }
    @GetMapping("/items")
    public String list(Model model){
        List<Item> items = itemService.findItems();
        model.addAttribute("items", items);
        return "items/itemsList";
    }
    //merge(병합)을 사용하지만 변경감지를 통한 설계가 좋은 설계방법이다.

    @GetMapping("/items/{itemId}/edit") //중간에 있는건 값이 변경될 수 있어 pass variable ->   {}
    public String updateItemForm(@PathVariable("itemId") Long itemId, Model model){ //@PathVariable을 통해서 위와 mapping해준다.
        Book item = (Book)itemService.findOne(itemId); //casting사용(예제의 단순화를 위해)
        BookForm form = new BookForm();;
        form.setId(item.getId());
        form.setName(item.getName());
        form.setPrice(item.getPrice());
        form.setStockQuantity(item.getStockQuantity());
        form.setAuthor(item.getAuthor());
        form.setIsbn(item.getIsbn());

        model.addAttribute("form", form);
        return "items/updateItemForm";
    }
    @PostMapping("/items/{itemId}/edit")
    public String updateItem(@ModelAttribute("form") BookForm form) { //@ModelAttribute -> html의 th:object에서 잡은걸 그대로 가져오도로고 한다
        //조회하는 유저가 해당 item에 권한이있는지 확인하는 logic이 있어야한다.
        Book book = new Book();
        book.setId(form.getId());
        book.setName(form.getName());
        book.setPrice(form.getPrice());
        book.setStockQuantity(form.getStockQuantity());
        book.setAuthor(form.getAuthor());
        book.setIsbn(form.getIsbn());
        //itemService.updateItem(itemId, form.getName(), form.getPrice()...)형태로 설계하여 updateItem에서(Entity레벨)에서 수정하도록 한다.
        //updateItem의 paramter가 너무 많은 경우 UpdateItemDto를 만들어 해당 parameter를 getter setter로 받아 넘겨주는 식으로 설계한다.
        itemService.saveItem(book);
        return "redirect:/items";
    }
}


