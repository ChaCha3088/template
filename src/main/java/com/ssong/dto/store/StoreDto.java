package com.ssong.dto.store;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import com.ssong.domain.Address;
import com.ssong.dto.address.AddressDto;
import com.ssong.enumstorage.status.StoreStatus;

import java.util.List;

@Getter
public class StoreDto {

    private Long id;

    private Long memberId;

    private String name;

    private AddressDto addressDto;

    private String storeStatus;

    @Builder
    @QueryProjection
    public StoreDto(Long id, Long memberId, String name, Address address, StoreStatus storeStatus) {
        this.id = id;
        this.memberId = memberId;
        this.name = name;
        this.addressDto = AddressDto.builder()
                .city(address.getCity())
                .street(address.getStreet())
                .zipcode(address.getZipcode())
                .build();
        this.storeStatus = storeStatus.toString();
    }
}
