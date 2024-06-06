package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
// (1) @AllArgsConstructor //lombok -> 필드의 memberRepository를 보고 생성자를 자동으로 만들어 준다
@RequiredArgsConstructor //final이 있는 필드만 생성자를 만들어 준다
//jpa의 모든 데이터 변경, logic들은 가급적 transcation 안에서 실행되어야한다.
//class level에서 transcation을 걸어주면 public method에도 걸린다
//
public class MemberService {
//    @Autowired
//    //field injection -> test시에 변경에 불리하다
//    private MemberRepository memberRepository;

//    @Autowired
//    public void setMemberRepository(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    } Setter injection -> setMethod로 들어와서 injection 해준다.

    private final MemberRepository memberRepository; //값을 변경하지 않을 것이므로 final 사 -> 생성자에서 값을 세팅하지 않으면 오류가 발생하여 확인할 수 있다.
    //생성자가 하나만있는 경우에 spring이 자동으로 생성자 주입을 한다(Autowired 생략 가능)
//    public MemberService(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//  } //생성자 주입 -> 생성시에 값이 정해지기때문에 중간에 값을 변경할 수 없다.


    //--회원가입--//
    @Transactional
    public Long join(Member member){
        validateDuplicateMember(member); //중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        //EXCEPTION
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    //--회원 전체 조회--//\
    @Transactional(readOnly = true)
    //readOnly = true를 하면 조회(읽기)하는 곳에서는 성능을 최적화할 수 있다.
    //조회가 아닌 변경(쓰기) 사용시에는 true 설정을 하면 안된다.(default = false)

    public List<Member> findMembers(){
        return memberRepository.findAll();
    }
    @Transactional(readOnly = true)
    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);
    }

}
