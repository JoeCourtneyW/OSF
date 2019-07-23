package osf.database;


import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringJoiner;

import org.pmw.tinylog.Logger;
import osf.OSF;


public abstract class Table implements Iterable<Row> {
    private Database database;

    private ArrayList<Row> contents;
    private Cell primaryKey;
    private Row defaultRow;
    private String tableName;

    public Table(Database database, String tableName, Row defaultRow) {
        this.database = database;
        this.tableName = tableName;
        this.defaultRow = defaultRow;
        this.primaryKey = defaultRow.getPrimaryKey();

        ensureTableExists();
        if (!isTableSchemaConsistent()) {
            alterTableSchema();
        }
        dumpTableContents();
    }

    private boolean isTableSchemaConsistent() {
        try {
            PreparedStatement tableColumns = database.getConnection().prepareStatement("SHOW COLUMNS FROM "
                    + "`" + Database.escape(getTableName()) + "`;");
            tableColumns.execute();

            ResultSet resultSet = tableColumns.getResultSet();

            for (int i = 0; i < getDefaultRow().getCells().length; i++) {
                resultSet.next();
                if (!getDefaultRow().getCells()[i].getColumnName().equalsIgnoreCase(resultSet.getString("Field"))) {
                    Logger.warn("Table schema inconsistencies found in table `" + getTableName() + "`. Attempting to alter table schema to match");
                    return false;
                }
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            Logger.error("Failed to confirm schema of table `" + getTableName() + "`");
            return false;
        }
    }

    private void alterTableSchema() {
        OSF.getInstance().getServer().shutdown();
        /*try {
            PreparedStatement tableColumns = database.getConnection().prepareStatement("SHOW COLUMNS FROM "
                    + "`" + Database.escape(getTableName()) + "`;");
            tableColumns.execute();

            ResultSet resultSet = tableColumns.getResultSet();
            resultSet.last();
            resultSet.getRow();
            for (int i = 0; i < getDefaultRow().getCells().length; i++) {
                if (!getDefaultRow().getCells()[i].getColumnName().equalsIgnoreCase(resultSet.getString("Field"))) {

                    return;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            Logger.error("Failed to confirm schema of table `" + getTableName() + "`");

        }*/
        //TODO
    }

    private void ensureTableExists() {
        if (doesTableExist()) {
            Logger.info("Found table `" + getTableName() + "` in database");
        } else {
            Logger.error("Couldn't find table `" + getTableName() + "` in database, creating it");
            createSQLTable();
        }
    }

    private boolean doesTableExist() {
        try {
            DatabaseMetaData databaseMetaData = database.getConnection().getMetaData();
            ResultSet resultSet = databaseMetaData.getTables(null, null, getTableName(), null);
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            Logger.error("Failed to check existance of table `" + getTableName() + "`");
            return false;
        }
    }

    public void createSQLTable() {
        try {
            StringBuilder preparedStatement = new StringBuilder();
            preparedStatement.append("CREATE TABLE IF NOT EXISTS `");
            preparedStatement.append(Database.escape(tableName));
            preparedStatement.append("` (");

            for (Cell cell : defaultRow.getCells()) {
                preparedStatement.append(Database.escape(cell.getColumnName()));
                preparedStatement.append(" ");
                preparedStatement.append(cell.getDatabaseType().getName());
                preparedStatement.append(cell.getLength());
                preparedStatement.append(", ");
            }

            preparedStatement.append("PRIMARY KEY (");
            preparedStatement.append(Database.escape(primaryKey.getColumnName()));
            preparedStatement.append(")");

            preparedStatement.append(");");
            PreparedStatement createTable = database.getConnection()
                    .prepareStatement(preparedStatement.toString());
            createTable.execute();
            createTable.close();

            Logger.info("Created table `" + getTableName() + "`");
        } catch (SQLException e) {
            e.printStackTrace();
            Logger.error("Failed to create table `" + getTableName() + "`");
        }
    }

    public void newEntry(final Row row) {
        contents.add(row);
        OSF.getConcurrentUtil().async(() -> {
            try {
                StringJoiner questionMarks = new StringJoiner(",");
                for (int i = 0; i < row.getCells().length; i++)
                    questionMarks.add("?");

                String preparedStatement = "INSERT INTO " + Database.escape(tableName) + " values(" + questionMarks.toString() + ")";

                PreparedStatement statement = database.getConnection()
                        .prepareStatement(preparedStatement);

                int count = 1; // Setting question marks requires an index
                for (Cell cell : row) {
                    if (cell.isPrimaryKey()) // Don't set primary key to arbitrary default key
                        statement.setString(count, cell.getValue().toString());
                    else
                        statement.setString(count, cell.getDefaultValue().toString());
                    count++;
                }

                statement.execute();
                statement.close();
                Logger.debug("Created new entry in `" + getTableName() + "` with PRIMARY KEY: " + row.getPrimaryKey().getValue().toString() + "");
            } catch (SQLException e) {
                e.printStackTrace();
                Logger.error("Failed to create new entry in `" + getTableName() + "` with PRIMARY KEY: " + row.getPrimaryKey().getValue().toString() + "");
            }
        });
    }

    public void updateEntry(final Row row) {
        OSF.getConcurrentUtil().async(() -> {
            try {
                StringJoiner cells = new StringJoiner(",");
                for (Cell cell : row.getCells()) {
                    if (!cell.isPrimaryKey())
                        cells.add("`" + cell.getColumnName() + "`=?");
                }

                String preparedStatement = "UPDATE " + Database.escape(tableName)
                        + " SET " + cells.toString() + " WHERE "
                        + "`" + Database.escape(row.getPrimaryKey().getColumnName()) + "`=?";

                PreparedStatement statement = database.getConnection()
                        .prepareStatement(preparedStatement);

                int count = 1; // Setting question marks requires an index
                for (Cell cell : row) {
                    if (cell.isPrimaryKey())
                        continue;
                    statement.setString(count, cell.getValue().asString());
                    count++;
                }
                statement.setString(count, row.getPrimaryKey().getValue().toString());

                statement.execute();
                statement.close();
                Logger.debug("Updated entry in `" + getTableName() + "` with PRIMARY KEY: " + row.getPrimaryKey().getValue().toString() + "");
            } catch (SQLException e) {
                e.printStackTrace();
                Logger.error("Failed to update entry in `" + getTableName() + "` with PRIMARY KEY: " + row.getPrimaryKey().getValue().toString() + "");
            }
        });
    }

    private void dumpTableContents() {
        ArrayList<Row> contents = new ArrayList<>();
        try {
            PreparedStatement statement = database.getConnection().prepareStatement("SELECT * FROM " + tableName);
            ResultSet resultSet = statement.executeQuery();
            Row row;
            while (resultSet.next()) {
                row = getBlankRow();
                for (Cell cell : row) {
                    cell.setValue(resultSet.getObject(cell.getColumnName()));
                }
                contents.add(row);
            }
            statement.close();
            Logger.info("Dumped contents of `" + getTableName() + "`");
        } catch (SQLException e) {
            e.printStackTrace();
            Logger.error("Failed to dump contents of `" + getTableName() + "`");
        }

        this.contents = contents;

    }

    @SuppressWarnings("unchecked")
    public ArrayList<Row> getTableContents() {
        return (ArrayList<Row>) contents.clone();// Clone the object so it doesn't change our values
    }

    public Row getRow(Object primaryKeyValue) {
        for (Row row : this) {
            if (row.getPrimaryKey().equals(primaryKeyValue))
                return row;
        }
        return getDefaultRow();
    }

    public Row getDefaultRow() {
        return new Row(defaultRow.getClonedCells()); // Clone the object so it doesn't change our default values
    }

    protected abstract Row getBlankRow();


    public String getTableName() {
        return tableName;
    }

    @Override
    public Iterator<Row> iterator() {
        return contents.iterator();
    }
}
