package com.wisdom.common.rest.util;

import java.io.IOException;

import org.hibernate.SessionFactory;
import org.json.JSONObject;

/**
 * Created by Zhendong Chen on 12/21/16.
 */
public class CmpFileBuilder {
//    private static Logger logger = Logger.getLogger(CmpFileBuilder.class);
  //  Map<String, DictionaryResourceImpl> dictMap = new HashMap<>();
    SessionFactory sessionFactory;
    private static final String[] RESTRICTED_VOCAB = new String[]{"cui", "tui", "ncid", "dict_type"};
//    EntryPropertiesRoot entryPropertiesRoot;

    private static final String DUMP_QUERY =
            "select vs.value as variant, GROUP_CONCAT( p.name SEPARATOR ',') as pkey, " +
                    "GROUP_CONCAT( pv.value SEPARATOR ',') as pvalue from tblVariant v " +
                    "left join tblVariantString vs on v.fkVariantStringId = vs.variantStringId " +
                    "left join tblDictionary d on v.fkDictionaryId = d.dictionaryId " +
                    "left join tblVariantPropertyValue vpv on vpv.fkVariantId = v.variantId " +
                    "inner join tblPropertyValue pv on vpv.fkPropertyValueId = pv.propertyValueId " +
                    "inner join tblProperty p on pv.fkPropertyId = p.propertyId " +
                    "where v.fkDictionaryId = :dictId " +
                    "group by vs.value";


    public CmpFileBuilder(JSONObject config) {
       // entryPropertiesRoot = new EntryPropertiesRoot(RESTRICTED_VOCAB);
    }

    public void compileCmpFiles(String path) throws Exception {
/*        Session session = sessionFactory.getCurrentSession();
        Transaction tx = session.beginTransaction();
        List<Dictionary> dictList = session.createCriteria(Dictionary.class).list();

        for (Dictionary d : dictList) {
            File f = new File(path + "/" + d.getName() + ".cmp");
            DictionaryResourceImpl dictImpl = new DictionaryResourceImpl();

            Query cq = session.createSQLQuery(DUMP_QUERY)
                    .addScalar("variant", StringType.INSTANCE)
                    .addScalar("pkey", StringType.INSTANCE)
                    .addScalar("pvalue", StringType.INSTANCE)
                    .setParameter("dictId", d.getDictionaryId());

            List<Object[]> results = cq.list();

            if (results.size() > 0) {
                for (Object[] v : results) {
                    //logger.info(v.getVariantstring().getValue() + " : " + v.getDictionary().getName());
                    if (v[0] == null || v[0].toString().length() == 0) {
                        continue;
                    }

                    String[] tokens = getTokens(v[0].toString());
                    String[] keys = v[1].toString().split(",", -1);
                    String[] values = v[2].toString().split(",", -1);
                    EntryProperties props = entryPropertiesRoot.newEntryProperties();
                    for (int i = 0; i < keys.length; i++) {
                        props.setProperty(keys[i], values[i]);
                    }
                    dictImpl.putEntry(tokens[0], tokens, null, tokens.length, props);
                }
                logger.info(results.size() + " records loaded for " + f.getCanonicalPath());
            } else {
                logger.info("There is no records loaded for " + f.getCanonicalPath());
            }
            OutputStream os = new FileOutputStream(f);
            dictImpl.serializeEntries(new FileOutputStream(f), entryPropertiesRoot);
            os.close();
            break;
        }

        session.close();
        tx.commit();
*/    }

    private String[] getTokens(String input) throws IOException {
       /* List<Token> tokenizerResult = TokenizerHelper.getInstance().tokenize(input);
        ArrayList<String> tokens = new ArrayList<String>();
        for (Token token : tokenizerResult) {
            String tokenText = token.getText().toLowerCase();
            if (!tokenText.contains("\n")) {
                tokens.add(tokenText);
            }
        }
        return tokens.toArray(new String[tokens.size()]);
        
*/  
    	return null;
    }

    public static void main(String[] args) throws Exception {
        long startTime = System.currentTimeMillis();
        JSONObject config = new JSONObject();
        config.put("m3h.db.url", "jdbc:mysql://zchen-server.usac.mmm.com:3316/RapidContent");
        config.put("m3h.db.user", "developer");
        config.put("m3h.db.password", "emids3mhis");

        CmpFileBuilder builder = new CmpFileBuilder(config);

        builder.compileCmpFiles("/tmp/cmp");

        long endTime   = System.currentTimeMillis();
        long totalTime = endTime - startTime;
      //  logger.info("total time spent: " + totalTime/1000 + " seconds.");
        System.exit(1);

    }
}
