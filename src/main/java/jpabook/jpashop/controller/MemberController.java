package jpabook.jpashop.controller;

import jakarta.validation.Valid;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.stream.Stream;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/members/new") //get방식으로 오면 return이 열린다
    public String createForm(Model model){ //model
        model.addAttribute("memberForm", new MemberForm()); //
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String create(@Valid MemberForm form, BindingResult result){
        if(result.hasErrors()){
            return "members/createMemberForm";
        } //라이브러리에 thymleaf과 spring이 강하게 integration되어있다.
        //서버 사이드에서 validation을 하고 문제가 생겨 result에 data 하나가 들어옴 -> (result)에서는 error에 대한 데이터를 찾을 수 있는 method가 많다 -> binding result에서 끌고와서 쓸 수 있게 도와준다
        //이름만 없을 경우 기입한 data들은 유지가 된다. 해당 data들은 form에 의해 createMemberForm으로 가지고 들어가게되어 다시 form에 뿌려지게 된다.

        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());
        Member member = new Member();
        member.setName(form.getName());
        member.setAddress(address);

        memberService.join(member);
        return "redirect:/"; //첫 화면으로 넘어간다
    }

    @GetMapping("/members")
    public String list(Model model){
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members); //addAttribute를 사용시에 넣어주는것을 명확하게 쓰기 위해 인라인을 사용하지 않았다
        return "/members/memberList";
    }
    //Entity를 form으로 처리하면 -> form에는 화면을 처리하기 위한 기능이 많아져  Entity가 화면 종속적으로 변해 코드가 복잡해져 유지 보수가 어려워진다.
    //Entity를 최대한 순수하기 유지하는 것이 중요하다.
    //DTO, 화면에 맞는 API를 사용하는것이 좋다.
    //핵심 비즈니스 로직과 화면을 분리해서 관리 -> 등록시에는 form을 사용하고 model을 이용해서 화면에 출력하는 형태이지만 data를 DTO로 변환해서 필요한 data만 가져가는것이 좋다.
    //API를 만들때는 절대로 Entity를 웹으로 반환하면 안된다 -> 보안에 문제가 생기며 api spec 자체가 바뀌어버릴 수 있다.
}
