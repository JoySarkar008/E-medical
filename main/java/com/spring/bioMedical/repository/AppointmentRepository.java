package com.spring.bioMedical.repository;

import java.time.LocalDate;
import java.util.List;

import com.spring.bioMedical.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.spring.bioMedical.entity.Appointment;
import com.spring.bioMedical.entity.User;
import org.springframework.web.bind.annotation.RequestParam;


@Repository("appointmentRepository")
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {


    @Query(value = "SELECT * FROM app WHERE doctor_id=:id and STR_TO_DATE(date, '%m/%d/%Y') = CURDATE()", nativeQuery = true)
    List<Appointment> findAllByDoctorId(Long id);

    @Query(value = "SELECT * FROM app WHERE doctor_id=:id and is_visiting=false", nativeQuery = true)
    List<Appointment> findAllUnvisitedPatientsByDoctorId(@Param("id") Long id);

    Appointment findByEmail(String email);

    @Query("SELECT MAX(a.serialNo) FROM Appointment a WHERE TRIM(a.date) = TRIM(:date)")
    Integer findMaxSerialNumberForDate(@Param("date") String date);

    @Query(value = "SELECT * FROM app WHERE doctor_id=:id and is_visiting=false and date=:date", nativeQuery = true)
    List<Appointment> findAllUnvisitedPatientsByDoctorIdAndDate(@Param("id")Long id, @Param("date")String date);

    List<Appointment> findAllByUserId(Long id);
}