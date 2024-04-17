package jpabook.jpashop.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    /**
     * 회원 등록 V1 : 요청 값을 Member 엔티티로 받는다.
     * 문제점
     *  - 엔티티에 프레젠테이션 계층(컨트롤러 단)을 위한 로직이 추가된다
     *      - 즉, 엔티티에 API 검증(Validation)을 위한 로직이 추가된다(@NotEmpty 등)
     *      - 실무에서는 회원 엔티티를 위한 다양한 API가 필요한데,
     *        한 엔티티에 API 각각을 위한 요청/요구사항을 담기는 어렵다
     *  - 엔티티가 변경되면 API 스펙이 변해야 한다
     * 결론
     *  - API 요청 스펙에 맞춘 별도의 DTO를 이용하여 파라미터로 값을 받아야 한다
     */
    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    /**
     * 회원 등록 V2 : 요청 값을 Member 엔티티 대신 별도의 DTO로 받는다.
     * DTO로 받을 때 장점
     *  - 엔티티와 프레젠테이션 계층을 위한 로직을 분리할 수 있다
     *  - 엔티티와 API 스펙을 명확하게 분리할 수 있다
     *  - 엔티티가 변해도 API 스펙이 변하지 않는다
     *  - 요청값으로 어떤 값이 들어왔는지 DTO를 확인함으로써 명확하게 알 수 있다
     *      - HOW?? DTO로 어떤 값을 받고 있는지 확인하면 된다
     * [참고] 실무에서는 엔티티를 API 스펙에 노출하면 안 된다!!!
     */
    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid CreateMemberRequest request) {
        Member member = new Member();
        member.setName(request.getName());

        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateMemberRequest request) {

        memberService.update(id, request.getName());
        Member findMember = memberService.findOne(id);
        return new UpdateMemberResponse(findMember.getId(), findMember.getName());
    }

    @Data
    static class UpdateMemberRequest {
        private String name;
    }

    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse {
        private Long id;
        private String name;

    }

    @Data
    static class CreateMemberRequest {
        @NotEmpty
        private String name;
    }

    @Data
    static class CreateMemberResponse {
        private Long id;

        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }
}
