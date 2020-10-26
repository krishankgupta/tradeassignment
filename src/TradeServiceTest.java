import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;

import tradeassignment.TradeService;
import tradeassignment.model.Trade;

class TradeServiceTest {

	private TradeService service;

	private static DateFormat format = new SimpleDateFormat("dd/mm/yyyy");

	@BeforeEach
	void setUp() throws Exception {
		service = new TradeService();
	}

	@AfterEach
	void tearDown() throws Exception {
		service = null;
	}

	public static Object[][] TradeData() throws ParseException {
		return new Object[][] {
				{ new Trade("T1", 1, "CP-1", "B1", format.parse("20/05/2020"), format.parse("26/11/2020")), true },
				{ new Trade("T2", 2, "CP-1", "B1", format.parse("20/05/2021"), format.parse("26/11/2020")), true },
				{ new Trade("T3", 3, "CP-1", "B1", format.parse("20/05/2014"), format.parse("26/11/2020")), true } };
	}

	@ParameterizedTest
	@MethodSource("TradeData")
	void test_positiveTrades(Trade t1, boolean result) throws Exception {
		assertEquals(result, service.addTrade(t1));
	}

	@ParameterizedTest
	@NullSource
	void testfor_NullTrade(Trade t1) {
		try {
			service.addTrade(t1);
		} catch (Exception e) {
			assertTrue(true);
			assertEquals("Invalid input", e.getMessage());
		}
	}

	@Test
	void testfor_ExpiredTrade() {
		try {
			Trade t1 = new Trade("T1", 1, "CP-1", "B1", format.parse("20/05/2020"), format.parse("26/11/2020"));
			t1.setExpired(t1.getMaturityDate().before(new Date()));
			service.addTrade(t1);
		} catch (Exception e) {
			assertTrue(true);
			assertEquals("Expired trade", e.getMessage());
		}
	}

	@Test
	void testfor_OlderVersionTrade() {
		try {
			Trade t1 = new Trade("T3", 2, "CP-1", "B1", format.parse("20/05/2021"), format.parse("26/11/2020"));
			t1.setExpired(t1.getMaturityDate().before(new Date()));
			service.addTrade(t1);
		} catch (Exception e) {
			assertTrue(true);
			assertEquals("Trade with higher version already exists, rejecting data.", e.getMessage());
		}
	}
}
