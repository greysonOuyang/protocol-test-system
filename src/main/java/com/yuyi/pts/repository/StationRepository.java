package com.yuyi.pts.repository;

import com.yuyi.pts.entity.SelectionEntity;
import com.yuyi.pts.entity.StationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * description
 *
 * @author greyson
 * @since 2021/9/25
 */
@Repository
public interface StationRepository extends JpaRepository<StationEntity, String>, JpaSpecificationExecutor<StationEntity> {
    StationEntity findStationEntityByStationId(String stationId);
}
