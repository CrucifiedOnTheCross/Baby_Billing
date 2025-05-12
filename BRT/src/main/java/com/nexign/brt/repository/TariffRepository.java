package com.nexign.brt.repository;

import com.nexign.brt.entity.TariffEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TariffRepository extends JpaRepository<TariffEntity, Integer> {

    TariffEntity findTariffById(Integer id);

}
