package com.revature.daos;

import java.util.List;

import com.revature.models.Reimbursement;

public interface ReimbursementDao {
	
	ReimbursementDao currentImplementation = new ReimbursementDaoSQL();
	
	int create(Reimbursement r);
		
	List<Reimbursement> findAll();
			
	List<Reimbursement> findByAuthor(int i);	

	int resolve(int r, int s, int i);

}