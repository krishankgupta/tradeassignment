package tradeassignment.dao;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

import tradeassignment.model.Trade;

public class StoreDAO {

	private static final Map<String, TreeSet<Trade>> trades = new HashMap<>();

	private DateFormat format = new SimpleDateFormat("dd/mm/yyyy");

	public StoreDAO() {

		try {
			Trade t1 = new Trade("T1", 1, "CP-1", "B1", format.parse("20/05/2020"), format.parse("26/11/2020"));
			t1.setExpired(t1.getMaturityDate().before(new Date()));
			TreeSet<Trade> tradeSet = new TreeSet<Trade>();
			tradeSet.add(t1);
			trades.put(t1.getTradeId(), tradeSet);

			Trade t2 = new Trade("T2", 2, "CP-2", "B1", format.parse("20/05/2021"), format.parse("26/11/2020"));
			t2.setExpired(t2.getMaturityDate().before(new Date()));
			TreeSet<Trade> tradeSet2 = new TreeSet<Trade>();
			tradeSet2.add(t2);

			Trade t3 = new Trade("T2", 1, "CP-1", "B1", format.parse("20/05/2020"), format.parse("26/11/2020"));
			t3.setExpired(t3.getMaturityDate().before(new Date()));
			tradeSet2.add(t3);
			trades.put(t2.getTradeId(), tradeSet2);

			Trade t4 = new Trade("T3", 3, "CP-3", "B2", format.parse("20/05/2014"), format.parse("26/11/2020"));
			t4.setExpired(t4.getMaturityDate().before(new Date()));
			TreeSet<Trade> tradeSet3 = new TreeSet<Trade>();
			tradeSet3.add(t4);
			trades.put(t4.getTradeId(), tradeSet3);

		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

	public Map<String, TreeSet<Trade>> getAllTrades() {
		return trades;
	}

}
