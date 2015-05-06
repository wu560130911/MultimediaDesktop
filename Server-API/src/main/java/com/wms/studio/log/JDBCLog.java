/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wms.studio.log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.ErrorCode;
import org.apache.log4j.spi.LoggingEvent;
/**
 * 后期需要修改为数据库连接池
 * @author WMS
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class JDBCLog extends org.apache.log4j.AppenderSkeleton implements
		org.apache.log4j.Appender {

	protected String databaseURL = "jdbc:odbc:myDB";

	protected String databaseUser = "me";

	protected String table = "tb_system_log";

	protected String machineId = "127.0.0.1";

	protected String databasePassword = "mypassword";

	protected Connection connection = null;

	protected String sqlStatement = "";

	protected int bufferSize = 10;

	protected ArrayList buffer;

	protected ArrayList removes;

	private boolean locationInfo = false;

	public JDBCLog() {
		super();
		buffer = new ArrayList(bufferSize);
		removes = new ArrayList(bufferSize);
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				if(connection!=null){
					try {
						if(!connection.isClosed()){
							connection.close();
						}
					} catch (SQLException e) {
					}
				}
			}
		});
	}

	public boolean getLocationInfo() {
		return locationInfo;
	}

	public void setLocationInfo(final boolean flag) {
		locationInfo = flag;
	}

	public void append(LoggingEvent event) {
		event.getNDC();
		event.getThreadName();
		// Get a copy of this thread's MDC.
		event.getMDCCopy();
		if (locationInfo) {
			event.getLocationInformation();
		}
		event.getRenderedMessage();
		event.getThrowableStrRep();
		buffer.add(event);

		if (buffer.size() >= bufferSize)
			flushBuffer();
	}

	protected String getLogStatement(LoggingEvent event) {
		return getLayout().format(event);
	}

	protected void execute(String sql) throws SQLException {

		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = getConnection();
			if(con==null)
				return;
			String[] values = sql.split("``");
			stmt = con
					.prepareStatement("INSERT INTO tb_system_log (class_path, lever, log_date_time, message,thread_name,log_from) VALUES (?,?,?,?,?,?)");
			stmt.setString(1, values[0]);
			stmt.setString(2, values[1]);
			stmt.setString(3, values[2]);
			stmt.setString(4, values[3]);
			stmt.setString(5, values[4]);
			stmt.setString(6, machineId);
			stmt.executeUpdate();
		} finally {
			if (stmt != null) {
				stmt.close();
			}
			closeConnection(con);
		}
	}

	protected void closeConnection(Connection con) {
	}

	protected Connection getConnection() throws SQLException {
		/*
		 * if (!DriverManager.getDrivers().hasMoreElements())
		 * setDriver("sun.jdbc.odbc.JdbcOdbcDriver");
		 */
		if (connection == null) {
			connection = DriverManager.getConnection(databaseURL, databaseUser,
					databasePassword);
			connection.setAutoCommit(false);
		}

		if (connection.isClosed()) {
			connection = null;
			return null;
		}

		return connection;
	}

	public void close() {
		flushBuffer();

		try {
			if (connection != null && !connection.isClosed())
				connection.close();
		} catch (SQLException e) {
			errorHandler.error("Error closing connection", e,
					ErrorCode.GENERIC_FAILURE);
		}
		this.closed = true;
	}

	public void flushBuffer() {
		removes.ensureCapacity(buffer.size());
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = getConnection();
			if(con==null||con.isClosed()){
				return;
			}
			stmt = con
					.prepareStatement("INSERT INTO "
							+ table
							+ " (class_path, lever, log_date_time, message,thread_name,log_from) VALUES (?,?,?,?,?,?)");

			for (Iterator i = buffer.iterator(); i.hasNext();) {
				LoggingEvent logEvent = (LoggingEvent) i.next();
				String sql = getLogStatement(logEvent);
				String[] values = sql.split("``");
				stmt.setString(1, values[0]);
				stmt.setString(2, values[1]);
				stmt.setString(3, values[2]);
				stmt.setString(4, values[3]);
				stmt.setString(5, values[4]);
				stmt.setString(6, machineId);
				stmt.addBatch();
				removes.add(logEvent);
			}
			stmt.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			errorHandler.error("Failed to excute sql", e,
					ErrorCode.FLUSH_FAILURE);
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
				}
			}
			closeConnection(con);
		}

		buffer.removeAll(removes);

		removes.clear();
	}

	public void finalize() {
		close();
	}

	public boolean requiresLayout() {
		return true;
	}

	public void setSql(String s) {
		sqlStatement = s;
		if (getLayout() == null) {
			this.setLayout(new PatternLayout(s));
		} else {
			((PatternLayout) getLayout()).setConversionPattern(s);
		}
	}

	public String getSql() {
		return sqlStatement;
	}

	public void setUser(String user) {
		databaseUser = user;
	}

	public void setURL(String url) {
		databaseURL = url;
	}

	public void setPassword(String password) {
		databasePassword = password;
	}

	public void setBufferSize(int newBufferSize) {
		bufferSize = newBufferSize;
		buffer.ensureCapacity(bufferSize);
		removes.ensureCapacity(bufferSize);
	}

	public String getUser() {
		return databaseUser;
	}

	public String getURL() {
		return databaseURL;
	}

	public String getPassword() {
		return databasePassword;
	}

	public int getBufferSize() {
		return bufferSize;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public String getTable() {
		return table;
	}

	public void setMachineId(String machineId) {
		this.machineId = machineId;
	}

	public String getMachineId() {
		return machineId;
	}

	public void setDriver(String driverClass) {
		try {
			Class.forName(driverClass);
		} catch (Exception e) {
			errorHandler.error("Failed to load driver", e,
					ErrorCode.GENERIC_FAILURE);
		}
	}
}
