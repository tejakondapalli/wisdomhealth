package com.wisdom.common.rest.util;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;


public class CMPVersionGenerater {

	private static final String STR_DOT = ".";

	SessionFactory sessionFactory;

	public static final String MAX_MAJOR_VALUE = "SELECT max(major) from tblRelease";
	public static final String MAX_MAJOR_IDS = "SELECT releaseId FROM tblRelease WHERE major = (" + MAX_MAJOR_VALUE
			+ ")";
	public static final String MAX_MINOR_VALUE = "SELECT max(minor) from tblRelease WHERE releaseId in ("
			+ MAX_MAJOR_IDS + ")";
	public static final String MAX_MINOR_IDS = "SELECT releaseId FROM tblRelease WHERE minor = (" + MAX_MINOR_VALUE
			+ ") AND releaseId in (" + MAX_MAJOR_IDS + ")";
	public static final String MAX_PATCH_VALUE = "SELECT max(patch) from tblRelease WHERE releaseId in ("
			+ MAX_MINOR_IDS + ")";
	public static final String MAX_VERSION_QUERY = "SELECT releaseId, major, minor, patch FROM tblRelease WHERE patch = ("
			+ MAX_PATCH_VALUE + ") AND releaseId in (" + MAX_MINOR_IDS + ")";
	
//	public static final String MAX_VERSION_QUERY = "SELECT releaseId, major, minor, patch FROM tblRelease WHERE releaseId = (SELECT max(releaseId) from tblRelease)";

//	protected static Logger logger = Logger.getLogger(CMPVersionGenerater.class);

	
	public String generateLatestVersion() {

		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();

		Query versionQuery = session.createSQLQuery(MAX_VERSION_QUERY);

		Object[] versionArr = (Object[]) versionQuery.uniqueResult();

		String latestVersion = versionArr[1] + STR_DOT + versionArr[2] + STR_DOT + versionArr[3];
		//logger.info("latestVersion: " + latestVersion);
		
		session.close();
        tx.commit();

		return latestVersion;

	}
	
	/*public static void main(String[] args) throws Exception {
        JSONObject config = new JSONObject();
        config.put("m3h.db.url", "jdbc:mysql://zchen-server.usac.mmm.com:3306/RapidContent");
        config.put("m3h.db.user", "developer");
        config.put("m3h.db.password", "emids3mhis");

        CMPVersionGenerater cmpVersionGenerator = new CMPVersionGenerater(config);

        String latestVersion = cmpVersionGenerator.generateLatestVersion();
        logger.info("Latest version is: " + latestVersion);

        System.exit(1);

    }*/


}
