package com.example.member.service;

import static com.example.delivery.model.entity.Delivery.toDeletedDeliveries;
import static com.example.member.exception.MemberErrorCode.DATABASE_CONSTRAINT_VIOLATION;
import static com.example.member.exception.MemberErrorCode.DUPLICATE_EMAIL;
import static com.example.member.exception.MemberErrorCode.DUPLICATE_NAME;
import static com.example.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;

import com.example.deleteddelivery.event.DeletedDeliveriesSavedEvent;
import com.example.delivery.event.DeliveriesBulkDeletedEvent;
import com.example.delivery.model.entity.Delivery;
import com.example.delivery.repository.DeliveryRepository;
import com.example.global.jwt.CustomUserDetails;
import com.example.global.jwt.JwtToken;
import com.example.global.jwt.JwtTokenProvider;
import com.example.member.exception.MemberException;
import com.example.member.model.dto.request.SignInRequest;
import com.example.member.model.dto.request.SignUpRequest;
import com.example.member.model.dto.response.MemberSaveResponse;
import com.example.member.model.entity.Member;
import com.example.member.repository.MemberRepository;
import com.example.wallet.event.WalletDeletedEvent;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final DeliveryRepository deliveryRepository;
    private final AuthenticationManager authenticationManager;
    private final ApplicationEventPublisher eventPublisher;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public JwtToken signIn(SignInRequest signInRequest) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(signInRequest.getMemberName(), signInRequest.getPassword());

        // 2. 실제 검증. authenticate() 메서드를 통해 요청된 Member 에 대한 검증 진행
        // authenticate 메서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드 실행
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(authenticationToken);
        } catch (InternalAuthenticationServiceException e) {
            throw new RuntimeException(e.getMessage());
        }
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = customUserDetails.getId();
        return jwtTokenProvider.generateToken(authentication, userId);
    }

    @Transactional
    public MemberSaveResponse signUp(SignUpRequest signUpRequest) {
        validateDuplicateEmail(signUpRequest.getEmail());
        validateDuplicateMemberName(signUpRequest.getMemberName());
        
        final Member member = signUpRequest.toMember();
        member.updatePassword(passwordEncoder.encode(signUpRequest.getPassword())); //동시성?
        member.assignUserRole();

        Member savedMember;
        try {
            savedMember = memberRepository.save(member);
        } catch (DataIntegrityViolationException e) {
            throw new MemberException(DATABASE_CONSTRAINT_VIOLATION);
        }

        return MemberSaveResponse.fromMember(savedMember);
    }

    @Transactional
    public void deleteMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> MemberException.fromErrorCode(MEMBER_NOT_FOUND));

        List<Delivery> deliveries = deliveryRepository.findDeliveriesByMemberId(memberId);

        eventPublisher.publishEvent(new DeliveriesBulkDeletedEvent(deliveries)); // 벌크 연산 먼저 실행
        eventPublisher.publishEvent(new WalletDeletedEvent(member.getId()));
        eventPublisher.publishEvent(new DeletedDeliveriesSavedEvent(toDeletedDeliveries(deliveries)));

        memberRepository.delete(member);
    }

    private void validateDuplicateMemberName(String memberName) {
        final boolean isDuplicate = memberRepository.findByMemberName(memberName).isPresent();
        if (isDuplicate) {
            throw MemberException.fromErrorCode(DUPLICATE_NAME);
        }
    }

    private void validateDuplicateEmail(String email) {
        final boolean isDuplicate = memberRepository.findByEmail(email).isPresent();
        if (isDuplicate) {
            throw MemberException.fromErrorCode(DUPLICATE_EMAIL);
        }
    }
}
