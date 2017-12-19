package com.epozen.atm.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerDAO {
	private static ClientVO myselfVO = new ClientVO();
	private static ClientVO transferVO = new ClientVO();

	public boolean accountCheck(String accountNumber, String bankName) { // 계좌 조회
		Connection conn = null;
		PreparedStatement pstmt = null;
		boolean result = false;
		ResultSet rs = null;

		try {
			conn = DataSourceManager.getConnection();
			pstmt = conn.prepareStatement("\r\n"
					+ "select A.accountNumber, A.code, A.balance from accountInfo A, bankcode B where A.code = B.code and A.accountNumber=? and B.name=?");
			pstmt.setString(1, accountNumber);

			pstmt.setString(2, bankName);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				result = true;
				System.out.println("rs.getString(1)=" + rs.getString(1));
				System.out.println(myselfVO.hashCode());
				myselfVO.setAccountNumber(rs.getString(1));
				myselfVO.setCode(rs.getString(2));
				myselfVO.setBankName(bankName);
				myselfVO.setBalance(rs.getInt(3));
			}

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}

	public boolean paswordCheck(String password) { // 비밀번호 체크
		Connection conn = null;
		PreparedStatement pstmt = null;
		boolean result = false;
		ResultSet rs = null;

		try {
			conn = DataSourceManager.getConnection();
			pstmt = conn.prepareStatement(
					"select customer.name, accountInfo.balance from accountInfo, customer where password=? and accountNumber=? and accountInfo.id = customer.id");
			pstmt.setString(1, password);
			pstmt.setString(2, myselfVO.getAccountNumber()); // 추가 계좌번호
			rs = pstmt.executeQuery();

			if (rs.next()) {
				result = true;

				myselfVO.setName(rs.getString(1));
				myselfVO.setBalance(Integer.parseInt(rs.getString(2)));

				System.out.println(myselfVO.toString());
			}

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}

	public boolean transferMinusBalance(int dealMoney) { // 보내는 사람 계좌에서 보내는 금액 차감
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmtSecond = null;
		boolean result = false;
		int rs = 0;
		ResultSet resultSet = null;

		try {
			conn = DataSourceManager.getConnection();
			pstmt = conn.prepareStatement("update accountInfo set balance=(balance-?) where accountNumber=?");
			pstmt.setInt(1, dealMoney);
			pstmt.setString(2, myselfVO.getAccountNumber()); // 추가 계좌번호
			rs = pstmt.executeUpdate();

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs == 1) {

				System.out.println("Minus  fist step clear");
				try {
					pstmtSecond = conn.prepareStatement(
							"select balance from accountinfo A, bankcode B where  A.code = B.code and B.name = ? and A.accountNumber=?");
					pstmtSecond.setString(1, myselfVO.getBankName()); // 확인하기!!
					pstmtSecond.setString(2, myselfVO.getAccountNumber());
					resultSet = pstmtSecond.executeQuery();

					if (resultSet.next()) {

						myselfVO.setBalance(Integer.parseInt(resultSet.getString(1)));
						myselfVO.setDealContent("계좌이체");
						myselfVO.setDealFlag("-");
						myselfVO.setDealMoney(dealMoney);

					}

					else {
						System.out.println("pstmtsecond fail");
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}

				return true;
			} else
				return false;
		}

	}

	public boolean transferPlusBalance(int money, String transferAccountNumber, String bankName) { // 받는사람 +
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmtSecond = null;
		int rs = 0;
		ResultSet resultSet = null;

		try {
			conn = DataSourceManager.getConnection();
			pstmt = conn.prepareStatement(
					"update accountinfo A, bankcode B set balance = balance+? where  A.code = B.code and B.name = ? and A.accountNumber=?");
			pstmt.setInt(1, money);
			pstmt.setString(2, bankName);
			pstmt.setString(3, transferAccountNumber);
			rs = pstmt.executeUpdate();

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs == 1) {
				System.out.println("fist step clear");
				try {
					pstmtSecond = conn.prepareStatement(
							"select B.code, balance, C.name from accountinfo A, bankcode B, customer C where  A.code = B.code and A.id = c.id and B.name = ? and A.accountNumber=?");
					pstmtSecond.setString(1, bankName);
					pstmtSecond.setString(2, transferAccountNumber);
					resultSet = pstmtSecond.executeQuery();

					if (resultSet.next()) {
						transferVO = new ClientVO(transferAccountNumber, resultSet.getString(1), "+", "계좌이체", money,
								Integer.parseInt(resultSet.getString(2)));
						transferVO.setName(resultSet.getString(3));
						transferVO.setBankName(bankName);

					} else {
						System.out.println("pstmtsecond fail");
					}

				} catch (SQLException e) {
					e.printStackTrace();
				}

				return true;
			} else
				return false;
		}

	}

	public boolean insertTrasverVODealInfo() {

		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmtSecond = null;
		boolean result = false;
		int rs = 0;
		ResultSet resultSet = null;

		try {
			conn = DataSourceManager.getConnection();
			pstmt = conn.prepareStatement("insert into dealinfo values(?, ?, now(), ?, ?, ?, ?)");

			pstmt.setString(1, transferVO.getAccountNumber());
			pstmt.setString(2, transferVO.getCode());
			pstmt.setString(3, transferVO.getDealFlag());
			pstmt.setString(4, transferVO.getDealContent());
			pstmt.setInt(5, transferVO.getDealMoney());
			pstmt.setInt(6, transferVO.getBalance());

			rs = pstmt.executeUpdate();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs == 1)
				return true;
			else
				return false;
		}
	}

	public boolean insertMyselfVODealInfo() {

		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmtSecond = null;
		boolean result = false;
		int rs = 0;
		ResultSet resultSet = null;

		try {
			conn = DataSourceManager.getConnection();
			pstmt = conn.prepareStatement("insert into dealinfo values(?, ?, now(), ?, ?, ?, ?)");

			pstmt.setString(1, myselfVO.getAccountNumber());
			pstmt.setString(2, myselfVO.getCode());
			pstmt.setString(3, myselfVO.getDealFlag());
			pstmt.setString(4, myselfVO.getDealContent());
			pstmt.setInt(5, myselfVO.getDealMoney());
			pstmt.setInt(6, myselfVO.getBalance());

			rs = pstmt.executeUpdate();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs == 1)
				return true;
			else
				return false;
		}

	}

	public boolean transferMoney(int transferMoney, String transferAccountNumber, String bankName) {
		boolean result = false;

		// autocommit 비활성화

		System.out.println("계좌 +");
		if (!transferPlusBalance(transferMoney, transferAccountNumber, bankName)) {
			return false;
		}
		System.out.println("계좌 -");
		if (!transferMinusBalance(transferMoney)) {
			return false;
		}
		System.out.println("내계좌 기록+");
		if (!insertMyselfVODealInfo()) {
			return false;
		}
		System.out.println("상대방계좌 기록+");
		if (!insertTrasverVODealInfo()) {
			return false;
		}

		return true;
	}

	public boolean withdrawlMoney(int money) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmtSecond = null;
		boolean result = false;
		int rs = 0;
		ResultSet resultSet = null;

		try {
			conn = DataSourceManager.getConnection();
			pstmt = conn.prepareStatement("update accountinfo set balance=balance-? where accountNumber=?");

			pstmt.setInt(1, money);
			pstmt.setString(2, myselfVO.getAccountNumber());

			rs = pstmt.executeUpdate();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs == 1) {

				myselfVO.setDealFlag("-");
				myselfVO.setDealContent("출금");
				myselfVO.setDealMoney(money);
				myselfVO.setBalance(myselfVO.getBalance() - money);

				return true;
			} else
				return false;
		}

	}

	public boolean depositMoney(int money) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmtSecond = null;
		boolean result = false;
		int rs = 0;
		ResultSet resultSet = null;

		try {
			conn = DataSourceManager.getConnection();
			pstmt = conn.prepareStatement("update accountinfo set balance=balance+? where accountNumber=?");

			pstmt.setInt(1, money);
			pstmt.setString(2, myselfVO.getAccountNumber());

			rs = pstmt.executeUpdate();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs == 1) {

				myselfVO.setDealFlag("+");
				myselfVO.setDealContent("입금");
				myselfVO.setDealMoney(money);
				myselfVO.setBalance(myselfVO.getBalance() + money);

				return true;
			} else
				return false;
		}

	}

	public boolean withdrawlOperation(int balance) {
		if (!withdrawlMoney(balance))
			return false;
		if (!insertMyselfVODealInfo())
			return false;

		return true;
	}

	public boolean depositOperation(int balance) {
		if (!depositMoney(balance))
			return false;
		if (!insertMyselfVODealInfo())
			return false;

		return true;
	}

	public static ClientVO getTransferVO() {
		return transferVO;
	}

	public ClientVO getMyselfVO() {
		return myselfVO;
	}

}
