package com.revature.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.daos.ReimbursementDao;
import com.revature.models.Reimbursement;

public class ReimbursementServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private ReimbursementDao reimDao = ReimbursementDao.currentImplementation;

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println(req.getRequestURL());
		resp.addHeader("Access-Control-Allow-Origin", "http://localhost:5500");
		resp.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE, HEAD");
		resp.addHeader("Access-Control-Allow-Headers",
				"Origin, Methods, Credentials, X-Requested-With, Content-Type, Accept");
		resp.addHeader("Access-Control-Allow-Credentials", "true");
		resp.setContentType("application/json");
		super.service(req, resp);

	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ObjectMapper om = new ObjectMapper();
		List<Reimbursement> reimbursements = new ArrayList<>();
		
		reimbursements = reimDao.findAll();

		String json = om.writeValueAsString(reimbursements);
		resp.addHeader("content-type", "application/json");
		resp.getWriter().write(json);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ObjectMapper om = new ObjectMapper();
		Reimbursement r = (Reimbursement) om.readValue(req.getReader(), Reimbursement.class);

		int reim = reimDao.create(r);
		System.out.println(reim);

		String json = om.writeValueAsString(reim);

		resp.getWriter().write(json);
		resp.setStatus(201); 

	}

//	@Override
//	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//		ObjectMapper om = new ObjectMapper();
//		String resolverStr = req.getParameter("resolver");
//		String statusStr = req.getParameter("status");
//
//		User resolver = Integer.parseInt(resolverStr);
//		int status = Integer.parseInt(statusStr);
//		int id = Integer.parseInt(req.getParameter("id"));
//		int check = reimDao.resolve(resolver, status, id);
//		 if (check == 0) {
//			resp.setStatus(400);
//		} else {
//			resp.setStatus(204);
//		}
//	}
}