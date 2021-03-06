package com.revature.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.revature.models.User;
import com.revature.util.ConnectionUtil;

public class UserDaoSQL implements UserDao {

	private Logger log = Logger.getRootLogger();

	User extractUser(ResultSet rs) throws SQLException {
		int id = rs.getInt("ers_user_id");
		System.out.println("1");
		String rsUsername = rs.getString("ers_username");
		System.out.println("2");
		String rsPassword = rs.getString("ers_password");
		System.out.println("3");
		String rsFirstName = rs.getString("user_first_name");
		System.out.println("4");
		String rsLastName = rs.getString("ers_last_name");
		System.out.println("5");
		String rsEmail = rs.getString("user_email");
		System.out.println("6");
		int role = rs.getInt("user_role_id");
		System.out.println("7");
		return new User(id, rsUsername, rsPassword, rsFirstName, rsLastName, rsEmail, role);
	}

	@Override
	public int save(User u) {
		log.debug("attempting to find user by credentials from DB");
		try (Connection c = ConnectionUtil.getConnection()) {

			String sql = "INSERT INTO ers_users (user_id, username, password) "
					+ " VALUES (users_id_seq.nextval ,?,?)";

			PreparedStatement ps = c.prepareStatement(sql);
			ps.setString(1, u.getUsername());
			ps.setString(2, u.getPassword());

			return ps.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
//		e.printStackTrace();
			return 0;
		}
	}

	@Override
	public List<User> findAll() {
		log.debug("attempting to find all users from DB");
		try (Connection c = ConnectionUtil.getConnection()) {

			String sql = "SELECT * FROM ers_users";

			PreparedStatement ps = c.prepareStatement(sql);

			ResultSet rs = ps.executeQuery();
			List<User> users = new ArrayList<User>();
			while (rs.next()) {
				users.add(extractUser(rs));
			}

			return users;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
//		e.printStackTrace();
			return null;
		}
	}

	@Override
	public User findById() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User findByUsernameAndPassword(String username, String password) {
		log.debug("attempting to find user by credentials from DB");
		try (Connection c = ConnectionUtil.getConnection()) {
			log.debug("connection check");
			String sql = "SELECT * FROM ers_users WHERE ers_username = ? AND ers_password = ?";
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setString(1, username);
			ps.setString(2, password);
			// i am here
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return extractUser(rs);
			} else {
				return null;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public User findByUsername(String username) {
		log.debug("attempting to find user by credentials from DB");
		try (Connection c = ConnectionUtil.getConnection()) {

			String sql = "SELECT * FROM ers_users WHERE username = ?";

			PreparedStatement ps = c.prepareStatement(sql);
			ps.setString(1, username);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				return extractUser(rs);
			} else {
				return null;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			return null;
		}
	}

}
