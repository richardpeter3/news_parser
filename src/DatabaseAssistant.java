//Class to provide utilities  for the easy retrieval of information from the database
import java.sql.*;

public class DatabaseAssistant {
	protected static String username = "fredfeed";
	protected static String password = "TheClaw78";
	protected static String database = "headlines_and_news";
	protected Connection conn;
	protected Statement st;
	protected Statement st2;
	protected boolean connected;
	protected boolean statementExecutedCorrectly;
	protected ResultSet rs; 

	public DatabaseAssistant(){
		conn = null;
		st = null;
		st2 = null;
		connected = false;
		statementExecutedCorrectly = false;
		rs = null;
	}
	public int connect() throws SQLException, ClassNotFoundException {
		if(connected)
			return -1;
		conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/"+database, username, password);
		st = conn.createStatement();
		st2 = conn.createStatement();
		connected = true;
		return 1;
	}
	public void execute(String query) throws SQLException{
		statementExecutedCorrectly = false;
		if(connected)
			statementExecutedCorrectly =   st.execute(query);
	}
	public void executeQuery(String query) throws SQLException{
		statementExecutedCorrectly = false;
		if(connected){
			rs = st.executeQuery(query);
			statementExecutedCorrectly = true;
		}
	}
	public ResultSet rtnExecuteQuery(String query) throws SQLException{
		statementExecutedCorrectly = false;
		ResultSet temp = null;
		if(connected){
			temp = st.executeQuery(query);
			statementExecutedCorrectly = true;
			return temp;
		}
		return temp;
	}
	public void close() throws SQLException {
		if(connected){
			st.close();
			conn.close();
			connected = false;
		}
	}
	//return the resultset
	public ResultSet getResults(){
		return rs;
	}

	//Check boolean success values
	public boolean isConnected(){
		return connected;
	}
	public boolean isExecutedCorrectly(){
		return statementExecutedCorrectly;
	}
}
