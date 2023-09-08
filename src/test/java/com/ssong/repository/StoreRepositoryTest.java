package com.ssong.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import com.ssong.domain.Store;
import com.ssong.dto.member.MemberCreationDto;
import com.ssong.dto.store.StoreCreationDto;
import com.ssong.repository.member.MemberRepository;
import com.ssong.repository.store.StoreRepository;
import com.ssong.service.member.MemberService;
import com.ssong.service.store.StoreService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class StoreRepositoryTest {

    @Autowired
    StoreRepository storeRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    private MemberService memberService;
    @Autowired
    private StoreService storeService;

    @PersistenceContext
    EntityManager em;

    private Long memberTestId;
    private Long storeTestId;

    @BeforeAll
    void setUp() {
        Long newMemberId = memberService.joinMember(MemberCreationDto.builder()
                .name("memberTest")
                .email("memberTest@gmail.com")
                .password("password")
                .city("city")
                .street("street")
                .zipcode("zipcode")
                .build());

        Long newStoreId = storeService.createStore(StoreCreationDto.builder()
                .memberId(newMemberId)
                .name("storeTest")
                .city("city")
                .street("street")
                .zipcode("zipcode")
                .build());

        memberTestId = newMemberId;
        storeTestId = newStoreId;
    }

    @Test
    void findStoreById() {
        //조회
        Optional<Store> findStore = storeRepository.findById(storeTestId);
        assertThat(findStore.get().getName()).isEqualTo("storeTest");
    }

    @Test
    void findStoreByName() {
        //조회
        Optional<Store> findStore = storeRepository.findStoreByName("storeTest");

        //검증
        assertThat(findStore.get().getName()).isEqualTo("storeTest");
    }
}