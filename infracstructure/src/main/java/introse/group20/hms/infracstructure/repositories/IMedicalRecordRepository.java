package introse.group20.hms.infracstructure.repositories;

import introse.group20.hms.infracstructure.models.MedicalRecordModel;
import introse.group20.hms.infracstructure.models.enums.StayType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface IMedicalRecordRepository extends JpaRepository<MedicalRecordModel, UUID> {
    List<MedicalRecordModel> findByStayType(StayType stayType);
}
