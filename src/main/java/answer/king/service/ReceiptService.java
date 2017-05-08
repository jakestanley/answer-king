package answer.king.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import answer.king.NotFoundException;
import answer.king.model.Receipt;
import answer.king.repo.ReceiptRepository;

@Service
public class ReceiptService {

	@Autowired
	private ReceiptRepository receiptRepository;
	
	public List<Receipt> getAll() {
		return receiptRepository.findAll();
	}
	
	public Receipt get(long id) throws NotFoundException {
		
		if(!receiptRepository.exists(id)) {
			throw new NotFoundException();
		}
		
		return receiptRepository.findOne(id);
	}

}
