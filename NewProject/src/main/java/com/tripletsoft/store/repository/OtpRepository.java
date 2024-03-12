package com.tripletsoft.store.repository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.tripletsoft.store.model.OtpStore;
public interface OtpRepository extends MongoRepository<OtpStore ,String>{
	@Query("{'emailID':?0}")
	OtpStore getEmailid(String emailId);

}
