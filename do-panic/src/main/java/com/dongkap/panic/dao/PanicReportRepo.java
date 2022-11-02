package com.dongkap.panic.dao;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dongkap.panic.entity.PanicReportEntity;
import com.dongkap.panic.entity.PanicStatisticAreaEntity;
import com.dongkap.panic.entity.PanicStatisticGenderEntity;

public interface PanicReportRepo extends JpaRepository<PanicReportEntity, String>, JpaSpecificationExecutor<PanicReportEntity> {
	
	@Query("SELECT pr FROM PanicReportEntity pr JOIN FETCH pr.panicDetails pd JOIN FETCH pd.location l JOIN FETCH pd.device d WHERE pr.id = :id AND LOWER(pr.username) = :username")
	PanicReportEntity loadPanicReportByIdUsername(@Param("id") String id, @Param("username") String username);
	
	@Query("SELECT pr FROM PanicReportEntity pr JOIN FETCH pr.panicDetails pd JOIN FETCH pd.location l JOIN FETCH pd.device d WHERE pr.panicCode = :code AND LOWER(pr.username) = :username")
	PanicReportEntity loadPanicReportByCodeUsername(@Param("code") String code, @Param("username") String username);
	
	PanicReportEntity findByPanicCode(String panicCode);
	
	List<PanicReportEntity> findByActiveAndStatusNull(boolean active);
	
	List<PanicReportEntity> findByActiveAndStatusNullAndAdministrativeAreaShort(boolean active, String administrativeAreaShort);

	@Query("SELECT pr.emergencyCategory as emergency, COUNT(pr) as total FROM PanicReportEntity pr WHERE pr.year = :year GROUP BY pr.emergencyCategory ORDER BY pr.emergencyCategory ASC")
	List<Map<String, Object>> loadDataGroupByEmergency(@Param("year") Integer year);

	@Query("SELECT pr.latestProvince as area, COUNT(pr) as total FROM PanicReportEntity pr WHERE pr.year = :year AND pr.emergencyCategory IS NOT NULL GROUP BY pr.latestProvince ORDER BY pr.latestProvince ASC")
	List<Map<String, Object>> loadDataGroupByProvince(@Param("year") Integer year);

	@Query("SELECT new com.dongkap.panic.entity.PanicStatisticAreaEntity(pr.latestProvince as area, pr.emergencyCategory as emergency, COUNT(pr.emergencyCategory) as total) FROM PanicReportEntity pr WHERE pr.year = :year AND pr.emergencyCategory IS NOT NULL GROUP BY pr.emergencyCategory, pr.latestProvince ORDER BY pr.latestProvince ASC")
	List<PanicStatisticAreaEntity> loadDataGroupProvinceByEmergencyCategory(@Param("year") Integer year);

	@Query("SELECT pr.gender as gender, COUNT(pr) as total FROM PanicReportEntity pr WHERE pr.year = :year AND pr.emergencyCategory IS NOT NULL GROUP BY pr.gender ORDER BY pr.gender ASC")
	List<Map<String, Object>> loadDataGroupByGender(@Param("year") Integer year);

	@Query("SELECT new com.dongkap.panic.entity.PanicStatisticGenderEntity(pr.gender as gender, pr.emergencyCategory as emergency, COUNT(pr.emergencyCategory) as total) FROM PanicReportEntity pr WHERE pr.year = :year AND pr.gender = :gender AND pr.emergencyCategory IS NOT NULL GROUP BY pr.emergencyCategory, pr.gender ORDER BY pr.gender ASC")
	List<PanicStatisticGenderEntity> loadDataGroupGenderByEmergencyCategory(@Param("year") Integer year, @Param("gender") String gender);

}