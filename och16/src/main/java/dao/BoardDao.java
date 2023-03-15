package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import oracle.net.aso.p;


//	Singleton + DBCP
public class BoardDao {
	
	//Singleton
	private static BoardDao instance;
	
	private BoardDao() {
	}
	
	public static BoardDao getInstance() {
		if(instance == null) {
			instance = new BoardDao();
		}
		return instance;
	}
	
	// DBCP
	private Connection getConnection() throws SQLException {
		Connection conn = null;
		
		Context ctx;
		try {
			ctx = new InitialContext();
			DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/OracleDB");
			conn = ds.getConnection();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println(e.getMessage());
		}
		return conn;
	}
	
	// Board Total Count Return
	public int getTotalCnt() throws SQLException {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		int tot = 0;
		String sql = "select count(*) from board";
		try {
			conn = getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			if(rs.next()) tot = rs.getInt(1);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			if(rs != null) rs.close();
			if(stmt != null) stmt.close();
			if(conn != null) conn.close();
		}
		return tot;
	}
	
	public Board select(int num) throws SQLException {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String sql = "select * from board where num = " + num;
		Board board = new Board();
		
		try {
			conn = getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			if(rs.next()) {
				board.setNum(rs.getInt("num"));
				board.setWriter(rs.getString("writer"));
				board.setSubject(rs.getString("subject"));
				board.setContent(rs.getString("content"));
				board.setEmail(rs.getString("email"));
				board.setReadcount(rs.getInt("readcount"));
				board.setIp(rs.getString("ip"));
				board.setReg_date(rs.getDate("reg_date"));
				board.setRef(rs.getInt("ref"));
				board.setRe_level(rs.getInt("re_level"));
				board.setRe_step(rs.getInt("re_step"));
			}
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			if(rs != null) rs.close();
			if(stmt != null) stmt.close();
			if(conn != null) conn.close();
		}
		return board;
	}
	
	/*
	select *
	from
	    (
	    select rownum rn, a.*
	    from( 
	        select * from board
	        order by ref desc, re_step
	        ) a
	    )
	WHERE rn between 1 and 10;
	이 SQL문을 밑에 구현
	*/
	public List<Board> boardList(int startRow, int endRow) throws SQLException {
		List<Board> list = new ArrayList<>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT * "
				   + "FROM (Select rownum rn, a.* "
				   + "     From (select * from board order by ref desc, re_step) a ) "
				   + "WHERE rn BETWEEN ? AND ? " ;
		
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, startRow);
			pstmt.setInt(2, endRow);
			rs = pstmt.executeQuery();
			
			/*
//			선생님 답			 
			while(rs.next()) {
				Board board = new Board();
				System.out.println("board -> " + board);
				board.setNum(rs.getInt("num"));
				board.setWriter(rs.getString("writer"));
				board.setSubject(rs.getString("subject"));
//				board.setContent(rs.getString("content"));
				board.setEmail(rs.getString("email"));
				board.setReadcount(rs.getInt("readcount"));
				board.setIp(rs.getString("ip"));
				board.setRef(rs.getInt("ref"));
				board.setRe_level(rs.getInt("re_level"));
				board.setRe_step(rs.getInt("re_step"));
				board.setReg_date(rs.getDate("reg_date"));
				
				list.add(board);
			}
			*/
			if(rs.next()) {
				do {
					Board board = new Board();
//					System.out.println("board -> " + board);
					board.setNum(rs.getInt("num"));
					board.setWriter(rs.getString("writer"));
					board.setSubject(rs.getString("subject"));
//					board.setContent(rs.getString("content"));
					board.setEmail(rs.getString("email"));
					board.setReadcount(rs.getInt("readcount"));
					board.setIp(rs.getString("ip"));
					board.setRef(rs.getInt("ref"));
					board.setRe_level(rs.getInt("re_level"));
					board.setRe_step(rs.getInt("re_step"));
					board.setReg_date(rs.getDate("reg_date"));
					
					list.add(board);
				} while(rs.next());
				System.out.println("rs -> " + rs);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		} finally {
			if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();
		}
		return list;
	}
	
	
}
