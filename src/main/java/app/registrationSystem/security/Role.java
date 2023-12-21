package app.registrationSystem.security;

import java.util.Set;
import static app.registrationSystem.security.Privilege.*;

public enum Role {
    ADMIN(Set.of(ADD_ADMIN, ADD_NURSE, ADD_DOCTOR, REMOVE_ADMIN, REMOVE_NURSE, REMOVE_DOCTOR, REMOVE_PATIENT,
            CHECK_DOCTORS)),
    DOCTOR(Set.of(CHANGE_CREDENTIALS, CHECK_DOCTORS)),
    NURSE(Set.of(CHANGE_CREDENTIALS)),
    PATIENT(Set.of(CHANGE_CREDENTIALS, ADD_ILLNESS, CHECK_DOCTORS));

    final Set<Privilege> privileges;
    Role(Set<Privilege> privileges) {
        this.privileges = privileges;
    }

    Set<Privilege> getPrivileges() {
        return privileges;
    }
}
