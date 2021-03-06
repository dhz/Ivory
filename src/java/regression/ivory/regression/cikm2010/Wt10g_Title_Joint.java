package ivory.regression.cikm2010;

import ivory.core.eval.Qrels;
import ivory.regression.GroundTruth;
import ivory.regression.GroundTruth.Metric;
import ivory.smrf.retrieval.Accumulator;
import ivory.smrf.retrieval.BatchQueryRunner;

import java.util.HashMap;
import java.util.Map;

import junit.framework.JUnit4TestAdapter;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.log4j.Logger;
import org.junit.Test;


import edu.umd.cloud9.collection.DocnoMapping;

/* Note: different metrics are optimized separately */

public class Wt10g_Title_Joint {

	private static final Logger sLogger = Logger.getLogger(Wt10g_Title_Joint.class);

	private static String[] x10_rawAP = new String[] { "501", "0.4156", "502", "0.0718", "503",
			"0.1695", "504", "0.4256", "505", "0.3530", "506", "0.0359", "507", "0.4171", "508",
			"0.2436", "509", "0.3895", "510", "0.6109", "511", "0.3493", "512", "0.1862", "513",
			"0.0965", "514", "0.1848", "515", "0.1677", "516", "0.0948", "517", "0.0349", "518",
			"0.2102", "519", "0.1074", "520", "0.1103", "521", "0.0218", "522", "0.2037", "523",
			"0.4681", "524", "0.0934", "525", "0.0994", "526", "0.0961", "527", "0.3665", "528",
			"0.4317", "529", "0.3851", "530", "0.6585", "531", "0.0443", "532", "0.1984", "533",
			"0.1985", "534", "0.0122", "535", "0.0303", "536", "0.0994", "537", "0.0353", "538",
			"0.3250", "539", "0.0816", "540", "0.1280", "541", "0.1871", "542", "0.0068", "543",
			"0.0020", "544", "0.6334", "545", "0.0906", "546", "0.1328", "547", "0.1920", "548",
			"0.8333", "549", "0.1951", "550", "0.0757" };

	private static String[] x15_rawAP = new String[] { "501", "0.4156", "502", "0.0718", "503",
			"0.1695", "504", "0.4256", "505", "0.3530", "506", "0.0359", "507", "0.4171", "508",
			"0.2470", "509", "0.3895", "510", "0.6109", "511", "0.3493", "512", "0.1862", "513",
			"0.0965", "514", "0.1848", "515", "0.1677", "516", "0.0948", "517", "0.0349", "518",
			"0.2102", "519", "0.1074", "520", "0.1103", "521", "0.0426", "522", "0.2165", "523",
			"0.4681", "524", "0.0934", "525", "0.0994", "526", "0.0961", "527", "0.3665", "528",
			"0.4810", "529", "0.3851", "530", "0.6564", "531", "0.0443", "532", "0.1984", "533",
			"0.1985", "534", "0.0122", "535", "0.0303", "536", "0.0953", "537", "0.0353", "538",
			"0.3250", "539", "0.0799", "540", "0.1280", "541", "0.1871", "542", "0.0068", "543",
			"0.0020", "544", "0.6334", "545", "0.1595", "546", "0.1328", "547", "0.1920", "548",
			"0.8333", "549", "0.1951", "550", "0.0757" };

	private static String[] x20_rawAP = new String[] { "501", "0.4385", "502", "0.0929", "503",
			"0.1683", "504", "0.4922", "505", "0.3807", "506", "0.0338", "507", "0.3256", "508",
			"0.2396", "509", "0.3992", "510", "0.6927", "511", "0.3972", "512", "0.1806", "513",
			"0.0955", "514", "0.1535", "515", "0.1826", "516", "0.0945", "517", "0.0341", "518",
			"0.2840", "519", "0.1175", "520", "0.1094", "521", "0.0454", "522", "0.2874", "523",
			"0.4668", "524", "0.0877", "525", "0.1311", "526", "0.0963", "527", "0.4749", "528",
			"0.4393", "529", "0.3531", "530", "0.6555", "531", "0.0266", "532", "0.2242", "533",
			"0.1482", "534", "0.0114", "535", "0.0271", "536", "0.1323", "537", "0.0558", "538",
			"0.3250", "539", "0.0490", "540", "0.1241", "541", "0.1645", "542", "0.0109", "543",
			"0.0026", "544", "0.6315", "545", "0.2622", "546", "0.1472", "547", "0.2009", "548",
			"0.8333", "549", "0.2268", "550", "0.0706" };

	private static String[] x25_rawAP = new String[] { "501", "0.4153", "502", "0.0929", "503",
			"0.1683", "504", "0.4922", "505", "0.4032", "506", "0.0338", "507", "0.3256", "508",
			"0.2339", "509", "0.3992", "510", "0.6927", "511", "0.4036", "512", "0.1806", "513",
			"0.0955", "514", "0.1535", "515", "0.1870", "516", "0.0945", "517", "0.0348", "518",
			"0.2587", "519", "0.1175", "520", "0.1094", "521", "0.0419", "522", "0.3191", "523",
			"0.4668", "524", "0.0877", "525", "0.1311", "526", "0.0963", "527", "0.4749", "528",
			"0.4391", "529", "0.3531", "530", "0.6555", "531", "0.0266", "532", "0.2242", "533",
			"0.1529", "534", "0.0077", "535", "0.0273", "536", "0.1661", "537", "0.0784", "538",
			"0.3250", "539", "0.0434", "540", "0.1208", "541", "0.1783", "542", "0.0109", "543",
			"0.0026", "544", "0.6315", "545", "0.2507", "546", "0.1472", "547", "0.2009", "548",
			"0.5000", "549", "0.2501", "550", "0.0706" };

	private static String[] x30_rawAP = new String[] { "501", "0.4079", "502", "0.1188", "503",
			"0.1624", "504", "0.4889", "505", "0.3922", "506", "0.0339", "507", "0.3388", "508",
			"0.2365", "509", "0.3924", "510", "0.7295", "511", "0.3784", "512", "0.1855", "513",
			"0.0955", "514", "0.1276", "515", "0.1967", "516", "0.0945", "517", "0.0290", "518",
			"0.2656", "519", "0.1175", "520", "0.1212", "521", "0.0452", "522", "0.3372", "523",
			"0.4668", "524", "0.0857", "525", "0.1483", "526", "0.0963", "527", "0.5070", "528",
			"0.4272", "529", "0.3149", "530", "0.6490", "531", "0.1002", "532", "0.2242", "533",
			"0.1639", "534", "0.0065", "535", "0.0521", "536", "0.1691", "537", "0.0445", "538",
			"0.3250", "539", "0.0433", "540", "0.1214", "541", "0.1785", "542", "0.0074", "543",
			"0.0020", "544", "0.6181", "545", "0.2507", "546", "0.1472", "547", "0.1904", "548",
			"0.5833", "549", "0.2502", "550", "0.0673" };

	private static String[] x35_rawAP = new String[] { "501", "0.4027", "502", "0.1188", "503",
			"0.1624", "504", "0.4889", "505", "0.3984", "506", "0.0339", "507", "0.3388", "508",
			"0.2847", "509", "0.3924", "510", "0.7295", "511", "0.3784", "512", "0.1855", "513",
			"0.0955", "514", "0.1276", "515", "0.1989", "516", "0.0945", "517", "0.0290", "518",
			"0.2656", "519", "0.1175", "520", "0.1212", "521", "0.0608", "522", "0.4848", "523",
			"0.4668", "524", "0.0857", "525", "0.1483", "526", "0.0963", "527", "0.5070", "528",
			"0.4551", "529", "0.3149", "530", "0.6416", "531", "0.1002", "532", "0.2242", "533",
			"0.1740", "534", "0.0076", "535", "0.0521", "536", "0.1691", "537", "0.0439", "538",
			"0.3250", "539", "0.0433", "540", "0.1214", "541", "0.1778", "542", "0.0074", "543",
			"0.0020", "544", "0.6181", "545", "0.2679", "546", "0.1472", "547", "0.1904", "548",
			"0.5833", "549", "0.2425", "550", "0.0673" };

	private static String[] x40_rawAP = new String[] { "501", "0.4027", "502", "0.1188", "503",
			"0.1624", "504", "0.4889", "505", "0.3984", "506", "0.0339", "507", "0.3388", "508",
			"0.2847", "509", "0.3924", "510", "0.7295", "511", "0.3784", "512", "0.1855", "513",
			"0.0955", "514", "0.1276", "515", "0.1989", "516", "0.0945", "517", "0.0290", "518",
			"0.2656", "519", "0.1175", "520", "0.1212", "521", "0.0608", "522", "0.4848", "523",
			"0.4668", "524", "0.0857", "525", "0.1483", "526", "0.0963", "527", "0.5070", "528",
			"0.4810", "529", "0.3149", "530", "0.6416", "531", "0.1002", "532", "0.2242", "533",
			"0.1740", "534", "0.0081", "535", "0.0521", "536", "0.1691", "537", "0.0439", "538",
			"0.3250", "539", "0.0433", "540", "0.1214", "541", "0.1778", "542", "0.0074", "543",
			"0.0020", "544", "0.6181", "545", "0.2828", "546", "0.1472", "547", "0.1904", "548",
			"0.5833", "549", "0.2425", "550", "0.0673" };

	private static String[] x45_rawAP = new String[] { "501", "0.4027", "502", "0.1188", "503",
			"0.1624", "504", "0.4889", "505", "0.3984", "506", "0.0339", "507", "0.3388", "508",
			"0.2847", "509", "0.3924", "510", "0.7295", "511", "0.3784", "512", "0.1855", "513",
			"0.0955", "514", "0.1276", "515", "0.1989", "516", "0.0945", "517", "0.0290", "518",
			"0.2656", "519", "0.1175", "520", "0.1212", "521", "0.0608", "522", "0.4848", "523",
			"0.4668", "524", "0.0857", "525", "0.1483", "526", "0.0963", "527", "0.5070", "528",
			"0.4810", "529", "0.3149", "530", "0.6416", "531", "0.1002", "532", "0.2242", "533",
			"0.2332", "534", "0.0081", "535", "0.0521", "536", "0.1691", "537", "0.0439", "538",
			"0.3250", "539", "0.0433", "540", "0.1214", "541", "0.1778", "542", "0.0074", "543",
			"0.0020", "544", "0.6181", "545", "0.2828", "546", "0.1472", "547", "0.1904", "548",
			"0.5833", "549", "0.2425", "550", "0.0673" };

	private static String[] x50_rawAP = new String[] { "501", "0.4027", "502", "0.1188", "503",
			"0.1624", "504", "0.4889", "505", "0.3984", "506", "0.0339", "507", "0.3388", "508",
			"0.2847", "509", "0.3924", "510", "0.7295", "511", "0.3784", "512", "0.1855", "513",
			"0.0955", "514", "0.1276", "515", "0.1989", "516", "0.0945", "517", "0.0290", "518",
			"0.2656", "519", "0.1175", "520", "0.1212", "521", "0.0608", "522", "0.4848", "523",
			"0.4668", "524", "0.0857", "525", "0.1483", "526", "0.0963", "527", "0.5070", "528",
			"0.4810", "529", "0.3149", "530", "0.6416", "531", "0.1002", "532", "0.2242", "533",
			"0.2791", "534", "0.0081", "535", "0.0521", "536", "0.1691", "537", "0.0439", "538",
			"0.3250", "539", "0.0433", "540", "0.1214", "541", "0.1778", "542", "0.0074", "543",
			"0.0020", "544", "0.6181", "545", "0.2828", "546", "0.1472", "547", "0.1904", "548",
			"0.5833", "549", "0.2425", "550", "0.0673" };

	@Test
	public void runRegression() throws Exception {
		Map<String, GroundTruth> g = new HashMap<String, GroundTruth>();

		g.put("joint-x1.0", new GroundTruth("joint-x1.0", Metric.AP, 50, x10_rawAP, 0.2200f));
		g.put("joint-x1.5", new GroundTruth("joint-x1.5", Metric.AP, 50, x15_rawAP, 0.2230f));
		g.put("joint-x2.0", new GroundTruth("joint-x2.0", Metric.AP, 50, x20_rawAP, 0.2325f));
		g.put("joint-x2.5", new GroundTruth("joint-x2.5", Metric.AP, 50, x25_rawAP, 0.2275f));
		g.put("joint-x3.0", new GroundTruth("joint-x3.0", Metric.AP, 50, x30_rawAP, 0.2307f));
		g.put("joint-x3.5", new GroundTruth("joint-x3.5", Metric.AP, 50, x35_rawAP, 0.2358f));
		g.put("joint-x4.0", new GroundTruth("joint-x4.0", Metric.AP, 50, x40_rawAP, 0.2366f));
		g.put("joint-x4.5", new GroundTruth("joint-x4.5", Metric.AP, 50, x45_rawAP, 0.2378f));
		g.put("joint-x5.0", new GroundTruth("joint-x5.0", Metric.AP, 50, x50_rawAP, 0.2387f));

		Qrels qrels = new Qrels("data/wt10g/qrels.wt10g.all");

    String[] params = new String[] {
            "data/wt10g/run.wt10g.CIKM2010.title.joint.xml",
            "data/wt10g/queries.wt10g.501-550.xml" };

		FileSystem fs = FileSystem.getLocal(new Configuration());

		BatchQueryRunner qr = new BatchQueryRunner(params, fs);

		long start = System.currentTimeMillis();
		qr.runQueries();
		long end = System.currentTimeMillis();

		sLogger.info("Total query time: " + (end - start) + "ms");

		DocnoMapping mapping = qr.getDocnoMapping();

		for (String model : qr.getModels()) {
			sLogger.info("Verifying results of model \"" + model + "\"");

			Map<String, Accumulator[]> results = qr.getResults(model);
			g.get(model).verify(results, mapping, qrels);

			sLogger.info("Done!");
		}
	}

	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(Wt10g_Title_Joint.class);
	}
}
