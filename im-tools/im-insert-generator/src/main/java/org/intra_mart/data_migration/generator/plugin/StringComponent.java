package org.intra_mart.data_migration.generator.plugin;

import org.intra_mart.data_migration.generator.GenerateException;

public class StringComponent implements ColumnValueComponent {

	public String getQueryValue(String value) throws GenerateException {
		if (value == null) {
			return "null";
		}
		return "'" + value.replaceAll("\\Q" + "'" + "\\E", "''") + "'";
	}
}
