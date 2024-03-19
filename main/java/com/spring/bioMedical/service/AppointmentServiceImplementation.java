package com.spring.bioMedical.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.spring.bioMedical.dto.AppointmentRequest;
import com.spring.bioMedical.entity.Admin;
import com.spring.bioMedical.entity.Prescription;
import com.spring.bioMedical.entity.User;
import com.spring.bioMedical.repository.PrescriptionRepository;
import com.spring.bioMedical.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.spring.bioMedical.entity.Appointment;
import com.spring.bioMedical.repository.AppointmentRepository;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class AppointmentServiceImplementation {

	private AppointmentRepository appointmentRepository;
	private PrescriptionRepository prescriptionRepository;
	private EmailService emailService;
	private UserRepository userRepository;

	public AppointmentServiceImplementation(AppointmentRepository appointmentRepository,
			PrescriptionRepository prescriptionRepository, EmailService emailService, UserRepository userRepository) {
		super();
		this.appointmentRepository = appointmentRepository;
		this.prescriptionRepository = prescriptionRepository;
		this.emailService = emailService;
		this.userRepository = userRepository;
	}

	public Appointment save(Appointment app) {

		return appointmentRepository.save(app);
	}

	public List<Appointment> findAll() {
		return appointmentRepository.findAll();
	}

	public List<Appointment> findDoctorAppointments(Long id) {
		return appointmentRepository.findAllByDoctorId(id);
	}

	public void prescribe(Prescription newPrescription) {
		Appointment appointment = appointmentRepository.findById(newPrescription.getAppointmentId()).orElseThrow(null);
		if (null != appointment) {
			// User user = userRepository.findByEmail(newPrescription.getEmail());
			sendPrescription(newPrescription, appointment.getUser());
			prescriptionRepository.save(newPrescription);
			appointment.setPrescribed(true);
			appointmentRepository.save(appointment);
		}

	}

	private void sendPrescription(Prescription prescription, User user) {
		try {
			// Create PDF document with prescription text
			byte[] pdfBytes = createPdf(prescription.getPrescription());

			// Create a mail message with attachment
			MimeMessage message = emailService.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setTo(user.getEmail());
			helper.setSubject("Your prescription is ready");

			String fname = null != user.getFirstName() ? user.getFirstName() : "NULL";
			String lname = null != user.getLastName() ? user.getLastName() : "NULL";

			String emailText = "Dear " + fname + " " + lname + ",\n\n";
			emailText += "Your prescription is ready. Please find the details below:\n\n";
			helper.setText(emailText);

			// Attach the PDF
			helper.addAttachment("prescription-" + UUID.randomUUID() + ".pdf", new ByteArrayResource(pdfBytes));

			helper.setFrom("spring.email.auth@gmail.com");

			// Send the email
			emailService.sendEmail(message);
		} catch (MessagingException | IOException e) {
			e.printStackTrace(); // Handle exceptions appropriately
		}
	}

	private byte[] createPdf(String prescriptionText) throws IOException {

		prescriptionText = prescriptionText.replaceAll("\\p{Cntrl}", "");

		PDDocument document = new PDDocument();
		PDPage page = new PDPage();
		document.addPage(page);

		try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
			contentStream.beginText();
			contentStream.setFont(PDType1Font.HELVETICA, 12);
			contentStream.newLineAtOffset(20, 700);
			contentStream.showText(prescriptionText);
			contentStream.endText();
		}

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		document.save(byteArrayOutputStream);
		document.close();

		return byteArrayOutputStream.toByteArray();
	}

	public void sendEmailToOthersPatient(int id, Admin admin) {

		Appointment appointment = appointmentRepository.findById(id).orElseThrow(null);
		if (null != appointment) {
			appointment.setVisiting(true);
			appointment.setVisitedTime(LocalDateTime.now());
			appointmentRepository.save(appointment);
			List<Appointment> currentAppointments = appointmentRepository
					.findAllUnvisitedPatientsByDoctorIdAndDate(admin.getId(), appointment.getDate());
			currentAppointments.forEach(app -> notifyOtherPatients(app.getEmail(), appointment));
		}
	}

	@Async
	private void notifyOtherPatients(String email, Appointment appointment) {
		SimpleMailMessage notifyPatients = new SimpleMailMessage();
		notifyPatients.setTo(email);
		notifyPatients.setSubject("Currently Visiting Patient. Serial Number: " + appointment.getSerialNo());

		String emailText = "Dear " + email + ",\n\n";
		emailText += "Currently we are visiting patient with serial number " + appointment.getSerialNo();

		notifyPatients.setText(emailText);

		notifyPatients.setFrom("spring.email.auth@gmail.com");
		emailService.sendEmail(notifyPatients);
	}

//    @Async
	public void sendAppointmentSuccessFullEmail(AppointmentRequest obj, Appointment res) {
		SimpleMailMessage notifyPatients = new SimpleMailMessage();
		notifyPatients.setTo(obj.getEmail());
		notifyPatients.setSubject("Your appointment has been successfully created.");

		User doctor = userRepository.findById(Long.valueOf(obj.getDoctorId())).orElseThrow(null);

		String emailText = "Dear " + obj.getName() + ",\n\n";
		emailText += "Successfully created your appointment with Doctor: " + doctor.getFirstName() + " "
				+ doctor.getLastName() + " ";
		emailText += "Your serial number is: " + res.getSerialNo() + " on Time: " + res.getDate() + " " + res.getTime();

		notifyPatients.setText(emailText);

		notifyPatients.setFrom("spring.email.auth@gmail.com");
		emailService.sendEmail(notifyPatients);
	}
}
