package jpabook.jpashop.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.logging.Logger;


@Controller
@Slf4j
//Logger log = LoggerFactory.getLogger(getClass());
//위와 같이 log를 뽑아올 수 있다
public class HomeController {

    @RequestMapping("/")
    public String home(){
        log.info("home controller");
        return "home";

    }
}
// "/"로 들어오면 controller에서 view(home)으로 넘어간다 -> include스타일로 대체된 html로 넘어가게 되고 해당 html은 bootstrap의 css를 사용하게 된다.

//html문법상 home.html에 있는 replace에 의해 fragments/xxx가 yyy로 대체되어 화면을 구성하게 된다.
//해당 방법은 include를 계속해줘야한다. -> Hierarchical-style layouts 으로 하면 된다.

//implementation 'org.springframework.boot:spring-boot-devtools' -> restartedMain으로 잡히면 기본적으로 파일들에 대해서 캐싱을 하지 않는다 -> 변경하거나 recompile하면 반영이 돼서 변형된게 바로 확인할 수 있도록 DevTools를 사용한다.
//서버를 켠채로 반영이 가능하다는 뜻이다.