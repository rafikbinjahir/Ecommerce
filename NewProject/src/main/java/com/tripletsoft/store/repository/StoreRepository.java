package com.tripletsoft.store.repository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.tripletsoft.store.model.Store;
public interface StoreRepository extends MongoRepository<Store, String>{
	
	@Query("{'emailID':?0}")
	Store getEmailid(String emailId);
}
