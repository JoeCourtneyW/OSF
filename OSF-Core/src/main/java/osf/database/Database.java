package osf.database;

import com.mysql.jdbc.Connection;
import org.pmw.tinylog.Logger;


import java.sql.DriverManager;
import java.sql.SQLException;

public class Database
{

    private static final int TIMEOUT = 10000;

	private Connection connection;
    private String host;
    private int port;
    private String databaseName;
    private String username;
    private String password;


	Database(String host, int port, String username, String password, String databaseName) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.databaseName = databaseName;
	}

	public Connection getConnection() {
		return this.connection;
	}


    public synchronized void connect() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = (Connection) DriverManager.getConnection("jdbc:mysql://" + host + "/" + databaseName + "?autoReconnect=true&useSSL=false",
                    username, password);
            connection.setAutoReconnect(true);
            connection.setConnectTimeout(TIMEOUT);
        } catch (SQLException | ClassNotFoundException e) {
            Logger.error("Failed to connect to MySQL Database @ " + host + "/" + databaseName);
            return ;
        }
        Logger.info("Connected to MySQL Database");
    }

	public synchronized void flush() throws SQLException {
        Logger.info("Flushed MySQL Database connection");
		connection.close();
	}

    public static String escape(String in) {
        StringBuilder out = new StringBuilder();
        for (char c : in.toCharArray()) {
            switch (c) {
                case '\u0000':
                    out.append("\\0");
                    break;
                case '\u001a':
                    out.append("\\Z");
                    break;
                case '\n':
                    out.append("\\n");
                    break;
                case '\r':
                    out.append("\\r");
                    break;
                case '\'':
                    out.append("\\'");
                    break;
                case '"':
                    out.append("\\\"");
                    break;
                case '\\':
                    out.append("\\\\");
                    break;
                default:
                    out.append(c);
            }
        }
        return out.toString();
    }
}
