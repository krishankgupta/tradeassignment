package tradeassignment;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import tradeassignment.dao.StoreDAO;
import tradeassignment.model.Trade;

public class TradeService {
	
	private StoreDAO store = new StoreDAO();

	public static void main(String[] args) {

		TradeService service = new TradeService();
		List<Trade> tradeCsv = service.readCsv();
		for(Trade trade : tradeCsv) {
			try {
				service.addTrade(trade);
			} catch(Exception ex) {
				System.out.println("Error adding trade :" + trade + ", exception is : " + ex.getMessage());
			}
		}
	}

	@SuppressWarnings("deprecation")
	private List<Trade> readCsv() {
		List<Trade> tradeList = new ArrayList<>();
		try {
			BufferedReader br = new BufferedReader(new FileReader("tradeList.csv"));  
			
			String line;
			while ((line = br.readLine()) != null) {  
			       // split on comma(',')  
			       String[] obj = line.split(",");  

			       // create car object to store values  
			       Trade t1 = new Trade();  

			       // add values from csv to car object  
			       t1.setTradeId(obj[0]);  
			       t1.setVersion(Integer.parseInt(obj[1]));  
			       t1.setCounterPartyId(obj[2]);  
			       t1.setBookId(obj[3]);
			       t1.setMaturityDate(new Date(obj[4]));
			       t1.setCreatedDate(new Date(obj[5]));
			       t1.setExpired(t1.getMaturityDate().before(new Date()));

			       // adding car objects to a list  
			       tradeList.add(t1);         
			}
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		} 
		return tradeList;
	}

	public boolean addTrade(Trade trade) throws Exception {
		
		if(trade == null) {
			throw new Exception("Invalid input");
		}
		if(trade.isExpired()) {
			throw new Exception("Expired trade");
		}
		
		Map<String, TreeSet<Trade>> trades = store.getAllTrades();
		if(trades != null && trades.size()>0) {
			TreeSet<Trade> tradeSet = trades.get(trade.getTradeId()) != null ? trades.get(trade.getTradeId()) : new TreeSet<Trade>();
			if(trade.getVersion() < tradeSet.first().getVersion()) {
				throw new Exception("Trade with higher version already exists, rejecting data.");
			}
			tradeSet.remove(tradeSet.first());
			tradeSet.add(trade);
			System.out.println(trades);
			return true;
		}
		return false;
	}
}
