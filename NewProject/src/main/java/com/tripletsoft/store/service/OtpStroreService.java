package com.tripletsoft.store.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import com.tripletsoft.store.model.OtpStore;

@Service
public class OtpStroreService {

	@Autowired
	private MongoTemplate mongoTemplate;
	
	
	public OtpStore getValuesOtp(String email) {
		Query query =new Query();
		query.addCriteria(Criteria.where("emailID").is(email));
		
		return mongoTemplate.findOne(query, OtpStore.class);
		
		
	}
}
