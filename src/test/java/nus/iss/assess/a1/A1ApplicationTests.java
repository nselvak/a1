package nus.iss.assess.a1;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import nus.iss.assess.a1.model.Quotation;
import nus.iss.assess.a1.service.QuotationService;


// Task 7
@SpringBootTest
class A1ApplicationTests {

	@Autowired
	private QuotationService quoteSvc;

	@Test
	void contextLoads() {
		List<String> items = new ArrayList<>();
		items.add("durian");
		items.add("plum");
		items.add("pear");

		Optional<Quotation> quote = quoteSvc.getQuotations(items);
		assertTrue(quote.isEmpty());
	}

}
