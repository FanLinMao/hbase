package com.flinm.pojo;
/**
 * 表数据描述
 * @date: 2019/04/11
 * @author: 范林茂
 */
public class TBDescribe {
	
	/**
	 * 行键
	 */
	private String rowkey;
	
	/**
	 * 列簇
	 */
	private String columnFamily;
	
	/**
	 * 限定名
	 */
	private String qualifier;
	
	/**
	 * 值
	 */
	private String cellValue;
	
	

	public TBDescribe() {
		
	}

	public TBDescribe(String rowkey, String columnFamily, String qualifier, String cellValue) {
		this.rowkey = rowkey;
		this.columnFamily = columnFamily;
		this.qualifier = qualifier;
		this.cellValue = cellValue;
	}

	@Override
	public String toString() {
		return "TBDescribe [rowkey=" + rowkey + ", columnFamily=" + columnFamily + ", qualifier=" + qualifier
				+ ", cellValue=" + cellValue + "]";
	}

	public String getRowkey() {
		return rowkey;
	}

	public void setRowkey(String rowkey) {
		this.rowkey = rowkey;
	}

	public String getColumnFamily() {
		return columnFamily;
	}

	public void setColumnFamily(String columnFamily) {
		this.columnFamily = columnFamily;
	}

	public String getQualifier() {
		return qualifier;
	}

	public void setQualifier(String qualifier) {
		this.qualifier = qualifier;
	}

	public String getCellValue() {
		return cellValue;
	}

	public void setCellValue(String cellValue) {
		this.cellValue = cellValue;
	}
	
	
}
