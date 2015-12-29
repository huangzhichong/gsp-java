package gsp;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class GSPDemo {

	public static void main(String[] args) throws InterruptedException, IOException {

		
		Properties prop = Helper.getProperties();

		String filePath = prop.getProperty("filePath");
		String contactPrerson = prop.getProperty("contactPrerson");
		String contactPhoneNumber = prop.getProperty("contactPhoneNumber");
		String purchaser = prop.getProperty("purchaser");
		String user = prop.getProperty("user");
		String password = prop.getProperty("password");
		String host = prop.getProperty("host");
		DBUtil db = DBUtil.getInstance(host,user,password);

		List<Map<String, String>> listFromFile = ExcelHelper.readExcel(filePath);
		for (Map<String, String> rowMap : listFromFile) {

			String productName = rowMap.get("商品名称");
			String model = rowMap.get("型号");
			String unit = rowMap.get("单位");
			String manufactory = rowMap.get("产地");
			String sku = rowMap.get("商品编码");
			String productRegNumber = rowMap.get("产品注册证") + " " + rowMap.get("产品注册证号");
			String number = rowMap.get("数量");

			String sqlSearchProduct = "Select 商品编号 from GoodsDefinition where 备注 = '" + sku + "'";
			List<Map<String, Object>> searchResults = db.executeQuery(sqlSearchProduct);

			if (searchResults.size() == 0) {
				String sqlInsertProdcut = ("INSERT INTO GoodsDefinition (商品名称,型号,基本单位,生产厂商,供应商,产品注册号,联系方式,联系人,备注,修改人,修改时间) VALUES ('"
				    + productName
				    + "','"
				    + model
				    + "','"
				    + unit
				    + "','"
				    + manufactory
				    + "','"
				    + manufactory
				    + "','"
				    + productRegNumber
				    + "','"
				    + contactPhoneNumber
				    + "','"
				    + contactPrerson
				    + "','"
				    + sku
				    + "','Administrator','" + Helper.timeStamp() + "')");
				db.execute(sqlInsertProdcut);
			}

			searchResults = db.executeQuery(sqlSearchProduct);
			String productNumber = searchResults.get(0).get("商品编号").toString();
			String purchaseNumber = "P" + Helper.timeStamp("yyyyMMddHHmmss");

			String sqlInsertPOMain = ("INSERT INTO PurchaseOrder_main(订单号,供应商,采购员,预计到货日期,摘要,附加说明,订单日期,是否完成验收,修改人,修改时间) VALUES ('"
			    + purchaseNumber
			    + "','"
			    + manufactory
			    + "','"
			    + purchaser
			    + "','"
			    + Helper.timeStamp()
			    + "','"
			    + "无"
			    + "','"
			    + "无" + "','" + Helper.timeStamp() + "','" + "0" + "','Administrator','" + Helper.timeStamp() + "')");

			db.execute(sqlInsertPOMain);

			String sqlInsertPODetail = ("INSERT INTO PurchaseOrder_detail(订单号,商品编号,商品名称,型号,基本单位,订单数量,生产厂商,供应商,产品注册号,联系方式,联系人,备注) VALUES ('"
			    + purchaseNumber
			    + "','"
			    + productNumber
			    + "','"
			    + productName
			    + "','"
			    + model
			    + "','"
			    + unit
			    + "','"
			    + number
			    + "','"
			    + manufactory
			    + "','"
			    + manufactory
			    + "','"
			    + productRegNumber
			    + "','"
			    + contactPrerson
			    + "','" + contactPhoneNumber + "','无')");
			db.execute(sqlInsertPODetail);
		}

	}
}
