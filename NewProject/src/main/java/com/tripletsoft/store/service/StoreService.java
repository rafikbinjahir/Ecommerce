package com.tripletsoft.store.service;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

//import com.example.user.model.User;
import com.tripletsoft.store.model.Store;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class StoreService {

	@Autowired
	private JavaMailSender javaMailSender;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	
	public void sendOtpByEmail(String email,String otp) throws MessagingException {
		
		
		System.out.println("email--->"+email);
		System.out.println("otp--->"+otp);
			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setTo(email);
			helper.setSubject("Your OTP for verification");
			helper.setText("Your administrator has just requested that you update your account , the verification code is "  +  otp  +  ". The code will be expired in 24 hours.",true);
		//	System.out.println("otp sucess");
			javaMailSender.send(message);
		}
		
	public String generateOtp() {
		Random random = new Random();
		//System.out.println("OTP"+String.format("%06d",random.nextInt(1000000)));
		return String.format("%06d",random.nextInt(1000000));
	}
	
	
	public Store getValues(String email) {
		Query query =new Query();
		query.addCriteria(Criteria.where("emailID").is(email));
		
		return mongoTemplate.findOne(query, Store.class);
		
		
	}
	public Page<Store> getStore(Pageable pageable,Store store)
	{
		
		Query query = new Query();
		if(store.getStorename()!=null) {
			query.addCriteria(Criteria.where("name").is(store.getStorename()));
		}
		if(store.getEmailID()!=null) {
			query.addCriteria(Criteria.where("email").is(store.getEmailID()));
		}
		if(store.getCurrency()!=null) {
			query.addCriteria(Criteria.where("currency").is(store.getCurrency()));
		}
		if(store.getCreatedDate()!=null) {
			query.addCriteria(Criteria.where("created date").is(store.getCreatedDate()));
		}		
		if(store.getUpdateDate()!=null) {
			query.addCriteria(Criteria.where("updated date").is(store.getUpdateDate()));
		}
		if (store.isActive() != false) {
		    query.addCriteria(Criteria.where("isActive").is(store.isActive()));
		}
		if (store.isDeleted() !=true) {
			query.addCriteria(Criteria.where("isDeleted").is(store.isDeleted()));
		}

	
		long totalRecord=mongoTemplate.count(query, Store.class);
		query.with(pageable);
		List<Store> stores=mongoTemplate.find(query, Store.class);
	
		
	
		return new PageImpl<>(stores,pageable, totalRecord);
		
	}
	
	public boolean emailExists(String email) {
        Query query = new Query();
        query.addCriteria(Criteria.where("emailID").is(email));
        System.out.println("cc"+query);
        return mongoTemplate.exists(query, Store.class);
        
    }


	
}
