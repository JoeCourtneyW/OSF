package osf.database.tables;

import osf.database.Cell;
import osf.database.Database;
import osf.database.Row;
import osf.database.Table;

import java.sql.JDBCType;
import java.util.ArrayList;

public class PlayerDataTable extends Table
{

	public PlayerDataTable(Database database) {
		super(database, "PlayerData", loadDefaultRow());
	}

	private static Row loadDefaultRow() {
		ArrayList<Cell> columnAry = new ArrayList<>();

		columnAry.add(new Cell("UUID", null, JDBCType.VARCHAR, 50).PRIMARY_KEY());

		columnAry.add(new Cell("NAME", "", JDBCType.VARCHAR, 20));
		columnAry.add(new Cell("IP", "127.0.0.1", JDBCType.VARCHAR, 30));

        columnAry.add(new Cell("CRATE_KEYS", "", JDBCType.VARCHAR, 1000));

        columnAry.add(new Cell("INFINITE_POTION_EFFECTS", "", JDBCType.VARCHAR, 1000));

        columnAry.add(new Cell("DISCORD_SNOWFLAKE", "", JDBCType.VARCHAR, 40));

		return new Row(columnAry.toArray(new Cell[0]));
	}

	protected Row getBlankRow() {
        return PlayerDataTable.loadDefaultRow();
    }


}
