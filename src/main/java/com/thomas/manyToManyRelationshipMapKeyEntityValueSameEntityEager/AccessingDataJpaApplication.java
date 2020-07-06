package com.thomas.manyToManyRelationshipMapKeyEntityValueSameEntityEager;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

import com.thomas.manyToManyRelationshipMapKeyEntityValueSameEntityEager.domains.A;
import com.thomas.manyToManyRelationshipMapKeyEntityValueSameEntityEager.domains.B;
import com.thomas.manyToManyRelationshipMapKeyEntityValueSameEntityEager.repositories.ARepository;
import com.thomas.manyToManyRelationshipMapKeyEntityValueSameEntityEager.repositories.BRepository;

@SpringBootApplication
@Transactional
public class AccessingDataJpaApplication {

	private static final Logger log = LoggerFactory.getLogger(AccessingDataJpaApplication.class);
	
	public static void main(String[] args) {
		SpringApplication.run(AccessingDataJpaApplication.class);
	}

	@Bean
	public CommandLineRunner demo(ARepository aRepository, BRepository bRepository) {
		return (args) -> {
			log.info("===== Persisting As and Bs");
			persistData(aRepository, bRepository);
			readData(aRepository, bRepository);
			log.info("===== Modifying some As and Bs");
			modifyData(aRepository, bRepository);
			readData(aRepository, bRepository);
			log.info("===== Deleting some As and Bs");
			deleteData(aRepository, bRepository);
			readData(aRepository, bRepository);
		};
	}
	
	private void readData(ARepository aRepository, BRepository bRepository) {
		Iterable<A> As = aRepository.findAll();
		log.info("===== As");
		for(A a : As) {
			log.info(a.toString());
		}
		
		Iterable<B> Bs = bRepository.findAll();
		log.info("===== Bs");
		for(B b : Bs) {
			log.info(b.toString());
		}
	}
	
	private void persistData(ARepository aRepository, BRepository bRepository) {
		//we build : a1.myMap{ b1 -> b1} and a2.myMap{ b1 -> b1, b2 -> b3, b3 -> b3, b4 ->b5}
		A a1 = new A("a1");
		A a2 = new A("a2");
		B b1 = new B("b1");
		B b2 = new B("b2");
		B b3 = new B("b3");
		B b4 = new B("b4");
		B b5 = new B("b5");
	    Map<B,B> a1Map = new HashMap<B, B>();
	    a1Map.put(b1,b1);
	    Map<B,B> a2Map = new HashMap<B, B>();
	    a2Map.put(b1,b1);
	    a2Map.put(b2,b3);
	    a2Map.put(b3,b3);
	    a2Map.put(b4,b5);
		a1.setMyMap(a1Map);
		a2.setMyMap(a2Map);
		bRepository.save(b1);
		bRepository.save(b2);
		bRepository.save(b3);
		bRepository.save(b4);
		bRepository.save(b5);
		aRepository.save(a1);
		aRepository.save(a2);
		
		//we can build an A with an empty Map
		A a3 = new A("a3");
		aRepository.save(a3);
	}

	private void modifyData(ARepository aRepository, BRepository bRepository) {
		//we move the b2 entry from a2 to a1 and switch values between b3 and b4 entries in a2
		A a1 = aRepository.findByA("a1").get(0);
		A a2 = aRepository.findByA("a2").get(0);
		B b2 = bRepository.findByB("b2").get(0);
		B b3 = bRepository.findByB("b3").get(0);
		B b4 = bRepository.findByB("b4").get(0);
		
		a1.getMyMap().put(b2, a2.getMyMap().get(b2));
		B a2_b3 = a2.getMyMap().get(b3);
		B a2_b4 = a2.getMyMap().get(b4);
		a2.getMyMap().put(b3, a2_b4);
		a2.getMyMap().put(b4, a2_b3);
		aRepository.save(a1);
		aRepository.save(a2);
	}
	
	private void deleteData(ARepository aRepository, BRepository bRepository) {
		//we delete a1, a2.b1 and a2's entries of value b3
		A a1 = aRepository.findByA("a1").get(0);
		A a2 = aRepository.findByA("a2").get(0);
		B b1 = bRepository.findByB("b1").get(0);
		B b3 = bRepository.findByB("b3").get(0);
		
		aRepository.delete(a1);
		a2.getMyMap().remove(b1);
		
		Iterator<Map.Entry<B, B>> iterator = a2.getMyMap().entrySet().iterator();
	    while (iterator.hasNext()) {
	        Map.Entry<B, B> entry = iterator.next();
	        if(entry.getValue().equals(b3)) {
	        	iterator.remove();
	        }
	    }
		aRepository.save(a2);
	}
}
