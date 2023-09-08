package com.ssong.config.auth.member;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ssong.config.auth.PrincipalUserDetails;
import com.ssong.domain.member.Member;
import com.ssong.enumstorage.errormessage.MemberErrorMessage;
import com.ssong.repository.member.MemberRepository;

@Service
@RequiredArgsConstructor
public class PrincipalUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    //이런 요청이 들어왔는데, 얘 혹시 회원이야?
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        //Member를 찾는다.
        Member member = memberRepository.findNotDeletedByEmail(email)
                //없으면, UsernameNotFoundException 발생
                .orElseThrow(() -> new UsernameNotFoundException(MemberErrorMessage.NO_SUCH_MEMBER_WITH_THAT_EMAIL.getMessage()));

        //로그인 시도 횟수 증가
        member.countUpLogInAttempt();

        //있으면, PrincipalUserDetails 생성
        return new PrincipalUserDetails(member);
    }
}
