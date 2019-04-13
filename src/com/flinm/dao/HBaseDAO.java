package com.flinm.dao;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.BinaryComparator;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.filter.QualifierFilter;
import org.apache.hadoop.hbase.filter.SubstringComparator;
import org.apache.hadoop.hbase.filter.ValueFilter;
import org.apache.hadoop.hbase.util.Bytes;

import com.flinm.pojo.TBDescribe;

import java.io.IOException;
import java.util.*;
/**
 * @date: 2019/04/09
 * @author: 范林茂
 */
public class HBaseDAO {
	
	 	
	    public static Configuration configuration;
	    public static Connection connection;
	    public static Admin admin;
	    
	    //初始化连接
	    static{
	        configuration = HBaseConfiguration.create();
	        
	        try{
	            connection = ConnectionFactory.createConnection(configuration);
	            admin = connection.getAdmin();
	        }catch (IOException e){
	            e.printStackTrace();
	        }
	    }
	    /**
	     * 创建表
	     * @param myTableName  表名
	     * @param colFamily 可变参数 列簇
	     * @throws IOException
	     */
	    public void createTable(String myTableName,String... colFamily) throws IOException {
	        TableName tableName = TableName.valueOf(myTableName);
	        //判断是否存在该表
	        if(admin.tableExists(tableName)){
	            System.out.println("talbe is exists!");
	        }else {
	            HTableDescriptor hTableDescriptor = new HTableDescriptor(tableName);
	            for(String str:colFamily){
	                HColumnDescriptor hColumnDescriptor = new HColumnDescriptor(str);
	                hTableDescriptor.addFamily(hColumnDescriptor);
	            }
	            admin.createTable(hTableDescriptor);
	        }
	        
	    }
	    /**
	     * 删除表
	     * @param tableName 表名
	     * @throws IOException
	     */
	    public void deleteTable(String tableName) throws IOException {
	        TableName tb_name = TableName.valueOf(tableName);
	        if (admin.tableExists(tb_name)) {
	            admin.disableTable(tb_name);
	            admin.deleteTable(tb_name);
	        }
	    }
	    /**
	     * 查看所有表（包括所有命名空间）
	     * @return
	     * @throws Exception
	     */
	    public List<String> listAllTables() throws Exception {
	    	List<String> tableNames = new ArrayList<>();
	        HTableDescriptor hTableDescriptors[] = admin.listTables();
	        for(HTableDescriptor hTableDescriptor :hTableDescriptors){
	            tableNames.add(hTableDescriptor.getNameAsString());
	        }
	        return tableNames;
	    }
	    /**
	     * 向表中插入数据
	     * @param tableName 表名
	     * @param rowKey 行键
	     * @param colFamily 列簇
	     * @param col 指定插入的列名
	     * @param val 指定插入列名的值
	     * @throws IOException
	     */
	    public void insertRow(String tableName,String rowKey,String colFamily,String col,String val) throws IOException {
	        Table table = connection.getTable(TableName.valueOf(tableName));
	        //利用Put插入数据
	        Put put = new Put(rowKey.getBytes());
	        //获取列簇并添加值
	        put.addColumn(colFamily.getBytes(), col.getBytes(), val.getBytes());
	        table.put(put);
	        table.close();
	        //close();
	    }
	    /**
	     * 查询所有一级部门(没有上级部门的部门)
	     * @param tableName 表名
	     * @return 过滤查询后的数据
	     * @throws Exception
	     */
	    public List<TBDescribe> selectByCondition(String tableName) throws Exception{
	    	Table table = connection.getTable(TableName.valueOf(tableName));
	    	List<TBDescribe> info = new ArrayList<TBDescribe>();
	    	//创建查询
	    	Scan scan = new Scan();
	    	//设置过滤器，行键以0_开头
	    	PrefixFilter prefixFilter = new PrefixFilter(Bytes.toBytes("0_"));
	    	//设置过滤查询
	    	scan.setFilter(prefixFilter);
	    	//执行查询
	    	ResultScanner scanner = table.getScanner(scan);
	    	//循环得到表数据
	    	scanner.forEach(s-> {
	    		s.listCells().forEach(cell ->{
	    			TBDescribe tbinfo = new TBDescribe(
	    					Bytes.toString(s.getRow()),
	    					Bytes.toString(CellUtil.cloneFamily(cell)),
	    					Bytes.toString(CellUtil.cloneQualifier(cell)),
	    					Bytes.toString(CellUtil.cloneValue(cell))
	    					);
	    			info.add(tbinfo);
	    		});
	    	});
	    	table.close();
	    	return info;
	    }
	    /**
	     * 已知rowkey，查询该部门的所有(直接)子部门信息
	     * @param tableName 表名
	     * @param rowKey 行键
	     * @return 过滤查询后的数据
	     * @throws Exception
	     */
	    public List<TBDescribe> selectSubdeptByRowkey(String tableName, String rowKey) throws Exception{
	    	Table table = connection.getTable(TableName.valueOf(tableName));
	    	List<TBDescribe> info = new ArrayList<TBDescribe>();
	    	//创建一个过滤器集合，每个过滤器之间用  与(and) 连接
	    	FilterList filter = new FilterList(FilterList.Operator.MUST_PASS_ALL);
	    	//设置过滤
	    	QualifierFilter columnPrefixFilter = new QualifierFilter(CompareOp.EQUAL, new SubstringComparator("f_pid"));
	    	ValueFilter valueFilter = new ValueFilter(CompareOp.EQUAL, new BinaryComparator(Bytes.toBytes(rowKey)));
	    	//将过滤器添加到FilterList
	    	filter.addFilter(columnPrefixFilter);
	    	filter.addFilter(valueFilter);
	    	//创建查询
	    	Scan scan = new Scan();
	    	//设置查询过滤
	    	scan.setFilter(filter);
	    	//执行scan
	    	ResultScanner scanner = table.getScanner(scan);
	    	//循环得到数据
	    	scanner.forEach(s-> {
	    		s.listCells().forEach(cell ->{
	    			TBDescribe tbinfo = new TBDescribe(
	    					Bytes.toString(s.getRow()),
	    					Bytes.toString(CellUtil.cloneFamily(cell)),
	    					Bytes.toString(CellUtil.cloneQualifier(cell)),
	    					Bytes.toString(CellUtil.cloneValue(cell))
	    					);
	    			info.add(tbinfo);
	    		});
	    	});
	    	table.close();
	    	return info;
	    }
	    /**
	     * 删除一行数据
	     * @param tableName
	     * @param rowKey
	     * @param colFamily
	     * @param col
	     * @throws IOException
	     */
	    public void deleteRow(String tableName,String rowKey,String colFamily,String col) throws IOException {
	        Table table = connection.getTable(TableName.valueOf(tableName));
	        Delete delete = new Delete(rowKey.getBytes());
	        //删除指定列族
	        //delete.addFamily(Bytes.toBytes(colFamily));
	        //删除指定列
	        //delete.addColumn(Bytes.toBytes(colFamily),Bytes.toBytes(col));
	        table.delete(delete);
	        table.close();
	        //close();
	    }
	    /**
	     * 查找指定cell数据
	     * @param tableName 指定表名
	     * @param rowKey 行键
	     * @param colFamily 列簇
	     * @param col 列名
	     * @throws IOException
	     */
	    public List<TBDescribe> getData(String tableName,String rowKey,String colFamily,String col)throws  IOException{
	        Table table = connection.getTable(TableName.valueOf(tableName));
	        List<TBDescribe> info = new ArrayList<TBDescribe>();
	        Get get = new Get(rowKey.getBytes());
	        get.addColumn(colFamily.getBytes(),col.getBytes());
	        Result result = table.get(get);
	        //循环添加数据
	        result.listCells().forEach(cell -> {
	        	TBDescribe tbinfo = new TBDescribe(
    					Bytes.toString(result.getRow()),
    					Bytes.toString(CellUtil.cloneFamily(cell)),
    					Bytes.toString(CellUtil.cloneQualifier(cell)),
    					Bytes.toString(CellUtil.cloneValue(cell))
    					);
	        	info.add(tbinfo);
	        });
	        table.close();
	        return info;
	    }
	    /**
	     * 根据表名查询表
	     * @param tbname
	     * @return
	     * @throws Exception
	     */
	    public List<TBDescribe> selectTable(String tbname) throws Exception {
	    	List<TBDescribe> tableInfo = new ArrayList<TBDescribe>();
	    	Table table = connection.getTable(TableName.valueOf(tbname));
	    	ResultScanner scanner = table.getScanner(new Scan());
	    	scanner.forEach(rs -> {
	    		rs.listCells().forEach(cell -> {
		    			TBDescribe tb = new TBDescribe();
						tb.setRowkey(Bytes.toString(rs.getRow()));
						tb.setColumnFamily(Bytes.toString(CellUtil.cloneFamily(cell)));
						tb.setQualifier(Bytes.toString(CellUtil.cloneQualifier(cell)));
						tb.setCellValue(Bytes.toString(CellUtil.cloneValue(cell)));
						tableInfo.add(tb);
	    		});
	    	});
	    	table.close();
	    	return tableInfo;
	    }
	    /**
	     * 关闭连接
	     */
	    public void close(){
	        try{
	            if(admin != null){
	                admin.close();
	            }
	            if(null != connection){
	                connection.close();
	            }
	        }catch (IOException e){
	            e.printStackTrace();
	        }
	    }
	    
	    /*public static void main(String[] args) throws Exception{
	    	HBaseDAO dao = new HBaseDAO();
	    	
	    	dao.listAllTables();
	    	
	    	List<TBDescribe> list = dao.selectSubdeptByRowkey("dp:dept", "1_001");
	    	list.forEach(s-> System.out.println(s));
	    	
	    	List<TBDescribe> info = dao.selectByCondition("dp:dept");
	    	info.forEach(s -> System.out.println(s));
	    	
	    	dao.close();
	    }*/
}
