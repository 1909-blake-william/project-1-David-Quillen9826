package com.revature.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.revature.models.Reimbursement;
import com.revature.util.ConnectionUtil;

public class ReimbursementDaoSQL implements ReimbursementDao {
	Reimbursement extractReimbursement(ResultSet rs) throws SQLException {
		int id = rs.getInt(1);
		Double amount = rs.getDouble(2);
		Timestamp submit = rs.getTimestamp(3);
		Timestamp resolve = rs.getTimestamp(4);
		String desc = rs.getString(5);
		int author = rs.getInt(6);
		int resolver = rs.getInt(7);
		int status = rs.getInt(8);
		int type = rs.getInt(9);
		String name = rs.getString(11);
		String nameRes = rs.getString(22);
		String aStatus = rs.getString(18);
		String aType = rs.getString(20);
		return new Reimbursement(id, amount, submit, resolve, desc, author, resolver, status, type, name, nameRes,
				aStatus, aType);
	}

	public int create(Reimbursement reimb) {
		try (Connection c = ConnectionUtil.getConnection()) {
			double amount = reimb.getReimbAmount();
			String desc = reimb.getReimbDescription();
			int author = reimb.getReimbAuthor();
			int type = reimb.getReimbTypeId();
			String sql = "INSERT INTO ers_reimbursement "
					+ "(reimb_amount, reimb_desction, reimb_author, reimb_type_id) " + "VALUES(?, ?, ?, ?)";
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setDouble(1, amount);
			ps.setString(2, desc);
			ps.setInt(3, author);
			ps.setInt(4, type);
			return ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}

	public List<Reimbursement> findAll() {

		try (Connection c = ConnectionUtil.getConnection()) {

			String sql = "SELECT * FROM (ers_reimbursement r "
					+ "LEFT JOIN ers_users u ON (r.reimb_author = u.ers_users_id)) "
					+ "LEFT JOIN ers_reimbursement_status s ON (r.reimb_status_id = s.reimb_status_id) "
					+ "LEFT JOIN ers_reimbursement_type t ON (r.reimb_type_id = t.reimb_type_id) "
					+ "LEFT JOIN ers_users uu ON (r.reimb_resolver = uu.ers_users_id)";

			PreparedStatement ps = c.prepareStatement(sql);

			ResultSet rs = ps.executeQuery();
			List<Reimbursement> reimbursements = new ArrayList<>();
			while (rs.next()) {
				reimbursements.add(extractReimbursement(rs));
			}

			return reimbursements;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	public List<Reimbursement> findByAuthor(int userId) {
		try (Connection c = ConnectionUtil.getConnection()) {
			String sql = "SELECT * FROM (ers_reimbursement r "
					+ "LEFT JOIN ers_users u ON (r.reimb_author = u.ers_users_id)) "
					+ "LEFT JOIN ers_reimbursement_status s ON (r.reimb_status_id = s.reimb_status_id) "
					+ "LEFT JOIN ers_reimbursement_type t ON (r.reimb_type_id = t.reimb_type_id) "
					+ "LEFT JOIN ers_users uu ON (r.reimb_resolver = uu.ers_users_id) " + "WHERE r.reimb_author = ?";
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setInt(1, userId);
			ResultSet rs = ps.executeQuery();
			List<Reimbursement> reimbursements = new ArrayList<>();
			while (rs.next()) {
				reimbursements.add(extractReimbursement(rs));
			}

			return reimbursements;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public int resolve(int resolver, int status, int id) {
		try (Connection c = ConnectionUtil.getConnection()) {

			String sql = "UPDATE ers_reimbursement " + "SET reimb_resolver = ?, reimb_status_id = ? "
					+ "WHERE reimb_id = ?";
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setInt(1, resolver);
			ps.setInt(2, status);
			ps.setInt(3, id);

			return ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}

}