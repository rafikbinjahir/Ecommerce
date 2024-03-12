package com.tripletsoft.store.controller;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tripletsoft.store.model.Store;
import com.tripletsoft.store.model.OtpStore;
import com.tripletsoft.store.service.StoreService;
import com.tripletsoft.store.service.OtpStroreService;

import jakarta.mail.MessagingException;

import com.tripletsoft.store.repository.StoreRepository;
import com.tripletsoft.store.repository.OtpRepository;
@RestController
@RequestMapping(path="/store")
public class StoreController {
	
	@Autowired
	private StoreRepository storeRepository;
	@Autowired
	private StoreService storeService;
	@Autowired
	private OtpStroreService otpStroreService;
	@Autowired
	private OtpRepository otpRepository;
	
	
	
	@PostMapping(path="/create",consumes="application/json")
	public ResponseEntity<String> createStore(@RequestBody Store store) throws MessagingException {
		if (storeService.emailExists(store.getEmailID())) {
            return new ResponseEntity<>("Email already exists.", HttpStatus.OK);
            
        }
		
		UUID uuid=UUID.randomUUID();
		store.setStoreId(uuid.toString());
		String email =store.getEmailID();
		UUID uuidSalt=UUID.randomUUID();
		store.setSaltKey(uuidSalt.toString());
		Date date = new Date();
		store.setCreatedDate(date);
		store.setActive(true);
		
		String otp=storeService.generateOtp();
		OtpStore saveOtp = new OtpStore();
		
		storeService.sendOtpByEmail(email,otp );
		saveOtp.setOtp(otp);
		saveOtp.setEmailID(email);
		saveOtp.setExpiryDate(date);
		System.out.println("OtpController-->"+otp);
		storeRepository.insert(store);
		otpRepository.insert(saveOtp);
		return ResponseEntity.status(HttpStatus.CREATED).body("created successfully");
		
		
	}
	
	
	@PostMapping(path="/setpassword",consumes="application/json")
	public String setPassword(@RequestBody Store store) {
		Store storeDetails =storeService.getValues(store.getEmailID());
		OtpStore getValues =otpStroreService.getValuesOtp(store.getEmailID());
		
		if(getValues==null) {
			return "User not found with the provided email.";
		}
		
		if(store.getOtp().equals(getValues.getOtp())) {
			String salt=storeDetails.getSaltKey();
			String password =store.getPassword();
			System.out.println("store"+store.getOtp());
			System.out.println("getValues"+getValues.getOtp());
			
			//Salt+Password
			String savePassword=salt+password;
			
			
			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			String storePassword =passwordEncoder.encode(savePassword);
			storeDetails.setPassword(storePassword);
			getValues.setOtp(null);
			
			storeRepository.save(storeDetails);
			otpRepository.save(getValues);
			
			return "Password set successfully.";
		}
		else {
			return "Invalid OTP or email.";
		}
	}
	
	
	
	@PostMapping(path="/login",consumes="application/json")
	public String login(@RequestBody Store storeLogin) throws Exception {
		String emailId = storeLogin.getEmailID();
		String password = storeLogin.getPassword();
		
		Store storeDetails = storeRepository.getEmailid(emailId);
		
		if(storeDetails!=null) {
		
		String salt = storeDetails.getSaltKey();
		System.out.println("salt"+storeDetails.getSaltKey());
		
        String saltKey = salt+password;
		
        
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if(passwordEncoder.matches(saltKey, storeDetails.getPassword())) {
        	return ("login successful");
        	}
        else
        {
        	return "Invalid email or password.";
        }
		}
		else {
			return "Invalid email or password.";
		}
	}
	

	@PutMapping(path = "/edit/{StoreId}", consumes = "application/json")
	public String editValues(@PathVariable(name = "StoreId") String StoreId, @RequestBody Store store) {
	    System.out.println("Received StoreId>>>> " + StoreId);

	    Optional<Store> editStore = storeRepository.findById(StoreId);
	    

	    if (editStore.isPresent()) {
	    	
	        Store editValues = editStore.get();
	        System.out.println("values>>>"+editValues.getId());
	        editValues.setStorename(store.getStorename());
	        editValues.setCurrency(store.getCurrency());
	        editValues.setPhonenumber(store.getPhonenumber());
	        editValues.setEmailID(store.getEmailID());
	        editValues.setUsername(store.getUsername());
	     
	        storeRepository.save(editValues);

	        System.out.println("Store with ID " + StoreId + " successfully updated.");
	        return "Store with ID " + StoreId + " successfully updated.";
	    } else {
	        System.out.println("Store with ID " + StoreId + " not found.");
	        
	        return "Store with ID " + StoreId + " not found.";
	    }
	}


	

	
	@DeleteMapping(path = "/delete/{StoreId}", consumes = "application/json")
	public String deleteStore(@PathVariable(name = "StoreId") String StoreId, @RequestBody Store store) {
	    Optional<Store> deleteStore = storeRepository.findById(StoreId);

	    if (deleteStore.isPresent()) {
	        Store deleteValues = deleteStore.get();
	    	deleteValues.setDeleted(store.isDeleted());
	    	//deleteValues.setActive(store.isActive());
	        storeRepository.save(deleteValues);
	        return "Deleted Successfully";
	    } else {
	        return "Store not found"; 
	    }
	}

	
	
	
	@PostMapping(path="/get",consumes="application/json")
	public ResponseEntity <List<Store>> getAllStore(@RequestParam(name = "perPage",required=false,defaultValue="5")int perPage, 
			@RequestParam(name = "page",required=false,defaultValue="0")int page,@RequestBody Store store){
		 System.out.println("Received perPage: " + perPage);
		    System.out.println("Received page: " + page);		
		List<Store> storelist = new ArrayList<Store>();
				Pageable paging=PageRequest.of(page, perPage);
				System.out.println("paging>>"+paging);
				Page<Store> pagingUser = storeService.getStore(paging, store);
				storelist = pagingUser.getContent();
				HttpHeaders headers=new HttpHeaders();
				headers.add("x-total-page", String.valueOf(pagingUser.getTotalPages()));
		        headers.add("x-page", String.valueOf(pagingUser.getNumber()));
		        headers.add("x-total-records", String.valueOf(pagingUser.getTotalElements()));
		        headers.add("X-Page-Size", String.valueOf(pagingUser.getSize()));
			    
		        return ResponseEntity.ok().headers(headers).body(storelist);
			}
}
