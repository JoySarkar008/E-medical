package com.spring.bioMedical.Controller;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import com.spring.bioMedical.dto.AppointmentRequest;
import com.spring.bioMedical.entity.User;
import com.spring.bioMedical.repository.AppointmentRepository;
import com.spring.bioMedical.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.spring.bioMedical.entity.Admin;
import com.spring.bioMedical.entity.Appointment;
import com.spring.bioMedical.service.AdminServiceImplementation;
import com.spring.bioMedical.service.AppointmentServiceImplementation;

@Controller
@RequestMapping("/user")
public class UserController {

	private AppointmentServiceImplementation appointmentServiceImplementation;
	private AppointmentRepository appointmentRepository;
	private AdminServiceImplementation adminServiceImplementation;
	private UserRepository userRepository;

	public UserController(AppointmentServiceImplementation appointmentServiceImplementation,
			AppointmentRepository appointmentRepository, AdminServiceImplementation adminServiceImplementation,
			UserRepository userRepository) {
		super();
		this.appointmentServiceImplementation = appointmentServiceImplementation;
		this.appointmentRepository = appointmentRepository;
		this.adminServiceImplementation = adminServiceImplementation;
		this.userRepository = userRepository;
	}

	@GetMapping("/index")
	public String index(Model model) {

		// get last seen
		String username = "";
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
			String Pass = ((UserDetails) principal).getPassword();
			System.out.println("One + " + username + "   " + Pass);
		} else {
			username = principal.toString();
			System.out.println("Two + " + username);
		}

		Admin admin = adminServiceImplementation.findByEmail(username);
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date now = new Date();
		String log = now.toString();
		admin.setLastseen(log);
		adminServiceImplementation.save(admin);

		AppointmentRequest obj = new AppointmentRequest();
		obj.setName(admin.getFirstName() + " " + admin.getLastName());
		obj.setEmail(admin.getEmail());
		List<Admin> doctors = adminServiceImplementation.findByRole("ROLE_DOCTOR");

		model.addAttribute("doctors", doctors);
		model.addAttribute("app", obj);
		model.addAttribute("username", admin.getFirstName() + " " + admin.getLastName());
		return "user/index";
	}

	@GetMapping("/doctors/{id}")
	public String doctors(@PathVariable Long id, Model model) {

		// get last seen
		String username = "";
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
			String Pass = ((UserDetails) principal).getPassword();
			System.out.println("One + " + username + "   " + Pass);

		} else {
			username = principal.toString();
			System.out.println("Two + " + username);
		}

		Admin admin = adminServiceImplementation.findByEmail(username);
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date now = new Date();
		String log = now.toString();
		admin.setLastseen(log);
		adminServiceImplementation.save(admin);

		AppointmentRequest obj = new AppointmentRequest();
		obj.setName(admin.getFirstName() + " " + admin.getLastName());
		obj.setEmail(admin.getEmail());

		Admin doctor = adminServiceImplementation.findByRoleAndId("ROLE_DOCTOR", id);
		model.addAttribute("doctor", doctor);
		model.addAttribute("app", obj);

		return "user/doctor";
	}

	private Integer getSerialNumberForDate(String date) {
		// String formattedDate =
		// LocalDate.now().format(DateTimeFormatter.ofPattern("M/d/yyyy"));
		// System.out.println("formated date" + formattedDate);
		Integer highestSerialNumber = appointmentRepository.findMaxSerialNumberForDate(date);
		System.out.println("serial number" + highestSerialNumber);
		return (highestSerialNumber != null) ? highestSerialNumber + 1 : 1;
	}

	@PostMapping("/save-app")
	public String saveEmployee(@ModelAttribute("app") AppointmentRequest obj) {

		User user = userRepository.findByEmail(obj.getEmail());

		Appointment newAppointment = new Appointment();
		newAppointment.setName(obj.getName());
		newAppointment.setEmail(obj.getEmail());
		newAppointment.setDate(obj.getDate());
		newAppointment.setTime(obj.getTime());
		newAppointment.setUser(user);
		newAppointment.setSerialNo(getSerialNumberForDate(obj.getDate()));
		newAppointment.setDescription(obj.getDescription());
		newAppointment.setDoctorId(Long.valueOf(obj.getDoctorId()));

		Appointment res = appointmentServiceImplementation.save(newAppointment);

		appointmentServiceImplementation.sendAppointmentSuccessFullEmail(obj, res);
		// use a redirect to prevent duplicate submissions
		return "redirect:/user/index";
	}

	@GetMapping("/about")
	public String about(Model model) {

		// get last seen
		String username = "";
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
			String Pass = ((UserDetails) principal).getPassword();
			System.out.println("One + " + username + "   " + Pass);

		} else {
			username = principal.toString();
			System.out.println("Two + " + username);
		}

		Admin admin = adminServiceImplementation.findByEmail(username);

		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date now = new Date();

		String log = now.toString();

		admin.setLastseen(log);

		adminServiceImplementation.save(admin);

		Appointment obj = new Appointment();

		obj.setName(admin.getFirstName() + " " + admin.getLastName());

		obj.setEmail(admin.getEmail());

		System.out.println(obj);

		model.addAttribute("app", obj);

		return "user/about";
	}

	@PostMapping("/appointments/{id}")
	public String deleteAppointments(@PathVariable("id") String id) {
		appointmentRepository.deleteById(Integer.parseInt(id));
		return "redirect:/user/appointments";
	}

	@GetMapping("/appointments")
	public String appointments(Model model) {

		// get last seen
		String username = "";
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
			String Pass = ((UserDetails) principal).getPassword();
			System.out.println("One + " + username + "   " + Pass);

		} else {
			username = principal.toString();
			System.out.println("Two + " + username);
		}

		Admin admin = adminServiceImplementation.findByEmail(username);

		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date now = new Date();

		String log = now.toString();

		admin.setLastseen(log);

		adminServiceImplementation.save(admin);

		List<Appointment> app = appointmentRepository.findAllByUserId(admin.getId());

		model.addAttribute("app", app);

		return "user/appointments";
	}

	@GetMapping("/blog-single")
	public String bs(Model model) {

		// get last seen
		String username = "";
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
			String Pass = ((UserDetails) principal).getPassword();
			System.out.println("One + " + username + "   " + Pass);

		} else {
			username = principal.toString();
			System.out.println("Two + " + username);
		}

		Admin admin = adminServiceImplementation.findByEmail(username);

		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date now = new Date();

		String log = now.toString();

		admin.setLastseen(log);

		adminServiceImplementation.save(admin);

		Appointment obj = new Appointment();

		obj.setName(admin.getFirstName() + " " + admin.getLastName());

		obj.setEmail(admin.getEmail());

		System.out.println(obj);

		model.addAttribute("app", obj);

		return "user/blog-single";
	}

	@GetMapping("/blog")
	public String blog(Model model) {

		// get last seen
		String username = "";
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
			String Pass = ((UserDetails) principal).getPassword();
			System.out.println("One + " + username + "   " + Pass);

		} else {
			username = principal.toString();
			System.out.println("Two + " + username);
		}

		Admin admin = adminServiceImplementation.findByEmail(username);

		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date now = new Date();

		String log = now.toString();

		admin.setLastseen(log);

		adminServiceImplementation.save(admin);

		Appointment obj = new Appointment();

		obj.setName(admin.getFirstName() + " " + admin.getLastName());

		obj.setEmail(admin.getEmail());

		System.out.println(obj);

		model.addAttribute("app", obj);

		return "user/blog";
	}

	@GetMapping("/diseases")
	public String diseases(Model model) {

		// get last seen
		String username = "";
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
			String Pass = ((UserDetails) principal).getPassword();
			System.out.println("One + " + username + "   " + Pass);

		} else {
			username = principal.toString();
			System.out.println("Two + " + username);
		}

		Admin admin = adminServiceImplementation.findByEmail(username);

		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date now = new Date();

		String log = now.toString();

		admin.setLastseen(log);

		adminServiceImplementation.save(admin);

		Appointment obj = new Appointment();

		obj.setName(admin.getFirstName() + " " + admin.getLastName());

		obj.setEmail(admin.getEmail());

		System.out.println(obj);

		model.addAttribute("app", obj);

		return "user/diseases";
	}

	@GetMapping("/contact")
	public String contact(Model model) {

		// get last seen
		String username = "";
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
			String Pass = ((UserDetails) principal).getPassword();
			System.out.println("One + " + username + "   " + Pass);

		} else {
			username = principal.toString();
			System.out.println("Two + " + username);
		}

		Admin admin = adminServiceImplementation.findByEmail(username);

		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date now = new Date();

		String log = now.toString();

		admin.setLastseen(log);

		adminServiceImplementation.save(admin);

		Appointment obj = new Appointment();

		obj.setName(admin.getFirstName() + " " + admin.getLastName());

		obj.setEmail(admin.getEmail());

		System.out.println(obj);

		model.addAttribute("app", obj);

		return "user/contact";
	}

	@GetMapping("/department-single")
	public String d(Model model) {

		// get last seen
		String username = "";
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
			String Pass = ((UserDetails) principal).getPassword();
			System.out.println("One + " + username + "   " + Pass);

		} else {
			username = principal.toString();
			System.out.println("Two + " + username);
		}

		Admin admin = adminServiceImplementation.findByEmail(username);

		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date now = new Date();

		String log = now.toString();

		admin.setLastseen(log);

		adminServiceImplementation.save(admin);

		Appointment obj = new Appointment();

		obj.setName(admin.getFirstName() + " " + admin.getLastName());

		obj.setEmail(admin.getEmail());

		System.out.println(obj);

		model.addAttribute("app", obj);

		return "user/department-single";
	}

	@GetMapping("/departments")
	public String dep(Model model) {

		// get last seen
		String username = "";
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
			String Pass = ((UserDetails) principal).getPassword();
			System.out.println("One + " + username + "   " + Pass);

		} else {
			username = principal.toString();
			System.out.println("Two + " + username);
		}

		Admin admin = adminServiceImplementation.findByEmail(username);

		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date now = new Date();

		String log = now.toString();

		admin.setLastseen(log);

		adminServiceImplementation.save(admin);

		Appointment obj = new Appointment();

		obj.setName(admin.getFirstName() + " " + admin.getLastName());

		obj.setEmail(admin.getEmail());

		System.out.println(obj);

		model.addAttribute("app", obj);

		return "user/departments";
	}

	@GetMapping("/doctor")
	public String doctor(Model model) {

		// get last seen
		String username = "";
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
			String Pass = ((UserDetails) principal).getPassword();
			System.out.println("One + " + username + "   " + Pass);

		} else {
			username = principal.toString();
			System.out.println("Two + " + username);
		}

		Admin admin = adminServiceImplementation.findByEmail(username);

		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date now = new Date();

		String log = now.toString();

		admin.setLastseen(log);

		adminServiceImplementation.save(admin);

		Appointment obj = new Appointment();

		obj.setName(admin.getFirstName() + " " + admin.getLastName());

		obj.setEmail(admin.getEmail());

		System.out.println(obj);

		model.addAttribute("app", obj);

		return "user/doctor";
	}
}