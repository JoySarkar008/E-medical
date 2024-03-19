package com.spring.bioMedical.service;

import java.util.List;
import com.spring.bioMedical.entity.Admin;
import com.spring.bioMedical.entity.User;

public interface AdminService {


	public List<Admin> findByRole(String role);

	public Admin findByEmail(String user);
	
	public List<Admin> findAll();

	public void save(Admin admin);
	
	Admin getDoctorById(Long id);
	Admin updateDoctor(Admin doctor);
	
}
