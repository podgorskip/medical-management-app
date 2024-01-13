package app.registrationSystem.security;

import java.util.Set;
import static app.registrationSystem.security.Privilege.*;

public enum Role {
    ADMIN(Set.of(ADD_ADMIN, ADD_NURSE, ADD_DOCTOR, REMOVE_ADMIN, REMOVE_NURSE, REMOVE_DOCTOR, REMOVE_PATIENT,
            CHECK_DOCTORS, CHECK_SPECIALIZATIONS)),
    DOCTOR(Set.of(CHANGE_CREDENTIALS, CHECK_DOCTORS, CHECK_SPECIALIZATIONS, ADD_VISIT, CHECK_AVAILABLE_VISITS, CHECK_SCHEDULED_VISITS)),
    NURSE(Set.of(CHANGE_CREDENTIALS)),
    PATIENT(Set.of(CHANGE_CREDENTIALS, ADD_ILLNESS, CHECK_DOCTORS, CHECK_SPECIALIZATIONS, CHECK_AVAILABLE_VISITS,
            SCHEDULE_VISIT, CHECK_SCHEDULED_VISITS, CHECK_ASSIGNED_ILLNESSES));

    final Set<Privilege> privileges;
    Role(Set<Privilege> privileges) {
        this.privileges = privileges;
    }

    Set<Privilege> getPrivileges() {
        return privileges;
    }
}
