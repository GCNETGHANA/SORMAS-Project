package de.symeda.sormas.app.backend.caze;

import de.symeda.sormas.api.caze.PreviousHospitalizationDto;
import de.symeda.sormas.app.backend.common.AdoDtoHelper;
import de.symeda.sormas.app.backend.common.DatabaseHelper;
import de.symeda.sormas.app.backend.facility.Facility;
import de.symeda.sormas.app.backend.facility.FacilityDtoHelper;

/**
 * Created by Mate Strysewske on 22.02.2017.
 */

public class PreviousHospitalizationDtoHelper extends AdoDtoHelper<PreviousHospitalization, PreviousHospitalizationDto> {

    private HospitalizationDtoHelper hospitalizationDtoHelper;

    public  PreviousHospitalizationDtoHelper() {
        hospitalizationDtoHelper = new HospitalizationDtoHelper();
    }

    @Override
    public PreviousHospitalization create() {
        return new PreviousHospitalization();
    }

    @Override
    public PreviousHospitalizationDto createDto() {
        return new PreviousHospitalizationDto();
    }

    @Override
    public void fillInnerFromDto(PreviousHospitalization a, PreviousHospitalizationDto b) {
        if (b.getHospitalization() != null) {
            a.setHospitalization(DatabaseHelper.getHospitalizationDao().queryUuid(b.getHospitalization().getUuid()));
        } else {
            a.setHospitalization(null);
        }
        if (b.getHealthFacility() != null) {
            a.setHealthFacility(DatabaseHelper.getFacilityDao().queryUuid(b.getHealthFacility().getUuid()));
        } else {
            a.setHealthFacility(null);
        }

        a.setIsolated(b.getIsolated());
        a.setAdmissionDate(b.getAdmissionDate());
        a.setDischargeDate(b.getDischargeDate());
        a.setDescription(b.getDescription());
    }

    @Override
    public void fillInnerFromAdo(PreviousHospitalizationDto a, PreviousHospitalization b) {
        if (b.getHospitalization() != null) {
            Hospitalization hospitalization = DatabaseHelper.getHospitalizationDao().queryForId(b.getHospitalization().getId());
            a.setHospitalization(hospitalizationDtoHelper.adoToDto(hospitalization));
        } else {
            a.setHospitalization(null);
        }
        if (b.getHealthFacility() != null) {
            Facility facility = DatabaseHelper.getFacilityDao().queryForId(b.getHealthFacility().getId());
            a.setHealthFacility(FacilityDtoHelper.toReferenceDto(facility));
        } else {
            a.setHealthFacility(null);
        }

        a.setIsolated(b.getIsolated());
        a.setAdmissionDate(b.getAdmissionDate());
        a.setDischargeDate(b.getDischargeDate());
        a.setDescription(b.getDescription());
    }

}
