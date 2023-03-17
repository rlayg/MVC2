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

import org.apache.catalina.connector.Request;

import com.mysql.cj.exceptions.RSAException;
import com.mysql.cj.x.protobuf.MysqlxConnection.Close;

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
			rs = stmt.executeQuery(sql); //map set list
			/*
			if(rs.next()) {
				int a = rs.getInt("num");
				board.setNum(a);
				String b = rs.getString(2);
				board.setWriter(b);
				String c = rs.getString("subject");
				board.setSubject(c);
				String d =  rs.getString(5);
				board.setEmail(d);
				board.setPasswd(rs.getString("passwd")); 
				
			}
			
			}catch (Exception e) {
				// TODO: handle exception
			} 
			return board;
			
			*/
			
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
				   + "WHERE rn BETWEEN ? AND ? " ; //보통 페이징작업 between안하고 이렇게 서브쿼리 함 빗윈으로만하면 글 삭제했을때 이빨빠져있음
					
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, startRow);
			pstmt.setInt(2, endRow);
			rs = pstmt.executeQuery();
			
//			rs = pstmt.executeQuery(); 안에 밑에거 들어감
//			1	39	양만춘	게시판 첫글
//			2	37	김현주	애인 있어요
//			3	38	지진희	[답변] 애인 있어요  종방
//			4	30	옥주현	몬테크리스토
//			5	41	허균	[답변] 옥주현 답글
//			6	33	엄기준	[답변]난 알아요
//			7	32	차지연	[답변]훌룡한 노래
//			8	31	신성록	[답변]우리사랑 영원히
//			9	40	사랑은 	[답변] 정말 영원
//			10	28	김준수	드라큐라 등 / 많이 생략함
			
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

	public void readCount(int num) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = "update board set readcount=readcount+1 where num=?";
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			if(pstmt != null) {
				pstmt.close();
			}
			if(conn != null) {
				conn.close();
			}
		}
		
	}

	public int update(Board board) throws SQLException {
		int result = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
//		String sql = "update board set subject=?, writer=?, email=?, content=? where num=?";
		String sql = "update board set subject=?, writer=?, email=?,"
						+ "passwd=?, content=?, ip=? where num=?";
		
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, board.getSubject());
			pstmt.setString(2, board.getWriter());
			pstmt.setString(3, board.getEmail());
			pstmt.setString(4, board.getPasswd());
			pstmt.setString(5, board.getContent());
			pstmt.setString(6, board.getIp());
			pstmt.setInt(7, board.getNum());
			
			result = pstmt.executeUpdate();
			
			if(result > 0){ 
				 System.out.println("업데이트 성공"); 
			 } else {
				 System.out.println("업데이트 실패"); 
			 }
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			if(pstmt != null) {
				pstmt.close();
			}
			if(conn != null) {
				conn.close();
			}
		}
		return result;
	}

	public int insert(Board board) throws SQLException {
		// 여기에 댓글쓰기도 같이 넣음 근데 이건 사람 취향. 근데 ref sept level은 필수
		int num = board.getNum();
		int result = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql1 = "select nvl(max(num), 0) from board";	// 방법1 Max방법, seq방법(시퀸스)은 spring에서 해본데
		String sql = "insert into board values(?,?,?,?,?,?,?,?,?,?,?,sysdate)";
//		String sq9 = "insert into board values((select nvl(max(num), 0) from board) "
//												+ ",?,?,?,?,?,?,?,?,?,?,?,sysdate)"; //위 sql 2개 합한거 근데 뭔가 오류뜨면 원본 보기
		
		// 홍해의 기적 - 댓글관련 sql
		String sql2 = "update board set re_step = re_step+1 where ref=? and re_step > ?";
		
		
		
		
		try {
			conn = getConnection();
			// 신규글 --> sql1 (PK ->num도출) num도출이 목적
			pstmt = conn.prepareStatement(sql1); // sql1을 먼저 넣어준데
			rs = pstmt.executeQuery();
			rs.next();
			// key인 num 1씩 증가, mysql auto_increment 또는 oracle sequence
			// sequence를 사용 : values(시퀸스명(board_seq).nextval,?,?....)
			int number = rs.getInt(1) + 1; //맥스번호를 가지고 오는거
			rs.close();
			pstmt.close(); // sql 2개자나 먼저 넣은 sql 닫는거 안엉키게
			
			
			
			// 댓글 -->sql2
			if(num != 0) {
				System.out.println("BoardDAO insert 댓글 sql2 -> " + sql2);
				System.out.println("BoardDAO insert 댓글 board.getRef() -> " + board.getRef());
				System.out.println("BoardDAO insert 댓글 board.getRe_step() -> " + board.getRe_step());
				pstmt = conn.prepareStatement(sql2);
				pstmt.setInt(1, board.getRef());
				pstmt.setInt(2, board.getRe_step());
				pstmt.executeQuery();
				pstmt.close();
				
				//댓글 관련 정보
				board.setRe_step(board.getRe_step()+1);
				board.setRe_level(board.getRe_level()+1);
				
			}
			
			
			
			
			//신규글
			//if(num == 0) board.setRef(number); // 신규글은 레퍼런스 맞춰주는거야
			if(num == 0) {
				board.setRef(number); // 신규글은 레퍼런스 맞춰주는거야
			}
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, number);   // MAX + 1
			pstmt.setString(2, board.getWriter());
			pstmt.setString(3, board.getSubject());
			pstmt.setString(4, board.getContent());
			pstmt.setString(5, board.getEmail());
			pstmt.setInt(6, board.getReadcount());
			pstmt.setString(7, board.getPasswd());
			pstmt.setInt(8, board.getRef());
			pstmt.setInt(9, board.getRe_step());
			pstmt.setInt(10, board.getRe_level());
			pstmt.setString(11, board.getIp());
			result = pstmt.executeUpdate(); 
			
			if(result > 0){ 
				 System.out.println("인서트 성공"); 
			 } else {
				 System.out.println("인서트 실패"); 
			 }
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			if(rs != null) {
				rs.close();
			}
			if(pstmt != null) {
				pstmt.close();
			}
			if(conn != null) {
				conn.close();
			}
		}
				
		return result;
		
		/*
			insert 선생님 답 원본
	public int insert(Board board) throws SQLException {
		int num = board.getNum();		
		Connection conn = null;	
		PreparedStatement pstmt= null; 
		int result = 0;			
		ResultSet rs = null;
		String sql1 = "select nvl(max(num),0) from board";
		String sql="insert into board values(?,?,?,?,?,?,?,?,?,?,?,sysdate)";

		try {			
			conn = getConnection();
			pstmt = conn.prepareStatement(sql1);
			rs = pstmt.executeQuery();
			rs.next();
			// key인 num 1씩 증가, mysql auto_increment 또는 oracle sequence
			// sequence를 사용 : values(시퀀스명(board_seq).nextval,?,?...)
			int number = rs.getInt(1) + 1;  
			rs.close();   
			pstmt.close();
			
			// 댓글 --> sql2
			
			// 신규글 
			if (num == 0) board.setRef(number);
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, number);   // MAX + 1
			pstmt.setString(2, board.getWriter());
			pstmt.setString(3, board.getSubject());
			pstmt.setString(4, board.getContent());
			pstmt.setString(5, board.getEmail());
			pstmt.setInt(6, board.getReadcount());
			pstmt.setString(7, board.getPasswd());
			pstmt.setInt(8, board.getRef());
			pstmt.setInt(9, board.getRe_step());
			pstmt.setInt(10, board.getRe_level());
			pstmt.setString(11, board.getIp());
			result = pstmt.executeUpdate(); 
		} catch(Exception e) {	
			System.out.println(e.getMessage()); 
		} finally {
			if (rs !=null) rs.close();
			if (pstmt != null) pstmt.close();
			if (conn !=null) conn.close();
		}
		return result;	
	} 
		*/
	}

	public int delete(int num, String passwd) throws SQLException {
		int result = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;	// sql1의 select을 하기 위해 있는것같아
		
		String sql1 = "select passwd from board where num = ?";
		String sql = "delete from board where num = ?";
		
	
				
		try {
			String dbPasswd = "";
			conn = getConnection();
			pstmt = conn.prepareStatement(sql1);
			pstmt.setInt(1, num);
			
			rs = pstmt.executeQuery();
			
			
			
			if(rs.next()) {
				dbPasswd = rs.getString(1);
				if(dbPasswd.equals(passwd)) {
					rs.close();
					pstmt.close();
					pstmt = conn.prepareStatement(sql);
					pstmt.setInt(1, num);
					result = pstmt.executeUpdate();
				} else {
					result = 0;
				}
			} else {
				result = -1;
			}
			
			if(result > 0) {
				System.out.println("dbPasswd.equals(passwd) 같음");
			} else if(result == 0) {
				System.out.println("dbPasswd.equals(passwd) 다름");
			} else {
				System.out.println("ResultSet rs에 글이 없어");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("delete e.getMessage()" + e.getMessage());
		} finally {
			if(pstmt != null) {
				pstmt.close();
			}
			if(conn != null) {
				conn.close();
			}
		}
		
		return result;
	}


}
