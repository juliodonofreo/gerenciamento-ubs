package com.ubs.ubs.dtos;

import com.ubs.ubs.entities.Doctor;
import com.ubs.ubs.entities.enums.Specialization;

public class DoctorInsertDTO extends UserInsertDTO {

        private Specialization specialization;


        public DoctorInsertDTO() {
        }

        public DoctorInsertDTO(Long id, String name, String email, String password, Specialization specialization) {
            super(id, name, email, password);
            this.specialization = specialization;
        }

        public DoctorInsertDTO(Doctor entity) {
            super(entity.getId(), entity.getName(), entity.getEmail(), entity.getPassword());
            this.specialization = entity.getSpecialization();
        }

        public Specialization getSpecialization() {
            return specialization;
        }

        public void setSpecialization(Specialization specialization) {
            this.specialization = specialization;
        }
}
