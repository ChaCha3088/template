package com.ssong.service.store;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ssong.domain.member.Member;
import com.ssong.domain.Store;
import com.ssong.dto.store.*;
import com.ssong.enumstorage.errormessage.MemberErrorMessage;
import com.ssong.enumstorage.errormessage.StoreErrorMessage;
import com.ssong.enumstorage.status.StoreStatus;
import com.ssong.exception.member.NoSuchMemberException;
import com.ssong.exception.store.NoSuchStoreException;
import com.ssong.repository.member.MemberRepository;
import com.ssong.repository.store.StoreRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StoreService {
    private final MemberRepository memberRepository;
    private final StoreRepository storeRepository;

    public StoreDto findStoreDtoById(Long id) throws NoSuchStoreException {
        return storeRepository.findById(id)
                //가게가 없으면 예외 발생
                .orElseThrow(() -> new NoSuchStoreException(StoreErrorMessage.NO_SUCH_STORE.getMessage()))
                //Dto로 변환
                .toStoreDto();
    }

    public List<StoreDto> findAllStoreDtoById(Long memberId) {
        //회원 id를 가지고 있는 가게를 찾아야지.
        return storeRepository.findAllStoreByMemberId(memberId).stream()
                //Dto로 변환
                .map(m -> m.toStoreDto())
                .collect(Collectors.toList());
    }

    @Transactional
    public Long createStore(StoreCreationDto storeCreationDto) throws NoSuchMemberException {
        //가게 생성
        Member member = findMemberById(storeCreationDto.getMemberId());

        Store createdStore = Store.builder()
                .member(member)
                .name(storeCreationDto.getName())
                .city(storeCreationDto.getCity())
                .street(storeCreationDto.getStreet())
                .zipcode(storeCreationDto.getZipcode())
                .build();

        //가게 저장
        Store savedStore = storeRepository.save(createdStore);

        //회원의 정보 저장
        memberRepository.save(member);

        return savedStore.getId();
    }

    /**
     * 가게 이름과 주소를 수정
     * 둘 중에 하나만 수정해도 됨
     * @param storeUpdateDto
     * @return
     */
    @Transactional
    public Long updateStore(StoreUpdateDto storeUpdateDto) {
        //업데이트 하려는 가게가 회원의 가게인지 검증
        Store findStore = validateStoreIsMembersStore(storeUpdateDto.getId(), storeUpdateDto.getMemberId());

        //가게 정보 업데이트
        findStore.updateStoreName(storeUpdateDto.getName());
        findStore.updateStoreAddress(storeUpdateDto.getCity(), storeUpdateDto.getStreet(), storeUpdateDto.getZipcode());

        //저장
        Store savedStore = storeRepository.save(findStore);

        return savedStore.getId();
    }

    /**
     * 가게 열기 / 닫기 토글
     */
    @Transactional
    public Long toggleStoreStatus(StoreToggleStatusDto storeToggleStatusDto) {
        //업데이트 하려는 가게가 회원의 가게인지 검증
        Store findStore = validateStoreIsMembersStore(storeToggleStatusDto.getId(), storeToggleStatusDto.getMemberId());

        //가게 상태 변경
        if (findStore.getStoreStatus() == StoreStatus.OPEN) {
            findStore.changeStoreStatus(StoreStatus.CLOSE);
        } else if (findStore.getStoreStatus() == StoreStatus.CLOSE) {
            findStore.changeStoreStatus(StoreStatus.OPEN);
        }

        //저장
        return storeRepository.save(findStore).getId();
    }

    private Member findMemberById(Long memberId) throws NoSuchMemberException {
        return memberRepository.findNotDeletedById(memberId)
                //해당하는 Id를 가진 회원이 없으면, 예외 발생
                .orElseThrow(() -> new NoSuchMemberException(MemberErrorMessage.NO_SUCH_MEMBER.getMessage()));
    }

    //업데이트 하려는 가게가 회원의 가게인지 검증
    private Store validateStoreIsMembersStore(Long storeId, Long memberId) {
        return storeRepository.findStoreByIdAndMemberId(storeId, memberId)
                //storeId와 memberId에 맞는 가게가 없으면, 예외 발생
                .orElseThrow(() -> new NoSuchStoreException(StoreErrorMessage.IS_NOT_MEMBERS_STORE.getMessage()));
    }
}
