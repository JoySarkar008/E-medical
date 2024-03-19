package com.spring.bioMedical.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.bioMedical.entity.Admin;
import com.spring.bioMedical.repository.AdminRepository;


@Service
public class AdminServiceImplementation implements AdminService {

	private AdminRepository adminRepository;

	@Autowired   
	public AdminServiceImplementation( AdminRepository obj)
	{
		adminRepository=obj;
	}
	
	@Override
	public List<Admin> findAll() {
		return adminRepository.findAll();
	}

	@Override
	public void save(Admin admin)
	{
		
		adminRepository.save(admin);
	}

	@Override
	public Admin findByEmail(String user) {
		
		return adminRepository.findByEmail(user);
		
	}

	@Override
	public List<Admin> findByRole(String user) {
		
		return adminRepository.findByRole(user);
	}

	@Override
	public Admin getDoctorById(Long id) {
		return adminRepository.findById(id).get();
		
		
	}

	@Override
	public Admin updateDoctor(Admin doctor) {
		return adminRepository.save(doctor);
	}


	public Admin findByRoleAndId(String roleDoctor, Long id) {
		return adminRepository.findByRoleAndId(roleDoctor, id);
	}
}
